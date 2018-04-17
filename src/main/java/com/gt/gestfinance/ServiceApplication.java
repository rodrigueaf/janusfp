package com.gt.gestfinance;

import com.gt.base.util.ApplicationContextProvider;
import com.gt.base.util.CustomResourceBundleMessageSource;
import com.gt.gestfinance.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe de démarrage du micro service de gestion des types d'opération et
 * leurs schémas comptable
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 17/07/2017
 */
@SpringBootApplication(scanBasePackages = {"com.gt.gestfinance", "com.gt.base"})
public class ServiceApplication implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    /**
     * Création d'une instance de log
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(ServiceApplication.class);

    /**
     * Injection d'une instance d'Environment
     */
    @Autowired
    private Environment env;

    /**
     * Le numero de port alloué dynamicement
     */
    private static Integer port = 0;

    /**
     * @param e
     * @see ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent e) {
        ServiceApplication.setPort(e.getEmbeddedServletContainer().getPort());
    }

    /**
     * Retourne le numero de port alloué dynamicement
     *
     * @return
     */
    public static Integer getPort() {
        return port;
    }

    /**
     * Modifie le numéro de port
     *
     * @param port : Le port
     */
    public static void setPort(Integer port) {
        ServiceApplication.port = port;
    }

    /**
     * Méthode de lancement
     *
     * @param args : Les arguments de lancement
     * @throws UnknownHostException : Exception survenue lorsque l'adresse ip
     *                              n'est pas disponible
     */
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(
                ServiceApplication.class)
                .properties(getPropertiesConfig())
                .build().run(args);
        Environment env = app.getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        LOGGER.info("\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLs:\n\t"
                        + "Local: \t\t{}://localhost:{}\n\t"
                        + "External: \t{}://{}:{}\n\t"
                        + "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                port,
                protocol,
                InetAddress.getLocalHost().getHostAddress(),
                port,
                env.getActiveProfiles());
    }

    /**
     * Verfie la configuration des profils. Le microservice ne peut pas démarrer
     * avec le profile dev et prod étant activé
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains("dev") && activeProfiles.contains("prod")) {
            LOGGER.error("Vous avez mal configuré votre application! L'application peut ne pas fonctionner avec les profiles 'dev' et 'prod' activés.");
        }
    }

    /**
     * Retourne le context actuel
     *
     * @return {@link ApplicationContextProvider}ontextProvider
     */
    @Bean
    public ApplicationContextProvider applicationContextProvider() {
        return new ApplicationContextProvider();
    }

    /**
     * Configuration des MessageSource
     *
     * @return MessageSource
     */
    @Bean
    public MessageSource messageSource() {
        CustomResourceBundleMessageSource messageSource = new CustomResourceBundleMessageSource();
        messageSource.setBasenames("i18n/default/messages", "i18n/gestfinance/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Configuration du bean PropertySourcesPlaceholderConfigurer
     *
     * @return
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
        Resource[] resources;
        String propertiesConfigPath = getPropertiesConfigPath();
        if (new File(propertiesConfigPath).exists()) {
            resources = new Resource[2];
            resources[0] = new FileSystemResource(propertiesConfigPath);
            resources[1] = new ClassPathResource(Constants.NOM_DU_FICHIER_DE_LAPPLICATION_CONTEXT);
        } else {
            resources = new Resource[1];
            resources[0] = new ClassPathResource(Constants.NOM_DU_FICHIER_DE_LAPPLICATION_CONTEXT);
        }
        properties.setLocations(resources);
        properties.setIgnoreResourceNotFound(false);
        return properties;
    }

    /**
     * Configuration des localisations de source des configurations
     *
     * @return
     */
    private static String getPropertiesConfigPath() {
        return System.getProperty("user.home")
                + File.separator + ".janusfp"
                + File.separator + "conf"
                + File.separator + Constants.NOM_DU_FICHIER_DE_LAPPLICATION_CONTEXT;
    }

    /**
     * Configuration des localisations de source des configurations
     *
     * @return
     */
    static Map<String, Object> getPropertiesConfig() {

        Map<String, Object> properties = new HashMap<>();
        properties.put("spring.config.name", "application");
        properties.put("spring.config.location", "file:${user.home}/.janusfp/conf/");

        return properties;
    }
}
