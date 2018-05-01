package com.gt.gestfinance.cucumber.step;

import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.AbstractFonctionalControllerTest;
import com.gt.gestfinance.dto.OperationVM;
import com.gt.gestfinance.entity.*;
import com.gt.gestfinance.filtreform.OperationFormulaireDeFiltre;
import com.gt.gestfinance.util.CustomMockMvc;
import com.gt.gestfinance.util.TestUtil;
import com.gt.gestfinance.util.UrlConstants;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.fr.Alors;
import cucumber.api.java.fr.Et;
import cucumber.api.java.fr.Etantdonné;
import cucumber.api.java.fr.Lorsqu;
import org.hamcrest.CoreMatchers;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration
public class OperationStep extends AbstractFonctionalControllerTest {

    private static final Double DEFAULT_MONTANT_TOTAL = 1000D;
    private static final Double DEFAULT_DETAIL_MONTANT_TOTAL = 5000D;
    private static final String DEFAULT_DESCRIPTION = "descritpion";
    private static final Double UPDATE_MONTANT_TOTAL = 2000D;
    private static final String UPDATE_DESCRIPTION = "libelleUpdate";
    private CustomMockMvc.CustomResultActions perform;
    private OperationVM operationVM;
    private Operation operation;

    public static OperationVM getOperation() {
        OperationVM operationVM = new OperationVM();
        Operation operation = new Operation();
        operation.setDescription(DEFAULT_DESCRIPTION);
        operation.setMontantTotal(DEFAULT_MONTANT_TOTAL);
        operationVM.operation = operation;
        return operationVM;
    }

    public static OperationDetail getOperationDetail() {
        OperationDetail operationDetail = new OperationDetail();
        operationDetail.setMontant(DEFAULT_DETAIL_MONTANT_TOTAL);
        operationDetail.setOperationSens(OperationSens.CREDIT);
        return operationDetail;
    }

    public OperationVM getDepenseOperation() {
        OperationVM vm = getOperation();
        vm.operation.setOperationType(OperationType.DEPENSE);
        OperationDetail operationDetail1 = getOperationDetail();
        Compte compte1 = CompteStep.getCompte();
        compte1.setCompteType(CompteType.TRESORERIE);
        operationDetail1.setCompte(compteRepository.save(compte1));
        operationDetail1.setOperationSens(OperationSens.CREDIT);

        OperationDetail operationDetail2 = getOperationDetail();
        Compte compte2 = CompteStep.getCompte();
        compte2.setLibelle(compte2.getLibelle() + "1");
        compte2.setCompteType(CompteType.TRESORERIE);
        operationDetail2.setCompte(compteRepository.save(compte2));
        operationDetail2.setOperationSens(OperationSens.CREDIT);

        OperationDetail operationDetail3 = getOperationDetail();
        Compte compte3 = CompteStep.getCompte();
        compte3.setLibelle(compte2.getLibelle() + "2");
        compte3.setCompteType(CompteType.SORTIE);
        operationDetail3.setCompte(compteRepository.save(compte3));
        operationDetail3.setOperationSens(OperationSens.DEBIT);

        vm.operationDetails = Arrays.asList(operationDetail1,
                operationDetail2, operationDetail3);
        return vm;
    }

    public OperationVM getProduitOperation() {
        OperationVM vm = getOperation();
        vm.operation.setOperationType(OperationType.PRODUIT);
        OperationDetail operationDetail1 = getOperationDetail();
        Compte compte1 = CompteStep.getCompte();
        compte1.setCompteType(CompteType.TRESORERIE);
        operationDetail1.setCompte(compteRepository.save(compte1));
        operationDetail1.setOperationSens(OperationSens.DEBIT);

        OperationDetail operationDetail2 = getOperationDetail();
        Compte compte2 = CompteStep.getCompte();
        compte2.setLibelle(compte2.getLibelle() + "1");
        compte2.setCompteType(CompteType.TRESORERIE);
        operationDetail2.setCompte(compteRepository.save(compte2));
        operationDetail2.setOperationSens(OperationSens.DEBIT);

        OperationDetail operationDetail3 = getOperationDetail();
        Compte compte3 = CompteStep.getCompte();
        compte3.setLibelle(compte2.getLibelle() + "2");
        compte3.setCompteType(CompteType.SORTIE);
        operationDetail3.setCompte(compteRepository.save(compte3));
        operationDetail3.setOperationSens(OperationSens.CREDIT);

        vm.operationDetails = Arrays.asList(operationDetail1,
                operationDetail2, operationDetail3);
        return vm;
    }

