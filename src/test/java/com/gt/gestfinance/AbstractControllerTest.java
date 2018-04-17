package com.gt.gestfinance;

import com.gt.base.controller.GlobalExceptionHandlerController;
import com.gt.gestfinance.util.CustomMockMvc;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityManager;

/**
 * Classe de base des classes de test pour les contrôleurs
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public abstract class AbstractControllerTest extends GestFinanceTest {

    protected CustomMockMvc restSampleMockMvc;
    @Autowired
    protected GlobalExceptionHandlerController globalExceptionHandlerController;
    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    protected MappingJackson2HttpMessageConverter jacksonMessageConverter;
    @Autowired
    protected PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    @Autowired
    protected EntityManager em;

    public void setup(Object... o) {
        MockitoAnnotations.initMocks(this);
        this.restSampleMockMvc = new CustomMockMvc(MockMvcBuilders.standaloneSetup(o)
                .alwaysDo(MockMvcResultHandlers.print())
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(globalExceptionHandlerController)
                .setMessageConverters(jacksonMessageConverter).build());
    }
}
