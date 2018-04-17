/**
 * @ngdoc function
 * @name app.config:uiRouter
 * @description
 *
 * Configuration des routes des pages du microservice security
 */
angular.module('app')
    .config(['$stateProvider',
            function ($stateProvider) {

                var baseViewUri = "app-compte/views/ui/";
                var baseServiceUri = "app-compte/scripts/services/";
                var baseControllerUri = "app-compte/scripts/controllers/";

                $stateProvider
                /*******************************************************************************************/
                // route parent des comptes
                    .state('ui.comptes', {
                        url: '/comptes',
                        abstract: true,
                        template: '<div ui-view></div>',
                        resolve: load(['ui.select', baseServiceUri + 'comptes.service.js',
                            baseControllerUri + 'comptes.controller.js'])
                    })

                    // route vers la modification ou l'ajout d'un compte
                    .state('ui.comptes.edit', {
                        url: '/edit',
                        params: {
                            initData: null,
                            isDetails: false
                        },
                        controller: 'ComptesControllerEdit',
                        templateUrl: baseViewUri + 'comptes.edit.html',
                        ncyBreadcrumb: {
                            parent: 'ui.comptes.list',
                            label: '{{breadCrumbLabel}}'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_COMPTE',
                                redirectTo: 'access.403'
                            }
                        }
                    })

                    // route vers la liste des comptes
                    .state('ui.comptes.list', {
                        url: '/list',
                        templateUrl: baseViewUri + 'comptes.list.html',
                        controller: 'ComptesControllerList',
                        resolve: load(['ui.select']),
                        ncyBreadcrumb: {
                            parent: 'home',
                            label: 'Liste des comptes'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_COMPTE',
                                redirectTo: 'access.403'
                            }
                        }

                    })

                    // route vers la liste des comptes
                    .state('ui.comptes.prevreal', {
                        url: '/prev-real',
                        templateUrl: baseViewUri + 'comptesprevrealisation.list.html',
                        controller: 'ComptePrevRealController',
                        resolve: load([
                            "app-budget/scripts/services/budget.service.js",
                            baseControllerUri + 'comptesprevrealisation.controller.js']),
                        ncyBreadcrumb: {
                            parent: 'home',
                            label: 'Rapport'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_COMPTE',
                                redirectTo: 'access.403'
                            }
                        }

                    })

                    // route vers la liste des comptes
                    .state('ui.comptes.grdlivre', {
                        url: '/grd-livre',
                        templateUrl: baseViewUri + 'comptesgrdlivre.list.html',
                        controller: 'CompteGrdLivreController',
                        resolve: load([
                            "app-compte/scripts/services/comptes.service.js",
                            baseControllerUri + 'comptesgrdlivre.controller.js']),
                        ncyBreadcrumb: {
                            parent: 'home',
                            label: 'Rapport'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_COMPTE',
                                redirectTo: 'access.403'
                            }
                        }

                    })
                ;
            }
        ]
    );

