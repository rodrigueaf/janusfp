/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.gestfinance.exception;

import com.gt.gestfinance.GestFinanceTest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Classe de test de la classe CustomException
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public class CustomExceptionTest extends GestFinanceTest {

    /**
     * Test des constructeurs de la classe CustomException
     */
    @Test
    public void testSomeMethod() {
        CustomException customException = new CustomException();
        assertNull(customException.getMessage());
        customException = new CustomException("test");
        assertEquals("test", customException.getMessage());
        try {
            throw new IllegalArgumentException("test");
        } catch (IllegalArgumentException e) {
            customException = new CustomException(e.getCause());
            assertEquals(e.getCause(), customException.getCause());

            customException = new CustomException("test", e.getCause());
            assertEquals("test", customException.getMessage());
            assertEquals(e.getCause(), customException.getCause());

            customException = new CustomException(e);
            assertNotNull(customException.getMessage());

            customException = new CustomException(e.getMessage(), e.getCause(), true, true);
            assertNotNull(customException.getMessage());
        }

    }
}
