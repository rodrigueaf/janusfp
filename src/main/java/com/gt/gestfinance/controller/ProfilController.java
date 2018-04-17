package com.gt.gestfinance.controller;

import com.gt.base.controller.BaseEntityController;
import com.gt.base.exception.CustomException;
import com.gt.base.util.*;
import com.gt.gestfinance.dto.DomainDto;
import com.gt.gestfinance.dto.ManagedProfilVM;
import com.gt.gestfinance.entity.Permission;
import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.filtreform.ProfilFormulaireDeFiltre;
import com.gt.gestfinance.repository.PermissionRepository;
import com.gt.gestfinance.repository.ProfilRepository;
import com.gt.gestfinance.service.IProfilService;
import com.gt.gestfinance.util.MPConstants;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contrôleur de gestion des utilisateurs
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@RestController
@Api(tags = {"profils"})
public class ProfilController extends BaseEntityController<Profil, Integer> {

    private final Logger log = LoggerFactory.getLogger(ProfilController.class);
    private ProfilRepository profilRepository;
    private PermissionRepository permissionRepository;

    public ProfilController(IProfilService profilService) {
        super(profilService);
    }

    /**
     * POST /profils : Créer un profil utilisateur.
     *
     * @param managedProfilVM Le profil à créer
     * @return le ResponseEntity avec le status 201 (Created) et le nouveau
     * profil dans le corps, ou le status 500 (Bad Request) si le nom du profil
     * déjà utilisés
     * @throws URISyntaxException si le Location URI syntax est incorrect
     */
    @PostMapping(UrlConstants.Profil.PROFIL_RACINE)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Créer un profil utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Profil enregistré avec succès")
            ,
            @ApiResponse(code = 500, message = "Si le nom du profil déjà utilisés", response = Response.class)})
    public ResponseEntity<Response> ajouter(@Valid @RequestBody ManagedProfilVM managedProfilVM)
            throws CustomException {
        log.debug("REST request to save profil : {}", managedProfilVM);

        if (managedProfilVM.identifiant != null) {
            throw new CustomException(MPConstants.LE_PROFIL_DOIT_PAS_AVOIR_ID);
        } else if (managedProfilVM.nom == null || managedProfilVM.nom.isEmpty()) {
            throw new CustomException(MPConstants.LE_NOM_DU_PROFIL_OBLIGATOIRE);
        } else if (profilRepository.findOneByNom(managedProfilVM.nom).isPresent()) {
            throw new CustomException(MPConstants.LE_NOM_DU_PROFIL_EXSTE_DEJA);
        } else {
            if (managedProfilVM.permissions != null) {
                managedProfilVM.getProfil().setPermissions(managedProfilVM.permissions);
            }
            return super.create(managedProfilVM.getProfil());
        }
    }

    /**
     * PUT /profils : Modifier un profil utilisateur.
     *
     * @param managedProfilVM Le profil à modifier
     * @return le ResponseEntity avec le status 200 (OK) et le profil modifié ,
     * ou avec le status 500 (Bad Request) si le nom du profil existe déjà, ou
     * le status 500 (Internal Server Error) si le profil ne peut pas être
     * modifié
     */
    @PutMapping(UrlConstants.Profil.PROFIL_RACINE)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Modifier un profil utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Profil.class)
            ,
            @ApiResponse(code = 500, message = "Si le nom du profil est déjà utilisé", response = Response.class)
            ,
            @ApiResponse(code = 500, message = "Si le profil ne peut pas être modifié", response = Response.class)})
    public ResponseEntity<Response> modifier(
            @ApiParam(value = "Le profil à modifier", required = true)
            @Valid @RequestBody ManagedProfilVM managedProfilVM)
            throws CustomException {
        log.debug("REST request to update profil : {}", managedProfilVM);
        if (managedProfilVM.nom == null || managedProfilVM.nom.isEmpty()) {
            throw new CustomException(MPConstants.LE_NOM_DU_PROFIL_OBLIGATOIRE);
        }
        Optional<Profil> existingProfil = profilRepository.findOneByNom(managedProfilVM.nom);
        if (existingProfil.isPresent() && (!existingProfil.get().getIdentifiant().equals(managedProfilVM.identifiant))) {
            throw new CustomException(MPConstants.LE_NOM_DU_PROFIL_EXSTE_DEJA);
        }
        if (managedProfilVM.permissions != null) {
            // Si les droits du profil doivent être réinitialisés ou s'il s'agit d'un ajout
            if (!managedProfilVM.keepOldAuthorities) {
                managedProfilVM.getProfil().setPermissions(managedProfilVM.permissions);
            } else {
                managedProfilVM.getProfil().getPermissions()
                        .addAll(service.findOne(managedProfilVM.identifiant).getPermissions());
                managedProfilVM.getProfil().getPermissions()
                        .addAll(managedProfilVM.permissions);
            }
        }

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(service.saveAndFlush(managedProfilVM.getProfil())).buildI18n(), HttpStatus.OK);
    }

    /**
     * GET /profils : Retourne la liste des profils
     *
     * @param pageable les informations de pagination
     * @return le ResponseEntity avec le status 200 (OK) et la liste des profils
     */
    @GetMapping(UrlConstants.Profil.PROFIL_RACINE)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Retourner la liste des profils",
            response = Profil.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")}
    )
    public ResponseEntity<Response> recupererLaListeDesProfils(
            @ApiParam(value = "Information de pagination (page & size)") Pageable pageable) {

        return super.readAll(pageable.getPageNumber(), pageable.getPageSize());
    }

    /**
     * GET /profils/:nom/permissions : Retourne les droits du profil
     *
     * @param nom
     * @return La liste des droits
     */
    @GetMapping(UrlConstants.Profil.PROFIL_PERMISSION)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Retourner la liste des droits",
            response = String.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> recupererLesPermissionsDUnProfil(
            @ApiParam(value = "Le nom du profil", required = true) @PathVariable String nom) {
        Optional<Profil> o = profilRepository.findOneWithPermissionsByNom(nom);
        return o.map(profil -> new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(profil.getPermissions().stream()
                        .map(Permission::getNom)
                        .collect(Collectors.toList()))
                .buildI18n(), HttpStatus.OK)).orElseGet(() -> ResponseEntity.notFound().build());

    }

    /**
     * POST /profils/:nom/permissions : Enregistrer les droits du profil
     *
     * @param nom : Le nom du profil
     * @return
     */
    @PostMapping(UrlConstants.Profil.PROFIL_PERMISSION)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Enregistrer les droits du profil",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> ajouterDesPermissionsAUnProfil(
            @ApiParam(value = "Le nom du profil", required = true) @PathVariable String nom,
            @ApiParam(value = "La liste des permissions", required = true) @RequestBody Set<String> authorities)
            throws CustomException {

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(((IProfilService) service).modifierLesPermissions(nom, authorities))
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * GET /profils/{profilId} : Retourne le profil à partir de son identifiant.
     *
     * @param profilId L'identifiant du profil
     * @return le ResponseEntity avec le status 200 (OK) et le profil, ou avec
     * le status 500
     */
    @GetMapping(UrlConstants.Profil.PROFIL_ID)
    @ApiOperation(value = "Retourner le profil à partir de son identifiant",
            response = Profil.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> recupererUnProfil(
            @ApiParam(value = "L'identifiant du profil", required = true)
            @PathVariable Integer profilId) {
        log.debug("REST request to get Profil : {}", profilId);

        return super.readOne(profilId);
    }

    /**
     * GET /profils/nom/{nom} : Retourne le profil à partir de son nom.
     *
     * @param nom Le nom du profil
     * @return le ResponseEntity avec le status 200 (OK) et le profil, ou avec
     * le status 500
     */
    @GetMapping(UrlConstants.Profil.PROFIL_NOM)
    @ApiOperation(value = "Retourner le profil à partir de son identifiant",
            response = Profil.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> recupererUnProfil(
            @ApiParam(value = "Le nom du profil", required = true)
            @PathVariable String nom) throws CustomException {
        log.debug("REST request to get Profil : {}", nom);

        Optional<Profil> o = profilRepository.findOneByNom(nom);

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(o.orElseThrow(CustomException::new))
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * DELETE /profils/:profilId : Supprime le profil à partir de son identifiant.
     *
     * @param profilId L'identifiant du profil à supprimer
     * @return Le ResponseEntity avec le status 200 (OK)
     */
    @DeleteMapping(UrlConstants.Profil.PROFIL_ID)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Supprimer le profil à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> supprimerUnProfil(
            @ApiParam(value = "Le nom du profil", required = true) @PathVariable Integer profilId) throws CustomException {
        log.debug("REST request to delete Profil: {}", profilId);

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(((IProfilService) service).supprimer(profilId))
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * PUT /profils/{profilId}/states
     * <p>
     * Changement l'état d'un profil
     *
     * @param profilId : L'identifiant du profil
     * @param state    : Le nouvel état
     * @return ResponseEntity
     * @throws CustomException : Une exception
     */
    @RequestMapping(value = UrlConstants.Profil.PROFIL_ID_STATE, method = RequestMethod.PUT)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Changer l'état d'un profil",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<Response> changerLEtatDUnProfil(
            @ApiParam(value = "L'identifiant du profil", required = true)
            @PathVariable("profilId") int profilId,
            @ApiParam(value = "Le nouvel état", required = true)
            @RequestBody StateWrapper state) throws CustomException {
        if (profilRepository.findOne(profilId) == null) {
            throw new CustomException(MPConstants.PROFIL_INTROUVABLE);
        }
        return super.changeState(profilId, state);
    }

    /**
     * GET /profils/search
     * <p>
     * Rechercher des profils
     *
     * @param profilFormulaireDeFiltre : L'information de filtre
     * @return ResponseEntity
     */
    @Secured(AuthoritiesConstants.ADMIN)
    @RequestMapping(value = UrlConstants.Profil.PROFIL_RECHERCHE, method = RequestMethod.POST)
    @ApiOperation(value = "Rechercher des profils",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<Response> rechercher(
            @ApiParam(value = "L'information de filtre")
            @RequestBody ProfilFormulaireDeFiltre profilFormulaireDeFiltre) {

        Pageable pageable = null;
        Page<Profil> profilFind;

        if (profilFormulaireDeFiltre.getPage() != null && profilFormulaireDeFiltre.getSize() != null) {
            pageable = new PageRequest(profilFormulaireDeFiltre.getPage(),
                    profilFormulaireDeFiltre.getSize(), Sort.Direction.ASC, "identifiant");
        }

        if (profilFormulaireDeFiltre.getProfil() == null) {
            profilFind = new PageImpl<>(Collections.emptyList(), new PageRequest(1, 5), 0);
        } else {
            profilFind = service.findAllByCriteres(profilFormulaireDeFiltre.getCriteres(), pageable);
        }

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(profilFind)
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * GET /domains : Retourne les domaines
     *
     * @return La liste des domaines
     */
    @GetMapping(UrlConstants.Profil.DOMAIN_URL)
    @Secured(AuthoritiesConstants.ADMIN)
    @ApiOperation(value = "Retourner la liste des domaines",
            response = String.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Response.class)})
    public ResponseEntity<Response> getDomains() {

        DomainDto domainDto = new DomainDto("SECURITE", new HashSet<>(permissionRepository.findAll()));

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(Collections.singletonList(domainDto))
                .buildI18n(), HttpStatus.OK);
    }

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Autowired
    public void setProfilRepository(ProfilRepository profilRepository) {
        this.profilRepository = profilRepository;
    }
}
