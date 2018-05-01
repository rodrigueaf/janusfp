package com.gt.gestfinance.config;

import com.gt.gestfinance.GestFinanceTest;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public class DatabaseConfigTest extends GestFinanceTest {

    /**
     * Injection d'une instance de Environment
     */
    @Autowired
    private Environment env;

    /**
     * Injection d'une instance de MethodInvocationProceedingJoinPoint
     */
    private MethodInvocationProceedingJoinPoint joinPoint;

    /**
     * Initialisation
     */
    @Before
    public void setUp() {
        joinPoint = DatabaseConfigTest.mockJoinPoint();
    }

    /**
     * Test of primaryDataSource method, of class DatabaseConfig.
     */
    @Test
    public void testPrimaryDataSource() {
        DatabaseConfig instance = new DatabaseConfig(env);
        DataSource primaryDataSource = instance.primaryDataSource();
        assertNotNull(primaryDataSource);
    }

    /**
     * Création d'un mock de MethodInvocationProceedingJoinPoint
     *
     * @return MethodInvocationProceedingJoinPoint
     */
    public static MethodInvocationProceedingJoinPoint mockJoinPoint() {
        MethodInvocationProceedingJoinPoint joinPoint = mock(MethodInvocationProceedingJoinPoint.class);
        Object[] args = new Object[]{false};
        Signature signature = mock(MethodSignature.class);
        when(joinPoint.getArgs()).thenReturn(args);
        when(signature.getName()).thenReturn("getClusters");
        when(joinPoint.getSignature()).thenReturn(signature);
        return joinPoint;
    }
}
