package com.gt.gestfinance.service.impl;

import com.gt.gestfinance.dto.OperationVM;
import com.gt.gestfinance.entity.Operation;
import com.gt.gestfinance.entity.OperationDetail;
import com.gt.gestfinance.entity.OperationSens;
import com.gt.gestfinance.entity.OperationType;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.repository.OperationDetailRepository;
import com.gt.gestfinance.repository.OperationRepository;
import com.gt.gestfinance.service.IOperationService;
import com.gt.gestfinance.service.ITresorerieService;
import com.gt.gestfinance.util.MPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public OperationService(OperationRepository repository) {
        super(repository);
    }

    @Override
    public Operation ajouter(OperationVM operationVM) throws CustomException {
        controlerLIntegriteDeLOperation(operationVM);

        Operation operation = repository.save(operationVM.operation);

        enregistrerLesDetailsDOperation(operation, operationVM);

        return saveAndFlush(operation);
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

        Operation operation = repository.saveAndFlush(operationVM.operation);

        operationDetailRepository.deleteByOperationIdentifiant(operation.getIdentifiant());

        enregistrerLesDetailsDOperation(operation, operationVM);

        return saveAndFlush(operation);
    }

    @Override
    public boolean supprimer(Integer operationId) throws CustomException {
        controlerLUtilisationDeLOperation(operationId);
        operationDetailRepository.deleteByOperationIdentifiant(operationId);
        repository.delete(operationId);
        return true;
    }

    private void controlerLUtilisationDeLOperation(Integer operationId) throws CustomException {
        Long nombreOperation = ((OperationRepository) repository)
                .countByOperationBudgetIdentifiant(operationId);
        if (nombreOperation != 0L) {
            throw new CustomException(MPConstants.OPERATION_DEJA_UTILISER);
        }
    }

    @Autowired
    public void setTresorerieService(ITresorerieService tresorerieService) {
        this.tresorerieService = tresorerieService;
    }

    @Autowired
    public void setOperationDetailRepository(OperationDetailRepository operationDetailRepository) {
        this.operationDetailRepository = operationDetailRepository;
    }
}
