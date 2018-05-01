/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.gestfinance.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Classe test de la classe State
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public class StateTest {

    /**
     * Test of values method, of class State.
     */
    @Test
    public void testValues() {
        State[] expResult = new State[3];
        expResult[0] = State.DISABLED;
        expResult[1] = State.ENABLED;
        expResult[2] = State.DELETED;
        State[] result = State.values();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of valueOf method, of class State.
     */
    @Test
    public void testValueOf() {
        State result = State.valueOf(State.DELETED.name());
        assertEquals(State.DELETED, result);
    }

    /**
     * Test of getValue method, of class State.
     */
    @Test
    public void testGetValue() {
        State instance = State.DELETED;
        int result = instance.getValue();
        assertEquals(2, result);
    }

}
