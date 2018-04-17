package com.gt.gestfinance.service;

import com.gt.base.exception.CustomException;
import com.gt.base.service.IBaseEntityService;
import com.gt.gestfinance.dto.OperationVM;
import com.gt.gestfinance.entity.Operation;

/**
 * Interface Service de l'entité Operation
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public interface IOperationService extends IBaseEntityService<Operation, Integer> {

    Operation ajouter(OperationVM operationVM) throws CustomException;

    Operation modifier(OperationVM operationVM) throws CustomException;

    boolean supprimer(Integer operationId) throws CustomException;
}
