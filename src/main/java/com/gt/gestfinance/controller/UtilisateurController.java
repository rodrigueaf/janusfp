package com.gt.gestfinance.controller;

import com.gt.base.controller.BaseEntityController;
import com.gt.base.exception.CustomException;
import com.gt.base.util.*;
import com.gt.gestfinance.dto.ManagedUserVM;
import com.gt.gestfinance.entity.Utilisateur;
import com.gt.gestfinance.filtreform.UtilisateurFormulaireDeFiltre;
import com.gt.gestfinance.repository.UtilisateurRepository;
import com.gt.gestfinance.service.IUtilisateurService;
import com.gt.gestfinance.util.MPConstants;
import com.gt.gestfinance.util.ResponseUtil;
import com.gt.gestfinance.util.UrlConstants;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur de gestion des utilisateurs
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@RestController
@Api(tags = {"utilisateurs"})
public class UtilisateurController extends BaseEntityController<Utilisateur, Long> {

    private final Logger log = LoggerFactory.getLogger(UtilisateurController.class);
    private UtilisateurRepository utilisateurRepository;

    public UtilisateurController(IUtilisateurService userService) {
        super(userService);
    }

    /**
     * POST /users : Créer un utilisateur.
     * <p>
     * Créer un nouveau utilisateur si le nom d'utilisateur et l'email ne sont
     * pas encore enregistrés, et envoie un mail de mot de passe par défaut sur
     * l'adresse email. L'utilisateur a besoin d'être activé.
     * </p>
     *
     * @param managedUserVM L'utilisateur à créer
     * @return le ResponseEntity avec le status 201 (Created) et le nouvel
     * utilisateur dans le corps, ou le status 400 (Bad Request) si le nom
     * d'utilisateur et l'email sont déjà utilisés
     * @throws URISyntaxException si le Location URI syntax est incorrect
     */
    @PostMapping(UrlConstants.Utilisateur.UTILISATEUR_RACINE)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Créer un utilisateur",
            notes = "Un email de modification du mot de passe est envoyé à l’utilisateur nouvellement créer")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Utilisateur enregistré avec succès")
            ,
            @ApiResponse(code = 500, message = "Si le nom  d'utilisateur et l'email sont déjà utilisés", response = Response.class)})
    public ResponseEntity ajouter(
            @ApiParam(value = "L'utilisateur à créer", required = true)
            @Valid @RequestBody ManagedUserVM managedUserVM) throws CustomException {
        log.debug("REST request to save Utilisateur : {}", managedUserVM);
        if (managedUserVM.login == null || managedUserVM.login.isEmpty()) {
            throw new CustomException(MPConstants.LE_LOGIN_EST_OBLIGATOIRE);
        }
        if (managedUserVM.nom == null || managedUserVM.nom.isEmpty()) {
            throw new CustomException(MPConstants.LE_NOM_EST_OBLIGATOIRE);
        }
        managedUserVM.getUtilisateur().setMotDePasse(managedUserVM.password);
        if (managedUserVM.getUtilisateur().getIdentifiant() != null) {
            throw new CustomException(MPConstants.UTILISATEUR_DOIT_PAS_AVOIR_ID);
            // Lowercase     the user login before comparing with database
        } else if (utilisateurRepository.findOneByLogin(managedUserVM.getUtilisateur().getLogin().toLowerCase()).isPresent()) {
            throw new CustomException(MPConstants.LE_LOGIN_EXISTE_DEJA);
        } else {
            Utilisateur utilisateur = managedUserVM.getUtilisateur();
            ((IUtilisateurService) service).creerUnUtilisateur(utilisateur);
            return new ResponseEntity<>(ResponseBuilder.info()
                    .code(null)
                    .title(DefaultMP.TITLE_SUCCESS)
                    .message(DefaultMP.MESSAGE_SUCCESS)
                    .data(utilisateur).buildI18n(), HttpStatus.CREATED);
        }
    }

    /**
     * PUT /users/: Modifier un utilisateur.
     *
     * @param managedUserVM L'utilisateur à modifier
     * @return le ResponseEntity avec le status 200 (OK) et l'utilisateur
     * modifié , ou avec le status 400 (Bad Request) si le nom d'utilisateur ou
     * l'email sont déjà utilisés, ou le status 500 (Internal Server Error) si
     * l'utilisateur ne peut pas être modifié
     */
    @PutMapping(UrlConstants.Utilisateur.UTILISATEUR_RACINE)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Modifier un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Utilisateur.class)
            ,
            @ApiResponse(code = 500, message = "Si le nom d'utilisateur ou l'email sont déjà utilisés", response = Response.class)
            ,
            @ApiResponse(code = 500, message = "Si l'utilisateur ne peut pas être modifié", response = Response.class)})
    public ResponseEntity<Response> modifier(
            @ApiParam(value = "L'utilisateur à modifier", required = true)
            @Valid @RequestBody ManagedUserVM managedUserVM) throws CustomException {
        log.debug("REST request to update Utilisateur : {}", managedUserVM);
        if (managedUserVM.login == null || managedUserVM.login.isEmpty()) {
            throw new CustomException(MPConstants.LE_LOGIN_EST_OBLIGATOIRE);
        }
        if (managedUserVM.nom == null || managedUserVM.nom.isEmpty()) {
            throw new CustomException(MPConstants.LE_NOM_EST_OBLIGATOIRE);
        }
        Optional<Utilisateur> existingUser = utilisateurRepository
                .findOneByLogin(managedUserVM.getUtilisateur().getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getIdentifiant()
                .equals(managedUserVM.getUtilisateur().getIdentifiant()))) {
            throw new CustomException(MPConstants.LE_LOGIN_EXISTE_DEJA);
        }
        Optional<Utilisateur> updatedUser = ((IUtilisateurService) service)
                .modifierUnUtilisateur(managedUserVM.getUtilisateur());
        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(updatedUser.orElse(null)).buildI18n(), HttpStatus.OK);
    }

    /**
     * GET /users : Retourne la liste des utilisateurs
     *
     * @param pageable les informations de pagination
     * @return le ResponseEntity avec le status 200 (OK) et la liste des
     * utilisateurs
     */
    @GetMapping(UrlConstants.Utilisateur.UTILISATEUR_RACINE)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Retourner la liste des utilisateurs",
            response = Utilisateur.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")}
    )
    public ResponseEntity<Response> recupererLaListeDesUtilisateurs(
            @ApiParam(value = "Information de pagination (page & size)") Pageable pageable) {

        final Page<Utilisateur> users = ((IUtilisateurService) service).recupererLaListeDesUtlisateurs(pageable);

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(users).buildI18n(), HttpStatus.OK);
    }

    /**
     * @return La liste des droits
     */
    @GetMapping(UrlConstants.Utilisateur.UTILISATEUR_PERMISSION)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Retourner la liste des droits",
            response = String.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public List<String> recupererLesPermissionsDeLUtilisateurCourant() {
        return ((IUtilisateurService) service).recupererLesPermissions();
    }

    /**
     * GET /users/:login : Retourne l'utilisateur à partir de son login.
     *
     * @param login Le login de l'utilisateur
     * @return le ResponseEntity avec le status 200 (OK) et l'utilisateur, ou
     * avec le status 404 (Not Found)
     */
    @GetMapping(UrlConstants.Utilisateur.UTILISATEUR_LOGIN)
    @ApiOperation(value = "Retourner l'utilisateur à partir de son login",
            response = Utilisateur.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Utilisateur> recupererUnUtilisateurAPartirDeSonLogin(
            @ApiParam(value = "Le login de l'utilisateur", required = true) @PathVariable String login) {
        log.debug("REST request to get Utilisateur : {}", login);
        return ResponseUtil.wrapOrNotFound(
                ((IUtilisateurService) service).recupererUnUtilisateurAvecSesPermissionsAPartirDeSonLogin(login));
    }

    /**
     * DELETE /users/:login : Supprime l'utilisateur à partir de son login.
     *
     * @param login Le login de l'utilisateur à supprimer
     * @return Le ResponseEntity avec le status 200 (OK)
     */
    @DeleteMapping(UrlConstants.Utilisateur.UTILISATEUR_LOGIN)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Supprimer l'utilisateur à partir de son login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> supprimerUnUtilisateur(
            @ApiParam(value = "Le login de l'utilisateur", required = true) @PathVariable String login)
            throws CustomException {
        log.debug("REST request to delete Utilisateur: {}", login);
        ((IUtilisateurService) service).supprimerUnUtilisateur(login);
        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(Boolean.TRUE).buildI18n(), HttpStatus.OK);
    }

    /**
     * PUT /users/{userId}/states
     * <p>
     * Changement de l'état d'un utilisateur
     *
     * @param userId : L'identifiant de l'utilisateur
     * @param state  : Le nouvel état
     * @return ResponseEntity
     * @throws CustomException : Une exception
     */
    @RequestMapping(value = UrlConstants.Utilisateur.UTILISATEUR_ID_STATE, method = RequestMethod.PUT)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Changer l'état d'un utilisateur",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<Response> changerLEtatDeLUtilisateur(
            @ApiParam(value = "L'identifiant de l'utilisateur", required = true)
            @PathVariable("userId") Long userId,
            @ApiParam(value = "Le nouvel état", required = true)
            @RequestBody StateWrapper state) throws CustomException {
        if (service.findOne(userId) == null) {
            throw new CustomException(MPConstants.UTILISATEUR_INTROUVABLE);
        }
        return super.changeState(userId, state);
    }

    /**
     * GET /users/search
     * <p>
     * Rechercher des utilisateurs
     *
     * @param utilisateurFormulaireDeFiltre : L'information de filtre
     * @return ResponseEntity
     */
    @Secured(AuthoritiesConstants.ADMIN)
    @RequestMapping(value = UrlConstants.Utilisateur.UTILISATEUR_RECHERCHE, method = RequestMethod.POST)
    @ApiOperation(value = "Rechercher des utilisateurs",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<Response> rechercher(
            @ApiParam(value = "L'information de filtre")
            @RequestBody UtilisateurFormulaireDeFiltre utilisateurFormulaireDeFiltre) {

        Pageable pageable = null;
        Page<Utilisateur> userFind;

        if (utilisateurFormulaireDeFiltre.getPage() != null && utilisateurFormulaireDeFiltre.getSize() != null) {
            pageable = new PageRequest(utilisateurFormulaireDeFiltre.getPage(),
                    utilisateurFormulaireDeFiltre.getSize(), Sort.Direction.ASC, "identifiant");
        }

        if (utilisateurFormulaireDeFiltre.getUtilisateur() == null) {
            userFind = new PageImpl<>(Collections.emptyList(), new PageRequest(1, 5), 0);
        } else {
            userFind = service.findAllByCriteres(utilisateurFormulaireDeFiltre.getCriteres(), pageable);
        }

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(userFind)
                .buildI18n(), HttpStatus.OK);
    }

    @Autowired
    public void setUtilisateurRepository(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }
}
