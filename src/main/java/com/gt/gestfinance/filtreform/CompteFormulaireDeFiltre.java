package com.gt.gestfinance.filtreform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gt.gestfinance.filtreform.BaseFilterForm;
import com.gt.gestfinance.entity.Compte;
import com.gt.gestfinance.repository.spec.CompteSpec;
import org.springframework.data.jpa.domain.Specification;

/**
 * Formulaire de filtre sur les dnnées de Profil
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public class CompteFormulaireDeFiltre extends BaseFilterForm<Compte> {

    private Compte compte;
    private String compteTypeDifferent;

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public String getCompteTypeDifferent() {
        return compteTypeDifferent;
    }

    public void setCompteTypeDifferent(String compteTypeDifferent) {
        this.compteTypeDifferent = compteTypeDifferent;
    }

    /**
     * Recuperer les donnees du filtre
     *
     * @return
     */
    @JsonIgnore
    @Override
    public Specification<Compte> getCriteres() {
        return CompteSpec.allCriteres(this);
    }
}
