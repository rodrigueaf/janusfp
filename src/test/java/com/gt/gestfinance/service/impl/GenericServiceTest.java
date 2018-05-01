/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.gestfinance.service.impl;

import com.gt.gestfinance.GestFinanceTest;
import com.gt.gestfinance.controller.entity.User;
import com.gt.gestfinance.controller.repository.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public class GenericServiceTest extends GestFinanceTest {

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
     * Test of save method, of class GenericService.
     */
    @Test
    public void testGenericService() {
        GenericService instance = new GenericServiceImpl();
        String test1 = "test1";
        String test2 = "test2";
        String test3 = "test3";
        List result = instance.save(Arrays.asList(
                new User(test1, test1, test1, test1),
                new User(test2, test2, test2, test2),
                new User(test3, test3, test3, test3)));
        assertNotNull(result);
        instance.saveAndFlush(instance.findAll());
        long count = instance.count();
        instance.delete(instance.findAll().get(0));
        assertEquals(count - 1, instance.count());
        assertNotNull(instance.isExist(((User) instance.findAll().get(0)).getId()));
        instance.isExist(100L);
        instance.isExist(100);
    }

    /**
     * Implémentation par défaut
     */
    public class GenericServiceImpl extends GenericService {

        /**
         * Le constructeur
         */
        public GenericServiceImpl() {
            super(userRepository);
        }
    }

}
