package com.gt.gestfinance;

import com.gt.gestfinance.controller.*;
import com.gt.gestfinance.repository.*;
import com.gt.gestfinance.service.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Classe de base des classes de test pour les contrôleurs
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
public abstract class AbstractFonctionalControllerTest extends AbstractControllerTest {

    @Autowired
    protected IProfilService profilService;
    @Autowired
    protected IUtilisateurService utilisateurService;
    @Autowired
    protected ProfilRepository profilRepository;
    @Autowired
    protected PermissionRepository permissionRepository;
    @Autowired
    protected UtilisateurRepository utilisateurRepository;
    @Autowired
    protected ICompteService compteService;
    @Autowired
    protected CompteRepository compteRepository;
    @Autowired
    protected OperationRepository operationRepository;
    @Autowired
    protected OperationDetailRepository operationDetailRepository;
    @Autowired
    protected IOperationService operationService;
    @Autowired
    protected IOperationDetailService operationDetailService;
    @Autowired
    protected IBudgetService budgetService;
    @Autowired
    protected BudgetRepository budgetRepository;


    public void setUp() {
        ProfilController profilController = new ProfilController(profilService);
        profilController.setProfilRepository(profilRepository);
        profilController.setPermissionRepository(permissionRepository);

        UtilisateurController utilisateurController = new UtilisateurController(utilisateurService);
        utilisateurController.setUtilisateurRepository(utilisateurRepository);

        CompteController compteController = new CompteController(compteService);

        OperationController operationController = new OperationController(operationService);

        BudgetController budgetController = new BudgetController(budgetService);

        super.setup(profilController, utilisateurController, compteController,
                operationController, budgetController);
    }

    public void tearDown() {
        operationDetailRepository.deleteAll();
        operationRepository.deleteAll();
        budgetService.deleteAll();
        compteRepository.deleteAll();
        utilisateurRepository.deleteAll();
        utilisateurRepository.deleteAll();
        profilRepository.deleteAll();
        permissionRepository.deleteAll();
    }

}
