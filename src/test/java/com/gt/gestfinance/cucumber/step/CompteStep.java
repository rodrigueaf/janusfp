package com.gt.gestfinance.cucumber.step;

import com.gt.base.exception.CustomException;
import com.gt.base.util.State;
import com.gt.base.util.StateWrapper;
import com.gt.gestfinance.AbstractFonctionalControllerTest;
import com.gt.gestfinance.entity.Compte;
import com.gt.gestfinance.entity.Operation;
import com.gt.gestfinance.entity.OperationDetail;
import com.gt.gestfinance.entity.OperationSens;
import com.gt.gestfinance.filtreform.CompteFormulaireDeFiltre;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration
public class CompteStep extends AbstractFonctionalControllerTest {

    private static final String DEFAULT_NUMERO_DE_COMPTE = "numeroDeCompte";
    private static final String DEFAULT_LIBELLE = "libelle";
    private static final String UPDATE_LIBELLE = "libelleUpdate";
    private CustomMockMvc.CustomResultActions perform;
    private Compte compteA;
    private Compte compteB;

    public static Compte getCompte() {
        Compte compte = new Compte();
        compte.setLibelle(DEFAULT_LIBELLE);
        return compte;
    }

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Etantdonné("^qu'aucun compte n'était enregistré$")
    public void qu_aucun_compte_n_était_enregistré() {
        compteRepository.deleteAll();
    }

    @Etantdonné("^qu'on dispose d'un compte valide à enregistrer$")
    public void qu_on_dispose_d_un_compte_valide_à_enregistrer() {
        this.compteA = CompteStep.getCompte();
    }

