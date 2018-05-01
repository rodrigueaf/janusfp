package com.gt.gestfinance.util;

/**
 * Classe définissant les états de certains attributs
 *
 * @author <a href="mailto:ali.amadou@ace3i.com?">Yasser Ali</a>
 *
 */
public enum State {

    DISABLED(0),
    ENABLED(1),
    DELETED(2);
 
    /**
     * La valeur
     */
    private final int value;

    /**
     * Modifie la valeur
     *
     * @param value
     */
    private State(int value) {
        this.value = value;
    }

    /**
     * Retourne la valeur
     *
     * @return
     */
    public int getValue() {
        return value;
    }

}
