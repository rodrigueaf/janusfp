/**
 * Service de la gestion des utilisateurs
 *
 * @param $resource
 * @returns
 */
angular.module('app')
    .factory('UserService', ['$resource',
        function ($resource) {

            return $resource('users/:userId', {id: '@id'}, {

                update: {method: 'PUT'},

                findAll: {method: 'GET', isArray: false},

                changeState: {url: 'users/:userId/states', method: 'PUT'},

                search: {url: 'users/search', method: 'POST', isArray: false}
            });
        }]);