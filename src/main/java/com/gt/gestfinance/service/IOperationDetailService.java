package com.gt.gestfinance.service;

import com.gt.gestfinance.entity.OperationDetail;
import com.gt.gestfinance.util.OperationAvecMontantRestantList;

/**
 * Interface Service de l'entité OperationDetail
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public interface IOperationDetailService extends IBaseEntityService<OperationDetail, Integer> {

    OperationAvecMontantRestantList recupererOperationAvecMontantRestant(Integer operationId);
}
