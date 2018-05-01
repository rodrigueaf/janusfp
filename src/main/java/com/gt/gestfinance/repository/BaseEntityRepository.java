package com.gt.gestfinance.repository;

import com.gt.gestfinance.entity.AbstractAuditingEntity;
import com.gt.gestfinance.util.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Repository parent des instance de <code>Base entit</code>
 *
 * @author <a href="mailto:ali.amadou@ace3i.com?">Yasser Ali</a>
 *
 * @param <T> Classe entité
 * @param <I> Classe de l'Id
 */
@NoRepositoryBean
public interface BaseEntityRepository<T extends AbstractAuditingEntity, I extends Serializable> extends GenericRepository<T, I> {

    /**
     * Renvoie la liste des éléments dont la valeur de l'attribut state est
     * égale au paramètre
     *
     * @param state
     * @return
     */
    List<T> findByState(State state);

    /**
     * Renvoie la page des éléments dont la valeur de l'attribut state est égale
     * au paramètre
     *
     * @param state
     * @param pageable
     * @return
     */
    Page<T> findByState(State state, Pageable pageable);

    /**
     * Renvoie la liste des éléments dont la valeur de l'attribut state est
     * différente au paramètre
     *
     * @param state
     * @return
     */
    List<T> findByStateNot(State state);

    /**
     * Renvoie la page des éléments dont la valeur de l'attribut state est
     * différente au paramètre
     *
     * @param state
     * @param pageable
     * @return
     */
    Page<T> findByStateNot(State state, Pageable pageable);

}
