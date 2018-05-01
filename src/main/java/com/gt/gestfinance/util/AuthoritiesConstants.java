package com.gt.gestfinance.util;

/**
 * Constants for Spring Security authorities.
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @since 10/08/2017
 * @version 1.0
 */
public final class AuthoritiesConstants {

    /**
     * Le role admin
     */
    public static final String ADMIN = "ROLE_ADMIN";

    /**
     * Le role user
     */
    public static final String USER = "ROLE_USER";

    /**
     * Le role anonyme
     */
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    /**
     * Le constructeur
     */
    private AuthoritiesConstants() {
        // do something
    }
}
