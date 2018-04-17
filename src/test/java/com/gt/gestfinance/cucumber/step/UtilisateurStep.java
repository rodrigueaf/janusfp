package com.gt.gestfinance.cucumber.step;

import com.gt.base.exception.CustomException;
import com.gt.base.util.State;
import com.gt.base.util.StateWrapper;
import com.gt.gestfinance.AbstractFonctionalControllerTest;
import com.gt.gestfinance.dto.ManagedUserVM;
import com.gt.gestfinance.entity.*;
import com.gt.gestfinance.filtreform.UtilisateurFormulaireDeFiltre;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration
public class UtilisateurStep extends AbstractFonctionalControllerTest {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String UPDATED_LOGIN = "jhipster";
    private static final String DEFAULT_PASS = "passjohndoe";
    private static final String UPDATED_PASS = "pass";
    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String UPDATED_EMAIL = "email@localhost";
    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String UPDATED_FIRSTNAME = "updateFirstName";
    private static final String DEFAULT_LASTNAME = "doe";
    private static final String UPDATED_LASTNAME = "updateLastName";
    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String UPDATED_IMAGEURL = "http://placehold.it/40x40";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

    private Utilisateur utilisateur;
    private ManagedUserVM managedUserVM;
    private CustomMockMvc.CustomResultActions perform;
    private Profil profil;

    @Before
    public void setup() {
        super.setUp();
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    public static Utilisateur getUtilisateur() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setLogin(DEFAULT_LOGIN);
        utilisateur.setActive(true);
        utilisateur.setEmail(DEFAULT_EMAIL);
        utilisateur.setPrenom(DEFAULT_FIRSTNAME);
        utilisateur.setNom(DEFAULT_LASTNAME);
        return utilisateur;
    }

    public static ManagedUserVM getManagedUserVM() {
        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.login = DEFAULT_LOGIN;
        managedUserVM.activated = true;
        managedUserVM.email = DEFAULT_EMAIL;
        managedUserVM.prenom = DEFAULT_FIRSTNAME;
        managedUserVM.nom = DEFAULT_LASTNAME;
        return managedUserVM;
    }

    @Etantdonné("^qu'aucun utilisateur n'était enregistré$")
    public void qu_aucun_utilisateur_n_était_enregistré() {
        utilisateurRepository.deleteAll();
    }

    @Etantdonné("^qu'on dispose d'un utilisateur valide à enregistrer$")
    public void qu_on_dispose_d_un_utilisateur_valide_à_enregistrer() {
        this.managedUserVM = UtilisateurStep.getManagedUserVM();
        this.managedUserVM.profil = profilRepository.save(ProfilStep.getProfil());
    }

