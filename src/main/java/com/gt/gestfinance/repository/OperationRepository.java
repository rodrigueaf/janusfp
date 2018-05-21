package com.gt.gestfinance.repository;


import com.gt.gestfinance.entity.Budget;
import com.gt.gestfinance.entity.Operation;
import com.gt.gestfinance.entity.OperationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Le repository de l'entité Operation
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
public interface OperationRepository extends BaseEntityRepository<Operation, Integer> {

    List<Operation> findByCreatedBy(String login);

    Page<Operation> findByBudgetIsNullOrderByIdentifiantDesc(Pageable pageable);

    Long countByOperationBudgetIdentifiant(Integer operationId);

    List<Operation> findByOperationBudgetIdentifiant(Integer operationId);

    List<Operation> findByBudgetIdentifiant(Integer budgetId);

    List<Operation> findByExploitationIdentifiant(Integer exploitationId);

    @Query("select coalesce(sum(o.montantTotal), 0) from Operation o where o.budget.identifiant = ?1 and o.operationType = ?2")
    Double sumByBudgetIdentifiantAndOperationType(Integer budgetId, OperationType operationType);

    @Query("select coalesce(sum(o.montantTotal), 0) from Operation o where o.exploitation.identifiant = ?1 and o.operationType = ?2")
    Double sumByExploitationIdentifiantAndOperationType(Integer exploitationId, OperationType operationType);

    @Query("select c from Operation c where c.id in :idSet order by c.operationBudget.identifiant asc")
    List<Operation> recupererLaListeVersionnee(@Param("idSet") Integer[] idSet);
}
