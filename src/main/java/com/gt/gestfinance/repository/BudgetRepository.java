package com.gt.gestfinance.repository;

import com.gt.gestfinance.entity.Budget;
import com.gt.gestfinance.entity.Tresorerie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Query("select c from Budget c where c.id in :idSet")
    List<Budget> recupererLaListeVersionnee(@Param("idSet") Integer[] idSet);
}
