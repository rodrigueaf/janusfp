/**
 * Created by PC27 on 30/11/2017.
 */
/**
 * Controlleur de la page d'accueil
 */
angular.module('app')
    .controller('HomeController',
        ['$scope', '$state',
            function ($scope, $state) {

                $scope.compteClick = function () {
                    $state.go('ui.comptes.list');
                };

                $scope.budgetClick = function () {
                    $state.go('ui.budgets.list');
                };

                $scope.operationClick = function () {
                    $state.go('ui.operations.list');
                };

                $scope.utilisateurClick = function () {
                    $state.go('ui.utilisateurs.list');
                }
            }
        ]);
