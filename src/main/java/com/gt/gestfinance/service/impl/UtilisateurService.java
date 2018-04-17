package com.gt.gestfinance.service.impl;

import com.gt.base.exception.CustomException;
import com.gt.base.service.impl.BaseEntityService;
import com.gt.base.util.AuthoritiesConstants;
import com.gt.base.util.CustomResourceBundleMessageSource;
import com.gt.base.util.SecurityUtils;
import com.gt.gestfinance.entity.Permission;
import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.entity.Utilisateur;
import com.gt.gestfinance.repository.OperationRepository;
import com.gt.gestfinance.repository.PermissionRepository;
import com.gt.gestfinance.repository.ProfilRepository;
import com.gt.gestfinance.repository.UtilisateurRepository;
import com.gt.gestfinance.service.IUtilisateurService;
import com.gt.gestfinance.util.Constants;
import com.gt.gestfinance.util.MPConstants;
import com.gt.gestfinance.util.PermissionsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service de gestion des utilisateurs
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@Service
@Transactional
public class UtilisateurService extends BaseEntityService<Utilisateur, Long> implements IUtilisateurService {

    private final Logger log = LoggerFactory.getLogger(UtilisateurService.class);
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;

    private SecurityUtils securityUtils;
    private OperationRepository operationRepository;
    private ProfilRepository profilRepository;
    private CustomResourceBundleMessageSource messageSource;
    private Environment env;
    private boolean crypterPas = true;


    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              PasswordEncoder passwordEncoder, PermissionRepository permissionRepository) {
        super(utilisateurRepository);
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Optional<Utilisateur> terminerLaModificationDuMotDePasse(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        return getUserRepository().findOneByCleDeMiseAJourDuMotDePasse(key)
                .filter(user -> user.getDateEtHeureDeMiseAJour().isAfter(Instant.now().minus(30, ChronoUnit.DAYS)))
                .map(user -> {
                    user.setMotDePasse(passwordEncoder.encode(newPassword));
                    user.setCleDeMiseAJourDuMotDePasse(null);
                    user.setDateEtHeureDeMiseAJour(null);
                    return user;
                });
    }

    @Override
    public Optional<Utilisateur> soumettreUneRequeteDeModificationDeMotDePasse(String mail) {
        return getUserRepository().findOneByEmail(mail)
                .filter(Utilisateur::getActive)
                .map(user -> {
                    user.setDateEtHeureDeMiseAJour(Instant.now());
                    return user;
                });
    }

    @Override
    public Utilisateur creerUnUtilisateur(String login, String password, String firstName,
                                          String lastName, String email, String imageUrl) {

        Utilisateur newUtilisateur = new Utilisateur();
        String encryptedPassword = passwordEncoder.encode(password);
        newUtilisateur.setLogin(login);
        // new user gets initially a generated password
        newUtilisateur.setMotDePasse(encryptedPassword);
        newUtilisateur.setPrenom(firstName);
        newUtilisateur.setNom(lastName);
        newUtilisateur.setEmail(email);
        // new user is not active
        newUtilisateur.setActive(false);
        // new user gets registration key
        newUtilisateur.setProfil(profilRepository.findOneByNom(
                messageSource.getMessage(PermissionsConstants.PROFIL_ADMIN))
                .orElseGet(() -> profilRepository.save(
                        new Profil(messageSource.getMessage(PermissionsConstants.PROFIL_ADMIN)))));
        Set<Permission> authorities = new HashSet<>();
        Permission permission = permissionRepository.findOne(AuthoritiesConstants.ADMIN);
        authorities.add(permission);
        newUtilisateur.setPermissions(authorities);
        getUserRepository().save(newUtilisateur);
        log.debug("Created Information for Utilisateur: {}", newUtilisateur);
        return newUtilisateur;
    }

    @Override
    public void creerUnUtilisateur(Utilisateur utilisateur) {
        if (crypterPas) {
            String encryptedPassword = passwordEncoder
                    .encode(utilisateur.getMotDePasse() != null
                            ? utilisateur.getMotDePasse() : env.getProperty("default.password"));
            utilisateur.setMotDePasse(encryptedPassword);
        }
        utilisateur.setDateEtHeureDeMiseAJour(Instant.now());
        utilisateur.setActive(true);
        Set<Permission> authorities = profilRepository
                .findOne(utilisateur.getProfil().getIdentifiant()).getPermissions();
        Set<Permission> objects = new HashSet<>();
        objects.addAll(authorities);
        utilisateur.setPermissions(objects);
        getUserRepository().save(utilisateur);
        log.debug("Created Information for Utilisateur: {}", utilisateur);
    }

    @Override
    public Optional<Utilisateur> modifierUnUtilisateur(Utilisateur utilisateur) {
        return Optional.of(getUserRepository().findOne(utilisateur.getIdentifiant()))
                .map(u -> {
                    u.setLogin(utilisateur.getLogin());
                    u.setPrenom(utilisateur.getPrenom());
                    u.setNom(utilisateur.getNom());
                    u.setEmail(utilisateur.getEmail());
                    u.setProfil(utilisateur.getProfil());
                    Set<Permission> authorities = profilRepository
                            .findOne(utilisateur.getProfil().getIdentifiant()).getPermissions();
                    Set<Permission> objects = new HashSet<>();
                    objects.addAll(authorities);
                    u.setPermissions(objects);
                    return getUserRepository().saveAndFlush(u);
                });
    }

    @Override
    public void supprimerUnUtilisateur(String login) throws CustomException {
        if(!operationRepository.findByCreatedBy(login).isEmpty()){
            throw new CustomException(MPConstants.LE_UTILISATEUR_EST_DEJA_UTILISER);
        }
        getUserRepository().findOneByLogin(login).ifPresent(user -> {
            getUserRepository().delete(user);
            log.debug("Deleted Utilisateur: {}", user);
        });
    }

    @Override
    public void changerLeMotDePasse(String password) {
        getUserRepository().findOneByLogin(securityUtils.getCurrentUserLogin()).ifPresent(user -> {
            String encryptedPassword = passwordEncoder.encode(password);
            user.setMotDePasse(encryptedPassword);
            log.debug("Changed password for Utilisateur: {}", user);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Utilisateur> recupererLaListeDesUtlisateurs(Pageable pageable) {
        return getUserRepository().findAllByLoginNot(pageable, Constants.UTILISATEUR_ANONYME);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Utilisateur> recupererUnUtilisateurAvecSesPermissionsAPartirDeSonLogin(String login) {
        return getUserRepository().findOneWithPermissionsByLogin(login);
    }

    @Transactional(readOnly = true)
    @Override
    public Utilisateur recupererLUtilisateurCourrantAvecSesPermissions() {
        return getUserRepository().findOneWithPermissionsByLogin(securityUtils.getCurrentUserLogin()).orElse(null);
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        List<Utilisateur> utilisateurs = getUserRepository()
                .findAllByActiveIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
        utilisateurs.stream().map(user -> {
            log.debug("Deleting not activated user {}", user.getLogin());
            return user;
        }).forEachOrdered(user
                -> getUserRepository().delete(user)
        );
    }

    @Override
    public List<String> recupererLesPermissions() {
        return permissionRepository.findAll().stream().map(Permission::getNom).collect(Collectors.toList());
    }

    @Override
    public Optional<Utilisateur> recuperLUtilisateurConnecter() {
        return getUserRepository().findOneByLogin(securityUtils.getCurrentUserLogin());
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
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Autowired
    public void setSecurityUtils(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    private UtilisateurRepository getUserRepository() {
        return (UtilisateurRepository) repository;
    }

    @Autowired
    public void setOperationRepository(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }
}
