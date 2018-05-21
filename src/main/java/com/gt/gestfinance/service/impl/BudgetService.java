package com.gt.gestfinance.service.impl;

import com.gt.gestfinance.entity.Budget;
import com.gt.gestfinance.entity.OperationType;
import com.gt.gestfinance.entity.Version;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.repository.BudgetRepository;
import com.gt.gestfinance.repository.ExploitationRepository;
import com.gt.gestfinance.repository.OperationRepository;
import com.gt.gestfinance.repository.VersionRepository;
import com.gt.gestfinance.service.IBudgetService;
import com.gt.gestfinance.service.IExploitationService;
import com.gt.gestfinance.service.IOperationService;
import com.gt.gestfinance.util.MPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Classe Service de l'entité Budget
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 31/03/2018
 */
@Service
public class BudgetService extends BaseEntityService<Budget, Integer> implements IBudgetService {

    private OperationRepository operationRepository;
    private VersionRepository versionRepository;
    private ExploitationRepository exploitationRepository;
    private IOperationService operationService;
    private IExploitationService exploitationService;

    @Autowired
    public BudgetService(BudgetRepository repository) {
        super(repository);
    }

    @Override
    public VersionRepository getVersionRepository() {
        return versionRepository;
    }

    @Override
    public synchronized Budget save(Budget budget) throws CustomException {
        controlerLIntegriteDuBudget(budget);
        budget = super.save(budget);
        miseAJourDeLaVersion(budget, Version.MOTIF_AJOUT, budget.getIdentifiant());
        return budget;
    }

    @Override
    public synchronized Budget saveAndFlush(Budget budget) throws CustomException {
        controlerLIntegriteDuBudget(budget);
        budget.setMontantTotal(0D);
        budget = super.saveAndFlush(budget);
        miseAJourDeLaVersion(budget, Version.MOTIF_MODIFICATION, budget.getIdentifiant());
        return budget;
    }

    private void controlerLIntegriteDuBudget(Budget budget) throws CustomException {
        if (budget.getLibelle() == null || budget.getLibelle().isEmpty()) {
            throw new CustomException(MPConstants.LE_LIBELLE_DU_BUDGET_EST_OBLIGATOIRE);
        }
        Optional<Budget> op = ((BudgetRepository) repository).findByLibelle(budget.getLibelle());
        if (op.isPresent() && !op.get().getIdentifiant().equals(budget.getIdentifiant())) {
            throw new CustomException(MPConstants.LE_LIBELLE_DU_BUDGET_EXSTE_DEJA);
        }
    }

    @Override
    public Page<Budget> findAll(Pageable p) {
        Page<Budget> budgetPage = super.findAll(p);
        return budgetPage.map(budget -> {
            Double total = operationRepository
                    .sumByBudgetIdentifiantAndOperationType(
                            budget.getIdentifiant(), OperationType.PRODUIT)
                    + operationRepository
                    .sumByBudgetIdentifiantAndOperationType(
                            budget.getIdentifiant(), OperationType.EMPRUNT)
                    + operationRepository
                    .sumByBudgetIdentifiantAndOperationType(
                            budget.getIdentifiant(), OperationType.VIREMENT)
                    - operationRepository
                    .sumByBudgetIdentifiantAndOperationType(
                            budget.getIdentifiant(), OperationType.DEPENSE)
                    - operationRepository
                    .sumByBudgetIdentifiantAndOperationType(
                            budget.getIdentifiant(), OperationType.PRET);
            budget.setMontantTotal(total);
            return budget;
        });
    }

    @Override
    public List<Budget> recupererLaListeVersionnee(Integer[] ints) {
        return ((BudgetRepository) repository).recupererLaListeVersionnee(ints);
    }

    @Override
    public boolean supprimerForcer(Integer budgetId) throws CustomException {
        operationRepository.findByBudgetIdentifiant(budgetId)
                .forEach(o -> {
                    try {
                        operationService.supprimerForcer(o.getIdentifiant());
                    } catch (CustomException e) {
                        Logger.getLogger(BudgetService.class.getSimpleName()).warning(e.getMessage());
                    }
                });
        exploitationRepository.findByBudgetParDefautIdentifiant(budgetId)
                .forEach(e -> {
                    try {
                        exploitationService.supprimerForcer(e.getIdentifiant());
                    } catch (CustomException ex) {
                        Logger.getLogger(BudgetService.class.getSimpleName()).warning(ex.getMessage());
                    }
                });
        return supprimer(budgetId);
    }

    @Override
    public boolean supprimer(Integer budgetId) throws CustomException {
        if (!operationRepository.findByBudgetIdentifiant(budgetId).isEmpty())
            throw new CustomException(MPConstants.LE_BUDGET_DEJA_UTILISER);
        if (!exploitationRepository.findByBudgetParDefautIdentifiant(budgetId).isEmpty())
            throw new CustomException(MPConstants.LE_BUDGET_DEJA_UTILISER);
        Budget budget = findOne(budgetId);
        if (super.delete(budgetId)) {
            miseAJourDeLaVersion(budget, Version.MOTIF_SUPPRESSION, budgetId);
            return true;
        }
        return false;
    }

    @Autowired
    public void setOperationRepository(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @Autowired
    public void setVersionRepository(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    @Autowired
    public void setExploitationRepository(ExploitationRepository exploitationRepository) {
        this.exploitationRepository = exploitationRepository;
    }

    @Autowired
    public void setOperationService(IOperationService operationService) {
        this.operationService = operationService;
    }

    @Autowired
    public void setExploitationService(IExploitationService exploitationService) {
        this.exploitationService = exploitationService;
    }
}