    @Lorsqu("^on ajoute le compte$")
    public void on_ajoute_le_compte() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(
                post(UrlConstants.SLASH + UrlConstants.Compte.COMPTE_RACINE)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(this.compteA)));
    }

    @Lorsqu("^on ajoute le nouveau compte$")
    public void onAjouteLeNouveauCompte() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(
                post(UrlConstants.SLASH + UrlConstants.Compte.COMPTE_RACINE)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(this.compteB)));
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération d'ajout du compte$")
    public void on_obtient_une_réponse_confirmant_la_réussite_de_l_opération_d_ajout() throws CustomException {
        this.perform.andExpect(status().isCreated());
    }

    @Alors("^(\\d+) compte est créé$")
    public void compte_est_créé(int arg1) {
        assertThat(compteRepository.count()).isEqualTo(arg1);
    }

    @Alors("^le compte créé est celui soumis à l'ajout$")
    public void le_compte_créé_est_celui_soumis_à_l_ajout() throws CustomException {
        this.perform
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.libelle")
                        .value(DEFAULT_LIBELLE));
    }

    @Etantdonné("^qu'un compte était déjà enregistré$")
    public void qu_un_compte_était_déjà_enregistré() {
        this.compteA = compteRepository.save(CompteStep.getCompte());
    }

    @Etantdonné("^qu'on dispose d'un compte avec le même libellé à enregistrer$")
    public void qu_on_dispose_d_un_compte_avec_le_même_libellé_à_enregistrer() {
        this.compteA = CompteStep.getCompte();
        this.compteA.setLibelle(DEFAULT_LIBELLE);
    }

    @Alors("^(\\d+) compte demeure créé$")
    public void compte_demeure_créé(int arg1) {
        assertThat(compteRepository.count()).isEqualTo(arg1);
    }

    @Etantdonné("^que le compte parent est utilisable$")
    public void que_le_compte_parent_est_utilisable() {
        this.compteA.setUtilisable(Boolean.TRUE);
    }

    @Et("^qu'on dispose d'un compte ayant ce compte pour compte parent$")
    public void quOnDisposeDUnCompteAyantCeComptePourCompteParent() {
        this.compteB = CompteStep.getCompte();
        this.compteB.setLibelle(UPDATE_LIBELLE);
        this.compteB.setCompteParent(compteA);
    }

    @Etantdonné("^qu'on dispose d'un compte dont le libellé n'est pas renseigné$")
    public void qu_on_dispose_d_un_compte_dont_le_libellé_n_est_pas_renseigné() {
        this.compteA = CompteStep.getCompte();
        this.compteA.setLibelle(null);
    }

    @Etantdonné("^qu'on récupère ce compte pour le modifier$")
    public void qu_on_récupère_ce_compte_pour_le_modifier() {
        this.compteA.setLibelle(UPDATE_LIBELLE);
    }

    @Lorsqu("^on modifie le compte$")
    public void on_modifie_le_compte() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(
                put(UrlConstants.SLASH + UrlConstants.Compte.COMPTE_RACINE)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(this.compteA)));
    }

    @Etantdonné("^qu'on récupère ce compte pour le modifier en ne renseignant pas le libellé$")
    public void qu_on_récupère_ce_compte_pour_le_modifier_en_ne_renseignant_pas_le_libellé() {
        this.compteA.setLibelle(null);
    }

    @Etantdonné("^que deux comptes étaient déjà enregistrés$")
    public void que_deux_comptes_étaient_déjà_enregistrés() {
        this.compteA = CompteStep.getCompte();
        this.compteA = compteRepository.save(this.compteA);
        this.compteB = CompteStep.getCompte();
        this.compteB.setLibelle(UPDATE_LIBELLE);
        this.compteB = compteRepository.save(this.compteB);
    }

    @Etantdonné("^qu'on récupère ce compte pour modifier son libellé par une valeur qui existe déjà$")
    public void qu_on_récupère_ce_compte_pour_modifier_son_libellé_par_une_valeur_qui_existe_déjà() {
        this.compteA.setLibelle(UPDATE_LIBELLE);
    }

    @Etantdonné("^que le compte A est le parent du compte B$")
    public void que_le_compte_A_est_le_parent_du_compte_B() {
        this.compteB.setCompteParent(this.compteA);
        compteRepository.saveAndFlush(this.compteB);
    }

    @Etantdonné("^qu'on récupère le compte A pour le rendre utilisable$")
    public void qu_on_récupère_le_compte_A_pour_le_rendre_utilisable() {
        this.compteA.setUtilisable(Boolean.TRUE);
    }

    @Lorsqu("^on récupère ce compte$")
    public void on_récupère_ce_compte() throws CustomException {
        this.perform = restSampleMockMvc
                .perform(get(UrlConstants.SLASH + UrlConstants.Compte.COMPTE_ID,
                        this.compteA.getIdentifiant()));
    }

    @Alors("^on obtient le compte déjà enregistré$")
    public void on_obtient_le_compte_déjà_enregistré() throws CustomException {
        this.perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.identifiant")
                        .value(this.compteA.getIdentifiant()))
                .andExpect(jsonPath("$.data.libelle")
                        .value(DEFAULT_LIBELLE));
    }

    @Lorsqu("^on récupère la liste des comptes$")
    public void on_récupère_la_liste_des_comptes() throws CustomException {
        this.perform = restSampleMockMvc
                .perform(get(String.format("%s?page=0&size=10",
                        UrlConstants.SLASH + UrlConstants.Compte.COMPTE_RACINE)));
    }

    @Alors("^on a une liste contenant le compte déjà enregistré$")
    public void on_a_une_liste_contenant_le_compte_déjà_enregistré() throws CustomException {
        this.perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.content.[*].identifiant")
                        .value(this.compteA.getIdentifiant()))
                .andExpect(jsonPath("$.data.content.[*].libelle")
                        .value(DEFAULT_LIBELLE));
    }

    @Lorsqu("^on supprime le compte$")
    public void on_supprime_le_compte() throws CustomException {
        this.perform = restSampleMockMvc.perform(delete(UrlConstants.SLASH
                + UrlConstants.Compte.COMPTE_ID, this.compteA.getIdentifiant())
                .accept(TestUtil.APPLICATION_JSON_UTF8));
    }


    @Etantdonné("^que ce compte est déjà utilisé dans une opération$")
    public void que_ce_compte_est_déjà_utilisé_dans_une_opération() {
        Operation operation = operationRepository.save(OperationStep.getOperation().operation);

        OperationDetail operationDetail = new OperationDetail();
        operationDetail.setCompte(this.compteA);
        operationDetail.setOperationSens(OperationSens.CREDIT);
        operationDetail.setMontant(10000D);
        operationDetail.setOperation(operation);
        operationDetailRepository.save(operationDetail);
    }

    @Lorsqu("^on recherche des comptes en fonction d'un critère$")
    public void on_recherche_des_comptes_en_fonction_d_un_critère() throws IOException, CustomException {
        Compte compte = getCompte();
        CompteFormulaireDeFiltre compteFormulaireDeFiltre = new CompteFormulaireDeFiltre();
        compteFormulaireDeFiltre.setCompte(compte);
        compteFormulaireDeFiltre.setPage(0);
        compteFormulaireDeFiltre.setSize(10);
        String searchUrl = UrlConstants.SLASH + UrlConstants.Compte.COMPTE_RECHERCHE;
        this.perform = restSampleMockMvc.perform(post(searchUrl)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(compteFormulaireDeFiltre)));
    }

    @Alors("^les comptes récupérés doivent respecter le critère défini$")
    public void les_comptes_récupérés_doivent_respecter_le_critère_défini() throws CustomException {
        this.perform
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.content.[*].identifiant")
                        .value(CoreMatchers.hasItem(this.compteA.getIdentifiant())))
                .andExpect(jsonPath("$.data.content.[*].libelle")
                        .value(CoreMatchers.hasItem(DEFAULT_LIBELLE)));
    }

    @Lorsqu("^on active le compte$")
    public void on_active_le_compte() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(put(UrlConstants.SLASH
                + UrlConstants.Compte.COMPTE_ID_STATE, compteA.getIdentifiant())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new StateWrapper(State.ENABLED))));
    }

    @Alors("^le compte récupéré doit être activé$")
    public void le_compte_récupéré_doit_être_activé() {
        assertThat(profilRepository.findOne(this.compteA.getIdentifiant()).getState())
                .isEqualTo(State.ENABLED);
    }

    @Lorsqu("^on désactive le compte$")
    public void on_désactive_le_compte() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(put(UrlConstants.SLASH
                + UrlConstants.Compte.COMPTE_ID_STATE, compteA.getIdentifiant())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new StateWrapper(State.DISABLED))));
    }

    @Alors("^le compte récupéré doit être désactivé$")
    public void le_compte_récupéré_doit_être_désactivé() {
        assertThat(compteRepository.findOne(this.compteA.getIdentifiant()).getState())
                .isEqualTo(State.DISABLED);
    }

    @Alors("^on obtient le message sur le compte \"([^\"]*)\"$")
    public void onObtientLeMessageSurLeCompte(String arg0) throws CustomException {
        this.perform.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(arg0));
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération de modification du compte$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDeModificationDuCompte() throws CustomException {
        this.perform.andExpect(status().isOk());
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération de suppression du compte$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDeSuppressionDuCompte() throws CustomException {
        this.perform.andExpect(status().isOk());
    }
}
