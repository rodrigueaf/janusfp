/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.gestfinance.entity;

import com.gt.gestfinance.util.State;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public class AbstractAuditingEntityTest {

    /**
     * Test of getState method, of class AbstractAuditingEntity.
     */
    @Test
    public void testGetState() {
        AbstractAuditingEntity instance = new AbstractAuditingEntityImpl();
        State result = instance.getState();
        assertNotNull(result);
    }

    /**
     * Test of setState method, of class AbstractAuditingEntity.
     */
    @Test
    public void testSetState() {
        State state = null;
        AbstractAuditingEntity instance = new AbstractAuditingEntityImpl();
        instance.setState(state);
    }

    /**
     * Test of isUsed method, of class AbstractAuditingEntity.
     */
    @Test
    public void testIsUsed() {
        AbstractAuditingEntity instance = new AbstractAuditingEntityImpl();
        boolean expResult = false;
        boolean result = instance.isUsed();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUsed method, of class AbstractAuditingEntity.
     */
    @Test
    public void testSetUsed() {
        boolean used = false;
        AbstractAuditingEntity instance = new AbstractAuditingEntityImpl();
        instance.setUsed(used);
    }

    /**
     * Implémentation par défaut
     */
    public class AbstractAuditingEntityImpl extends AbstractAuditingEntity {
    }

}
