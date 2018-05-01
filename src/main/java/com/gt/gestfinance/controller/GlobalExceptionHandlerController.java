package com.gt.gestfinance.controller;

import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.exception.ResourceNotFoundException;
import com.gt.gestfinance.util.DefaultMP;
import com.gt.gestfinance.util.Response;
import com.gt.gestfinance.util.ResponseBuilder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * @author <a href="mailto:ali.amadou@ace3i.com?">Yasser Ali</a>
 * @version 1.0
 * @since 30/06/17
 */
@ControllerAdvice
public class GlobalExceptionHandlerController {

    /**
     * Traitement qui survient lorsqu'une exception est lancée et qu'elle n'est
     * pas gérée spécifiquement.
     *
     * @param e : une <code>Exception</code>
     * @return un objet réponse et un code status http 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> globalExceptionResolver(Exception e) {

        java.util.logging.Logger.getLogger(this.getClass().getName()).warning(e.getMessage());

        return new ResponseEntity<>(ResponseBuilder.error()
                .code(null)
                .title(DefaultMP.TITLE_ERROR)
                .message(DefaultMP.EXCEPTION)
                .data(e.getClass()).buildI18n(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Traitement qui survient lorsqu'une exception est lancée et qu'elle n'est
     * pas gérée spécifiquement.
     *
     * @param e : une <code>Exception</code>
     * @return un objet réponse et un code status http 500
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response> customExceptionResolver(CustomException e) {

        java.util.logging.Logger.getLogger(this.getClass().getName()).warning(e.getMessage());

        return new ResponseEntity<>(ResponseBuilder.error()
                .code(null)
                .title(DefaultMP.TITLE_ERROR)
                .message(e.getMessage())
                .data(e.getClass()).buildI18n(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Traitement des exceptions de type
     * <code>DataIntegrityViolationException</code>
     *
     * @param e : un <code>DataIntegrityViolationException</code>
     * @return un objet réponse et un code status http 500
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response> uniqueConstraintExceptionResolver(DataIntegrityViolationException e) {

        java.util.logging.Logger.getLogger(this.getClass().getName()).warning(e.getMessage());

        return new ResponseEntity<>(ResponseBuilder.error()
                .code(null)
                .title(DefaultMP.TITLE_ERROR)
                .message(DefaultMP.DUPLICATED_ENTRY)
                .data(e.getClass()).buildI18n(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Cette méthode encapsule les erreurs liés à l'accès des resources sur
     * lesquelles l'utilisateur n'a pas le droit
     *
     * @param e exception capturée
     * @return une réponse, avec un status d'erreur vers la vue
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response> accessDeniedResolver(AccessDeniedException e) {

        java.util.logging.Logger.getLogger(this.getClass().getName()).warning(e.getMessage());

        return new ResponseEntity<>(ResponseBuilder.warn()
                .code(null)
                .title(DefaultMP.TITLE_WARN)
                .message(DefaultMP.ACCESS_DENIED)
                .data(e.getClass()).buildI18n(), HttpStatus.FORBIDDEN);
    }

    /**
     * Cette méthode encapsule les erreurs liés à l'accès des resources non trouvé
     *
     * @param e exception capturée
     * @return une réponse, avec un status d'erreur vers la vue
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response> resourceNotFoundResolver(ResourceNotFoundException e) {

        java.util.logging.Logger.getLogger(this.getClass().getName()).warning(e.getMessage());

        return new ResponseEntity<>(ResponseBuilder.warn()
                .code(null)
                .title(DefaultMP.TITLE_WARN)
                .message(DefaultMP.NOT_FOUND)
                .data(e.getClass()).buildI18n(), HttpStatus.NOT_FOUND);
    }

    /**
     * Encapsuler les erreurs de validations lors de création des ojects annoté
     * par <code>@valid</code> ou <code>@validated</code>
     *
     * @param e l'exception
     * @return un objet response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> methodArgumentNotValidResolver(MethodArgumentNotValidException e) {
        java.util.logging.Logger.getLogger(this.getClass().getName()).warning(e.getMessage());
        List<ObjectError> errors = e.getBindingResult().getAllErrors();

        String msgError;
        if (errors.isEmpty()) {
            msgError = "";
        } else {
            msgError = errors.get(0).getDefaultMessage();
        }

        return new ResponseEntity<>(ResponseBuilder.warn()
                .code("").title("")
                .message(msgError).data(e.getClass()).buildI18n(), HttpStatus.BAD_REQUEST);
    }
}
