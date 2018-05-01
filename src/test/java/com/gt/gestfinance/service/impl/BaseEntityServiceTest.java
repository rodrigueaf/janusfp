/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.gestfinance.service.impl;

import com.gt.gestfinance.GestFinanceTest;
import com.gt.gestfinance.controller.entity.User;
import com.gt.gestfinance.controller.repository.UserRepository;
import com.gt.gestfinance.exception.CustomException;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public class BaseEntityServiceTest extends GestFinanceTest {

    /**
     * Injection d'une instance de UserRepository
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Se déroule après chaque test
     */
    @After
    public void tearDown() {
        userRepository.deleteAll();
    }

    /**
     * Test of enabled method, of class BaseEntityService.
     *
     * @throws CustomException
     */
    @Test
    public void testEnabled() throws CustomException {
        BaseEntityService instance = new BaseEntityServiceImpl();
        boolean result = instance.enabled(1);
        assertFalse(result);
        instance.save(new User("test", "test", "test", "test"));
        result = instance.enabled(userRepository.findAll().get(0).getId());
        assertTrue(result);
    }

    /**
     * Test of disabled method, of class BaseEntityService.
     *
     * @throws CustomException
     */
    @Test
    public void testDisabled() throws CustomException {
        BaseEntityService instance = new BaseEntityServiceImpl();
        boolean result = instance.disabled(1);
        assertFalse(result);
        instance.save(new User("test", "test", "test", "test"));
        result = instance.disabled(userRepository.findAll().get(0).getId());
        assertTrue(result);
    }

    /**
     * Test of deleteSoft method, of class BaseEntityService.
     *
     * @throws CustomException
     */
    @Test
    public void testDeleteSoft() throws CustomException {
        BaseEntityService instance = new BaseEntityServiceImpl();
        boolean result = instance.deleteSoft(1);
        assertFalse(result);
        instance.save(new User("test", "test", "test", "test"));
        result = instance.deleteSoft(userRepository.findAll().get(0).getId());
        assertTrue(result);
    }

    /**
     * Implémentation par défaut
     */
    public class BaseEntityServiceImpl extends BaseEntityService {

        /**
         * Le constructeur
         */
        public BaseEntityServiceImpl() {
            super(userRepository);
        }
    }

}
