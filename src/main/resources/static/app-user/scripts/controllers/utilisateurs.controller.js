/**
 * Controlleur d'édition des utilisateurs
 */
angular.module('app')
    .controller('UserControllerEdit',
        ['$scope', '$state', '$stateParams', 'UserService', 'ProfilesService', 'uiNotif', 'utils',
            function ($scope, $state, $stateParams, UserService, ProfilesService, uiNotif, utils) {

                /**
                 * Initialisation des données
                 */
                var initUser = {
                    identifiant: null,
                    login: '',
                    prenom: '',
                    nom: '',
                    email: '',
                    profil: null,
                    state: utils.states[0].value
                };

                if ($stateParams.initData == null) {
                    $scope.user = angular.copy(initUser);
                } else {
                    $scope.user = angular.copy($stateParams.initData);
                }

                // récupération des profils
                ProfilesService.findAll().$promise.then(function (response) {

                    $scope.profils = response.data.content.filter(function (item) {
                        return item.state === 'ENABLED';
                    });

                }, function (error) {
                    uiNotif.info(error.data.message);
                });

                /*********************************************************************************/
                /**
                 * Ajout d'un utilisateur
                 */
                var create = function () {

                    $scope.promise = UserService.save($scope.user).$promise;
                    $scope.promise.then(function (response) {

                        $scope.user = angular.copy(initUser);
                        uiNotif.info(response.message);

                    }, function (error) {
                        uiNotif.info(error.data.message);
                    });
                };
                /*********************************************************************************/

                /**
                 * Mise à jour d'un utilisateur
                 */
                var update = function () {

                    $scope.promise = UserService.update($scope.user).$promise;
                    $scope.promise.then(function (response) {

                        $scope.user = angular.copy(initUser);
                        uiNotif.info(response.message);
                        $state.go('ui.utilisateurs.list');

                    }, function (error) {
                        $scope.user = angular.copy($stateParams.initData);
                        uiNotif.info(error.data.message);
                    });
                };
                /*********************************************************************************/

                /**
                 * Edition de utilisateur
                 */
                $scope.doEdit = function () {

                    if ($scope.user.identifiant == null) {
                        create();
                    } else {
                        update();
                    }
                };
            }
        ]);

/**
 * Controlleur de la page de liste des utilisateurs
 */
angular.module('app')
    .controller('UserControllerList',
        ['$scope', '$state', 'UserService', 'uiNotif', 'utils',
            function ($scope, $state, UserService, uiNotif, utils) {

                /**
                 * Initialisation des données
                 */
                $scope.states = utils.states;

                $scope.users = [];

                $scope.usersSelected = [];

                $scope.query = utils.initPagination;

                var initFilterForm = {
                    utilisateur: {
                        prenom: '',
                        nom: '',
                        login: '',
                        email: '',
                        state: null
                    },
                    page: $scope.query.page - 1,
                    size: $scope.query.limit
                };

                $scope.filterForm = angular.copy(initFilterForm);

                $scope.isResearch = false;

                /*********************************************************************************/

                /**
                 * Liste des utilisateurs
                 */
                $scope.getAll = function (page, limit) {

                    if ($scope.isResearch) {

                        $scope.filterForm.page = page - 1;
                        $scope.filterForm.size = limit;
                        $scope.doSearch();

                    } else {
                        $scope.promise = UserService.findAll({page: page - 1, size: limit}).$promise;
                        $scope.promise.then(function (response) {

                                $scope.users = response.data.content;
                                $scope.query.count = response.data.totalElements;

                            },
                            function (error) {
                                uiNotif.info(error.data.message);
                            });
                    }
                };

                /*********************************************************************************/
                /**
                 * Appel de la liste des utilisateurs
                 */
                $scope.getAll($scope.query.page, $scope.query.limit);


                /*********************************************************************************/

                /**
                 * Méthode de suppression d'un utilisateur
                 */
                $scope.remove = function (login, ev) {

                    var title = utils.dialogTitleRemoval;
                    var message = 'Cet utilisateur sera définitivement supprimé';

                    uiNotif.mdDialog(ev, title, message).then(function () {

                        $scope.promise = UserService.remove({userId: login}).$promise;
                        $scope.promise.then(function (response) {

                                uiNotif.info(response.message);
                                $scope.getAll($scope.query.page, $scope.query.limit);

                            },
                            function (error) {
                                uiNotif.info(error.data.message);
                            });

                    }, function () {
                        // traitement quand on clique sur 'NON'
                    });
                };

                /*********************************************************************************/

                /**
                 * Méthode de changement d'état d'un utilisateur
                 */
                $scope.changeState = function (idUser, newState, ev) {

                    var state = {state: newState};

                    var msg = utils.iTranslate(newState).toLowerCase();

                    var title = utils.dialogTitleChangeState;
                    var message = 'Cet utilisateur sera ' + msg;

                    uiNotif.mdDialog(ev, title, message).then(function () {

                        $scope.promise = UserService.changeState({userId: idUser}, state).$promise;
                        $scope.promise.then(function (response) {

                                uiNotif.info(response.message);
                                $scope.getAll($scope.query.page, $scope.query.limit);

                            },
                            function (error) {
                                uiNotif.info(error.data.message);
                            });

                    }, function () {
                        // traitement quand on clique sur 'NON'
                    });
                };
                /*********************************************************************************/

                /**
                 * Méthode de recherche des utilisateurs
                 */
                $scope.doSearch = function () {

                    $scope.isResearch = true;

                    $scope.promise = UserService.search($scope.filterForm).$promise;
                    $scope.promise.then(function (response) {

                            $scope.users = response.data.content;
                            $scope.query.count = response.data.totalElements;

                        },
                        function (error) {
                            uiNotif.info(error.data.message);
                        });
                };
                /*********************************************************************************/

                /**
                 * Méthode pour rafraichir la liste des utilisateurs
                 */
                $scope.refresh = function () {
                    $scope.isResearch = false;
                    $scope.getAll($scope.query.page, $scope.query.limit);
                    $scope.filterForm = angular.copy(initFilterForm);
                };
            }
        ]);