    public OperationVM getVirementOperation() {
        OperationVM vm = getOperation();
        vm.operation.setOperationType(OperationType.VIREMENT);
        OperationDetail operationDetail1 = getOperationDetail();
        Compte compte1 = CompteStep.getCompte();
        compte1.setCompteType(CompteType.TRESORERIE);
        operationDetail1.setCompte(compteRepository.save(compte1));
        operationDetail1.setOperationSens(OperationSens.DEBIT);

        OperationDetail operationDetail2 = getOperationDetail();
        Compte compte2 = CompteStep.getCompte();
        compte2.setLibelle(compte2.getLibelle() + "1");
        compte2.setCompteType(CompteType.TRESORERIE);
        operationDetail2.setCompte(compteRepository.save(compte2));
        operationDetail2.setOperationSens(OperationSens.DEBIT);

        OperationDetail operationDetail3 = getOperationDetail();
        Compte compte3 = CompteStep.getCompte();
        compte3.setLibelle(compte3.getLibelle() + "2");
        compte3.setCompteType(CompteType.SORTIE);
        operationDetail3.setCompte(compteRepository.save(compte3));
        operationDetail3.setOperationSens(OperationSens.CREDIT);

        OperationDetail operationDetail4 = getOperationDetail();
        Compte compte4 = CompteStep.getCompte();
        compte4.setLibelle(compte4.getLibelle() + "3");
        compte4.setCompteType(CompteType.SORTIE);
        operationDetail4.setCompte(compteRepository.save(compte4));
        operationDetail4.setOperationSens(OperationSens.CREDIT);

        vm.operationDetails = Arrays.asList(operationDetail1,
                operationDetail2, operationDetail3, operationDetail4);
        return vm;
    }

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Etantdonné("^qu'aucune opération n'était enregistrée$")
    public void quAucuneOpérationNÉtaitEnregistrée() {
        operationDetailRepository.deleteAll();
        operationRepository.deleteAll();
    }

    @Et("^qu'on dispose d'une opération de dépense valide à enregistrer$")
    public void quOnDisposeDUneOpérationDeDepenseValideÀEnregistrer() {
        this.operationVM = getDepenseOperation();
    }

    @Et("^qu'on dispose d'une opération de produit valide à enregistrer$")
    public void quOnDisposeDUneOpérationDeProduitValideÀEnregistrer() {
        this.operationVM = getProduitOperation();
    }

    @Et("^qu'on dispose d'une opération de virement valide à enregistrer$")
    public void quOnDisposeDUneOpérationDeVirementValideÀEnregistrer() {
        this.operationVM = getVirementOperation();
    }

