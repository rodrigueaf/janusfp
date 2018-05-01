/**
 * @ngdoc function
 * @name app.config:uiRouter
 * @description
 *
 * Config parent des routes
 */
angular.module('app')
    .run(
        ['$rootScope', '$state', '$stateParams',
            function ($rootScope, $state, $stateParams) {
                $rootScope.$state = $state;
                $rootScope.$stateParams = $stateParams;
            }
        ]
    )
    .config(['$stateProvider', '$urlRouterProvider',
            function ($stateProvider, $urlRouterProvider) {

                $urlRouterProvider.otherwise(function ($injector) {
                    var $state = $injector.get("$state");
                    $state.go('home');
                });

                $stateProvider

                    .state('access', {
                        url: '/access',
                        resolve: load(['app/scripts/services/auth.service.js']),
                        template: '<div class="indigo bg-big"><div ui-view class="fade-in-down smooth"></div></div>'
                    })

                    .state('access.signin', {
                        url: '/signin',
                        params: {
                            username: null,
                            password: null
                        },
                        templateUrl: 'app/views/ui/signin.html'
                    })

                    .state('access.404', {
                        url: '/404',
                        templateUrl: 'app/views/ui/404.html'
                    })

                    .state('access.403', {
                        url: '/403',
                        templateUrl: 'app/views/ui/403.html'
                    })

                    /*********************************************************************************/
                    // routes vers le dashboard
                    .state('home', {
                        url: '/home',
                        params: {
                            isFromLoginPage: false
                        },
                        resolve: load(['app/scripts/services/auth.service.js',
                            'app-account/scripts/services/account.service.js']),
                        views: {
                            '': {
                                templateUrl: 'app/views/common/layout.html'
                            },
                            'header': {
                                templateUrl: 'app/views/common/header.html'
                            },
                            'topnavbar': {
                                templateUrl: 'app/views/common/topnavbar.html'
                            },
                            'content': {
                                templateUrl: 'app/views/ui/home.html'
                            },
                            'footer': {
                                templateUrl: 'app/views/common/footer.html'
                            }
                        },
                        ncyBreadcrumb: {
                            label: 'Accueil'
                        }
                    })

                    /*********************************************************************************/

                    // base des routes
                    .state('ui', {
                        url: '/ui',
                        abstract: true,
                        resolve: load(['app/scripts/services/auth.service.js']),
                        views: {
                            '': {
                                templateUrl: 'app/views/common/layout.html'
                            },
                            'header': {
                                templateUrl: 'app/views/common/header.html'
                            },
                            'topnavbar': {
                                templateUrl: 'app/views/common/topnavbar.html'
                            },
                            'navigation': {
                                templateUrl: 'app/views/common/navigation.html'
                            },
                            'content': {
                                templateUrl: 'app/views/common/content.html'
                            },
                            'footer': {
                                templateUrl: 'app/views/common/footer.html'
                            }
                        }
                    });
            }
        ]
    );


//fonction de chargement des fichiers à la demande depuis les states
var load = function (srcs, callback) {
    return {
        deps: ['$ocLazyLoad', '$q', 'MODULE_CONFIG',
            function ($ocLazyLoad, $q, MODULE_CONFIG) {
                var deferred = $q.defer();
                var promise = false;
                srcs = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
                if (!promise) {
                    promise = deferred.promise;
                }
                angular.forEach(srcs, function (src) {
                    promise = promise.then(function () {
                        angular.forEach(MODULE_CONFIG, function (module) {
                            if (module.name === src) {
                                if (!module.module) {
                                    name = module.files;
                                } else {
                                    name = module.name;
                                }
                            } else {
                                name = src;
                            }
                        });
                        return $ocLazyLoad.load(name);
                    });
                });
                deferred.resolve();
                return callback ? promise.then(function () {
                        return callback();
                    }) : promise;
            }]
    }
};