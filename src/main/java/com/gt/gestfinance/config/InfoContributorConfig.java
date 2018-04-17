package com.gt.gestfinance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Classe permettant d'afficher les informations sur le microservice.
 * L'information est accessible à l'adresse [adresseip]:[port]/info
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 21/04/2017
 */
@Component
public class InfoContributorConfig implements InfoContributor {


    private Environment env;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("Nom", env.getProperty("spring.application.nom"))
                .withDetail("Description", env.getProperty("spring.application.description"));
    }

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }
}
