package com.gt.gestfinance.controller;

import com.gt.gestfinance.controller.BaseEntityController;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.util.DefaultMP;
import com.gt.gestfinance.util.Response;
import com.gt.gestfinance.util.ResponseBuilder;
import com.gt.gestfinance.dto.OperationVM;
import com.gt.gestfinance.entity.Operation;
import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.filtreform.OperationFormulaireDeFiltre;
import com.gt.gestfinance.repository.OperationDetailRepository;
import com.gt.gestfinance.repository.OperationRepository;
import com.gt.gestfinance.service.IOperationDetailService;
import com.gt.gestfinance.service.IOperationService;
import com.gt.gestfinance.util.PermissionsConstants;
import com.gt.gestfinance.util.UrlConstants;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Collections;

/**
 * Contrôleur de gestion des Budget
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@RestController
@Api(tags = {"operations"})
public class OperationController extends BaseEntityController<Operation, Integer> {

    private OperationDetailRepository operationDetailRepository;
    private IOperationDetailService operationDetailService;
    private OperationRepository operationRepository;

    public OperationController(IOperationService operationService) {
        super(operationService);
    }

    /**
     * POST /operations : Créer une opération.
     *
     * @param operationVM Le operationVM à créer
     * @return le ResponseEntity avec le status 201 (Created) et le nouveau
     * profil dans le corps, ou le status 500 (Bad Request) si le nom du profil
     * déjà utilisés
     * @throws URISyntaxException si le Location URI syntax est incorrect
     */
    @PostMapping(UrlConstants.Operation.OPERATION_RACINE)
    @Secured(PermissionsConstants.OPERATION_PERMISSION)
    @ApiOperation(value = "Créer une opération")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Opération enregistrée avec succès")
            ,
            @ApiResponse(code = 500, message = "Si le contrainte d'intégrités ne sont pas respectées",
                    response = Response.class)})
    public ResponseEntity<Response> ajouter(@Valid @RequestBody OperationVM operationVM)
            throws CustomException {

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(((IOperationService) service).ajouter(operationVM))
                .buildI18n(), HttpStatus.CREATED);
    }

    /**
     * GET /operations/{operationId} : Retourne l'opération à partir de son identifiant.
     *
     * @param operationId L'identifiant du profil
     * @return le ResponseEntity avec le status 200 (OK) et le profil, ou avec
     * le status 500
     */
    @GetMapping(UrlConstants.Operation.OPERATION_ID)
    @ApiOperation(value = "Retourner l'opération à partir de son identifiant",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> recupererUneOperation(
            @ApiParam(value = "L'identifiant de l'opération", required = true)
            @PathVariable Integer operationId) {
        return super.readOne(operationId);
    }

    /**
     * GET /operations : Retourne la liste des opérations
     *
     * @param pageable les informations de pagination
     * @return le ResponseEntity avec le status 200 (OK) et la liste des opérations
     */
    @GetMapping(UrlConstants.Operation.OPERATION_RACINE)
    @Secured(PermissionsConstants.OPERATION_PERMISSION)
    @ApiOperation(value = "Retourner la liste des opérations",
            response = Operation.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")}
    )
    public ResponseEntity<Response> recupererLaListeDesOperations(
            @ApiParam(value = "Information de pagination (page & size)") Pageable pageable) {

        return super.readAll(pageable.getPageNumber(), pageable.getPageSize());
    }

    /**
     * GET /operations/non-budgetiser : Retourne la liste des opérations non budgetisées
     *
     * @param pageable les informations de pagination
     * @return le ResponseEntity avec le status 200 (OK) et la liste des opérations
     */
    @GetMapping(UrlConstants.Operation.OPERATION_NON_BUDGETISER)
    @Secured(PermissionsConstants.OPERATION_PERMISSION)
    @ApiOperation(value = "Retourner la liste des opérations",
            response = Operation.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")}
    )
    public ResponseEntity<Response> recupererLaListeDesOperationsNonBudgetiser(
            @ApiParam(value = "Information de pagination (page & size)") Pageable pageable) {

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(operationRepository.findByBudgetIsNullOrderByIdentifiantDesc(pageable))
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * GET /operations/{operationId}/operation-details : Retourne la liste des détails opérations
     *
     * @return le ResponseEntity avec le status 200 (OK) et la liste des opérations
     */
    @GetMapping(UrlConstants.Operation.OPERATION_DETAIL)
    @Secured(PermissionsConstants.OPERATION_PERMISSION)
    @ApiOperation(value = "Retourner la liste des détails opérations",
            response = Operation.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")}
    )
    public ResponseEntity<Response> recupererLaListeDesDetailsDOperations(
            @PathVariable Integer operationId) {

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(operationDetailRepository.findByOperationIdentifiant(operationId))
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * GET /operations/{operationId}/operation-details/reste : Retourne la liste des détails opérations d'une prévision et le montant restant
     *
     * @return le ResponseEntity avec le status 200 (OK) et la liste des opérations
     */
    @GetMapping(UrlConstants.Operation.OPERATION_DETAIL_AVEC_RESTE)
    @Secured(PermissionsConstants.OPERATION_PERMISSION)
    @ApiOperation(value = "Retourne la liste des détails opérations d'une prévision et le montant restant",
            response = Operation.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")}
    )
    public ResponseEntity<Response> recupererOperationAvecMontantRestant(
            @PathVariable Integer operationId) {

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(operationDetailService.recupererOperationAvecMontantRestant(operationId))
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * PUT /operations : Modifier une opération.
     *
     * @param operationVM L'opération à modifier
     * @return le ResponseEntity avec le status 200 (OK) et l'opération modifiée ,ou
     * le status 500 (Internal Server Error) si l'opération ne peut pas être
     * modifiée
     */
    @PutMapping(UrlConstants.Operation.OPERATION_RACINE)
    @Secured(PermissionsConstants.OPERATION_PERMISSION)
    @ApiOperation(value = "Modifier une  opération")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Profil.class)
            ,
            @ApiResponse(code = 500, message = "Si l'opération ne peut pas être modifiée", response = Response.class)})
    public ResponseEntity<Response> modifier(
            @ApiParam(value = "L'opération à modifier", required = true)
            @Valid @RequestBody OperationVM operationVM)
            throws CustomException {

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(((IOperationService) service).modifier(operationVM))
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * DELETE /operations/:operationId : Supprime l'opération à partir de son identifiant.
     *
     * @param operationId L'identifiant de l'opération à supprimer
     * @return Le ResponseEntity avec le status 200 (OK)
     */
    @DeleteMapping(UrlConstants.Operation.OPERATION_ID)
    @Secured(PermissionsConstants.OPERATION_PERMISSION)
    @ApiOperation(value = "Supprimer l'opération à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> supprimerUneOperation(
            @ApiParam(value = "L'identifiant de l'opération", required = true) @PathVariable Integer operationId)
            throws CustomException {

        return new ResponseEntity<>(ResponseBuilder.success()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(((IOperationService) service).supprimer(operationId))
                .buildI18n(), HttpStatus.OK);
    }

    /**
     * GET /operations/search
     * <p>
     * Rechercher des opérations
     *
     * @param operationFormulaireDeFiltre : L'information de filtre
     * @return ResponseEntity
     */
    @Secured(PermissionsConstants.OPERATION_PERMISSION)
    @RequestMapping(value = UrlConstants.Operation.OPERATION_RECHERCHE, method = RequestMethod.POST)
    @ApiOperation(value = "Rechercher des opérations",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<Response> rechercher(
            @ApiParam(value = "L'information de filtre")
            @RequestBody OperationFormulaireDeFiltre operationFormulaireDeFiltre) {

        Pageable pageable = null;
        Page<Operation> operationFind;

        if (operationFormulaireDeFiltre.getPage() != null && operationFormulaireDeFiltre.getSize() != null) {
            pageable = new PageRequest(operationFormulaireDeFiltre.getPage(),
                    operationFormulaireDeFiltre.getSize(), Sort.Direction.DESC, "identifiant");
        }

        if (operationFormulaireDeFiltre.getOperation() == null) {
            operationFind = new PageImpl<>(Collections.emptyList(), new PageRequest(1, 5), 0);
        } else {
            operationFind = service.findAllByCriteres(operationFormulaireDeFiltre.getCriteres(), pageable);
        }

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(operationFind)
                .buildI18n(), HttpStatus.OK);
    }

    @Autowired
    public void setOperationDetailRepository(OperationDetailRepository operationDetailRepository) {
        this.operationDetailRepository = operationDetailRepository;
    }

    @Autowired
    public void setOperationRepository(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @Autowired
    public void setOperationDetailService(IOperationDetailService operationDetailService) {
        this.operationDetailService = operationDetailService;
    }
}
