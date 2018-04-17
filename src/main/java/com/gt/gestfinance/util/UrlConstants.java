package com.gt.gestfinance.util;

/**
 * Classe des constantes contenant les URL
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 * @version 1.0
 * @since 18/09/2017
 */
public class UrlConstants {

    public static final String SLASH = "/";

    public static class Profil {
        public static final String PROFIL_RACINE = "profils";
        public static final String PROFIL_ID = "profils/{profilId}";
        public static final String PROFIL_NOM = "profils/nom/{nom}";
        public static final String PROFIL_ID_STATE = "profils/{profilId}/states";
        public static final String PROFIL_RECHERCHE = "profils/search";
        public static final String PROFIL_PERMISSION = "profils/{nom}/permissions";
        public static final String DOMAIN_URL = "domains";

        private Profil() {
        }
    }

    public static class Utilisateur {
        public static final String UTILISATEUR_RACINE = "users";
        public static final String UTILISATEUR_IMPORTATION = "users/import";
        public static final String UTILISATEUR_LOGIN = "users/{login:" + Constants.LOGIN_REGEX + "}";
        public static final String UTILISATEUR_ID_STATE = "users/{userId}/states";
        public static final String UTILISATEUR_RECHERCHE = "users/search";
        public static final String UTILISATEUR_PERMISSION = "users/permissions";

        private Utilisateur() {
        }
    }

    public static class Compte {
        public static final String COMPTE_RACINE = "comptes";
        public static final String COMPTE_ID = "comptes/{compteId}";
        public static final String COMPTE_ID_STATE = "comptes/{compteId}/states";
        public static final String COMPTE_RECHERCHE = "comptes/search";
        public static final String COMPTE_PREV_REAL = "comptes/prevision-realisation/{budgetId}";
        public static final String COMPTE_GRD_LIVRE = "comptes/{compteId}/grand-livre";

        private Compte() {
        }
    }

    public static class Operation {
        public static final String OPERATION_RACINE = "operations";
        public static final String OPERATION_NON_BUDGETISER = "operations/non-budgetiser";
        public static final String OPERATION_DETAIL = "operations/{operationId}/operation-details";
        public static final String OPERATION_DETAIL_AVEC_RESTE = "operations/{operationId}/operation-details/reste";
        public static final String OPERATION_ID = "operations/{operationId}";
        public static final String OPERATION_RECHERCHE = "operations/search";

        private Operation() {
        }
    }

    public static class Budget {
        public static final String BUDGET_RACINE = "budgets";
        public static final String BUDGET_ID = "budgets/{budgetId}";
        public static final String BUDGET_RECHERCHE = "budgets/search";

        private Budget() {
        }
    }

    public static class Exploitation {
        public static final String EXPLOITATION_RACINE = "exploitations";
        public static final String EXPLOITATION_ID = "exploitations/{exploitationId}";
        public static final String EXPLOITATION_RECHERCHE = "exploitations/search";

        private Exploitation() {
        }
    }

    public static class Tresorerie {
        public static final String TRESORERIE_RACINE = "tresoreries";
        public static final String TRESORERIE_ID = "tresoreries/{tresorerieId}";
        public static final String TRESORERIE_RECHERCHE = "tresoreries/search";
        public static final String TRESORERIE_GRD_LIVRE = "tresoreries/{tresorerieId}/grand-livre";

        private Tresorerie() {
        }
    }

    private UrlConstants() {
    }
}
