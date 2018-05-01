/**
 * Service de la gestion des profiles
 *
 * @param $resource
 * @returns
 */
angular.module('app')
    .factory('ProfilesService', ['$resource',
        function ($resource) {

            return $resource('profils/:profilId', {id: '@id'}, {

                update: {method: 'PUT'},

                findAll: {method: 'GET', isArray: false},

                changeState: {url: 'profils/:profilId/states', method: 'PUT'},

                search: {url: 'profils/search', method: 'POST', isArray: false},

                getAuthorities: {url: 'profils/:nom/permissions', method: 'GET', isArray: false},

                createAuthorities: {url: 'profils/:nom/permissions', method: 'POST'},

                getDomains: {url: 'domains', method: 'GET', isArray: false}
            });
        }]);