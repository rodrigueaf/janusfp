/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gt.gestfinance.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration de la base de données
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @since 10/08/2017
 * @version 1.0
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class DatabaseConfig {

    /**
     * Une instance de log
     */
    protected Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    /**
     * Injection d'un instance de Environment
     */
    private final Environment env;

    /**
     * Le constructeur
     * @param env
     */
    public DatabaseConfig(Environment env) {
        this.env = env;
    }
    
    /**
     * Création d'un bean pour la source de donnée
     *
     * @return DataSource
     */
    @Bean
    @ConditionalOnExpression("'${spring.jpa.database}' != 'H2'")
    public DataSource primaryDataSource() {
        DataSource ds = new DataSource();
        ds.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
        ds.setUrl(env.getProperty("spring.datasource.url"));
        ds.setUsername(env.getProperty("spring.datasource.username"));
        ds.setPassword(env.getProperty("spring.datasource.password"));
        ds.setTestOnBorrow(true);
        ds.setRemoveAbandoned(true);
        ds.setRemoveAbandonedTimeout(3600);
        ds.setValidationQuery(env.getProperty("spring.datasource.validationQuery"));
        ds.setMaxActive(400); // Nombre maximal de connexions actives dans le pool
        return ds;
    }

}
