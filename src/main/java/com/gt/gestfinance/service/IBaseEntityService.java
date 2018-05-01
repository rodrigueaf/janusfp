package com.gt.gestfinance.service;

import com.gt.gestfinance.entity.AbstractAuditingEntity;
import com.gt.gestfinance.util.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

/**
 * Interface pour définir les méthodes basique des services des BaseEntity
 *
 * @author <a href="mailto:ali.amadou@ace3i.com?">Yasser Ali</a>,
 * @param <T> : L'entié
 * @param <I> : La clé primaire
 *
 */
public interface IBaseEntityService<T extends AbstractAuditingEntity, I extends Serializable> extends IGenericService<T, I> {

    /**
     * Liste des enregistrements paginées d'une entité non supprimé
     * logiquement<code>T</code>
     *
     * @param p : L'objet encapsulant les informations de pagination
     * @return une liste de page
     *
     */
    public Page<T> findByStateNotDeleted(Pageable p);

    /**
     * Liste des enregistrements par page en fonctions de leur état
     *
     * @param state état
     * @param p L'objet encapsulant les informations de pagination
     * @return
     */
    public Page<T> findByState(State state, Pageable p);

    /**
     * Active une entité
     *
     * @param i : L'id de l'entité
     * @return boolean
     */
    public boolean enabled(I i);

    /**
     * Désactiver une entité
     *
     * @param i : L'id de l'entité
     * @return boolean
     */
    public boolean disabled(I i);

    /**
     * Suppression logique d'une entité
     *
     * @param id
     * @return boolean
     */
    public boolean deleteSoft(I id);

}
