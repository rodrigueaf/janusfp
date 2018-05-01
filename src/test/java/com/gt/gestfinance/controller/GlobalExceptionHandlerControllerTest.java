package com.gt.gestfinance.controller;

import com.gt.gestfinance.GestFinanceTest;
import com.gt.gestfinance.controller.entity.User;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.service.IGenericService;
import com.gt.gestfinance.util.Response;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.Assert.assertNotNull;

/**
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public class GlobalExceptionHandlerControllerTest extends GestFinanceTest {

    /**
     * Test of globalExceptionResolver method, of class
     * GlobalExceptionHandlerController.
     */
    @Test
    public void testGlobalExceptionResolver() {
        GlobalExceptionHandlerController instance = new GlobalExceptionHandlerController();
        ResponseEntity<Response> result = instance.globalExceptionResolver(new CustomException());
        assertNotNull(result);
    }

    /**
     * Test of customExceptionResolver method, of class
     * GlobalExceptionHandlerController.
     */
    @Test
    public void testCustomExceptionResolver() {
        GlobalExceptionHandlerController instance = new GlobalExceptionHandlerController();
        ResponseEntity<Response> result = instance.customExceptionResolver(new CustomException());
        assertNotNull(result);
    }

    /**
     * Test of uniqueConstraintExceptionResolver method, of class
     * GlobalExceptionHandlerController.
     */
    @Test
    public void testUniqueConstraintExceptionResolver() {
        GlobalExceptionHandlerController instance = new GlobalExceptionHandlerController();
        ResponseEntity<Response> result = instance.uniqueConstraintExceptionResolver(new DataIntegrityViolationException("test"));
        assertNotNull(result);
    }

    /**
     * test de la méthode <code>methodArgumentNotValidResolver</code>
     *
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testMethodArgumentNotValidResolver() throws NoSuchMethodException {
        GlobalExceptionHandlerController instance = new GlobalExceptionHandlerController();
        ResponseEntity<Response> result = instance.methodArgumentNotValidResolver(
                new MethodArgumentNotValidException(new MethodParameter(Entity.class.getMethod("setAttr", String.class), 0),
                        new BindException(new Entity(), "attr")));
        assertNotNull(result);
    }

    /**
     * Implémetation par défaut
     */
    public class GenericControllerImpl extends GenericController<User, Long> {

        /**
         * Le constructeur
         *
         * @param service
         */
        public GenericControllerImpl(IGenericService<User, Long> service) {
            super(service);
        }
    }

    /**
     * Simple pojo pour test
     */
    public static class Entity {

        /**
         *
         */
        private String attr;

        /**
         * @return
         */
        public String getAttr() {
            return attr;
        }

        /**
         * @param attr
         */
        public void setAttr(String attr) {
            this.attr = attr;
        }
    }
}
