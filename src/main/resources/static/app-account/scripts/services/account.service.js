/**
 * Service de la gestion des comptes utilisateurs
 *
 * @param $resource
 * @returns
 */
angular.module('app')
    .factory('AccountService', ['$resource',
        function ($resource) {

            return $resource('accounts/:accountId', {id: '@id'}, {

                resetPassword: {url: 'accounts/reset_password/finish', method: 'POST', isArray: false},

                getAccount: {url: 'accounts', method: 'GET'}

            });
        }]);