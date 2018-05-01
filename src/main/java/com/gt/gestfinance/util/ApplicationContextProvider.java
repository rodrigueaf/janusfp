package com.gt.gestfinance.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Classe permettant de récupérer le context de l'application
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @since 10/08/2017
 * @version 1.0
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    /**
     * Le context de l'application
     */
    private static ApplicationContext context;

    /**
     * Methode de récupération du context
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * Le setter
     *
     * @param context
     */
    public static void setContext(ApplicationContext context) {
        ApplicationContextProvider.context = context;
    }

    /**
     * Modificateur du context
     *
     * @param ac : Le context
     * @see
     * ApplicationContextAware#setApplicationContext(ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        ApplicationContextProvider.setContext(ac);
    }
}
