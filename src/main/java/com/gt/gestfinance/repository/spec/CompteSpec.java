package com.gt.gestfinance.repository.spec;


import com.gt.gestfinance.entity.Compte;
import com.gt.gestfinance.entity.CompteForme;
import com.gt.gestfinance.entity.CompteType;
import com.gt.gestfinance.filtreform.CompteFormulaireDeFiltre;
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
public class CompteSpec extends BaseSpecifications {

    private static Specifications spec;

    private CompteSpec() {
        // DO
    }

    /**
     * Filtrer les données
     *
     * @param compteFormulaireDeFiltre
     * @return
     */
    @SuppressWarnings({"unchecked", "null"})
    public static Specification<Compte> allCriteres(CompteFormulaireDeFiltre compteFormulaireDeFiltre) {

        setSpec(Specifications.where((root, query, cb) -> cb.equal(cb.literal(1), 1)));

        CompteSpec.filtrerParLibelle(getSpec(), compteFormulaireDeFiltre.getCompte().getLibelle());
        CompteSpec.filtrerParLaForme(getSpec(), compteFormulaireDeFiltre.getCompte().getCompteForme());
        CompteSpec.filtrerParLeType(getSpec(), compteFormulaireDeFiltre.getCompte().getCompteType());
        CompteSpec.filtrerParLEtat(getSpec(), compteFormulaireDeFiltre.getCompte().getState());
        if (compteFormulaireDeFiltre.getCompteTypeDifferent() != null) {
            setSpec(getSpec().and((root, query, cb) -> cb.notEqual(root.get("compteType"),
                    CompteType.valueOf(compteFormulaireDeFiltre.getCompteTypeDifferent()))));
        }
        if (compteFormulaireDeFiltre.getCompte().getUtilisable() != null)
            if (compteFormulaireDeFiltre.getCompte().getUtilisable())
                filtrerParUtilisable(getSpec());
            else
                filtrerParNonUtilisable(getSpec());

        return spec;
    }

    private static void filtrerParUtilisable(Specifications spec) {
        setSpec(spec.and((root, query, cb) -> cb.isTrue(root.get("utilisable").as(Boolean.class))));
    }

    private static void filtrerParNonUtilisable(Specifications spec) {
        setSpec(spec.and((root, query, cb) -> cb.isFalse(root.get("utilisable").as(Boolean.class))));
    }

    private static void filtrerParLibelle(Specifications spec, String libelle) {
        if (libelle != null && !libelle.isEmpty()) {
            setSpec(spec.and(containsLike("libelle", libelle)));
        }
    }

    private static void filtrerParLEtat(Specifications spec, State etat) {
        if (etat != null) {
            setSpec(spec.and(enumMatcher("state", etat)));
        }
    }

    private static void filtrerParLaForme(Specifications spec, CompteForme compteForme) {
        if (compteForme != null) {
            setSpec(spec.and(enumMatcher("compteForme", compteForme)));
        }
    }

    private static void filtrerParLeType(Specifications spec, CompteType compteType) {
        if (compteType != null) {
            setSpec(spec.and(enumMatcher("compteType", compteType)));
        }
    }

    public static Specifications getSpec() {
        return spec;
    }

    public static void setSpec(Specifications spec) {
        CompteSpec.spec = spec;
    }
}
