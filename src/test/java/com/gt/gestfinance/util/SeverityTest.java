package com.gt.gestfinance.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author PC27
 */
public class SeverityTest {

    /**
     * Test of values method, of class Severity.
     */
    @Test
    public void testValues() {
        Severity[] expResult = new Severity[7];
        expResult[0] = Severity.INFO;
        expResult[1] = Severity.ERROR;
        expResult[2] = Severity.WARN;
        expResult[3] = Severity.DEBUG;
        expResult[4] = Severity.FATAL;
        expResult[5] = Severity.TRACE;
        expResult[6] = Severity.SUCCESS;
        Severity[] result = Severity.values();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of valueOf method, of class Severity.
     */
    @Test
    public void testValueOf() {
        Severity result = Severity.valueOf(Severity.DEBUG.name());
        assertEquals(Severity.DEBUG, result);
    }

    /**
     * Test of getValue method, of class Severity.
     */
    @Test
    public void testGetValue() {
        Severity instance = Severity.DEBUG;
        String result = instance.getValue();
        assertEquals("DEBUG", result);
    }

}
