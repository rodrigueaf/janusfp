package com.gt.gestfinance.service;

import com.gt.gestfinance.entity.Budget;
import com.gt.gestfinance.exception.CustomException;

import java.util.List;

/**
 * Interface Service de l'entité Budget
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public interface IBudgetService extends IBaseEntityService<Budget, Integer> {

    List<Budget> recupererLaListeVersionnee(Integer[] ints);

    boolean supprimerForcer(Integer budgetId) throws CustomException;

    boolean supprimer(Integer budgetId) throws CustomException;
}
