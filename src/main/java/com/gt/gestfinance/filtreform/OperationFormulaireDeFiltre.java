package com.gt.gestfinance.filtreform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gt.base.filterform.BaseFilterForm;
import com.gt.gestfinance.entity.Operation;
import com.gt.gestfinance.repository.spec.OperationSpec;
import org.springframework.data.jpa.domain.Specification;

/**
 * Formulaire de filtre sur les dnnées de Operation
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public class OperationFormulaireDeFiltre extends BaseFilterForm<Operation> {

    private Operation operation;

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    /**
     * Recuperer les donnees du filtre
     *
     * @return
     */
    @JsonIgnore
    @Override
    public Specification<Operation> getCriteres() {
        return OperationSpec.allCriteres(this.operation);
    }
}
