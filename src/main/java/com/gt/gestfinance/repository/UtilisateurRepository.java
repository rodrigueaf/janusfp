package com.gt.gestfinance.repository;

import com.gt.base.repository.BaseEntityRepository;
import com.gt.gestfinance.entity.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Le repository de l'entité Utilisateur
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
public interface UtilisateurRepository extends BaseEntityRepository<Utilisateur, Long> {

    /**
     * Retourner un utilisateur à partir de sa clé d'activation
     *
     * @param cleDActivation : La clé d'activation
     * @return Optional
     */
    Optional<Utilisateur> findOneByCleDActivation(String cleDActivation);

    /**
     * Retourner la liste des utilisateurs non activés et créés avant une certaine date
     *
     * @param dateTime : La date
     * @return List
     */
    List<Utilisateur> findAllByActiveIsFalseAndCreatedDateBefore(Instant dateTime);

    /**
     * Retourner un utilisateur à partir de sa clé de mise à jour de son mot de passe
     *
     * @param cleDeMiseAJourDuMotDePasse : La clé de mise à jour de son mot de passe
     * @return Optional
     */
    Optional<Utilisateur> findOneByCleDeMiseAJourDuMotDePasse(String cleDeMiseAJourDuMotDePasse);

    /**
     * Retourner un utilisateur à partir de son email
     *
     * @param email : L'email
     * @return Optional
     */
    Optional<Utilisateur> findOneByEmail(String email);

    /**
     * Retourner un utilisateur à partir de son login
     *
     * @param login : Le login
     * @return Optional
     */
    Optional<Utilisateur> findOneByLogin(String login);

    /**
     * Retourner un utilisateur à partir ddu code de son gestcionaire
     *
     * @param codeDuGestionnaire : Le code de son gestionnaire
     * @return Optional
     */
    Optional<Utilisateur> findOneByCodeDuGestionnaire(String codeDuGestionnaire);

    /**
     * retourner un utilisateur avec ses permissions à partir de son login
     *
     * @param login : Le login
     * @return Optional
     */
    @Query("select u from Utilisateur u left join fetch u.permissions p where u.login = ?1")
    Optional<Utilisateur> findOneWithPermissionsByLogin(String login);


    /**
     * retourner tous les utilisateurs dont le login est différent de celui passé en paramètre
     *
     * @param pageable : L'information de la page
     * @param login    : Le login
     * @return Page
     */
    Page<Utilisateur> findAllByLoginNot(Pageable pageable, String login);

    /**
     * Retourner la liste des utilisateurs d'un profil
     *
     * @param nom : Le nom du profil
     * @return List
     */
    List<Utilisateur> findAllByProfilNom(String nom);
}
