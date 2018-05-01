/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.gestfinance.controller;

import com.gt.gestfinance.GestFinanceTest;
import com.gt.gestfinance.controller.entity.User;
import com.gt.gestfinance.controller.service.IUserService;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.filtreform.BaseFilterForm;
import com.gt.gestfinance.service.IGenericService;
import com.gt.gestfinance.util.Response;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public class GenericControllerTest extends GestFinanceTest {

    /**
     * Injection d'un dépendance de type IUserService
     */
    @Autowired
    private IUserService userService;

    /**
     * Se déroule après chaque test
     */
    @After
    public void tearDown() {
        userService.deleteAll();
    }

    /**
     * Test of create method, of class GenericController.
     *
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        GenericController instance = new GenericControllerImpl(userService);
        long preCount = userService.count();
        ResponseEntity<Response> result = instance.create(new User("test", "test", "test", "test"));
        assertNotNull(result);
        assertEquals(preCount + 1, userService.count());
    }

    /**
     * Test of readOne method, of class GenericController.
     *
     * @throws CustomException
     */
    @Test
    public void testReadOne() throws CustomException {
        GenericController instance = new GenericControllerImpl(userService);
        instance.create(new User("test", "test", "test", "test"));
        assertNotNull(instance.readOne(1L));
    }

    /**
     * Test of readAll method, of class GenericController.
     */
    @Test
    public void testReadAll() {
        GenericController instance = new GenericControllerImpl(userService);
        ResponseEntity<Response> result = instance.readAll(0, 5);
        assertNotNull(result);
        result = instance.readAll(0, null);
        assertNotNull(result);
        result = instance.readAll(null, null);
        assertNotNull(result);
    }

    /**
     * Test of search method, of class GenericController.
     */
    @Test
    public void testSearch() {
        GenericController instance = new GenericControllerImpl(userService);
        ResponseEntity<Response> result = instance.search(new BaseFilterForm() {
            @Override
            public Specification getCriteres() {
                return null;
            }

        });
        assertNotNull(result);
        result = instance.search(new BaseFilterForm() {

            /**
             * @see BaseFilterForm#getCriteres()
             * @return
             */
            @Override
            public Specification getCriteres() {
                return null;
            }

            /**
             * @see BaseFilterForm#getPage()
             * @return
             */
            @Override
            public Integer getPage() {
                return 0;
            }

        });
        assertNotNull(result);
        result = instance.search(new BaseFilterForm() {

            /**
             * @see BaseFilterForm#getCriteres()
             * @return
             */
            @Override
            public Specification getCriteres() {
                return null;
            }

            /**
             * @see BaseFilterForm#getPage()
             * @return
             */
            @Override
            public Integer getPage() {
                return 0;
            }

            /**
             * @see BaseFilterForm#getSize()
             * @return
             */
            @Override
            public Integer getSize() {
                return 1;
            }

        });
        assertNotNull(result);
    }

    /**
     * Test of update method, of class GenericController.
     *
     * @throws Exception
     */
    @Test
    public void testUpdate() throws Exception {
        GenericController instance = new GenericControllerImpl(userService);
        instance.create(new User("test", "test", "test", "test"));
        User user = userService.findAll().get(0);
        user.setFirstName("test2");
        instance.update(user.getId(), user);
        user = userService.findAll().get(0);
        assertEquals("test2", user.getFirstName());
        try {
            instance.update(null, user);
        } catch (CustomException e) {
            Logger.getLogger(GenericControllerTest.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Test of delete method, of class GenericController.
     *
     * @throws CustomException
     */
    @Test
    public void testDelete() throws CustomException {
        GenericController instance = new GenericControllerImpl(userService);
        instance.create(new User("test", "test", "test", "test"));
        instance.delete(userService.findAll().get(0).getId());
        assertEquals(0, userService.count());
    }

    /**
     * Implémentation par défaut
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

}
