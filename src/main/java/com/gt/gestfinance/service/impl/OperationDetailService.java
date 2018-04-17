package com.gt.gestfinance.service.impl;

import com.gt.base.service.impl.BaseEntityService;
import com.gt.gestfinance.entity.Operation;
import com.gt.gestfinance.entity.OperationDetail;
import com.gt.gestfinance.repository.OperationDetailRepository;
import com.gt.gestfinance.repository.OperationRepository;
import com.gt.gestfinance.service.IOperationDetailService;
import com.gt.gestfinance.util.OperationAvecMontantRestantList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe Service de l'entité OperationDetail
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 31/03/2018
 */
@Service
public class OperationDetailService extends BaseEntityService<OperationDetail, Integer> implements IOperationDetailService {

    private OperationRepository operationRepository;

    @Autowired
    public OperationDetailService(OperationDetailRepository repository) {
        super(repository);
    }

    @Override
    public OperationAvecMontantRestantList recupererOperationAvecMontantRestant(Integer operationBudgetId) {
        OperationAvecMontantRestantList operationAvecMontantRestantList = new OperationAvecMontantRestantList();
        operationAvecMontantRestantList.operationDetails = ((OperationDetailRepository) repository)
                .findByOperationIdentifiant(operationBudgetId);
        double sommeConsommer = operationRepository
                .findByOperationBudgetIdentifiant(operationBudgetId)
                .stream().mapToDouble(Operation::getMontantTotal).sum();
        operationAvecMontantRestantList.montantRestant = operationRepository
                .findOne(operationBudgetId).getMontantTotal() - sommeConsommer;
        return operationAvecMontantRestantList;
    }

    @Autowired
    public void setOperationRepository(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }
}
