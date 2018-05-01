package com.gt.gestfinance.config;

import com.gt.gestfinance.entity.Permission;
import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.entity.Utilisateur;
import com.gt.gestfinance.repository.PermissionRepository;
import com.gt.gestfinance.repository.ProfilRepository;
import com.gt.gestfinance.repository.UtilisateurRepository;
import com.gt.gestfinance.service.IUtilisateurService;
import com.gt.gestfinance.util.AuthoritiesConstants;
import com.gt.gestfinance.util.Constants;
import com.gt.gestfinance.util.CustomResourceBundleMessageSource;
import com.gt.gestfinance.util.PermissionsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Classe d'initialisaion de l'application
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 21/04/2017
 */
@Component
public class DataInit implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInit.class);
    private final String valeurFalse = "false";

    private UtilisateurRepository utilisateurRepository;
    private PermissionRepository permissionRepository;
    private ProfilRepository profilRepository;
    private IUtilisateurService userService;
    private CustomResourceBundleMessageSource messageSource;

    public DataInit() {
        // do something later
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        LOGGER.info("Application initilized");

        initSecurity();
    }

    void initSecurity() {
        // Création d'un utilisateur par défaut
        if (utilisateurRepository.count() == 0) {
            // Création de la permision ADMIN
            Permission permissionAdmin = permissionRepository.save(new Permission(AuthoritiesConstants.ADMIN,
                    messageSource.getMessage("auth.admin")));
            // Création de la permision COMPTE
            Permission permissionCompte = permissionRepository.save(new Permission(PermissionsConstants.COMPTE_PERMISSION,
                    messageSource.getMessage("auth.compte")));
            // Création de la permision OPERATION
            Permission permissionOperation = permissionRepository.save(new Permission(PermissionsConstants.OPERATION_PERMISSION,
                    messageSource.getMessage("auth.operation")));
            // Création de la permision BUDGET
            Permission permissionBudget = permissionRepository.save(new Permission(PermissionsConstants.BUDGET_PERMISSION,
                    messageSource.getMessage("auth.budget")));
            // Création de la permision TRESORERIE_PERMISSION
            Permission permissionTresorerie = permissionRepository.save(new Permission(PermissionsConstants.TRESORERIE_PERMISSION,
                    messageSource.getMessage("auth.tresorerie")));
            // Création de la permision EXPLOITATION_PERMISSION
            Permission permissionExploitation = permissionRepository.save(new Permission(PermissionsConstants.EXPLOITATION_PERMISSION,
                    messageSource.getMessage("auth.exploitation")));

            // Création du profil ADMIN
            Profil profilAdmin = profilRepository.findOneByNom(messageSource.getMessage(PermissionsConstants.PROFIL_ADMIN))
                    .orElseGet(() -> {
                        Profil p = new Profil(messageSource.getMessage(PermissionsConstants.PROFIL_ADMIN));
                        p.getPermissions().add(permissionAdmin);
                        p.getPermissions().add(permissionCompte);
                        p.getPermissions().add(permissionOperation);
                        p.getPermissions().add(permissionBudget);
                        p.getPermissions().add(permissionTresorerie);
                        p.getPermissions().add(permissionExploitation);
                        return profilRepository.save(p);
                    });

            // Création de l'utilisateur ADMIN
            Utilisateur utilisateur = new Utilisateur(Constants.NOM_DE_LUTILISATEUR_ADMIN,
                    Constants.MOT_DE_PASSE_DE_LUTILISATEUR_ADMIN,
                    Constants.NOM_DE_LUTILISATEUR_ADMIN,
                    Constants.NOM_DE_LUTILISATEUR_ADMIN,
                    Constants.EMAIL_DE_LUTILISATEUR_ADMIN);
            utilisateur.setProfil(profilAdmin);
            utilisateur.getPermissions().add(permissionAdmin);
            userService.creerUnUtilisateur(utilisateur);
        }
    }

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Autowired
    public void setMessageSource(CustomResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setProfilRepository(ProfilRepository profilRepository) {
        this.profilRepository = profilRepository;
    }

    @Autowired
    public void setUtilisateurRepository(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Autowired
    public void setUserService(IUtilisateurService userService) {
        this.userService = userService;
    }

}
