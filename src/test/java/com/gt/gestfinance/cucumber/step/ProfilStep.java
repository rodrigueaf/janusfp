package com.gt.gestfinance.cucumber.step;

import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.util.State;
import com.gt.gestfinance.util.StateWrapper;
import com.gt.gestfinance.AbstractFonctionalControllerTest;
import com.gt.gestfinance.dto.ManagedProfilVM;
import com.gt.gestfinance.entity.Profil;
import com.gt.gestfinance.entity.Utilisateur;
import com.gt.gestfinance.filtreform.ProfilFormulaireDeFiltre;
import com.gt.gestfinance.util.CustomMockMvc;
import com.gt.gestfinance.util.TestUtil;
import com.gt.gestfinance.util.UrlConstants;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.fr.Alors;
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
public class ProfilStep extends AbstractFonctionalControllerTest {

    private static final String DEFAULT_NOM = "nom";
    private static final String UPDATE_NOM = "nomUpdate";
    private CustomMockMvc.CustomResultActions perform;
    private Profil profil;
    private ManagedProfilVM managedProfilVM;

    public static Profil getProfil() {
        Profil profil = new Profil();
        profil.setNom(DEFAULT_NOM);
        return profil;
    }

    public static ManagedProfilVM getManagedProfilVM() {
        ManagedProfilVM managedProfilVM = new ManagedProfilVM();
        managedProfilVM.nom = DEFAULT_NOM;
        return managedProfilVM;
    }

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Etantdonné("^qu'aucun profil n'était enregistré$")
    public void qu_aucun_profil_n_était_enregistré() {
        utilisateurRepository.deleteAll();
        profilRepository.deleteAll();
    }

    @Etantdonné("^qu'on dispose d'un profil valide à enregistrer$")
    public void qu_on_dispose_d_un_profil_valide_à_enregistrer() {
        this.managedProfilVM = ProfilStep.getManagedProfilVM();
    }

