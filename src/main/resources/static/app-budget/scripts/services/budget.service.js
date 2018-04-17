/**
 * Service de la gestion des budgets
 *
 * @param $resource
 * @returns
 */
angular.module('app')
    .factory('BudgetsService', ['$resource',
        function ($resource) {

            return $resource('budgets/:budgetId', {id: '@id'}, {

                update: {method: 'PUT'},

                findAll: {method: 'GET', isArray: false},

                search: {url: 'budgets/search', method: 'POST', isArray: false}
            });
        }]);