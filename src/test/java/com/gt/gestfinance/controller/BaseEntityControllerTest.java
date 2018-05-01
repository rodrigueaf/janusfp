package com.gt.gestfinance.controller;

import com.gt.gestfinance.GestFinanceTest;
import com.gt.gestfinance.controller.entity.User;
import com.gt.gestfinance.controller.service.IUserService;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.service.IBaseEntityService;
import com.gt.gestfinance.util.Response;
import com.gt.gestfinance.util.State;
import com.gt.gestfinance.util.StateWrapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public class BaseEntityControllerTest extends GestFinanceTest {

    /**
     * Injection d'un dépendance de type IUserService
     */
    @Autowired
    private IUserService userService;

    /**
     * S'éxécute apres chaque test
     */
    @After
    public void tearDown() {
        userService.deleteAll();
    }

    /**
     * Test of readAllByState method, of class BaseEntityController.
     */
    @Test
    public void testReadAllByState() {
        try {
            int value = 0;
            Integer page = 0;
            Integer size = 5;
            BaseEntityController instance = new BaseEntityControllerImpl(userService);
            ResponseEntity<Response> result = instance.readAllByState(value, page, size);
            assertNotNull(result);
            result = instance.readAllByState(value, page, null);
            assertNotNull(result);
            result = instance.readAllByState(value, null, null);
            assertNotNull(result);
            result = instance.readAllByState(1000, null, null);
            assertNotNull(result);
        } catch (CustomException e) {
            Logger.getLogger(BaseEntityControllerTest.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Test of readAllByStateNotDelete method, of class BaseEntityController.
     */
    @Test
    public void testReadAllByStateNotDelete() {
        Integer page = 0;
        Integer size = 5;
        BaseEntityController instance = new BaseEntityControllerImpl(userService);
        ResponseEntity<Response> result = instance.readAllByStateNotDelete(page, size);
        assertNotNull(result);
        result = instance.readAllByStateNotDelete(page, null);
        assertNotNull(result);
        result = instance.readAllByStateNotDelete(null, null);
        assertNotNull(result);
    }

    /**
     * Test of getState method, of class BaseEntityController.
     */
    @Test
    public void testGetState() {
        BaseEntityController instance = new BaseEntityControllerImpl(userService);
        Assert.assertEquals(State.DISABLED, instance.getState(0));
        Assert.assertEquals(State.ENABLED, instance.getState(1));
        Assert.assertEquals(State.DELETED, instance.getState(2));
    }

    /**
     * Test of getState method, of class BaseEntityController.
     *
     * @throws CustomException
     */
    @Test
    public void testDeleteWhenNotUsed() throws CustomException {
        BaseEntityController instance = new BaseEntityControllerImpl(userService);
        User user = new User("test", "test", "test", "test");
        user.setUsed(true);
        instance.create(user);
        boolean isUsedTested = false;
        try {
            instance.deleteWhenNotUsed(userService.findAll().get(0).getId());
        } catch (CustomException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            isUsedTested = true;
            Logger.getLogger(BaseEntityControllerTest.class.getName()).log(Level.INFO, null, e);
        }
        assertTrue(isUsedTested);

        userService.deleteAll();

        user.setUsed(false);
        instance.create(user);
        instance.deleteWhenNotUsed(userService.findAll().get(0).getId());
        assertTrue(userService.findAll().isEmpty());
    }

    /**
     * Test of getState method, of class BaseEntityController.
     *
     * @throws CustomException
     */
    @Test
    public void testChangeCompteState() throws CustomException {
        BaseEntityController instance = new BaseEntityControllerImpl(userService);
        User user = new User("test", "test", "test", "test");
        user.setUsed(true);
        instance.create(user);

        instance.changeState(user.getId(), new StateWrapper(State.DELETED));
        Assert.assertEquals(State.DELETED, userService.findAll().get(0).getState());

        instance.changeState(user.getId(), new StateWrapper(State.DISABLED));
        Assert.assertEquals(State.DISABLED, userService.findAll().get(0).getState());

        instance.changeState(user.getId(), new StateWrapper(State.ENABLED));
        Assert.assertEquals(State.ENABLED, userService.findAll().get(0).getState());

        boolean isNull = true;
        try {
            instance.changeState(user.getId(), new StateWrapper(null));
            isNull = false;
        } catch (CustomException e) {
            Logger.getLogger(BaseEntityControllerTest.class.getName()).log(Level.INFO, null, e);
        }
        assertTrue(isNull);

        try {
            instance.changeState(user.getId(), null);
            isNull = false;
        } catch (CustomException e) {
            Logger.getLogger(BaseEntityControllerTest.class.getName()).log(Level.INFO, null, e);
        }
        assertTrue(isNull);
    }

    /**
     * Implémentation par défaut
     */
    public class BaseEntityControllerImpl extends BaseEntityController<User, Long> {

        /**
         * Le constructeur
         *
         * @param service
         */
        public BaseEntityControllerImpl(IBaseEntityService<User, Long> service) {
            super(service);
        }
    }

}
