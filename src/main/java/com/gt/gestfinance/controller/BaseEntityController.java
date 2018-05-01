package com.gt.gestfinance.controller;

import com.gt.gestfinance.entity.AbstractAuditingEntity;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.service.IBaseEntityService;
import com.gt.gestfinance.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
public abstract class BaseEntityController<T extends AbstractAuditingEntity, I extends Serializable> extends GenericController<T, I> {

    /**
     * Créer un controlleur
     *
     * @param service : une instance du service de l'entité dont le contrôleur
     *                est dérivé de cette classe.
     */
    public BaseEntityController(IBaseEntityService<T, I> service) {
        super(service);
    }

    /**
     * Répurérer les enregistrements en fonction de leur état
     *
     * @param value valueur possible d'un état : 0, 1, 2
     * @param page
     * @param size
     * @return
     * @throws CustomException
     */
    public ResponseEntity<Response> readAllByState(int value, Integer page, Integer size) throws CustomException {
        State state = getState(value);
        if (state == null) {
            throw new CustomException("L'état est obilgatoire");
        }
        Page<T> entities;
        if (page != null && size != null) {
            entities = ((IBaseEntityService<T, I>) service).findByState(state, new PageRequest(page, size));
        } else {
            entities = ((IBaseEntityService<T, I>) service).findByState(state, null);
        }

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(entities).buildI18n(), HttpStatus.OK);
    }

    /**
     * Récupérer les enregistrements qui n'ont pas l'état supprimé
     *
     * @param page
     * @param size
     * @return
     */
    public ResponseEntity<Response> readAllByStateNotDelete(Integer page, Integer size) {
        Page<T> entities;

        if (page == null || size == null) {
            entities = ((IBaseEntityService<T, I>) service).findByStateNotDeleted(null);
        } else {
            entities = ((IBaseEntityService<T, I>) service).findByStateNotDeleted(new PageRequest(page, size));
        }

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(entities).buildI18n(), HttpStatus.OK);
    }

    /**
     * Vérifie si l'objet est utilisé avant de procéder à la suppression
     *
     * @param id
     * @return
     * @throws CustomException
     */
    public ResponseEntity<Response> deleteWhenNotUsed(I id) throws CustomException {
        T findOne = service.findOne(id);
        if (findOne != null && findOne.isUsed()) {
            throw new CustomException(DefaultMP.MSG_USED_ENTITY);
        }
        return super.delete(id);
    }

    /**
     * Changement d'état de l'entité
     *
     * @param id
     * @param state
     * @return
     * @throws CustomException
     */
    public ResponseEntity<Response> changeState(I id, StateWrapper state) throws CustomException {
        if (state == null || state.getState() == null) {
            throw new CustomException(DefaultMP.MESSAGE_ERROR);
        }
        boolean isStateChanged = false;
        switch (state.getState()) {
            case ENABLED:
                isStateChanged = ((IBaseEntityService<T, I>) service).enabled(id);
                break;
            case DISABLED:
                isStateChanged = ((IBaseEntityService<T, I>) service).disabled(id);
                break;
            case DELETED:
                isStateChanged = ((IBaseEntityService<T, I>) service).deleteSoft(id);
                break;
            default:
        }

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.MESSAGE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(isStateChanged)
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * @param value
     * @return
     */
    protected State getState(int value) {

        State state = null;

        switch (value) {
            case 0:
                state = State.DISABLED;
                break;
            case 1:
                state = State.ENABLED;
                break;
            case 2:
                state = State.DELETED;
                break;

            default:
                break;
        }

        return state;
    }
}
