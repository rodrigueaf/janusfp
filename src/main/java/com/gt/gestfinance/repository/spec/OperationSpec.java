package com.gt.gestfinance.repository.spec;

import com.gt.base.repository.spec.BaseSpecifications;
import com.gt.gestfinance.entity.Operation;
import com.gt.gestfinance.entity.OperationType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

/**
 * Specification de l'entité Operation
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public class OperationSpec extends BaseSpecifications {

    private static Specifications spec;

    private OperationSpec() {
        // DO
    }

    /**
     * Filtrer les données
     *
     * @param operation
     * @return
     */
    @SuppressWarnings({"unchecked", "null"})
    public static Specification<Operation> allCriteres(Operation operation) {

        setSpec(Specifications.where((root, query, cb) -> cb.equal(cb.literal(1), 1)));

        OperationSpec.filtrerParDescription(getSpec(), operation.getDescription());
        OperationSpec.filtrerParOperationType(getSpec(), operation.getOperationType());
        if (operation.getBudget() != null) {
            if (operation.getBudget().getIdentifiant() == null)
                OperationSpec.filtrerParOperationBudgetiser(getSpec());
            else
                OperationSpec.filtrerParBudget(getSpec(), operation.getBudget().getIdentifiant());
        }
        if (operation.getExploitation() != null)
            if (operation.getExploitation().getIdentifiant() != null)
                OperationSpec.filtrerParExploitation(getSpec(), operation.getExploitation().getIdentifiant());

        return spec;
    }

    private static void filtrerParDescription(Specifications spec, String description) {
        if (description != null && !description.isEmpty()) {
            setSpec(spec.and(containsLike("description", description)));
        }
    }

    private static void filtrerParOperationType(Specifications spec, OperationType operationType) {
        if (operationType != null) {
            setSpec(spec.and(enumMatcher("operationType", operationType)));
        }
    }

    private static void filtrerParOperationBudgetiser(Specifications spec) {
        setSpec(spec.and((root, query, cb) -> cb.isNotNull(root.get("budget").get("identifiant"))));
    }

    private static void filtrerParBudget(Specifications spec, Integer budgetId) {
        setSpec(spec.and((root, query, cb) -> cb.equal(root.get("budget").get("identifiant"), budgetId)));
    }

    private static void filtrerParExploitation(Specifications spec, Integer exploitationId) {
        setSpec(spec.and((root, query, cb) -> cb.equal(root.get("exploitation").get("identifiant"), exploitationId)));
    }

    public static Specifications getSpec() {
        return spec;
    }

    public static void setSpec(Specifications spec) {
        OperationSpec.spec = spec;
    }
}
