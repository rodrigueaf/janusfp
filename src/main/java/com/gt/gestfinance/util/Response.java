package com.gt.gestfinance.util;

/**
 * La classe qui encapsule les informations du message à renvoyés par les
 * méthodes du controller rest
 *
 * @author <a href="mailto:ali.amadou@ace3i.com?">Yasser Ali</a>
 * <a href="mailto:afandi-ekoue.amah-tchoutchoui@ace3i.com?">Ekoue</a>
 *
 */
public class Response {

    /**
     * le niveau de gravité du message
     */
    private Severity severity;

    /**
     * Un code d'erreur ou de succès du message
     */
    private String code;

    /**
     * le titre du message
     */
    private String title;

    /**
     * le méssage à véhiculer
     */
    private String message;
    
    /**
     * La donnée contenu dans le message
     */
    private Object data;
    
    /**
     * Constructeur par défaut
     */
    public Response() {
        super();
    }

	/**
     * Retourne severity
     *
     * @return
     */
    public Severity getSeverity() {
        return severity;
    }

    /**
     * Modifier severity
     *
     * @param severity
     */
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    /**
     * @return title
     */
    public String getTitle(   ) {
        return title;
    }

    /**
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param detail message
     */
    public void setMessage(String detail) {
        this.message = detail;
    }

    /**
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code code
     */
    public void setCode(String code) {
        this.code = code;
    }

	/**
	 * @return
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 */
	public void setData(Object data) {
		this.data = data;
	}
}
