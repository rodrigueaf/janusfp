package com.gt.gestfinance.controller;

import com.gt.gestfinance.dto.DateFiltre;
import com.gt.gestfinance.entity.Compte;
import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.filtreform.CompteFormulaireDeFiltre;
import com.gt.gestfinance.service.ICompteService;
import com.gt.gestfinance.util.*;
import io.swagger.annotations.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Collections;

/**
 * Contrôleur de gestion des comptes
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@RestController
@Api(tags = {"comptes"})
public class CompteController extends BaseEntityController<Compte, Integer> {

    public CompteController(ICompteService compteService) {
        super(compteService);
    }

    @RequestMapping(value = UrlConstants.Compte.COMPTE_RACINE + "/liste/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response> selectionnerListe(@PathVariable String id) {
        String[] split = id.split("&");
        Integer[] ints = new Integer[split.length];
        for (int i = 0; i < split.length; i++) {
            ints[i] = Integer.valueOf(split[i]);
        }
        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(((ICompteService) service).recupererLaListeVersionnee(ints))
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * POST /comptes : Créer un compte.
     *
     * @param compte Le compte à créer
     * @return le ResponseEntity avec le status 201 (Created) et le nouveau
     * profil dans le corps, ou le status 500 (Bad Request) si le nom du profil
     * déjà utilisés
     * @throws URISyntaxException si le Location URI syntax est incorrect
     */
    @PostMapping(UrlConstants.Compte.COMPTE_RACINE)
    @Secured(PermissionsConstants.COMPTE_PERMISSION)
    @ApiOperation(value = "Créer un compte")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Compte enregistré avec succès")
            ,
            @ApiResponse(code = 500, message = "Si le contrainte d'intégrités ne sont pas respectées", response = Response.class)})
    public ResponseEntity<Response> ajouter(@Valid @RequestBody Compte compte)
            throws CustomException {
        return super.create(compte);
    }

    /**
     * PUT /comptes : Modifier un compte.
     *
     * @param compte Le compte à modifier
     * @return le ResponseEntity avec le status 200 (OK) et le compte modifié ,
     * ou avec le status 500 (Bad Request) si le nom du compte existe déjà, ou
     * le status 500 (Internal Server Error) si le compte ne peut pas être
     * modifié
     */
    @PutMapping(UrlConstants.Compte.COMPTE_RACINE)
    @Secured(PermissionsConstants.COMPTE_PERMISSION)
    @ApiOperation(value = "Modifier un compte")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Profil.class)
            ,
            @ApiResponse(code = 500, message = "Si le compte ne peut pas être modifié", response = Response.class)})
    public ResponseEntity<Response> modifier(
            @ApiParam(value = "Le compte à modifier", required = true)
            @Valid @RequestBody Compte compte) throws CustomException {

        return super.update(compte.getIdentifiant(), compte);
    }

    /**
     * GET /comptes/{compteId} : Retourne le compte à partir de son identifiant.
     *
     * @param compteId L'identifiant du compte
     * @return le ResponseEntity avec le status 200 (OK) et le profil, ou avec
     * le status 500
     */
    @GetMapping(UrlConstants.Compte.COMPTE_ID)
    @ApiOperation(value = "Retourner le compte à partir de son identifiant",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> recupererUnCompte(
            @ApiParam(value = "L'identifiant du compte", required = true)
            @PathVariable Integer compteId) {

        return super.readOne(compteId);
    }

    /**
     * GET /comptes : Retourne la liste des comptes
     *
     * @param pageable les informations de pagination
     * @return le ResponseEntity avec le status 200 (OK) et la liste des comptes
     */
    @GetMapping(UrlConstants.Compte.COMPTE_RACINE)
    @Secured(PermissionsConstants.COMPTE_PERMISSION)
    @ApiOperation(value = "Retourner la liste des comptes",
            response = Response.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")}
    )
    public ResponseEntity<Response> recupererLaListeDesComptes(
            @ApiParam(value = "Information de pagination (page & size)") Pageable pageable) {

        return super.readAll(pageable.getPageNumber(), pageable.getPageSize());
    }

    /**
     * GET /comptes/prevision-realisation/{budgetId} : Retourner la liste des prévison et réalisation
     *
     * @return le ResponseEntity avec le status 200 (OK) et la liste des comptes
     */
    @GetMapping(UrlConstants.Compte.COMPTE_PREV_REAL)
    @Secured(PermissionsConstants.COMPTE_PERMISSION)
    @ApiOperation(value = "Retourner la liste des prévison et réalisation",
            response = Response.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")}
    )
    public ResponseEntity<Response> realisationParPrevision(
            @PathVariable Integer budgetId) {

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(((ICompteService) service).realisationParPrevision(budgetId))
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * POST /comptes/{compteId}/grand-livre : Retourner le grand livre d'un compte
     *
     * @return le ResponseEntity avec le status 200 (OK) et la liste des comptes
     */
    @PostMapping(UrlConstants.Compte.COMPTE_GRD_LIVRE)
    @Secured(PermissionsConstants.COMPTE_PERMISSION)
    @ApiOperation(value = "Retourner le grand livre d'un compte",
            response = Response.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")}
    )
    public ResponseEntity<Response> recupererLeGrandLivre(
            @PathVariable Integer compteId, @RequestBody DateFiltre dateFiltre) {

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(((ICompteService) service).recupererLeGrandLivre(compteId,
                        dateFiltre.dateDebut, dateFiltre.dateFin))
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * DELETE /comptes/:compteId : Supprime le compte à partir de son identifiant.
     *
     * @param compteId L'identifiant du compte à supprimer
     * @return Le ResponseEntity avec le status 200 (OK)
     */
    @DeleteMapping(UrlConstants.Compte.COMPTE_ID)
    @Secured(PermissionsConstants.COMPTE_PERMISSION)
    @ApiOperation(value = "Supprimer le compte à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> supprimerUnCompte(
            @ApiParam(value = "L'identififant du compte", required = true)
            @PathVariable Integer compteId,
            @ApiParam(value = "Si il s'agit d'une suppression forcée", required = true)
            @RequestParam(required = false) Boolean forcer) throws CustomException {

        boolean resultat;
        if (forcer != null && forcer) {
            resultat = ((ICompteService) service).supprimerForcer(compteId);
        } else
            resultat = ((ICompteService) service).supprimer(compteId);

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(resultat)
                .buildI18n(), HttpStatus.NO_CONTENT);
    }

    /**
     * GET /comptes/search
     * <p>
     * Rechercher des comptes
     *
     * @param compteFormulaireDeFiltre : L'information de filtre
     * @return ResponseEntity
     */
    @Secured(PermissionsConstants.COMPTE_PERMISSION)
    @RequestMapping(value = UrlConstants.Compte.COMPTE_RECHERCHE, method = RequestMethod.POST)
    @ApiOperation(value = "Rechercher des comptes",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<Response> rechercher(
            @ApiParam(value = "L'information de filtre")
            @RequestBody CompteFormulaireDeFiltre compteFormulaireDeFiltre) {

        Pageable pageable = null;
        Page<Compte> comptesTrouver;

        if (compteFormulaireDeFiltre.getPage() != null && compteFormulaireDeFiltre.getSize() != null) {
            pageable = new PageRequest(compteFormulaireDeFiltre.getPage(),
                    compteFormulaireDeFiltre.getSize(), Sort.Direction.DESC, "identifiant");
        }

        if (compteFormulaireDeFiltre.getCompte() == null) {
            comptesTrouver = new PageImpl<>(Collections.emptyList(), new PageRequest(1, 5), 0);
        } else {
            comptesTrouver = service.findAllByCriteres(compteFormulaireDeFiltre.getCriteres(), pageable);
        }

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(comptesTrouver)
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * PUT /comptes/{compteId}/states
     * <p>
     * Changement l'état d'un compte
     *
     * @param compteId : L'identifiant du compte
     * @param state    : Le nouvel état
     * @return ResponseEntity
     * @throws CustomException : Une exception
     */
    @RequestMapping(value = UrlConstants.Compte.COMPTE_ID_STATE, method = RequestMethod.PUT)
    @Secured(PermissionsConstants.COMPTE_PERMISSION)
    @ApiOperation(value = "Changer l'état d'un compte",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<Response> changerLEtatDUnCompte(
            @ApiParam(value = "L'identifiant du compte", required = true)
            @PathVariable("compteId") int compteId,
            @ApiParam(value = "Le nouvel état", required = true)
            @RequestBody StateWrapper state) throws CustomException {
        return super.changeState(compteId, state);
    }
}
