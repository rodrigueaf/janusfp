package com.gt.gestfinance.repository;

import com.gt.gestfinance.entity.Budget;

import java.util.Optional;

/**
 * Le repository de l'entité Budget
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
public interface BudgetRepository extends BaseEntityRepository<Budget, Integer> {

    Optional<Budget> findByLibelle(String libelle);
}