    @Lorsqu("^on ajoute l'utilisateur$")
    public void on_ajoute_l_utilisateur() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(
                post(UrlConstants.SLASH + UrlConstants.Utilisateur.UTILISATEUR_RACINE)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(managedUserVM)));
    }

    @Alors("^(\\d+) utilisateur est créé$")
    public void utilisateur_est_créé(int arg1) {
        assertThat(utilisateurRepository.count()).isEqualTo(arg1);
    }

    @Alors("^l’utilisateur créé est celui soumis à l'ajout$")
    public void l_utilisateur_créé_est_celui_soumis_à_l_ajout() throws CustomException {
        this.perform
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.nom")
                        .value(UtilisateurStep.DEFAULT_LASTNAME));
    }

    @Etantdonné("^qu'un utilisateur était déjà enregistré$")
    public void qu_un_utilisateur_était_déjà_enregistré() {
        utilisateurRepository.deleteAll();
        profilRepository.deleteAll();
        this.utilisateur = UtilisateurStep.getUtilisateur();
        this.utilisateur.setProfil(profilRepository.save(ProfilStep.getProfil()));
        this.utilisateur = this.utilisateurRepository
                .save(this.utilisateur);
        assertThat(utilisateurRepository.findAll()).hasSize(1);
    }

    @Etantdonné("^qu'on dispose d'un utilisateur avec le même login à enregistrer$")
    public void qu_on_dispose_d_un_utilisateur_avec_le_même_login_à_enregistrer() {
        this.managedUserVM = UtilisateurStep.getManagedUserVM();
        this.managedUserVM.nom = DEFAULT_LASTNAME;
    }


    @Alors("^(\\d+) utilisateur demeure créé$")
    public void utilisateur_demeure_créé(int arg1) {
        assertThat(utilisateurRepository.count()).isEqualTo(arg1);
    }

    @Etantdonné("^qu'on dispose d'un utilisateur dont le login n'est pas renseigné$")
    public void qu_on_dispose_d_un_utilisateur_dont_le_login_n_est_pas_renseigné() {
        this.managedUserVM = UtilisateurStep.getManagedUserVM();
        this.managedUserVM.login = null;
    }

    @Etantdonné("^qu'on récupère cet utilisateur pour le modifier$")
    public void qu_on_récupère_cet_utilisateur_pour_le_modifier() {
        this.managedUserVM = UtilisateurStep.getManagedUserVM();
        this.managedUserVM.identifiant = this.utilisateur.getIdentifiant();
        this.managedUserVM.nom = UPDATED_LASTNAME;
        this.managedUserVM.prenom = UPDATED_FIRSTNAME;
        this.managedUserVM.profil = this.utilisateur.getProfil();
    }

    @Etantdonné("^qu'on récupère cet utilisateur pour le modifier en ne renseignant pas le nom$")
    public void qu_on_récupère_cet_utilisateur_pour_le_modifier_en_ne_renseignant_pas_le_nom() {
        this.managedUserVM = UtilisateurStep.getManagedUserVM();
        this.managedUserVM.identifiant = this.utilisateur.getIdentifiant();
        this.managedUserVM.nom = null;
    }

    @Lorsqu("^on modifie l'utilisateur$")
    public void on_modifie_l_utilisateur() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(
                put(UrlConstants.SLASH + UrlConstants.Utilisateur.UTILISATEUR_RACINE)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(managedUserVM)));
    }

    @Etantdonné("^qu'on récupère cet utilisateur pour le modifier en ne renseignant pas le login$")
    public void qu_on_récupère_cet_utilisateur_pour_le_modifier_en_ne_renseignant_pas_le_login() {
        this.managedUserVM = UtilisateurStep.getManagedUserVM();
        this.managedUserVM.identifiant = this.utilisateur.getIdentifiant();
        this.managedUserVM.login = null;
    }

    @Etantdonné("^que deux utilisateurs étaient déjà enregistrés$")
    public void que_deux_utilisateurs_étaient_déjà_enregistrés() {
        utilisateurRepository.deleteAll();
        this.utilisateur = UtilisateurStep.getUtilisateur();
        this.utilisateur.setProfil(profilRepository.save(ProfilStep.getProfil()));
        this.utilisateur = this.utilisateurRepository.save(this.utilisateur);
        Utilisateur utilisateur1 = UtilisateurStep.getUtilisateur();
        utilisateur1.setNom(UPDATED_LASTNAME);
        utilisateur1.setLogin(UPDATED_LOGIN);
        utilisateur1.setProfil(utilisateur.getProfil());
        this.utilisateurRepository.save(utilisateur1);
        assertThat(utilisateurRepository.findAll()).hasSize(2);
    }

    @Etantdonné("^qu'on récupère cet utilisateur pour modifier son login par une valeur qui existe déjà$")
    public void qu_on_récupère_cet_utilisateur_pour_modifier_son_login_par_une_valeur_qui_existe_déjà() {
        this.managedUserVM = UtilisateurStep.getManagedUserVM();
        this.managedUserVM.identifiant = this.utilisateur.getIdentifiant();
        this.managedUserVM.nom = UPDATED_LASTNAME;
        this.managedUserVM.prenom = UPDATED_FIRSTNAME;
        this.managedUserVM.login = UPDATED_LOGIN;
        this.managedUserVM.profil = this.utilisateur.getProfil();
    }

    @Lorsqu("^on récupère cet utilisateur$")
    public void on_récupère_cet_utilisateur() throws CustomException {
        this.perform = restSampleMockMvc
                .perform(get(UrlConstants.SLASH + UrlConstants.Utilisateur.UTILISATEUR_LOGIN,
                        this.utilisateur.getLogin()));
    }

    @Alors("^on obtient l'utilisateur déjà enregistré$")
    public void on_obtient_l_utilisateur_déjà_enregistré() throws CustomException {
        this.perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.identifiant")
                        .value(this.utilisateur.getIdentifiant()))
                .andExpect(jsonPath("$.nom")
                        .value(DEFAULT_LASTNAME));
    }

    @Lorsqu("^on récupère la liste des utilisateurs$")
    public void on_récupère_la_liste_des_utilisateurs() throws CustomException {
        this.perform = restSampleMockMvc
                .perform(get(String.format("%s?page=0&size=10",
                        UrlConstants.SLASH + UrlConstants.Utilisateur.UTILISATEUR_RACINE)));
    }

    @Alors("^on a une liste contenant l'utilisateur déjà enregistré$")
    public void on_a_une_liste_contenant_l_utilisateur_déjà_enregistré() throws CustomException {
        this.perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.content.[*].identifiant")
                        .value(this.utilisateur.getIdentifiant().intValue()))
                .andExpect(jsonPath("$.data.content.[*].nom")
                        .value(DEFAULT_LASTNAME));
    }

    @Lorsqu("^on supprime l'utilisateur$")
    public void on_supprime_l_utilisateur() throws CustomException {
        this.perform = restSampleMockMvc.perform(delete(UrlConstants.SLASH
                + UrlConstants.Utilisateur.UTILISATEUR_LOGIN, this.utilisateur.getLogin())
                .accept(TestUtil.APPLICATION_JSON_UTF8));
    }

    @Etantdonné("^que ce utilisateur a déjà effectué une opération$")
    public void que_ce_utilisateur_a_déjà_effectué_une_opération() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(
                this.utilisateur.getLogin(), this.utilisateur.getLogin()));
        SecurityContextHolder.setContext(securityContext);

        Operation operation = operationRepository.save(OperationStep.getOperation().operation);

        OperationDetail operationDetail = new OperationDetail();
        operationDetail.setCompte(compteRepository.save(CompteStep.getCompte()));
        operationDetail.setOperationSens(OperationSens.DEBIT);
        operationDetail.setMontant(10000D);
        operationDetail.setOperation(operation);
        operationDetailRepository.save(operationDetail);
    }

    @Lorsqu("^on recherche des utilisateurs en fonction d'un critère$")
    public void on_recherche_des_utilisateurs_en_fonction_d_un_critère() throws IOException, CustomException {
        Utilisateur utilisateur = getUtilisateur();
        UtilisateurFormulaireDeFiltre utilisateurFormulaireDeFiltre = new UtilisateurFormulaireDeFiltre();
        utilisateurFormulaireDeFiltre.setUtilisateur(utilisateur);
        utilisateurFormulaireDeFiltre.setPage(0);
        utilisateurFormulaireDeFiltre.setSize(10);
        String searchUrl = UrlConstants.SLASH + UrlConstants.Utilisateur.UTILISATEUR_RECHERCHE;
        this.perform = restSampleMockMvc.perform(post(searchUrl)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(utilisateurFormulaireDeFiltre)));
    }

    @Alors("^les utilisateurs récupérés doivent respecter le critère défini$")
    public void les_utilisateurs_récupérés_doivent_respecter_le_critère_défini() throws CustomException {
        this.perform
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.data.content.[*].identifiant")
                        .value(CoreMatchers.hasItem(this.utilisateur.getIdentifiant().intValue())))
                .andExpect(jsonPath("$.data.content.[*].nom")
                        .value(CoreMatchers.hasItem(DEFAULT_LASTNAME)));
    }

    @Lorsqu("^on active l'utilisateur$")
    public void on_active_l_utilisateur() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(put(UrlConstants.SLASH
                + UrlConstants.Utilisateur.UTILISATEUR_ID_STATE, utilisateur.getIdentifiant())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new StateWrapper(State.ENABLED))));
    }

    @Alors("^l’utilisateur récupéré doit être activé$")
    public void l_utilisateur_récupéré_doit_être_activé() {
        assertThat(utilisateurRepository.findOne(this.utilisateur.getIdentifiant()).getState())
                .isEqualTo(State.ENABLED);
    }

    @Lorsqu("^on désactive l'utilisateur$")
    public void on_désactive_l_utilisateur() throws IOException, CustomException {
        this.perform = restSampleMockMvc.perform(put(UrlConstants.SLASH
                + UrlConstants.Utilisateur.UTILISATEUR_ID_STATE, utilisateur.getIdentifiant())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new StateWrapper(State.DISABLED))));
    }

    @Alors("^l’utilisateur récupéré doit être désactivé$")
    public void l_utilisateur_récupéré_doit_être_désactivé() {
        assertThat(utilisateurRepository.findOne(this.utilisateur.getIdentifiant()).getState())
                .isEqualTo(State.DISABLED);
    }

    @Lorsqu("^on attribue un profil à l'utilisateur$")
    public void on_attribue_un_profil_à_l_utilisateur() throws IOException, CustomException {
        this.utilisateur.getProfil().getPermissions().add(permissionRepository.save(new Permission("test")));
        profilRepository.saveAndFlush(this.utilisateur.getProfil());
        this.managedUserVM = getManagedUserVM();
        this.managedUserVM.identifiant = this.utilisateur.getIdentifiant();
        this.managedUserVM.profil = this.utilisateur.getProfil();

        this.perform = restSampleMockMvc.perform(
                put(UrlConstants.SLASH + UrlConstants.Utilisateur.UTILISATEUR_RACINE)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(managedUserVM)));
    }

    @Alors("^l'utilisateur dispose des droits de ce profil$")
    public void l_utilisateur_dispose_des_droits_de_ce_profil() {
        assertThat(utilisateurRepository
                .findOneWithPermissionsByLogin(this.utilisateur.getLogin())
                .get().getPermissions()).hasSize(this.utilisateur.getProfil().getPermissions().size());
    }

    @Alors("^on obtient le message sur l'utilisateur \"([^\"]*)\"$")
    public void onObtientLeMessageSurLUtilisateur(String arg0) throws CustomException {
        this.perform.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(arg0));
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération d'ajout de l'utilisateur$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDAjoutDeLUtilisateur() throws CustomException {
        this.perform.andExpect(status().isCreated());
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération de modification de l'utilisateur$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDeModificationDeLUtilisateur() throws CustomException {
        this.perform.andExpect(status().isOk());
    }

    @Alors("^on obtient une réponse confirmant la réussite de l'opération de suppression de l'utilisateur$")
    public void onObtientUneRéponseConfirmantLaRéussiteDeLOpérationDeSuppressionDeLUtilisateur() throws CustomException {
        this.perform.andExpect(status().isOk());
    }
}
