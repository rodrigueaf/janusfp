package com.gt.gestfinance.util;

import com.gt.gestfinance.GestFinanceTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * Classe de test de ApplicationContextProvider
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public class ApplicationContextProviderTest extends GestFinanceTest {

    /**
     * Test de la crétaion du context
     */
    @Test
    public void testCreateContext() {
        ApplicationContext context = ApplicationContextProvider.getContext();
        Assert.assertNotNull("Expected not null", context);
    }

    /**
     * Test de la récupération du Bean
     */
    @Test
    public void testGetBean() {
        ComposantSpringTest composant = ApplicationContextProvider.getContext()
                .getBean(ComposantSpringTest.class);
        Assert.assertNotNull("Expected not null", composant);
    }
}
