package com.gt.gestfinance.service;

import com.gt.gestfinance.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * Interface pour définir les méthodes basique des services
 *
 * @author <a href="mailto:ali.amadou@ace3i.com?">Yasser Ali</a>,
 * <a href="mailto:afandi-ekoue.amah-tchoutchoui@ace3i.com?">Ekoue</a>
 * @param <T> : L'entié
 * @param <I> : La clé primaire
 *
 */
public interface IGenericService<T, I extends Serializable> {

    /**
     * Création d'une entité
     *
     * @param entity : l'entité à créer.
     *
     * @return l'entité créer
     * @throws CustomException
     */
    public T save(T entity) throws CustomException;

    /**
     * Création de plusieurs entités
     *
     * @param entities : liste des entités à créer
     *
     * @return liste des entités créées
     */
    public List<T> save(Iterable<T> entities);

    /**
     * Modification d'une entité
     *
     * @param entity : l'entité à modifier
     *
     * @return l'entité modifiée
     * @throws CustomException
     */
    public T saveAndFlush(T entity) throws CustomException;

    /**
     * Modification en lot des entités
     * 
     * @param entities
     * @return la liste des entités modifiées
     */
	public List<T> saveAndFlush(List<T> entities);
    
    /**
     * Récupération d'une entité en fonction de son id
     *
     * @param id : clé primaire ou Id de l'entité
     *
     * @return l'entité dont l'Id est passé en paramètre.
     */
    public T findOne(I id);

    /**
     * Suppression d'une entité en fonction de son id
     *
     * @param id : clé primaire ou l'Id de l'entité
     *
     * @return true si l'opération s'est bien passé, sinon false
     */
    public boolean delete(I id);

    /**
     * Suppression d'une entité
     *
     * @param entity : l'objet entité à supprimer
     *
     * @return true si l'opération s'est bien passé, sinon false
     */
    public boolean delete(T entity);

    /**
     * Suppression toutes les lignes
     *
     * @return true si l'opération s'est bien passé, sinon false
     */
    public boolean deleteAll();

    /**
     * Liste des enregistrements d'une entité <code>T</code>
     *
     * @return toutes les enregistrements d'une entité <code>T</code>
     */
    public List<T> findAll();

    /**
     * Liste des enregistrements paginées d'une entité <code>T</code>
     *
     * @param p
     *
     * @return une liste de page
     *
     */
    public Page<T> findAll(Pageable p);

    /**
     * Renvoie une page d'enregistrements d'une entité <code>T</code> en
     * fonction des critères fournies
     *
     * @param spec
     * @param pageable
     * @return page d'éléments trouvés <code>T</code>
     */
    public Page<T> findAllByCriteres(Specification<T> spec, Pageable pageable);

    /**
     * Vérifie l'existance de l'objet
     *
     * @param i : La clé primaire
     * @return un boolan
     */
    public boolean isExist(I i);

    /**
     * Compter le nombre d'enregistrement sur une entité <code>T</code>
     *
     * @return le nombre compter
     */
    public long count();
    
}
