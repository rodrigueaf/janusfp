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

                var baseViewUri = "app-tresorerie/views/ui/";
                var baseServiceUri = "app-tresorerie/scripts/services/";
                var baseControllerUri = "app-tresorerie/scripts/controllers/";
                var baseComptesServiceUri = "app-compte/scripts/services/";

                $stateProvider
                /*******************************************************************************************/
                // route parent des tresoreries
                    .state('ui.tresoreries', {
                        url: '/tresoreries',
                        abstract: true,
                        template: '<div ui-view></div>',
                        resolve: load(['ui.select', baseServiceUri + 'tresorerie.service.js',
                            baseComptesServiceUri + 'comptes.service.js',
                            baseControllerUri + 'tresorerie.controller.js'])
                    })

                    // route vers la modification ou l'ajout d'un tresorerie
                    .state('ui.tresoreries.edit', {
                        url: '/edit',
                        params: {
                            initData: null,
                            isDetails: false
                        },
                        controller: 'TresoreriesControllerEdit',
                        templateUrl: baseViewUri + 'tresorerie.edit.html',
                        ncyBreadcrumb: {
                            parent: 'ui.tresoreries.list',
                            label: '{{breadCrumbLabel}}'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_TRESORERIE',
                                redirectTo: 'access.403'
                            }
                        }
                    })

                    // route vers la liste des tresoreries
                    .state('ui.tresoreries.list', {
                        url: '/list',
                        templateUrl: baseViewUri + 'tresorerie.list.html',
                        controller: 'TresoreriesControllerList',
                        resolve: load(['ui.select']),
                        ncyBreadcrumb: {
                            parent: 'home',
                            label: 'Liste des tresoreries'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_TRESORERIE',
                                redirectTo: 'access.403'
                            }
                        }

                    })
                    // route vers la liste des comptes
                    .state('ui.tresoreries.grdlivre', {
                        url: '/grd-livre',
                        templateUrl: baseViewUri + 'tresoreriegrdlivre.list.html',
                        controller: 'TresorerieGrdLivreController',
                        resolve: load([
                            baseControllerUri + 'tresoreriegrdlivre.controller.js']),
                        ncyBreadcrumb: {
                            parent: 'home',
                            label: 'Rapport'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_TRESORERIE',
                                redirectTo: 'access.403'
                            }
                        }

                    })
                ;
            }
        ]
    );

