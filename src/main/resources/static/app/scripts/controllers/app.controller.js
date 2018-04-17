/**
 * Controlleur global de l'application
 */
angular.module('app')
    .controller('AppController', ['$scope', '$translate', '$localStorage', '$window',
        '$document', '$rootScope', '$mdColorPalette',
        function ($scope, $translate, $localStorage, $window, $document,
                  $rootScope, $mdColorPalette) {

            // add 'ie' classes to html
            var isIE = !!navigator.userAgent.match(/MSIE/i) || !!navigator.userAgent.match(/Trident.*rv:11\./);
            isIE && angular.element($window.document.body).addClass('ie');
            isSmartDevice($window) && angular.element($window.document.body).addClass('smart');

            // config
            $scope.app = {
                name: 'Epaiement',
                version: '1.0',
                // for chart colors
                color: {
                    primary: '#3f51b5',
                    info: '#2196f3',
                    success: '#4caf50',
                    warning: '#ffc107',
                    danger: '#f44336',
                    accent: '#7e57c2',
                    white: '#ffffff',
                    light: '#f1f2f3',
                    dark: '#475069'
                },
                setting: {
                    theme: {
                        primary: 'indigo',
                        accent: 'green',
                        warn: 'amber'
                    }
                }
            };

            $scope.setTheme = function (theme) {
                $scope.app.setting.theme = theme;
            };

            // save settings to local storage
            if (angular.isDefined($localStorage.appSetting)) {
                $scope.app.setting = $localStorage.appSetting;
            } else {
                $localStorage.appSetting = $scope.app.setting;
            }
            $scope.$watch('app.setting', function () {
                $localStorage.appSetting = $scope.app.setting;
            }, true);

            // angular translate
            $scope.langs = {en: 'English', fr: 'Français'};
            $scope.selectLang = $scope.langs[$translate.proposedLanguage()] || "English";
            $scope.setLang = function (langKey) {
                // set the current lang
                $scope.selectLang = $scope.langs[langKey];
                // You can change the language during runtime
                $translate.use(langKey);
            };

            function isSmartDevice($window) {
                var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
                // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
                return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
            }

            $scope.getColor = function (color, hue) {
                if (color === "bg-dark" || color === "bg-white") {
                    return $scope.app.color[color.substr(3, color.length)];
                }
                return rgb2hex($mdColorPalette[color][hue]['value']);
            };

            //Function to convert hex format to a rgb color
            function rgb2hex(rgb) {
                return "#" + hex(rgb[0]) + hex(rgb[1]) + hex(rgb[2]);
            }

            function hex(x) {
                var hexDigits = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"];
                return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
            }

            $scope.goBack = function () {
                $window.history.back();
            }

        }
    ]);

/**
 * Controlleur global contenant les méthode de validation
 */
angular.module('app')
    .controller('AppController', ['$rootScope', '$stateParams', 'jwtHelper', '$cookies',
        function ($rootScope, $stateParams, jwtHelper, $cookies) {

            //retour de la condition des champs requis
            var required = function (data) {
                console.log(data);
                var type = typeof data;
                return (data === null || ( type !== 'object'
                    && !angular.isUndefined(data) && data !== null && data.toString().trim() === ''));
            };

            // message à internationnaliser
            var requiredMsg = "Ce champ est obligatoire";
            var minMsg = "Nombre de caractères minimum requis";
            var maxMsg = "Nombre de caractères maximum requis ";
            var patternMsg = "Ce champ comporte des caractères non supportés";

            /**
             * Méthode de validation des champs obligatoire
             *
             * @data la donnée à valider
             */
            $rootScope.validRequired = function (data) {
                if (required(data)) {
                    return requiredMsg;
                }
            };

            /**
             * Méthode de validation des champs sous un pattern donnée
             * @param data  la donnée à valider
             * @param motif  le motif du regex
             */
            $rootScope.validPattern = function (data, motif) {

                if (required(data)) {
                    return requiredMsg;
                }

                var reg = new RegExp(motif);

                if (!reg.test(data)) {
                    return patternMsg;
                }
            };

            /**
             * Méthode de validation des champs sur ca longueure min et max
             * @param min la taille minimum
             * @param max la taille maximum
             * @param data le motif du regex
             */
            $rootScope.validMinMax = function (data, min, max) {

                if (required(data)) {
                    return requiredMsg;
                }

                if (data.length < min) {
                    return minMsg + " " + min;
                } else if ((data.length > max) && (max !== -1)) {
                    return maxMsg + " " + max;
                }
            };

            /**
             * Méthode de validation des champs sous un pattern donnée et
             * sur la longueur min et max
             *
             * @param min la taille minimum
             * @param max la taille maximum (-1 pour longeur non fixe)
             * @param data la donnée à valider
             * @param motif le motif du regex
             */
            $rootScope.validMinMaxAndPattern = function (data, min, max, motif) {

                if (required(data)) {
                    return requiredMsg;
                }
                if (data.length < min) {
                    return minMsg + " " + min;
                }
                if ((data.length > max) && (max !== -1)) {
                    return maxMsg + " " + max;
                }

                var reg = new RegExp(motif);

                if (!reg.test(data)) {
                    return patternMsg;
                }
            };

            /**
             * comportements formulaire & réaction sur le breadcrumb
             */
            /*********************************************************************************/


            $rootScope.onState = function () {

                if ($stateParams.initData === null) {
                    $rootScope.breadCrumbLabel = 'Détails';
                } else {
                    $rootScope.breadCrumbLabel = 'Modification';
                    if ($stateParams.isDetails) {
                        $rootScope.breadCrumbLabel = 'Détails';
                    }
                }
            };

            $rootScope.openForm = function (form) {
                form.$show();
                $rootScope.breadCrumbLabel = 'Modification';
            };

            $rootScope.closeForm = function (form) {
                if ($stateParams.initData !== null) {
                    form.$cancel();
                    $rootScope.breadCrumbLabel = 'Details';
                }
            };

            $rootScope.getUserName = function () {
                return jwtHelper.decodeToken($cookies.get('token')).user_name;
            };

            $rootScope.dateSysteme = function () {
                return new Date();
            }

        }
    ]);