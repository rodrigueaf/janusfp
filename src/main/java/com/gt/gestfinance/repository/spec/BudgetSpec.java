package com.gt.gestfinance.repository.spec;


import com.gt.gestfinance.entity.Budget;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

/**
 * Specification de l'entité Budget
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public class BudgetSpec extends BaseSpecifications {

    private static Specifications spec;

    private BudgetSpec() {
        // DO
    }

    /**
     * Filtrer les données
     *
     * @param budget
     * @return
     */
    @SuppressWarnings({"unchecked", "null"})
    public static Specification<Budget> allCriteres(Budget budget) {

        setSpec(Specifications.where((root, query, cb) -> cb.equal(cb.literal(1), 1)));

        BudgetSpec.filtrerParLibelle(getSpec(), budget.getLibelle());

        return spec;
    }

    private static void filtrerParLibelle(Specifications spec, String libelle) {
        if (libelle != null && !libelle.isEmpty()) {
            setSpec(spec.and(containsLike("libelle", libelle)));
        }
    }

    public static Specifications getSpec() {
        return spec;
    }

    public static void setSpec(Specifications spec) {
        BudgetSpec.spec = spec;
    }
}
