/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.gestfinance.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public class CustomResourceBundleMessageSourceTest {

    /**
     * Test of getMessage method, of class CustomResourceBundleMessageSource.
     */
    @Test
    public void testGetMessage() {
        String cle = "";
        CustomResourceBundleMessageSource instance = new CustomResourceBundleMessageSource();
        String expResult = "";
        String result = instance.getMessage(cle);
        assertEquals(expResult, result);
    }

}
