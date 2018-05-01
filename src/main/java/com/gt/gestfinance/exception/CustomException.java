package com.gt.gestfinance.exception;

/**
 * Classe de base des exceptions
 *
 * @author <a href="mailto:afandi-ekoue.amah-tchoutchoui@ace3i.com?">Ekoue</a>,
 * <a href="mailto:love.faya@ace3i.com?">Love</a>
 *
 */
public class CustomException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur par défaut
     */
    public CustomException() {
        // do something
    }

    /**
     * Constructeur par défaut
     *
     * @param ex : L'exception
     */
    public CustomException(Exception ex) {
        this(ex.getMessage(), ex.getCause(), false, true);
    }

    /**
     * Constructeur personnalisé
     *
     * @param message : Le message à afficher
     * @param cause : La cause de l'exception
     * @param enableSuppression : Si on supprimer la levée de l'exception
     * @param writableStackTrace : Si on affiche la pile d'exécution
     */
    public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Constructeur personnalisé
     *
     * @param cause Le cause de l'exception
     */
    public CustomException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructeur personnalisé
     *
     * @param message : Le message à afficher
     * @param cause : La cause de l'exception
     */
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur personnalisé
     *
     * @param message : Le message à afficher
     *
     */
    public CustomException(String message) {
        super(message);
    }
}
