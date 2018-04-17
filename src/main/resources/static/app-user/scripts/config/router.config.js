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

                var baseViewUri = "app-user/views/ui/";
                var baseServiceUri = "app-user/scripts/services/";
                var baseControllerUri = "app-user/scripts/controllers/";

                $stateProvider
                /*******************************************************************************************/
                // route parent des profiles
                    .state('ui.profiles', {
                        url: '/profiles',
                        abstract: true,
                        template: '<div ui-view></div>',
                        resolve: load([baseServiceUri + 'profiles.service.js',
                            baseControllerUri + 'profiles.controller.js',
                            baseControllerUri + 'profiles-droits.controller.js'])
                    })

                    // route vers la modification ou l'ajout d'un profile
                    .state('ui.profiles.edit', {
                        url: '/edit',
                        params: {
                            initData: null,
                            isDetails: false
                        },
                        controller: 'ProfilesControllerEdit',
                        templateUrl: baseViewUri + 'profiles.edit.html',
                        ncyBreadcrumb: {
                            parent: 'ui.profiles.list',
                            label: '{{breadCrumbLabel}}'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_ADMIN',
                                redirectTo: 'access.403'
                            }
                        }
                    })

                    // route vers les droits d'un profil
                    .state('ui.profiles.permission', {
                        url: '/permission',
                        params: {
                            profil: null
                        },
                        controller: 'ProfilesControllerPermission',
                        templateUrl: baseViewUri + 'profiles-droits.edit.html',
                        ncyBreadcrumb: {
                            parent: 'ui.profiles.list',
                            label: 'Attribution des droits'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_ADMIN',
                                redirectTo: 'access.403'
                            }
                        }
                    })

                    // route vers la liste des profiles
                    .state('ui.profiles.list', {
                        url: '/list',
                        templateUrl: baseViewUri + 'profiles.list.html',
                        controller: 'ProfilesControllerList',
                        resolve: load(['ui.select']),
                        ncyBreadcrumb: {
                            parent: 'home',
                            label: 'Liste des profils'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_ADMIN',
                                redirectTo: 'access.403'
                            }
                        }

                    })
                    /*******************************************************************************************/
                    // route parent des utilisateurs
                    .state('ui.utilisateurs', {
                        url: '/utilisateurs',
                        abstract: true,
                        template: '<div ui-view></div>',
                        resolve: load(['ui.select', baseServiceUri + 'utilisateurs.service.js',
                            baseServiceUri + 'profiles.service.js',
                            baseControllerUri + 'utilisateurs.controller.js'])
                    })

                    // route vers la modification ou l'ajout d'un utilisateur
                    .state('ui.utilisateurs.edit', {
                        url: '/edit',
                        params: {
                            initData: null,
                            isDetails: false
                        },
                        controller: 'UserControllerEdit',
                        templateUrl: baseViewUri + 'utilisateurs.edit.html',
                        ncyBreadcrumb: {
                            parent: 'ui.utilisateurs.list',
                            label: '{{breadCrumbLabel}}'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_ADMIN',
                                redirectTo: 'access.403'
                            }
                        }
                    })

                    // route vers la liste des utilisateurs
                    .state('ui.utilisateurs.list', {
                        url: '/list',
                        templateUrl: baseViewUri + 'utilisateurs.list.html',
                        controller: 'UserControllerList',
                        resolve: load(['ui.select']),
                        ncyBreadcrumb: {
                            parent: 'home',
                            label: 'Liste des utilisateurs'
                        },
                        data: {
                            permissions: {
                                only: 'ROLE_ADMIN',
                                redirectTo: 'access.403'
                            }
                        }

                    })
                /*******************************************************************************************/
                ;
            }
        ]
    );

