/**
 * @ngdoc function
 * @name app.config:uiRouter
 * @description
 *
 * Configuration des routes des pages d' opération
 */
angular.module('app')
    .config(['$stateProvider',
            function ($stateProvider) {

                var baseViewUri = "app-operation/views/ui/";
                var baseServiceUri = "app-operation/scripts/services/";
                var baseComptesServiceUri = "app-compte/scripts/services/";
                var baseControllerUri = "app-operation/scripts/controllers/";
                var baseServiceTresorerie = "app-tresorerie/scripts/services/";
                var baseServiceBudget = "app-budget/scripts/services/";
                var baseServiceExploitation = "app-exploitation/scripts/services/";

                $stateProvider
                /*******************************************************************************************/
                // route parent des opérations
                    .state('ui.operations', {
                        url: '/operations',
                        abstract: true,
                        template: '<div ui-view></div>',
                        resolve: load(['ui.select', baseServiceUri + 'operation.service.js',
                            baseServiceTresorerie + 'tresorerie.service.js',
                            baseServiceExploitation + 'exploitation.service.js',
                            baseComptesServiceUri + 'comptes.service.js',
                            baseServiceBudget + 'budget.service.js',
                            baseControllerUri + 'operation.controller.js'])
                    })

                    // route vers la liste des operations
                    .state('ui.operations.list', {
                        url: '/list',
                        templateUrl: baseViewUri + 'operation.list.html',
                        controller: 'OperationsControllerList',
                        params: {
                            budget: null,
                            exploitation: null
                        },
                        ncyBreadcrumb: {
                            parent: 'home',
                            label: 'Liste des opérations'
                        }
                    })

                    // route vers la modification ou l'ajout d'une opération
                    .state('ui.operations.editmulti', {
                        url: '/edit-multi',
                        params: {
                            operation: null,
                            budget: null,
                            isMonoLigne: false,
                            exploitation: null,
                            filtreDataCompte: null,
                            filtreDataTresorerie: null
                        },
                        controller: 'OperationsControllerEdit',
                        templateUrl: baseViewUri + 'operation.edit.html',
                        ncyBreadcrumb: {
                            parent: 'ui.operations.list',
                            label: 'Edition d\'une opération'
                        }
                    })

                    // route vers la modification ou l'ajout d'une opération
                    .state('ui.operations.editmono', {
                        url: '/edit-mono',
                        params: {
                            operation: null,
                            budget: null,
                            isMonoLigne: true,
                            exploitation: null
                        },
                        controller: 'OperationsControllerEdit',
                        templateUrl: baseViewUri + 'operation.mono.edit.html',
                        ncyBreadcrumb: {
                            parent: 'ui.operations.list',
                            label: 'Edition d\'une opération'
                        }
                    })

                /*******************************************************************************************/
                ;
            }
        ]
    );

