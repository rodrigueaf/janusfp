package com.gt.gestfinance.cucumber.step;

import com.gt.gestfinance.AbstractFonctionalControllerTest;
import com.gt.gestfinance.entity.Budget;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.filtreform.BudgetFormulaireDeFiltre;
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
public class BudgetStep extends AbstractFonctionalControllerTest {

    private static final Double DEFAULT_MONTANT_TOTAL = 1000D;
    private static final String DEFAULT_LIBELLE = "libelle";
    private static final Double UPDATE_MONTANT_TOTAL = 2000D;
    private static final String UPDATE_LIBELLE = "libelleUpdate";
    private CustomMockMvc.CustomResultActions perform;
    private Budget budget;

    public static Budget getBudget() {
        Budget budget = new Budget();
        budget.setLibelle(DEFAULT_LIBELLE);
        budget.setMontantTotal(DEFAULT_MONTANT_TOTAL);
        return budget;
    }

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Etantdonné("^qu'aucun budget n'était enregistré$")
    public void quAucunBudgetNÉtaitEnregistré() {
        budgetRepository.deleteAll();
    }

    @Et("^qu'on dispose d'un budget valide à enregistrer$")
    public void quOnDisposeDUnBudgetValideÀEnregistrer() {
        this.budget = getBudget();
    }

