package com.gt.gestfinance.service.impl;

import com.gt.gestfinance.dto.OperationVM;
import com.gt.gestfinance.entity.*;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.repository.*;
import com.gt.gestfinance.service.IOperationService;
import com.gt.gestfinance.service.ITresorerieService;
import com.gt.gestfinance.util.MPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Classe Service de l'entité Operation
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 31/03/2018
 */
@Service
public class OperationService extends BaseEntityService<Operation, Integer> implements IOperationService {

    private ITresorerieService tresorerieService;
    private OperationDetailRepository operationDetailRepository;
    private ExploitationRepository exploitationRepository;
    private BudgetRepository budgetRepository;
    private CompteRepository compteRepository;
    private TresorerieRepository tresorerieRepository;
    private VersionRepository versionRepository;

    @Autowired
    public OperationService(OperationRepository repository) {
        super(repository);
    }

    @Override
    public VersionRepository getVersionRepository() {
        return versionRepository;
    }

    @Override
    public Operation ajouter(OperationVM operationVM) throws CustomException {
        controlerLIntegriteDeLOperation(operationVM);
        reconstrureLOperation(operationVM);
        Operation operation = repository.save(operationVM.operation);
        enregistrerLesDetailsDOperation(operation, operationVM);
        operation = saveAndFlush(operation);
        miseAJourDeLaVersion(operation, Version.MOTIF_AJOUT, operation.getIdentifiant());
        return operation;
    }

    private void reconstrureLOperation(OperationVM operationVM) {
        if (operationVM.operation.getOperationBudget() != null
                && operationVM.operation.getOperationBudget().getIdentifiant() != null) {
            operationVM.operation.setOperationBudget(
                    findOne(operationVM.operation.getOperationBudget().getIdentifiant()));
        }
        if (operationVM.operation.getExploitation() != null
                && operationVM.operation.getExploitation().getIdentifiant() != null) {
            operationVM.operation.setExploitation(
                    exploitationRepository.findOne(operationVM.operation.getExploitation().getIdentifiant()));
        }
        if (operationVM.operation.getBudget() != null
                && operationVM.operation.getBudget().getIdentifiant() != null) {
            operationVM.operation.setBudget(
                    budgetRepository.findOne(operationVM.operation.getBudget().getIdentifiant()));
        }

        operationVM.operationDetails = operationVM.operationDetails
                .stream()
                .peek(od -> {
                    od.setIdentifiant(null);
                    if (od.getCompte() != null && od.getCompte().getIdentifiant() != null)
                        od.setCompte(compteRepository.findOne(od.getCompte().getIdentifiant()));
                    else if (od.getTresorerie() != null && od.getTresorerie().getIdentifiant() != null)
                        od.setTresorerie(tresorerieRepository.findOne(od.getTresorerie().getIdentifiant()));
                }).collect(Collectors.toList());
    }

    private void controlerLIntegriteDeLOperation(OperationVM operationVM) throws CustomException {
        if (operationVM.operation.getDescription() == null) {
            throw new CustomException(MPConstants.LA_DESCRIPTION_EST_OBLIGATOIRE);
        }
        if (operationVM.operation.getOperationType() == OperationType.DEPENSE
                && operationVM.operationDetails.size() < 2) {
            throw new CustomException(MPConstants.LE_NOMBRE_COMPTE_RENSEIGNER_INCORRECT);
        }
        if (operationVM.operation.getOperationType() == OperationType.PRODUIT
                && operationVM.operationDetails.size() < 2) {
            throw new CustomException(MPConstants.LE_NOMBRE_COMPTE_RENSEIGNER_INCORRECT);
        }
        if (operationVM.operation.getOperationType() == OperationType.VIREMENT
                && operationVM.operationDetails.size() < 2) {
            throw new CustomException(MPConstants.LE_NOMBRE_COMPTE_RENSEIGNER_INCORRECT);
        }
    }

