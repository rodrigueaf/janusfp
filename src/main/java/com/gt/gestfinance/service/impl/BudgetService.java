package com.gt.gestfinance.service.impl;

import com.gt.base.exception.CustomException;
import com.gt.base.service.impl.BaseEntityService;
import com.gt.gestfinance.entity.Budget;
import com.gt.gestfinance.repository.BudgetRepository;
import com.gt.gestfinance.service.IBudgetService;
import com.gt.gestfinance.util.MPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Classe Service de l'entité Budget
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 31/03/2018
 */
@Service
public class BudgetService extends BaseEntityService<Budget, Integer> implements IBudgetService {


    @Autowired
    public BudgetService(BudgetRepository repository) {
        super(repository);
    }

    @Override
    public synchronized Budget save(Budget budget) throws CustomException {
        controlerLIntegriteDuBudget(budget);
        return super.save(budget);
    }

    @Override
    public synchronized Budget saveAndFlush(Budget budget) throws CustomException {
        controlerLIntegriteDuBudget(budget);
        return super.saveAndFlush(budget);
    }

    private void controlerLIntegriteDuBudget(Budget budget) throws CustomException {
        if(budget.getLibelle() == null || budget.getLibelle().isEmpty()){
            throw new CustomException(MPConstants.LE_LIBELLE_DU_BUDGET_EST_OBLIGATOIRE);
        }
        Optional<Budget> op = ((BudgetRepository) repository).findByLibelle(budget.getLibelle());
        if(op.isPresent() && !op.get().getIdentifiant().equals(budget.getIdentifiant())){
            throw new CustomException(MPConstants.LE_LIBELLE_DU_BUDGET_EXSTE_DEJA);
        }
    }
}
