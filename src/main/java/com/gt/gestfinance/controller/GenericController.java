package com.gt.gestfinance.controller;

import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.filtreform.BaseFilterForm;
import com.gt.gestfinance.service.IGenericService;
import com.gt.gestfinance.util.DefaultMP;
import com.gt.gestfinance.util.Response;
import com.gt.gestfinance.util.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * Le contrôleur global qui fourni les méthodes basiques (CRUD)
 * <p>
 * chaque méthode retourne un objet réponse et un code status http. l'objet
 * réponse contient ou non une donnée et ou non un objet <code>Message</code>.
 * Le code status diffère en fonction du succès de l'opération ou pas.( OK :
 * 200, 202, ...) (Error : 404, 500, ...)
 *
 * @param <T> : L'entité
 * @param <I> : La clé primaire
 * @author <a href="mailto:ali.amadou@ace3i.com?">Yasser Ali</a>,
 * <a href="mailto:afandi-ekoue.amah-tchoutchoui@ace3i.com?">Ekoue</a>
 */
public abstract class GenericController<T, I extends Serializable> {

    /**
     * Une instance de logger pour tous les services du module.
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Une instance de service pour tous les contrôleurs dérivés de cette classe
     */
    protected IGenericService<T, I> service;

    /**
     * Créer un controlleur
     *
     * @param service : une instance du service de l'entité dont le contrôleur
     *                est dérivé de cette classe.
     */
    protected GenericController(IGenericService<T, I> service) {
        super();
        this.service = service;
    }

    /**
     * Méthode rest de création d'une entité
     *
     * @param t : l'entité à créer
     * @return un objet réponse et un code status http
     * @throws CustomException
     */
    public ResponseEntity<Response> create(@Valid T t) throws CustomException {
        T entity = this.service.save(t);

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(entity).buildI18n(), HttpStatus.CREATED);
    }

    /**
     * Méthode rest de récupération d'une entité par son <code>Id</code>
     *
     * @param id : clé primaire de l'entité
     * @return un objet réponse et un code status http
     */
    public ResponseEntity<Response> readOne(I id) {
        T entity = this.service.findOne(id);

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(entity).buildI18n(), HttpStatus.OK);
    }

    /**
     * Méthode rest de récupération des enrégistrements paginés ou non, d'une
     * entité en fonction des paramètre fournis de pagination existant ou pas.
     *
     * @param page : numéro de page (optionnel)
     * @param size : nombre de ligne (optionnel)
     * @return un objet réponse et un code status http
     */
    public ResponseEntity<Response> readAll(Integer page, Integer size) {

        Page<T> entities;
        Pageable p = null;
        if ((page == null) || (size == null)) {
            entities = this.service.findAll(p);
        } else {
            p = new PageRequest(page, size);
            entities = this.service.findAll(p);
        }

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(entities).buildI18n(), HttpStatus.OK);
    }

    /**
     * Méthode rest de récupération des enrégistrements paginés ou non et
     * filtrés, d'une entité en fonction des paramètre fournis de pagination
     * existant ou pas.
     *
     * @param filterForm : le critère de filtre
     * @return un objet réponse et un code status http
     */
    public ResponseEntity<Response> search(BaseFilterForm<T> filterForm) {

        Pageable p = (filterForm.getPage() == null || filterForm.getSize() == null)
                ? null
                : new PageRequest(filterForm.getPage(), filterForm.getSize());

        Page<T> entities = this.service.findAllByCriteres(filterForm.getCriteres(), p);

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(entities).buildI18n(), HttpStatus.OK);
    }

    /**
     * Méthode rest de mise à jour d'une entité
     *
     * @param id : clé primaire de l'entité
     * @param t  : l'objet de l'entité avec ses nouvelles informations
     * @return un objet réponse et un code status http
     * @throws CustomException
     */
    public ResponseEntity<Response> update(I id, T t) throws CustomException {
        if (id == null) {
            throw new CustomException("Id null");
        }
        T updatedEntity = this.service.saveAndFlush(t);

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(updatedEntity).buildI18n(), HttpStatus.OK);
    }

    /**
     * Méthode rest de suppression permanente d'une entité par son
     * <code>ID</code>
     *
     * @param id : clé primaire de l'entité
     * @return un objet réponse et un code status http
     */
    public ResponseEntity<Response> delete(I id) {
        boolean isDeleted = this.service.delete(id);

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(isDeleted).buildI18n(), HttpStatus.OK);
    }

}
