/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.gestfinance.spec;

import com.gt.gestfinance.repository.spec.BaseSpecifications;
import com.gt.gestfinance.util.State;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

/**
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public class BaseSpecificationsTest {

    /**
     * test du constructeur
     */
    @Test
    public void setUp() {
        try {
            Constructor<BaseSpecifications> constructeur = BaseSpecifications.class.getDeclaredConstructor();
            constructeur.setAccessible(true);
            constructeur.newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(BaseSpecificationsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of containsLike method, of class BaseSpecifications.
     */
    @Test
    public void testContainsLike() {
        String attribute = "";
        String value = "";
        Specification result = BaseSpecifications.containsLike(attribute, value);
        assertNotNull(result);
    }

    /**
     * Test of startWith method, of class BaseSpecifications.
     */
    @Test
    public void testStartWith() {
        String attribute = "";
        String value = "";
        Specification result = BaseSpecifications.startWith(attribute, value);
        assertNotNull(result);
    }

    /**
     * Test of endWith method, of class BaseSpecifications.
     */
    @Test
    public void testEndWith() {
        String attribute = "";
        String value = "";
        Specification result = BaseSpecifications.endWith(attribute, value);
        assertNotNull(result);
    }

    /**
     * Test of isEqual method, of class BaseSpecifications.
     */
    @Test
    public void testIsEqual() {
        String attribute = "";
        Date value = new Date();
        Specification result = BaseSpecifications.isEqual(attribute, value);
        assertNotNull(result);
    }

    /**
     * Test of isBetween method, of class BaseSpecifications.
     */
    @Test
    public void testIsBetween3args1() {
        String attribute = "";
        int min = 0;
        int max = 0;
        Specification result = BaseSpecifications.isBetween(attribute, min, max);
        assertNotNull(result);
    }

    /**
     * Test of isBetween method, of class BaseSpecifications.
     */
    @Test
    public void testIsBetween3args2() {
        String attribute = "";
        double min = 0.0;
        double max = 0.0;
        Specification result = BaseSpecifications.isBetween(attribute, min, max);
        assertNotNull(result);
    }

    /**
     * Test of enumMatcher method, of class BaseSpecifications.
     */
    @Test
    public void testEnumMatcher() {
        Specification result = BaseSpecifications.enumMatcher("", null);
        assertNotNull(result);
        result = BaseSpecifications.enumMatcher("", State.ENABLED);
        assertNotNull(result);
    }

    /**
     * Test of enumMatcher method, of class BaseSpecifications.
     */
    @Test
    public void testBaseSpecifications() {
        BaseSpecifications baseSpecifications = new BaseSpecifications();
        baseSpecifications.toString();
    }

}
