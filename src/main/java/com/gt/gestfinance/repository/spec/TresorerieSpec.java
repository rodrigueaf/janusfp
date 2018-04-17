package com.gt.gestfinance.repository.spec;

import com.gt.base.repository.spec.BaseSpecifications;
import com.gt.gestfinance.entity.Compte;
import com.gt.gestfinance.entity.Tresorerie;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

/**
 * Specification de l'entité Tresorerie
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public class TresorerieSpec extends BaseSpecifications {

    private static Specifications spec;

    private TresorerieSpec() {
        // DO
    }

    /**
     * Filtrer les données
     *
     * @param tresorerie
     * @return
     */
    @SuppressWarnings({"unchecked", "null"})
    public static Specification<Tresorerie> allCriteres(Tresorerie tresorerie) {

        setSpec(Specifications.where((root, query, cb) -> cb.equal(cb.literal(1), 1)));

        TresorerieSpec.filtrerParCompteLogique(getSpec(), tresorerie.getCompteLogique());
        TresorerieSpec.filtrerParComptePhysique(getSpec(), tresorerie.getComptePhysique());

        return spec;
    }

    private static void filtrerParCompteLogique(Specifications spec, Compte compte) {
        if (compte != null && compte.getLibelle() != null && !compte.getLibelle().isEmpty()) {
            setSpec(spec.and((root, query, cb) -> cb.like(cb.lower(
                    root.get("compteLogique").get("libelle").as(String.class)),
                    "%" + compte.getLibelle().toLowerCase() + "%")));
        }
    }

    private static void filtrerParComptePhysique(Specifications spec, Compte compte) {
        if (compte != null && compte.getLibelle() != null && !compte.getLibelle().isEmpty()) {
            setSpec(spec.and((root, query, cb) -> cb.like(cb.lower(
                    root.get("comptePhysique").get("libelle").as(String.class)),
                    "%" + compte.getLibelle().toLowerCase() + "%")));
        }
    }

    public static Specifications getSpec() {
        return spec;
    }

    public static void setSpec(Specifications spec) {
        TresorerieSpec.spec = spec;
    }
}
