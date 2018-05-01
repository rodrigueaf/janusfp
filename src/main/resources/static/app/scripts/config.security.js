/**
 * Created by Claude Rodrigue AFFODOGANDJI on 17/10/2017.
 */

/**
 * Défini le controller pour l'auhtentification
 *
 * @param $state
 * @param $scope
 * @param $cookies
 * @param $http
 * @param Authservice
 * @returns
 */
function authController($http, $state, $scope, $cookies, Authservice, PermPermissionStore, jwtHelper) {

    var reset = function () {

        $scope.user = {
            username: null,
            password: null
        };
    };

    $scope.chargement = false;
    $scope.msgError = "";

    // methode de connexion d'un utilisateur
    $scope.login = function () {

        var dataForPasswordGrantType = {
            username: $scope.user.username,
            password: $scope.user.password,
            grant_type: "password"
        };

        Authservice.returnData(dataForPasswordGrantType)
            .then(function (data) {
                var accessToken = data.data["access_token"];
                var refreshToken = data.data["refresh_token"];

                /*
                 * on stocke le jeton dans le cookie
                 */
                var decToken = jwtHelper.decodeToken(accessToken);
                $cookies.put('token', accessToken);
                $cookies.put('refresh_token', refreshToken);
                $cookies.put('is_gest', decToken.isGest);

                $state.go("home", {isFromLoginPage: true});

                PermPermissionStore.clearStore();

                // Stocker les la liste des permissions pour un utilisateur
                PermPermissionStore.defineManyPermissions(decToken.authorities,
                    function () {
                        return true;
                    });

                $scope.chargement = false;
                reset();
            }, function (error) {
                // traitement en cas d'erreur
                $scope.msgError = error.data.error_description;
                $scope.chargement = false;
                reset();
            });
    };

    /* Déconnexion */
    $scope.logout = function () {
        var headers = {
            'Content-Type': 'application/x-www-form-urlencoded',
            "Authorization": "Basic d2ViX2FwcDp3ZWJfYXBw",
            "token": $cookies.get("token") || $cookies.get("refresh_token")
        };
        $http({
            url: 'oauth/token',
            method: 'delete',
            headers: headers
        }).then(function () {
            $cookies.remove('token');
            $cookies.remove('refresh_token');
            PermPermissionStore.clearStore();
            //redirection sur la page de connexion
            $state.go("access.signin");
        });
    }
}

/**
 * Méthode de configuration pour l'authentificaton
 *
 * @param $httpProvider
 * @param jwtOptionsProvider
 * @returns
 */
function authConfig($httpProvider, jwtOptionsProvider) {

    /*
     * Configuration angular-jwt, La fonction tokenGetter permet de retourner le
     * jeton et de l'attacher en en-tête de chaque requête AJAX
     */
    jwtOptionsProvider.config({

        tokenGetter: function ($cookies) {

            /*
             * Implique que le jeton existe dans les cookies via la clé 'token'
             */
            return $cookies.get('token');
        },

        whiteListedDomains: [],

        /*
         * Le mécanisme qui s'enclanche lorsque le serveur renvoie une erreur 401
         * (unauthorize) à la fin une redirection s'effectue vers la page de
         * connexion
         */
        unauthenticatedRedirector: ['$state', '$cookies', 'Authservice',
            function ($state, $cookies, Authservice) {
                var token = $cookies.get('token');
                if (typeof token !== 'undefined') {
                    var dataForRefreshGrantType = {
                        grant_type: "refresh_token",
                        refresh_token: $cookies.get('refresh_token')
                    };

                    Authservice.returnData(dataForRefreshGrantType)
                        .then(function (data) {
                            var accessToken = data.data["access_token"];
                            var refreshToken = data.data["refresh_token"];
                            /*
                             * on stocke le jeton dans le cookie
                             */
                            $cookies.put('token', accessToken);
                            $cookies.put('refresh_token', refreshToken);
                        }, function () {
                            $state.go("access.signin");
                            $cookies.remove('token');
                        });
                } else {
                    $state.go("access.signin");
                }
            }]
    });

    $httpProvider.interceptors.push('jwtInterceptor');
}

/**
 *
 * @param authManager
 * @param $cookies
 * @param $rootScope
 * @param jwtHelper
 * @returns
 */
function authRun(authManager, $cookies, $rootScope, jwtHelper, PermPermissionStore) {

    /*
     * lorsque le serveur retourne un 401 (unauthorize),
     * redirectionner vers la page de login la configuration
     * vers la page de login est géré par
     * unauthenticatedRedirector
     */
    authManager.redirectWhenUnauthenticated();

    /*
     * Lors du chargement/rechargement d'une page on vérifie que le token existe
     * dans le cookies. si oui, on recupère les droits de l'utilisateur connecté du
     * token si non, on définie une permission ANONYMOUS par défaut pour la
     * connexion
     */
    var token = $cookies.get('token');

    if (typeof token !== 'undefined') {

        /*
         * la variable est définie dans une $scope afin d'attendre la
         * promesse de retour du token décoder par la fonction
         * decodeToken de jwtHelper
         */
        $rootScope.decodeToken = jwtHelper.decodeToken(token);

        /*
         * ressensé les permissions de l'utilisateur connecté les accès
         * se trouve dans la variable scopes du token
         */
        var permissions = $rootScope.decodeToken.authorities;

        // Stocker les la liste des permissions pour un utilisateur
        PermPermissionStore.defineManyPermissions(permissions,
            function () {
                return true;
            });

    } else {

        PermPermissionStore.definePermission("ANONYMOUS", function () {
            return true;
        });
    }
}

angular.module('app')
    .controller('AuthController', authController)
    .config(authConfig)
    .run(authRun);
