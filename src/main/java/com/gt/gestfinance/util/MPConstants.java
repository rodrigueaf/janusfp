package com.gt.gestfinance.util;

/**
 * Classe des constantes
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 18/09/2017
 */
public class MPConstants {

    /**
     * ***********  Profil ****************
     */

    public static final String LE_PROFIL_DOIT_PAS_AVOIR_ID = "error.profil.no.id";
    public static final String LE_NOM_DU_PROFIL_EXSTE_DEJA = "error.code.profil.nom.exist";
    public static final String LE_NOM_DU_PROFIL_OBLIGATOIRE = "error.code.profil.nom.required";
    public static final String PROFIL_INTROUVABLE = "error.profil.not.found";
    public static final String PROFIL_DEJA_ATTRIBUER = "error.profil.deja.attribuer";

    /**
     * ***********  Utilisateur ****************
     */

    public static final String UTILISATEUR_DOIT_PAS_AVOIR_ID = "msg.error.utilisateur.no.id";
    public static final String UTILISATEUR_INTROUVABLE = "error.user.not.found";
    public static final String LE_LOGIN_EXISTE_DEJA = "error.user.login.exist";
    public static final String LE_LOGIN_EST_OBLIGATOIRE = "error.user.login.required";
    public static final String LE_NOM_EST_OBLIGATOIRE = "error.user.nom.required";
    public static final String LE_UTILISATEUR_EST_DEJA_UTILISER = "error.user.deja.use";

    /**
     * ***********  Compte ****************
     */
    public static final String LE_LIBELLE_EST_OBLIGATOIRE = "error.comtpe.libelle.required";
    public static final String LE_LIBELLE_EXISTE_DEJA = "error.comtpe.libelle.existe.deja";
    public static final String LE_COMPTE_PARENT_EST_DEJA_UTILISABLR = "error.comtpe.parent.deja.utilisable";
    public static final String LE_COMPTE_EST_DEJA_PARENT = "error.comtpe.deja.parent";
    public static final String LE_COMPTE_A_DEJA_SOUS_COMPTE = "error.comtpe.deja.sous.compte";
    public static final String LE_COMPTE_DEJA_UTILISER = "error.comtpe.deja.utiliser";

    /**
     * ********** Opération **************
     */
    public static final String LE_NOMBRE_COMPTE_RENSEIGNER_INCORRECT = "error.nbre.compte.renseigner.incorrect";
    public static final String LA_DESCRIPTION_EST_OBLIGATOIRE = "error.description.obligatoire";
    public static final String LE_MONTANT_EST_DIFFERENT = "error.operation.montant.incorrect";
    public static final String LE_MONTANT_EST_INSUFFISANT = "error.operation.montant.insuffisant";
    public static final String OPERATION_DEJA_UTILISER = "error.operation.deja.utiliser";

    /**
     * ********** Budget *************
     */
    public static final String LE_LIBELLE_DU_BUDGET_EXSTE_DEJA = "error.code.budget.libelle.exist";
    public static final String LE_LIBELLE_DU_BUDGET_EST_OBLIGATOIRE = "error.code.budget.libelle.obligatoire";

    /**
     * ********** Exploitation *************
     */
    public static final String LE_LIBELLE_DU_EXPLOITATION_EXSTE_DEJA = "error.code.exploitation.libelle.exist";
    public static final String LE_LIBELLE_DU_EXPLOITATION_EST_OBLIGATOIRE = "error.code.exploitation.libelle.obligatoire";

    private MPConstants() {
    }
}
