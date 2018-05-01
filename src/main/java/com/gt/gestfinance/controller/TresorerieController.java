package com.gt.gestfinance.controller;

import com.gt.gestfinance.controller.BaseEntityController;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.util.DefaultMP;
import com.gt.gestfinance.util.Response;
import com.gt.gestfinance.util.ResponseBuilder;
import com.gt.gestfinance.dto.DateFiltre;
import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.entity.Tresorerie;
import com.gt.gestfinance.filtreform.TresorerieFormulaireDeFiltre;
import com.gt.gestfinance.service.ITresorerieService;
import com.gt.gestfinance.util.PermissionsConstants;
import com.gt.gestfinance.util.UrlConstants;
import io.swagger.annotations.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

/**
 * Contrôleur de gestion des Tresorerie
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@RestController
@Api(tags = {"tresoreries"})
public class TresorerieController extends BaseEntityController<Tresorerie, Integer> {

    public TresorerieController(ITresorerieService tresorerieService) {
        super(tresorerieService);
    }

    /**
     * POST /tresoreries : Créer une trésorerie.
     *
     * @param tresorerie La trésorerie à créer
     */
    @PostMapping(UrlConstants.Tresorerie.TRESORERIE_RACINE)
    @Secured(PermissionsConstants.TRESORERIE_PERMISSION)
    @ApiOperation(value = "Créer une trésorerie")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Tresorerie enregistré avec succès")
            ,
            @ApiResponse(code = 500, message = "Si le contrainte d'intégrités ne sont pas respectées",
                    response = Response.class)})
    public ResponseEntity<Response> ajouter(@Valid @RequestBody Tresorerie tresorerie)
            throws CustomException {
        return super.create(tresorerie);
    }

    /**
     * PUT /tresoreries : Modifier une trésorerie.
     *
     * @param tresorerie La trésorerie à modifier
     * @return le ResponseEntity avec le status 200 (OK) et la trésorerie modifié ,
     * ou avec le status 500 (Bad Request) si le libellé de la trésorerie existe déjà, ou
     * le status 500 (Internal Server Error) si la trésorerie ne peut pas être
     * modifié
     */
    @PutMapping(UrlConstants.Tresorerie.TRESORERIE_RACINE)
    @Secured(PermissionsConstants.TRESORERIE_PERMISSION)
    @ApiOperation(value = "Modifier une trésorerie")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Profil.class)
            ,
            @ApiResponse(code = 500, message = "Si la trésorerie ne peut pas être modifié",
                    response = Response.class)})
    public ResponseEntity<Response> modifier(
            @ApiParam(value = "La trésorerie à modifier", required = true)
            @Valid @RequestBody Tresorerie tresorerie) throws CustomException {

        return super.update(tresorerie.getIdentifiant(), tresorerie);
    }

    /**
     * GET /tresoreries/{tresorerieId} : Retourne la trésorerie à partir de son identifiant.
     *
     * @param tresorerieId L'identifiant de la trésorerie
     * @return le ResponseEntity avec le status 200 (OK) et la trésorerie, ou avec
     * le status 500
     */
    @GetMapping(UrlConstants.Tresorerie.TRESORERIE_ID)
    @ApiOperation(value = "Retourner la trésorerie à partir de son identifiant",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> recupererUnTresorerie(
            @ApiParam(value = "L'identifiant de la trésorerie", required = true)
            @PathVariable Integer tresorerieId) {

        return super.readOne(tresorerieId);
    }

    /**
     * GET /tresoreries : Retourne la liste des comptes
     *
     * @param pageable les informations de pagination
     * @return le ResponseEntity avec le status 200 (OK) et la liste des comptes
     */
    @GetMapping(UrlConstants.Tresorerie.TRESORERIE_RACINE)
    @Secured(PermissionsConstants.TRESORERIE_PERMISSION)
    @ApiOperation(value = "Retourner la liste des tresoreries",
            response = Response.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")}
    )
    public ResponseEntity<Response> recupererLaListeDesTresoreries(
            @ApiParam(value = "Information de pagination (page & size)") Pageable pageable) {

        return super.readAll(pageable.getPageNumber(), pageable.getPageSize());
    }

    /**
     * DELETE /tresoreries/{tresorerieId} : Supprime la trésorerie à partir de son identifiant.
     *
     * @param tresorerieId L'identifiant de la trésorerie à supprimer
     * @return Le ResponseEntity avec le status 200 (OK)
     */
    @DeleteMapping(UrlConstants.Tresorerie.TRESORERIE_ID)
    @Secured(PermissionsConstants.TRESORERIE_PERMISSION)
    @ApiOperation(value = "Supprimer la trésorerie à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> supprimerUnTresorerie(
            @ApiParam(value = "L'identififant de la trésorerie", required = true)
            @PathVariable Integer tresorerieId) throws CustomException {

        return super.delete(tresorerieId);
    }

    /**
     * GET /tresoreries/search
     * <p>
     * Rechercher des tresoreries
     *
     * @param tresorerieFormulaireDeFiltre : L'information de filtre
     * @return ResponseEntity
     */
    @Secured(PermissionsConstants.TRESORERIE_PERMISSION)
    @RequestMapping(value = UrlConstants.Tresorerie.TRESORERIE_RECHERCHE, method = RequestMethod.POST)
    @ApiOperation(value = "Rechercher des tresoreries",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<Response> rechercher(
            @ApiParam(value = "L'information de filtre")
            @RequestBody TresorerieFormulaireDeFiltre tresorerieFormulaireDeFiltre) {

        Pageable pageable = null;
        Page<Tresorerie> tresoreriesTrouver;

        if (tresorerieFormulaireDeFiltre.getPage() != null && tresorerieFormulaireDeFiltre.getSize() != null) {
            pageable = new PageRequest(tresorerieFormulaireDeFiltre.getPage(),
                    tresorerieFormulaireDeFiltre.getSize(), Sort.Direction.DESC, "identifiant");
        }

        if (tresorerieFormulaireDeFiltre.getTresorerie() == null) {
            tresoreriesTrouver = new PageImpl<>(Collections.emptyList(), new PageRequest(1, 5), 0);
        } else {
            tresoreriesTrouver = service.findAllByCriteres(tresorerieFormulaireDeFiltre.getCriteres(), pageable);
        }

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(tresoreriesTrouver)
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * POST /tresoreries/{tresorerieId}/grand-livre : Retourner le grand livre d'une trésorerie
     *
     * @return le ResponseEntity avec le status 200 (OK) et la liste des comptes
     */
    @PostMapping(UrlConstants.Tresorerie.TRESORERIE_GRD_LIVRE)
    @Secured(PermissionsConstants.TRESORERIE_PERMISSION)
    @ApiOperation(value = "Retourner le grand livre d'une trésorerie",
            response = Response.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")}
    )
    public ResponseEntity<Response> recupererLeGrandLivre(
            @PathVariable Integer tresorerieId, @RequestBody DateFiltre dateFiltre) {

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(((ITresorerieService) service).recupererLeGrandLivre(tresorerieId,
                        dateFiltre.dateDebut, dateFiltre.dateFin))
                .buildI18n(), HttpStatus.OK);
    }
}
