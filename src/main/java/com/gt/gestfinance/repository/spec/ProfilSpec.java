package com.gt.gestfinance.repository.spec;


import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.util.State;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

/**
 * Specification de l'entité Profil
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public class ProfilSpec extends BaseSpecifications {

    private static Specifications spec;

    private ProfilSpec() {
        // DO
    }

    /**
     * Filtrer les données
     *
     * @param profil
     * @return
     */
    @SuppressWarnings({"unchecked", "null"})
    public static Specification<Profil> allCriteres(Profil profil) {

        setSpec(Specifications.where((root, query, cb) -> cb.equal(cb.literal(1), 1)));

        ProfilSpec.filtrerParNom(getSpec(), profil.getNom());
        ProfilSpec.filtrerParLEtat(getSpec(), profil.getState());

        return spec;
    }

    private static void filtrerParLEtat(Specifications spec, State etat) {
        if (etat != null) {
            setSpec(spec.and(enumMatcher("state", etat)));
        }
    }

    private static void filtrerParNom(Specifications spec, String nom) {
        if (nom != null && !nom.isEmpty()) {
            setSpec(spec.and(containsLike("nom", nom)));
        }
    }

    public static Specifications getSpec() {
        return spec;
    }

    public static void setSpec(Specifications spec) {
        ProfilSpec.spec = spec;
    }
}
