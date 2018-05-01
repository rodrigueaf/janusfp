package com.gt.gestfinance.filtreform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gt.gestfinance.filtreform.BaseFilterForm;
import com.gt.gestfinance.entity.Utilisateur;
import com.gt.gestfinance.repository.spec.UtilisateurSpec;
import org.springframework.data.jpa.domain.Specification;

/**
 * Formulaire de filtre sur les dnnées de Utilisateur
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public class UtilisateurFormulaireDeFiltre extends BaseFilterForm<Utilisateur> {

    private Utilisateur utilisateur;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    /**
     * Recuperer les donnees du filtre
     *
     * @return
     */
    @JsonIgnore
    @Override
    public Specification<Utilisateur> getCriteres() {
        return UtilisateurSpec.allCriteres(this.utilisateur);
    }
}