    private void enregistrerLesDetailsDOperation(Operation operation, OperationVM operationVM)
            throws CustomException {
        Double montantTotalDebit = 0D;
        Double montantTotalCredit = 0D;
        for (OperationDetail od : operationVM.operationDetails) {
            if (operationVM.operation.getBudget() == null && od.getTresorerie() != null
                    && od.getOperationSens() == OperationSens.CREDIT) {
                Double solde = tresorerieService.calculerLeSolde(od.getTresorerie());
                if (solde < od.getMontant())
                    throw new CustomException(MPConstants.LE_MONTANT_EST_INSUFFISANT);
            }
            od.setOperation(operation);
            operationDetailRepository.save(od);
            if (od.getOperationSens() == OperationSens.CREDIT)
                montantTotalCredit += od.getMontant();
            else
                montantTotalDebit += od.getMontant();
        }
        if (montantTotalCredit.equals(0D) || !montantTotalCredit.equals(montantTotalDebit)) {
            throw new CustomException(MPConstants.LE_MONTANT_EST_DIFFERENT);
        }
        operation.setMontantTotal(montantTotalCredit);
    }

    @Override
    public Operation modifier(OperationVM operationVM) throws CustomException {
        controlerLIntegriteDeLOperation(operationVM);
        reconstrureLOperation(operationVM);
        Operation operation = repository.saveAndFlush(operationVM.operation);
        operationDetailRepository.deleteByOperationIdentifiant(operation.getIdentifiant());
        enregistrerLesDetailsDOperation(operation, operationVM);
        operation = saveAndFlush(operation);
        miseAJourDeLaVersion(operation, Version.MOTIF_MODIFICATION, operation.getIdentifiant());
        return operation;
    }

    @Override
    public boolean supprimer(Integer operationId) throws CustomException {
        controlerLUtilisationDeLOperation(operationId);
        operationDetailRepository.deleteByOperationIdentifiant(operationId);
        Operation operation = findOne(operationId);
        repository.delete(operationId);
        miseAJourDeLaVersion(operation, Version.MOTIF_SUPPRESSION, operation.getIdentifiant());
        return true;
    }

    @Override
    public boolean supprimerForcer(Integer operationId) throws CustomException {
        ((OperationRepository) repository)
                .findByOperationBudgetIdentifiant(operationId)
                .forEach(o -> {
                    try {
                        supprimer(o.getIdentifiant());
                        miseAJourDeLaVersion(o, Version.MOTIF_SUPPRESSION, o.getIdentifiant());
                    } catch (CustomException e) {
                        Logger.getLogger(OperationService.class.getSimpleName()).warning(e.getMessage());
                    }
                });
        return supprimer(operationId);
    }

    private void controlerLUtilisationDeLOperation(Integer operationId) throws CustomException {
        Long nombreOperation = ((OperationRepository) repository)
                .countByOperationBudgetIdentifiant(operationId);
        if (nombreOperation != 0L) {
            throw new CustomException(MPConstants.OPERATION_DEJA_UTILISER);
        }
    }

    @Override
    public List<OperationVM> recupererLaListeVersionnee(Integer[] ints) {
        return ((OperationRepository) repository).recupererLaListeVersionnee(ints)
                .stream()
                .map(o -> {
                    OperationVM operationVM = new OperationVM();
                    operationVM.operation = o;
                    operationVM.operationDetails = operationDetailRepository
                            .findByOperationIdentifiant(o.getIdentifiant());
                    return operationVM;
                }).collect(Collectors.toList());
    }

    @Override
    public OperationVM recupererLOperationVersionnee(Integer operationId) {
        OperationVM operationVM = new OperationVM();
        operationVM.operation = findOne(operationId);
        operationVM.operationDetails = operationDetailRepository.findByOperationIdentifiant(operationId);
        return operationVM;
    }

    @Autowired
    public void setTresorerieService(ITresorerieService tresorerieService) {
        this.tresorerieService = tresorerieService;
    }

    @Autowired
    public void setOperationDetailRepository(OperationDetailRepository operationDetailRepository) {
        this.operationDetailRepository = operationDetailRepository;
    }

    @Autowired
    public void setExploitationRepository(ExploitationRepository exploitationRepository) {
        this.exploitationRepository = exploitationRepository;
    }

    @Autowired
    public void setBudgetRepository(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Autowired
    public void setCompteRepository(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    @Autowired
    public void setTresorerieRepository(TresorerieRepository tresorerieRepository) {
        this.tresorerieRepository = tresorerieRepository;
    }

    @Autowired
    public void setVersionRepository(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }
}
