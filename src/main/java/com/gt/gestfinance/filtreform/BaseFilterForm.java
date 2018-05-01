package com.gt.gestfinance.filtreform;

import org.springframework.data.jpa.domain.Specification;

/**
 * Classe de base des formulaires de filtre
 *
 * @author <a href="mailto:ali.amadou@ace3i.com?">Yasser Ali</a>
 * @param <T> : L'entité
 *
 */
public abstract class BaseFilterForm<T> {

    /**
     * numéro de la page de pagination
     */
    protected Integer page;

    /**
     * nombre d'élément à afficher par liste
     */
    protected Integer size;

    /**
     * @return the page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return the size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * Retourne la Specification
     * @return
     */
    public abstract Specification<T> getCriteres();
}
