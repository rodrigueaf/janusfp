package com.gt.gestfinance.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Les constante de base
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @since 10/08/2017
 * @version 1.0
 */
public class BaseConstant {

    /**
     * Le profile dev
     */
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    /**
     * Le profile qual
     */
    public static final String SPRING_PROFILE_QUALITY = "qual";
    /**
     * Le profile prod
     */
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    /**
     * Le profile local
     */
    public static final String SPRING_PROFILE_LOCAL_DEVELOPPEMENT = "local";
    /**
     * Le profile test
     */
    public static final String SPRING_PROFILE_TEST = "test";
    /**
     * Le système
     */
    public static final String SYSTEM_ACCOUNT = "system";

    /**
     * Le constructeur
     */
    private BaseConstant() {
        // do something
    }

    /**
     * Compare de entity en se basant sur les attributs
     *
     * @param entityClass : La classe de l'entité
     * @param oldObj : une première instance de l'entité
     * @param newObj : une seconde instance de l'entité
     * @param equalFields : La liste des accesseurs des attributs sur lequel le
     * test d'égalité se fera. Exemple : pour l'attribut "id" ça donne "getId"
     * @return boolean
     */
    public static boolean entityEqualById(Class entityClass,
            Object oldObj, Object newObj, String... equalFields) {
        try {
            if (oldObj == newObj) {
                return true;
            }
            if (newObj == null) {
                return false;
            }
            if (oldObj.getClass() != newObj.getClass()) {
                return false;
            }
            final Object other = entityClass.cast(newObj);
            for (String ef : equalFields) {
                if (!Objects.equals(oldObj.getClass().getDeclaredMethod(ef).invoke(oldObj),
                        other.getClass().getDeclaredMethod(ef).invoke(newObj))) {
                    return false;
                }
            }
            return true;
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException ex) {
            Logger.getLogger(BaseConstant.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
