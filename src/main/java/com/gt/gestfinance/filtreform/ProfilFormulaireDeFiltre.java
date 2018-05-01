package com.gt.gestfinance.filtreform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gt.gestfinance.filtreform.BaseFilterForm;
import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.repository.spec.ProfilSpec;
import org.springframework.data.jpa.domain.Specification;

/**
 * Formulaire de filtre sur les dnnées de Profil
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public class ProfilFormulaireDeFiltre extends BaseFilterForm<Profil> {

    private Profil profil;

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    /**
     * Recuperer les donnees du filtre
     *
     * @return
     */
    @JsonIgnore
    @Override
    public Specification<Profil> getCriteres() {
        return ProfilSpec.allCriteres(this.profil);
    }
}
