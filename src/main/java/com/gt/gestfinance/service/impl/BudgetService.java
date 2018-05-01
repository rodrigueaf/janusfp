package com.gt.gestfinance.service.impl;

import com.gt.gestfinance.entity.Budget;
import com.gt.gestfinance.entity.OperationType;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.repository.BudgetRepository;
import com.gt.gestfinance.repository.OperationRepository;
import com.gt.gestfinance.service.IBudgetService;
import com.gt.gestfinance.util.MPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Autowired
    public BudgetService(BudgetRepository repository) {
        super(repository);
    }

    @Override
    public synchronized Budget save(Budget budget) throws CustomException {
        controlerLIntegriteDuBudget(budget);
        return super.save(budget);
    }

    @Override
    public synchronized Budget saveAndFlush(Budget budget) throws CustomException {
        controlerLIntegriteDuBudget(budget);
        return super.saveAndFlush(budget);
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

    @Autowired
    public void setOperationRepository(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }
}
