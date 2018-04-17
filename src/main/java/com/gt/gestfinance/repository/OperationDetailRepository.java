package com.gt.gestfinance.repository;

import com.gt.base.repository.BaseEntityRepository;
import com.gt.gestfinance.entity.OperationDetail;
import com.gt.gestfinance.entity.OperationSens;

import java.util.List;

/**
 * Le repository de l'entité OperationDetail
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
public interface OperationDetailRepository extends BaseEntityRepository<OperationDetail, Integer> {

    List<OperationDetail> findByOperationSens(OperationSens operationSens);

    List<OperationDetail> findByOperationIdentifiant(Integer operationId);

    void deleteByOperationIdentifiant(Integer operationId);

    List<OperationDetail> findByCompteIdentifiant(Integer compteId);

    List<OperationDetail> findByOperationBudgetIsNullAndTresorerieIdentifiantAndOperationSens(Integer tresorerieId, OperationSens sens);

    List<OperationDetail> findByOperationBudgetIdentifiantAndCompteIdentifiant(Integer budgetId, Integer compteId);

    List<OperationDetail> findByOperationOperationBudgetBudgetIdentifiantAndCompteIdentifiant(Integer budgetId, Integer compteId);

    List<OperationDetail> findByOperationBudgetIsNullAndCompteIdentifiant(Integer compteId);

    List<OperationDetail> findByOperationBudgetIsNullAndTresorerieIdentifiant(Integer tresorerieId);
}