    @Lorsqu("^on ajoute le budget$")
    public void onAjouteLeBudget() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(
                post(UrlConstants.SLASH + UrlConstants.Budget.BUDGET_RACINE)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(budget)));
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération d'ajout du budget$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDAjoutDuBudget() throws CustomException {
        this.perform.andExpect(status().isCreated());
    }

    @Et("^(\\d+) budget est créé$")
    public void budgetEstCréé(int arg0) {
        assertThat(budgetRepository.count()).isEqualTo(arg0);
    }

    @Et("^le budget créé est celui soumis à l'ajout$")
    public void leBudgetCrééEstCeluiSoumisÀLAjout() throws CustomException {
        this.perform
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.libelle")
                        .value(DEFAULT_LIBELLE));
    }

    @Etantdonné("^qu'un budget était déjà enregistré$")
    public void quUnBudgetÉtaitDéjàEnregistré() {
        this.budget = this.budgetRepository.save(getBudget());
        assertThat(budgetRepository.findAll()).hasSize(1);
    }

    @Et("^qu'on dispose d'un budget avec le même libellé à enregistrer$")
    public void quOnDisposeDUnBudgetAvecLeMêmeLibelléÀEnregistrer() {
        this.budget = getBudget();
        this.budget.setLibelle(DEFAULT_LIBELLE);
    }

    @Alors("^on obtient le message sur le budget \"([^\"]*)\"$")
    public void onObtientLeMessageSurLeBudget(String arg0) throws CustomException {
        this.perform.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(arg0));
    }

    @Et("^(\\d+) budget demeure créé$")
    public void budgetDemeureCréé(int arg0) {
        assertThat(budgetRepository.count()).isEqualTo(arg0);
    }

    @Et("^qu'on dispose d'un budget dont le libellé n'est pas renseigné$")
    public void quOnDisposeDUnBudgetDontLeLibelléNEstPasRenseigné() {
        this.budget = getBudget();
        this.budget.setLibelle(null);
    }

    @Et("^qu'on récupère ce budget pour le modifier$")
    public void quOnRécupèreCeBudgetPourLeModifier() {
        this.budget.setLibelle(UPDATE_LIBELLE);
    }

    @Lorsqu("^on modifie le budget$")
    public void onModifieLeBudget() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(
                put(UrlConstants.SLASH + UrlConstants.Budget.BUDGET_RACINE)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(this.budget)));
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération de modification du budget$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDeModificationDuBudget() throws CustomException {
        this.perform.andExpect(status().isOk());
    }

    @Et("^qu'on récupère ce budget pour le modifier en ne renseignant pas le libellé$")
    public void quOnRécupèreCeBudgetPourLeModifierEnNeRenseignantPasLeLibellé() {
        this.budget.setLibelle(null);
    }

    @Etantdonné("^que deux budgets étaient déjà enregistrés$")
    public void queDeuxBudgetsÉtaientDéjàEnregistrés() {
        this.budget = budgetRepository.save(getBudget());
        Budget budget1 = getBudget();
        budget1.setLibelle(UPDATE_LIBELLE);
        budget1.setMontantTotal(UPDATE_MONTANT_TOTAL);
        budgetRepository.save(budget1);
    }

    @Et("^qu'on récupère ce budget pour modifier son libellé par une valeur qui existe déjà$")
    public void quOnRécupèreCeBudgetPourModifierSonLibelléParUneValeurQuiExisteDéjà() {
        this.budget.setLibelle(UPDATE_LIBELLE);
    }

    @Lorsqu("^on récupère ce budget$")
    public void onRécupèreCeBudget() throws CustomException {
        this.perform = restSampleMockMvc
                .perform(get(UrlConstants.SLASH + UrlConstants.Budget.BUDGET_ID,
                        this.budget.getIdentifiant()));
    }

    @Alors("^on obtient le budget déjà enregistré$")
    public void onObtientLeBudgetDéjàEnregistré() throws CustomException {
        this.perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.identifiant")
                        .value(this.budget.getIdentifiant()))
                .andExpect(jsonPath("$.data.libelle")
                        .value(DEFAULT_LIBELLE));
    }

    @Lorsqu("^on récupère la liste des budgets$")
    public void onRécupèreLaListeDesBudgets() throws CustomException {
        this.perform = restSampleMockMvc
                .perform(get(String.format("%s?page=0&size=10",
                        UrlConstants.SLASH + UrlConstants.Budget.BUDGET_RACINE)));
    }

    @Alors("^on a une liste contenant le budget déjà enregistré$")
    public void onAUneListeContenantLeBudgetDéjàEnregistré() throws CustomException {
        this.perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.content.[*].identifiant")
                        .value(this.budget.getIdentifiant()))
                .andExpect(jsonPath("$.data.content.[*].libelle")
                        .value(DEFAULT_LIBELLE));
    }

    @Lorsqu("^on supprime le budget$")
    public void onSupprimeLeBudget() throws CustomException {
        this.perform = restSampleMockMvc.perform(delete(UrlConstants.SLASH
                + UrlConstants.Budget.BUDGET_ID, this.budget.getIdentifiant())
                .accept(TestUtil.APPLICATION_JSON_UTF8));
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération de suppression du budget$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDeSuppressionDuBudget() throws CustomException {
        this.perform.andExpect(status().isOk());
    }

    @Lorsqu("^on recherche des budgets en fonction d'un critère$")
    public void onRechercheDesBudgetsEnFonctionDUnCritère() throws IOException, CustomException {
        Budget budget = getBudget();
        BudgetFormulaireDeFiltre budgetFormulaireDeFiltre = new BudgetFormulaireDeFiltre();
        budgetFormulaireDeFiltre.setBudget(budget);
        budgetFormulaireDeFiltre.setPage(0);
        budgetFormulaireDeFiltre.setSize(10);
        String searchUrl = UrlConstants.SLASH + UrlConstants.Budget.BUDGET_RECHERCHE;
        this.perform = restSampleMockMvc.perform(post(searchUrl)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budgetFormulaireDeFiltre)));
    }

    @Alors("^les budgets récupérés doivent respecter le critère défini$")
    public void lesBudgetsRécupérésDoiventRespecterLeCritèreDéfini() throws CustomException {
        this.perform
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.content.[*].identifiant")
                        .value(CoreMatchers.hasItem(this.budget.getIdentifiant())))
                .andExpect(jsonPath("$.data.content.[*].libelle")
                        .value(CoreMatchers.hasItem(DEFAULT_LIBELLE)));
    }
}
