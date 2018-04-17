/**
 * Service de la gestion des Tresorerie
 *
 * @param $resource
 * @returns
 */
angular.module('app')
    .factory('TresoreriesService', ['$resource',
        function ($resource) {

            return $resource('tresoreries/:tresorerieId', {id: '@id'}, {

                update: {method: 'PUT'},

                findAll: {method: 'GET', isArray: false},

                search: {url: 'tresoreries/search', method: 'POST', isArray: false},

                recupererLeGrandLivre: {url: 'tresoreries/:tresorerieId/grand-livre', method: 'POST', isArray: false}
            });
        }]);