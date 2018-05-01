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

                var baseViewUri = "app-budget/views/ui/";
                var baseServiceUri = "app-budget/scripts/services/";
                var baseControllerUri = "app-budget/scripts/controllers/";

                $stateProvider
                /*******************************************************************************************/
                // route parent des budgets
                    .state('ui.budgets', {
                        url: '/budgets',
                        abstract: true,
                        template: '<div ui-view></div>',
                        resolve: load(['ui.select', baseServiceUri + 'budget.service.js',
                            baseControllerUri + 'budget.controller.js'])
                    })

                    // route vers la modification ou l'ajout d'un budget
                    .state('ui.budgets.edit', {
                        url: '/edit',
                        params: {
                            initData: null,
                            isDetails: false
                        },
                        controller: 'BudgetsControllerEdit',
                        templateUrl: baseViewUri + 'budget.edit.html',
                        ncyBreadcrumb: {
                            parent: 'ui.budgets.list',
                            label: '{{breadCrumbLabel}}'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_BUDGET',
                                redirectTo: 'access.403'
                            }
                        }
                    })

                    // route vers la liste des budgets
                    .state('ui.budgets.list', {
                        url: '/list',
                        templateUrl: baseViewUri + 'budget.list.html',
                        controller: 'BudgetsControllerList',
                        resolve: load(['ui.select']),
                        ncyBreadcrumb: {
                            parent: 'home',
                            label: 'Liste des budgets'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_BUDGET',
                                redirectTo: 'access.403'
                            }
                        }

                    })
                ;
            }
        ]
    );

