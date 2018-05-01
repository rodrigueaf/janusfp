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
public class ResponseTest {

    /**
     * Test of getSeverity method, of class Response.
     */
    @Test
    public void testGetSeverity() {
        ResponseBuilder.severity(Severity.ERROR);
        Response instance = new Response();
        Severity expResult = null;
        Severity result = instance.getSeverity();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCode method, of class Response.
     */
    @Test
    public void testGetCode() {
        Response instance = new Response();
        String expResult = null;
        String result = instance.getCode();
        assertEquals(expResult, result);
    }
}
