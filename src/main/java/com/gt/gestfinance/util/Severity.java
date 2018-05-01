package com.gt.gestfinance.util;

/**
 * Enumération des différents types de niveau de gravité pour les messages à
 * renvoyer
 *
 * @author <a href="mailto:ali.amadou@ace3i.com?">Yasser Ali</a>
 * <a href="mailto:afandi-ekoue.amah-tchoutchoui@ace3i.com?">Ekoue</a>
 *
 */
public enum Severity {

    INFO("INFO"),
    ERROR("ERROR"),
    WARN("WARN"),
    DEBUG("DEBUG"),
    FATAL("FATAL"),
    TRACE("TRACE"),
    SUCCESS("SUCCESS");

    /**
     * la valeur correspondant à chaque type de sévérité (ex : info, error,
     * warning)
     */
    private String value;

    private Severity(String value) {
        this.setValue(value);
    }

    /**
     * Retourne value
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * Modifie value
     *
     * @param value
     */
    private void setValue(String value) {
        this.value = value;
    }

}