    @Lorsqu("^on ajoute le profil$")
    public void on_ajoute_le_profil() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(
                post(UrlConstants.SLASH + UrlConstants.Profil.PROFIL_RACINE)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(managedProfilVM)));
    }

    @Alors("^(\\d+) profil est créé$")
    public void profil_est_créé(int arg1) {
        assertThat(profilRepository.count()).isEqualTo(arg1);
    }

    @Alors("^le profil créé est celui soumis à l'ajout$")
    public void le_profil_créé_est_celui_soumis_à_l_ajout() throws CustomException {
        this.perform
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.nom")
                        .value(ProfilStep.DEFAULT_NOM));
    }

    @Etantdonné("^qu'un profil était déjà enregistré$")
    public void qu_un_profil_était_déjà_enregistré() {
        utilisateurRepository.deleteAll();
        profilRepository.deleteAll();
        this.profil = this.profilRepository
                .save(ProfilStep.getProfil());
        assertThat(profilRepository.findAll()).hasSize(1);
    }

    @Etantdonné("^qu'on dispose d'un profil avec le même nom à enregistrer$")
    public void qu_on_dispose_d_un_profil_avec_le_même_nom_à_enregistrer() {
        this.managedProfilVM = ProfilStep.getManagedProfilVM();
        this.managedProfilVM.nom = DEFAULT_NOM;
    }

    @Alors("^(\\d+) profil demeure créé$")
    public void profil_demeure_créé(int arg1) {
        assertThat(profilRepository.count()).isEqualTo(arg1);
    }

    @Etantdonné("^qu'on dispose d'un profil dont le nom n'est pas renseigné$")
    public void qu_on_dispose_d_un_profil_dont_le_nom_n_est_pas_renseigné() {
        this.managedProfilVM = ProfilStep.getManagedProfilVM();
        this.managedProfilVM.nom = null;
    }

    @Etantdonné("^qu'on récupère ce profil pour le modifier$")
    public void qu_on_récupère_ce_profil_pour_le_modifier() {
        this.managedProfilVM = ProfilStep.getManagedProfilVM();
        this.managedProfilVM.identifiant = this.profil.getIdentifiant();
        this.managedProfilVM.nom = UPDATE_NOM;
    }

    @Lorsqu("^on modifie le profil$")
    public void on_modifie_le_profil() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(
                put(UrlConstants.SLASH + UrlConstants.Profil.PROFIL_RACINE)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(managedProfilVM)));
    }

    @Etantdonné("^qu'on récupère ce profil pour le modifier en ne renseignant pas le nom$")
    public void qu_on_récupère_ce_profil_pour_le_modifier_en_ne_renseignant_pas_le_nom() {
        this.managedProfilVM = ProfilStep.getManagedProfilVM();
        this.managedProfilVM.identifiant = this.profil.getIdentifiant();
        this.managedProfilVM.nom = null;
    }

    @Etantdonné("^que deux profils étaient déjà enregistrés$")
    public void que_deux_profils_étaient_déjà_enregistrés() {
        utilisateurRepository.deleteAll();
        profilRepository.deleteAll();
        this.profil = this.profilRepository
                .save(ProfilStep.getProfil());
        Profil profil1 = ProfilStep.getProfil();
        profil1.setNom(UPDATE_NOM);
        this.profilRepository.save(profil1);
        assertThat(profilRepository.findAll()).hasSize(2);
    }

    @Etantdonné("^qu'on récupère ce profil pour modifier son nom par une valeur qui existe déjà$")
    public void qu_on_récupère_ce_profil_pour_modifier_son_nom_par_une_valeur_qui_existe_déjà() {
        this.managedProfilVM = ProfilStep.getManagedProfilVM();
        this.managedProfilVM.identifiant = this.profil.getIdentifiant();
        this.managedProfilVM.nom = UPDATE_NOM;
    }

    @Lorsqu("^on récupère ce profil$")
    public void on_récupère_ce_profil() throws CustomException {
        this.perform = restSampleMockMvc
                .perform(get(UrlConstants.SLASH + UrlConstants.Profil.PROFIL_ID,
                        this.profil.getIdentifiant()));
    }

    @Alors("^on obtient le profil déjà enregistré$")
    public void on_obtient_le_profil_déjà_enregistré() throws CustomException {
        this.perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.identifiant")
                        .value(this.profil.getIdentifiant()))
                .andExpect(jsonPath("$.data.nom")
                        .value(ProfilStep.DEFAULT_NOM));
    }

    @Lorsqu("^on récupère la liste des profils$")
    public void on_récupère_la_liste_des_profils() throws CustomException {
        this.perform = restSampleMockMvc
                .perform(get(String.format("%s?page=0&size=10",
                        UrlConstants.SLASH + UrlConstants.Profil.PROFIL_RACINE)));
    }

    @Alors("^on a une liste contenant le profil déjà enregistré$")
    public void on_a_une_liste_contenant_le_profil_déjà_enregistré() throws CustomException {
        this.perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.content.[*].identifiant")
                        .value(this.profil.getIdentifiant()))
                .andExpect(jsonPath("$.data.content.[*].nom")
                        .value(ProfilStep.DEFAULT_NOM));
    }

    @Lorsqu("^on supprime le profil$")
    public void on_supprime_le_profil() throws CustomException {
        this.perform = restSampleMockMvc.perform(delete(UrlConstants.SLASH
                + UrlConstants.Profil.PROFIL_ID, this.profil.getIdentifiant())
                .accept(TestUtil.APPLICATION_JSON_UTF8));
    }

    @Etantdonné("^que ce profil est déjà attribué à un utilisateur$")
    public void que_ce_profil_est_déjà_attribué_à_un_utilisateur() {
        Utilisateur utilisateur = UtilisateurStep.getUtilisateur();
        utilisateur.setProfil(this.profil);
        utilisateurRepository.save(utilisateur);
    }

    @Lorsqu("^on recherche des profils en fonction d'un critère$")
    public void on_recherche_des_profils_en_fonction_d_un_critère() throws IOException, CustomException {
        Profil profil = getProfil();
        ProfilFormulaireDeFiltre profilFormulaireDeFiltre = new ProfilFormulaireDeFiltre();
        profilFormulaireDeFiltre.setProfil(profil);
        profilFormulaireDeFiltre.setPage(0);
        profilFormulaireDeFiltre.setSize(10);
        String searchUrl = UrlConstants.SLASH + UrlConstants.Profil.PROFIL_RECHERCHE;
        this.perform = restSampleMockMvc.perform(post(searchUrl)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(profilFormulaireDeFiltre)));
    }

    @Alors("^les profils récupérés doivent respecter le critère défini$")
    public void les_profils_récupérés_doivent_respecter_le_critère_défini() throws CustomException {
        this.perform
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.content.[*].identifiant")
                        .value(CoreMatchers.hasItem(this.profil.getIdentifiant())))
                .andExpect(jsonPath("$.data.content.[*].nom")
                        .value(CoreMatchers.hasItem(ProfilStep.DEFAULT_NOM)));
    }

    @Lorsqu("^on active le profil$")
    public void on_active_le_profil() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(put(UrlConstants.SLASH
                + UrlConstants.Profil.PROFIL_ID_STATE, profil.getIdentifiant())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new StateWrapper(State.ENABLED))));
    }

    @Alors("^le profil récupéré doit être activé$")
    public void le_profil_récupéré_doit_être_activé() {
        assertThat(profilRepository.findOne(this.profil.getIdentifiant()).getState())
                .isEqualTo(State.ENABLED);
    }

    @Lorsqu("^on désactive le profil$")
    public void on_désactive_le_profil() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(put(UrlConstants.SLASH
                + UrlConstants.Profil.PROFIL_ID_STATE, profil.getIdentifiant())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new StateWrapper(State.DISABLED))));
    }

    @Alors("^le profil récupéré doit être désactivé$")
    public void le_profil_récupéré_doit_être_désactivé() {
        assertThat(profilRepository.findOne(this.profil.getIdentifiant()).getState())
                .isEqualTo(State.DISABLED);
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération d'ajout du profil$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDAjoutDuProfil() throws CustomException {
        this.perform.andExpect(status().isCreated());
    }

    @Alors("^on obtient le message sur le profil \"([^\"]*)\"$")
    public void onObtientLeMessageSurLeProfil(String arg0) throws CustomException {
        this.perform.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(arg0));
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération de modification du profil$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDeModificationDuProfil() throws CustomException {
        this.perform.andExpect(status().isOk());
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération de suppression du profil$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDeSuppressionDuProfil() throws CustomException {
        this.perform.andExpect(status().isOk());
    }
}
