/**
 * Service de la gestion des comptees
 *
 * @param $resource
 * @returns
 */
angular.module('app')
    .factory('ComptesService', ['$resource',
        function ($resource) {

            return $resource('comptes/:compteId', {id: '@id'}, {

                update: {method: 'PUT'},

                findAll: {method: 'GET', isArray: false},

                changeState: {url: 'comptes/:compteId/states', method: 'PUT'},

                search: {url: 'comptes/search', method: 'POST', isArray: false},

                realisationParPrevision: {url: 'comptes/prevision-realisation/:budgetId', method: 'GET', isArray: false},

                recupererLeGrandLivre: {url: 'comptes/:compteId/grand-livre', method: 'POST', isArray: false}
            });
        }]);