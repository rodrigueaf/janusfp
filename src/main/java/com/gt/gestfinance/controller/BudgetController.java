package com.gt.gestfinance.controller;

import com.gt.base.controller.BaseEntityController;
import com.gt.base.exception.CustomException;
import com.gt.base.util.DefaultMP;
import com.gt.base.util.Response;
import com.gt.base.util.ResponseBuilder;
import com.gt.gestfinance.entity.Budget;
import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.filtreform.BudgetFormulaireDeFiltre;
import com.gt.gestfinance.service.IBudgetService;
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
 * Contrôleur de gestion des Budget
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 23/06/2017
 */
@RestController
@Api(tags = {"budgets"})
public class BudgetController extends BaseEntityController<Budget, Integer> {

    public BudgetController(IBudgetService budgetService) {
        super(budgetService);
    }

    /**
     * POST /budgets : Créer un budget.
     *
     * @param budget Le budget à créer
     */
    @PostMapping(UrlConstants.Budget.BUDGET_RACINE)
    @Secured(PermissionsConstants.BUDGET_PERMISSION)
    @ApiOperation(value = "Créer un budget")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Budget enregistré avec succès")
            ,
            @ApiResponse(code = 500, message = "Si le contrainte d'intégrités ne sont pas respectées",
                    response = Response.class)})
    public ResponseEntity<Response> ajouter(@Valid @RequestBody Budget budget)
            throws CustomException {
        return super.create(budget);
    }

    /**
     * PUT /budgets : Modifier un budget.
     *
     * @param budget Le budget à modifier
     * @return le ResponseEntity avec le status 200 (OK) et le budget modifié ,
     * ou avec le status 500 (Bad Request) si le libellé du budget existe déjà, ou
     * le status 500 (Internal Server Error) si le budget ne peut pas être
     * modifié
     */
    @PutMapping(UrlConstants.Budget.BUDGET_RACINE)
    @Secured(PermissionsConstants.BUDGET_PERMISSION)
    @ApiOperation(value = "Modifier un budget")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Profil.class)
            ,
            @ApiResponse(code = 500, message = "Si le budget ne peut pas être modifié", response = Response.class)})
    public ResponseEntity<Response> modifier(
            @ApiParam(value = "Le budget à modifier", required = true)
            @Valid @RequestBody Budget budget) throws CustomException {

        return super.update(budget.getIdentifiant(), budget);
    }

    /**
     * GET /budgets/{budgetId} : Retourne le budget à partir de son identifiant.
     *
     * @param budgetId L'identifiant du budget
     * @return le ResponseEntity avec le status 200 (OK) et le budget, ou avec
     * le status 500
     */
    @GetMapping(UrlConstants.Budget.BUDGET_ID)
    @ApiOperation(value = "Retourner le budget à partir de son identifiant",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> recupererUnBudget(
            @ApiParam(value = "L'identifiant du budget", required = true)
            @PathVariable Integer budgetId) {

        return super.readOne(budgetId);
    }

    /**
     * GET /budgets : Retourne la liste des comptes
     *
     * @param pageable les informations de pagination
     * @return le ResponseEntity avec le status 200 (OK) et la liste des comptes
     */
    @GetMapping(UrlConstants.Budget.BUDGET_RACINE)
    @Secured(PermissionsConstants.BUDGET_PERMISSION)
    @ApiOperation(value = "Retourner la liste des budgets",
            response = Response.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")}
    )
    public ResponseEntity<Response> recupererLaListeDesBudgets(
            @ApiParam(value = "Information de pagination (page & size)") Pageable pageable) {

        return super.readAll(pageable.getPageNumber(), pageable.getPageSize());
    }

    /**
     * DELETE /budgets/{budgetId} : Supprime le budget à partir de son identifiant.
     *
     * @param budgetId L'identifiant du budget à supprimer
     * @return Le ResponseEntity avec le status 200 (OK)
     */
    @DeleteMapping(UrlConstants.Budget.BUDGET_ID)
    @Secured(PermissionsConstants.BUDGET_PERMISSION)
    @ApiOperation(value = "Supprimer le budget à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<Response> supprimerUnBudget(
            @ApiParam(value = "L'identififant du budget", required = true)
            @PathVariable Integer budgetId) throws CustomException {

        return super.delete(budgetId);
    }

    /**
     * GET /budgets/search
     * <p>
     * Rechercher des budgets
     *
     * @param budgetFormulaireDeFiltre : L'information de filtre
     * @return ResponseEntity
     */
    @Secured(PermissionsConstants.BUDGET_PERMISSION)
    @RequestMapping(value = UrlConstants.Budget.BUDGET_RECHERCHE, method = RequestMethod.POST)
    @ApiOperation(value = "Rechercher des budgets",
            response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseEntity.class)})
    public ResponseEntity<Response> rechercher(
            @ApiParam(value = "L'information de filtre")
            @RequestBody BudgetFormulaireDeFiltre budgetFormulaireDeFiltre) {

        Pageable pageable = null;
        Page<Budget> budgetsTrouver;

        if (budgetFormulaireDeFiltre.getPage() != null && budgetFormulaireDeFiltre.getSize() != null) {
            pageable = new PageRequest(budgetFormulaireDeFiltre.getPage(),
                    budgetFormulaireDeFiltre.getSize(), Sort.Direction.DESC, "identifiant");
        }

        if (budgetFormulaireDeFiltre.getBudget() == null) {
            budgetsTrouver = new PageImpl<>(Collections.emptyList(), new PageRequest(1, 5), 0);
        } else {
            budgetsTrouver = service.findAllByCriteres(budgetFormulaireDeFiltre.getCriteres(), pageable);
        }

        return new ResponseEntity<>(ResponseBuilder.info()
                .code(null)
                .title(DefaultMP.TITLE_SUCCESS)
                .message(DefaultMP.MESSAGE_SUCCESS)
                .data(budgetsTrouver)
                .buildI18n(), HttpStatus.OK);
    }
}
