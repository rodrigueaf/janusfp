package com.gt.gestfinance.repository.spec;


import com.gt.gestfinance.entity.Utilisateur;
import com.gt.gestfinance.util.State;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

/**
 * Specification de l'entité Utilisateur
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public class UtilisateurSpec extends BaseSpecifications {

    private static Specifications spec;

    private UtilisateurSpec() {
        // do
    }

    /**
     * Filtrer les données
     *
     * @param utilisateur
     * @return
     */
    @SuppressWarnings({"unchecked", "null"})
    public static Specification<Utilisateur> allCriteres(Utilisateur utilisateur) {

        setSpec(Specifications.where((root, query, cb) -> cb.equal(cb.literal(1), 1)));

        UtilisateurSpec.filtrerParLeLogin(getSpec(), utilisateur.getLogin());
        UtilisateurSpec.filtrerParLEmail(getSpec(), utilisateur.getEmail());
        UtilisateurSpec.filtrerParLePrenom(getSpec(), utilisateur.getPrenom());
        UtilisateurSpec.filtrerParLeNom(getSpec(), utilisateur.getNom());
        UtilisateurSpec.filtrerParLEtat(getSpec(), utilisateur.getState());

        return spec;
    }

    private static void filtrerParLEtat(Specifications spec, State etat) {
        if (etat != null) {
            setSpec(spec.and(enumMatcher("state", etat)));
        }
    }

    private static void filtrerParLeLogin(Specifications spec, String login) {
        if (login != null && !login.isEmpty()) {
            setSpec(spec.and(containsLike("login", login)));
        }
    }

    private static void filtrerParLePrenom(Specifications spec, String prenom) {
        if (prenom != null && !prenom.isEmpty()) {
            setSpec(spec.and(containsLike("prenom", prenom)));
        }
    }

    private static void filtrerParLeNom(Specifications spec, String nom) {
        if (nom != null && !nom.isEmpty()) {
            setSpec(spec.and(containsLike("nom", nom)));
        }
    }

    private static void filtrerParLEmail(Specifications spec, String email) {
        if (email != null && !email.isEmpty()) {
            setSpec(spec.and(containsLike("email", email)));
        }
    }

    public static Specifications getSpec() {
        return spec;
    }

    public static void setSpec(Specifications spec) {
        UtilisateurSpec.spec = spec;
    }
}