    @Lorsqu("^on ajoute l'opération$")
    public void onAjouteLOpération() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(
                post(UrlConstants.SLASH + UrlConstants.Operation.OPERATION_RACINE)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(operationVM)));
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération d'ajout de l'opération$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDAjoutDeLOpération() throws CustomException {
        this.perform.andExpect(status().isCreated());
    }

    @Et("^(\\d+) opération est créée$")
    public void opérationEstCréée(int arg0) {
        assertThat(operationRepository.count()).isEqualTo(arg0);
    }

    @Et("^qu'on dispose d'une opération de dépense disposant d'un nombre incorrect de compte à enregistrer$")
    public void quOnDisposeDUneOpérationDeDepenseDisposantDUnNombreIncorrectDeCompteÀEnregistrer() {
        this.operationVM = getOperation();
        this.operationVM.operation.setOperationType(OperationType.DEPENSE);
        OperationDetail operationDetail1 = getOperationDetail();
        operationDetail1.setCompte(compteRepository.save(CompteStep.getCompte()));
        operationDetail1.setOperationSens(OperationSens.CREDIT);

        this.operationVM.operationDetails = Collections.singletonList(operationDetail1);
    }

    @Et("^qu'on dispose d'une opération de produit disposant d'un nombre incorrect de compte à enregistrer$")
    public void quOnDisposeDUneOpérationDeProduitDisposantDUnNombreIncorrectDeCompteÀEnregistrer() {
        this.operationVM = getOperation();
        this.operationVM.operation.setOperationType(OperationType.PRODUIT);
        OperationDetail operationDetail1 = getOperationDetail();
        operationDetail1.setCompte(compteRepository.save(CompteStep.getCompte()));
        operationDetail1.setOperationSens(OperationSens.CREDIT);

        this.operationVM.operationDetails = Collections.singletonList(operationDetail1);
    }

    @Et("^qu'on dispose d'une opération de virement disposant d'un nombre incorrect de compte à enregistrer$")
    public void quOnDisposeDUneOpérationDeVirmentDisposantDUnNombreIncorrectDeCompteÀEnregistrer() {
        this.operationVM = getOperation();
        this.operationVM.operation.setOperationType(OperationType.VIREMENT);
        OperationDetail operationDetail1 = getOperationDetail();
        operationDetail1.setCompte(compteRepository.save(CompteStep.getCompte()));
        operationDetail1.setOperationSens(OperationSens.CREDIT);

        this.operationVM.operationDetails = Collections.singletonList(operationDetail1);
    }

    @Alors("^on obtient le message sur l'opération \"([^\"]*)\"$")
    public void onObtientLeMessageSurLOpération(String arg0) throws CustomException {
        this.perform.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(arg0));
    }

    @Et("^qu'on dispose d'une opération dont la description n'est pas renseignée$")
    public void quOnDisposeDUneOpérationDontLaDescriptionNEstPasRenseignée() {
        this.operationVM = getOperation();
        this.operationVM.operation.setDescription(null);
    }

    @Etantdonné("^qu'une opération était déjà enregistrée$")
    public void quUneOpérationÉtaitDéjàEnregistrée() {
        this.operationVM = getDepenseOperation();
        this.operation = operationRepository.save(this.operationVM.operation);
        this.operationVM.operationDetails
                .forEach(od -> {
                    od.setOperation(operation);
                    operationDetailRepository.save(od);
                });
    }

    @Lorsqu("^on récupère cette opération$")
    public void onRécupèreCetteOpération() throws CustomException {
        this.perform = restSampleMockMvc
                .perform(get(UrlConstants.SLASH + UrlConstants.Operation.OPERATION_ID,
                        this.operation.getIdentifiant()));
    }

    @Alors("^on obtient l'opération déjà enregistrée$")
    public void onObtientLOpérationDéjàEnregistrée() throws CustomException {
        this.perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.identifiant")
                        .value(this.operation.getIdentifiant()))
                .andExpect(jsonPath("$.data.description")
                        .value(DEFAULT_DESCRIPTION));
    }

    @Lorsqu("^on récupère la liste des opérations$")
    public void onRécupèreLaListeDesOpérations() throws CustomException {
        this.perform = restSampleMockMvc
                .perform(get(String.format("%s?page=0&size=10",
                        UrlConstants.SLASH + UrlConstants.Operation.OPERATION_RACINE)));
    }

    @Alors("^on a une liste contenant l'opération déjà enregistrée$")
    public void onAUneListeContenantLOpérationDéjàEnregistrée() throws CustomException {
        this.perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.content.[*].identifiant")
                        .value(this.operation.getIdentifiant()))
                .andExpect(jsonPath("$.data.content.[*].description")
                        .value(DEFAULT_DESCRIPTION));
    }

    @Et("^qu'on récupère cette opération pour le modifier$")
    public void quOnRécupèreCetteOpérationPourLeModifier() {
        this.operationVM.operation = this.operation;
        this.operationVM.operation.setDescription(UPDATE_DESCRIPTION);
        this.operationVM.operation.setMontantTotal(UPDATE_MONTANT_TOTAL);
    }

    @Lorsqu("^on modifie l’opération$")
    public void onModifieLOpération() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(
                put(UrlConstants.SLASH + UrlConstants.Operation.OPERATION_RACINE)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(this.operationVM)));
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération de modification de l'opération$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDeModificationDeLOpération() throws CustomException {
        this.perform.andExpect(status().isOk());
    }

    @Lorsqu("^on supprime l'opération$")
    public void onSupprimeLOpération() throws CustomException {
        this.perform = restSampleMockMvc.perform(delete(UrlConstants.SLASH
                + UrlConstants.Operation.OPERATION_ID, this.operation.getIdentifiant())
                .accept(TestUtil.APPLICATION_JSON_UTF8));
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération de suppression de l'opération$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDeSuppressionDeLOpération() throws CustomException {
        this.perform.andExpect(status().isOk());
    }

    @Lorsqu("^on recherche des opérations en fonction d'un critère$")
    public void onRechercheDesOpérationsEnFonctionDUnCritère() throws IOException, CustomException {
        Operation operation = getOperation().operation;
        OperationFormulaireDeFiltre operationFormulaireDeFiltre = new OperationFormulaireDeFiltre();
        operationFormulaireDeFiltre.setOperation(operation);
        operationFormulaireDeFiltre.setPage(0);
        operationFormulaireDeFiltre.setSize(10);
        String searchUrl = UrlConstants.SLASH + UrlConstants.Operation.OPERATION_RECHERCHE;
        this.perform = restSampleMockMvc.perform(post(searchUrl)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(operationFormulaireDeFiltre)));
    }

    @Alors("^les opérations récupérées doivent respecter le critère défini$")
    public void lesOpérationsRécupéréesDoiventRespecterLeCritèreDéfini() throws CustomException {
        this.perform
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.content.[*].identifiant")
                        .value(CoreMatchers.hasItem(this.operation.getIdentifiant())))
                .andExpect(jsonPath("$.data.content.[*].description")
                        .value(CoreMatchers.hasItem(DEFAULT_DESCRIPTION)));
    }

    @Et("^(\\d+) détail d'opération au \"([^\"]*)\" sont créé$")
    public void détailDOpérationAuSontCréé(int arg0, String arg1) {
        List<OperationDetail> operationDetails = operationDetailRepository
                .findByOperationSens(OperationSens.valueOf(arg1));
        assertThat(operationDetails.size()).isEqualTo(arg0);
    }
}
