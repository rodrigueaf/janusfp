package com.gt.gestfinance.util;

/**
 * Classe POJO pour encapsuler l'etat d'un objet
 *
 * @author <a href="moukaila.labodja@ace3i.com?">Moukaila LABODJA</a>
 *
 * @since 09/08/2017
 *
 * @version 1.0
 */
public class StateWrapper {

    /**
     * State à mapper
     */
    private State state;

    /**
     * Le constructeur
     *
     * @param state
     */
    public StateWrapper(State state) {
        this.state = state;
    }

    /**
     * Constructeur par defaut
     */
    public StateWrapper() {
        super();
    }

    /**
     * Retourne le State
     *
     * @return
     */
    public State getState() {
        return state;
    }

    /**
     * attribut un state
     *
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }
}
