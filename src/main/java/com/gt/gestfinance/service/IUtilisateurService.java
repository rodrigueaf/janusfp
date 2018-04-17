package com.gt.gestfinance.service;

import com.gt.base.exception.CustomException;
import com.gt.base.service.IBaseEntityService;
import com.gt.gestfinance.entity.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface service des utilisateurs
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
public interface IUtilisateurService extends IBaseEntityService<Utilisateur, Long> {

    Optional<Utilisateur> terminerLaModificationDuMotDePasse(String newPassword, String key);

    Optional<Utilisateur> soumettreUneRequeteDeModificationDeMotDePasse(String mail);

    Utilisateur creerUnUtilisateur(String login, String password, String firstName, String lastName, String email,
                                   String imageUrl);

    void creerUnUtilisateur(Utilisateur utilisateur);

    Optional<Utilisateur> modifierUnUtilisateur(Utilisateur utilisateur);

    void supprimerUnUtilisateur(String login) throws CustomException;

    void changerLeMotDePasse(String password);

    Page<Utilisateur> recupererLaListeDesUtlisateurs(Pageable pageable);

    Optional<Utilisateur> recupererUnUtilisateurAvecSesPermissionsAPartirDeSonLogin(String login);

    Utilisateur recupererLUtilisateurCourrantAvecSesPermissions();

    List<String> recupererLesPermissions();

    Optional<Utilisateur> recuperLUtilisateurConnecter();
}
