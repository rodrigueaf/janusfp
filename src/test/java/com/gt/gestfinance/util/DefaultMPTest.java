/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.gestfinance.util;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

/**
 * Classe test de la classe EntitiesConstants
 *
 * @author <a href="mailto:mawulolo.kpogo@ace3i.com?">MAWULOLO KPOGO</a>
 * @version : 1.0
 * @since : 09/03/2017
 */
public class DefaultMPTest {

    /**
     * Test of getValue method, of class State.
     */
    @Test
    public void testGetValue() {
        try {
            Constructor<DefaultMP> constructeur = DefaultMP.class.getDeclaredConstructor();
            constructeur.setAccessible(true);
            DefaultMP defaultMP = constructeur.newInstance();

            assertNotNull(defaultMP);
        } catch (NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(DefaultMPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
