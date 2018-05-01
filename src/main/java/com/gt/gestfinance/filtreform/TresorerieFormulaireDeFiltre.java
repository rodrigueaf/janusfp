package com.gt.gestfinance.filtreform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gt.gestfinance.filtreform.BaseFilterForm;
import com.gt.gestfinance.entity.Tresorerie;
import com.gt.gestfinance.repository.spec.TresorerieSpec;
import org.springframework.data.jpa.domain.Specification;

/**
 * Formulaire de filtre sur les dnnées de Budget
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/10/2017
 */
public class TresorerieFormulaireDeFiltre extends BaseFilterForm<Tresorerie> {

    private Tresorerie tresorerie;

    public Tresorerie getTresorerie() {
        return tresorerie;
    }

    public void setTresorerie(Tresorerie tresorerie) {
        this.tresorerie = tresorerie;
    }

    /**
     * Recuperer les donnees du filtre
     *
     * @return
     */
    @JsonIgnore
    @Override
    public Specification<Tresorerie> getCriteres() {
        return TresorerieSpec.allCriteres(this.tresorerie);
    }
}
