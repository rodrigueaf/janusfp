/**
 * Service de la gestion des opérations
 *
 * @param $resource
 * @returns
 */
angular.module('app')
    .factory('OperationsService', ['$resource',
        function ($resource) {

            return $resource('operations/:operationId', {id: '@operationId'}, {

                update: {method: 'PUT'},

                findAll: {method: 'GET', isArray: false},

                search: {url: 'operations/search', method: 'POST', isArray: false},

                recupererLaListeDesDetailsDOperations: {
                    url: 'operations/:operationId/operation-details', method: 'GET', isArray: false
                },

                recupererOperationAvecMontantRestant: {
                    url: 'operations/:operationId/operation-details/reste', method: 'GET', isArray: false
                },

                recupererLaListeDesOperationsNonBudgetiser: {
                    url: 'operations/non-budgetiser', method: 'GET', isArray: false
                }

            });
        }]);