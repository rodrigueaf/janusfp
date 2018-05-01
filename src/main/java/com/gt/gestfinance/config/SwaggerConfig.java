package com.gt.gestfinance.config;

import com.gt.gestfinance.util.CustomResourceBundleMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Classe de configuration de Swagger (Documentation d'Api)
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private CustomResourceBundleMessageSource messageSource;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ace3i.paiement.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .tags(new Tag("comptes", messageSource.getMessage("tag.api.comptes.desc")),
                        new Tag("users", messageSource.getMessage("tag.api.users.desc")),
                        new Tag("profils", messageSource.getMessage("tag.api.profils.desc")),
                        new Tag("oauth", messageSource.getMessage("tag.api.oauth.desc")));
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact(messageSource.getMessage("api.contact.nom"),
                messageSource.getMessage("api.contact.url"), messageSource.getMessage("api.contact.email"));
        return new ApiInfoBuilder()
                .description(messageSource.getMessage("api.desc"))
                .title(messageSource.getMessage("api.title"))
                .version("1.0")
                .contact(contact)
                .build();
    }

    @Autowired
    public void setMessageSource(CustomResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
