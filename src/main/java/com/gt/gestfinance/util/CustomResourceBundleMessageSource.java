package com.gt.gestfinance.util;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * Le ResourceBundleMessageSource customisé
 *
 * @author <a href="mailto:ali.amadou@ace3i.com?">Yasser Ali</a>
 * @since 30/06/17
 * @version 1
 */
public class CustomResourceBundleMessageSource extends ResourceBundleMessageSource {

    /**
     * Instance de Locale
     */
    private final Locale locale = LocaleContextHolder.getLocale();

    /**
     * @param cle
     * @return
     */
    public String getMessage(String cle) {

        return (cle == null || "".equals(cle.trim()))
                ? "" : getMessage(cle.trim(), null, locale);
    }
}
