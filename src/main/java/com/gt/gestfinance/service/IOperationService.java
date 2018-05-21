package com.gt.gestfinance.service;

import com.gt.gestfinance.dto.OperationVM;
import com.gt.gestfinance.entity.Operation;
import com.gt.gestfinance.exception.CustomException;

import java.util.List;

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

    boolean supprimerForcer(Integer operationId) throws CustomException;

    List<OperationVM> recupererLaListeVersionnee(Integer[] ints);

    OperationVM recupererLOperationVersionnee(Integer operationId);
}
