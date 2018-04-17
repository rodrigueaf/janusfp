package com.gt.gestfinance.filtreform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gt.base.filterform.BaseFilterForm;
import com.gt.gestfinance.entity.Budget;
import com.gt.gestfinance.repository.spec.BudgetSpec;
import org.springframework.data.jpa.domain.Specification;

/**
 * Formulaire de filtre sur les dnnées de Budget
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public class BudgetFormulaireDeFiltre extends BaseFilterForm<Budget> {

    private Budget budget;

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    /**
     * Recuperer les donnees du filtre
     *
     * @return
     */
    @JsonIgnore
    @Override
    public Specification<Budget> getCriteres() {
        return BudgetSpec.allCriteres(this.budget);
    }
}
