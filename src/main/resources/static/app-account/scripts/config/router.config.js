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

                var baseViewUri = "app-account/views/ui/";
                var baseServiceUri = "app-account/scripts/services/";
                var baseControllerUri = "app-account/scripts/controllers/";


                $stateProvider

                    .state('account', {
                        url: '/account',
                        abstract:true,
                        resolve: load(['ui.select',
                            baseServiceUri + 'account.service.js',
                            'app/scripts/services/auth.service.js', baseControllerUri + 'account.controller.js']),
                        template: '<div class="indigo bg-big"><div ui-view class="fade-in-down smooth"></div></div>'
                    })

                    .state('account.reset', {
                        url: '/reset/finish?key',
                        templateUrl: baseViewUri + 'reset-password.html',
                        controller: 'AccountController'
                    })
                ;
            }
        ]
    );

