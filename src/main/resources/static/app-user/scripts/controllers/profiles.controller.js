/**
 * Controlleur d'édition des profiles
 */
angular.module('app')
    .controller('ProfilesControllerEdit',
        ['$scope', '$state', '$stateParams', 'ProfilesService', 'uiNotif', 'utils',
            function ($scope, $state, $stateParams, ProfilesService, uiNotif, utils) {

                /**
                 * Initialisation des données
                 */
                var initProfiles = {
                    identifiant: null,
                    nom: '',
                    state: utils.states[0].value
                };

                if ($stateParams.initData == null) {
                    $scope.profile = angular.copy(initProfiles);
                } else {
                    $scope.profile = angular.copy($stateParams.initData);
                }

                /*********************************************************************************/
                /**
                 * Ajout d'un profile
                 */
                var create = function () {

                    $scope.promise = ProfilesService.save($scope.profile).$promise;
                    $scope.promise.then(function (response) {

                        $scope.profile = angular.copy(initProfiles);
                        uiNotif.info(response.message);

                    }, function (error) {
                        uiNotif.info(error.data.message);
                    });
                };
                /*********************************************************************************/

                /**
                 * Mise à jour d'un profile
                 */
                var update = function () {

                    $scope.promise = ProfilesService.update($scope.profile).$promise;
                    $scope.promise.then(function (response) {

                        $scope.profile = angular.copy(initProfiles);
                        uiNotif.info(response.message);
                        $state.go('ui.profiles.list');

                    }, function (error) {
                        $scope.profile = angular.copy($stateParams.initData);
                        uiNotif.info(error.data.message);
                    });
                };
                /*********************************************************************************/

                /**
                 * Edition de profile
                 */
                $scope.doEdit = function () {

                    if ($scope.profile.identifiant == null) {
                        create();
                    } else {
                        update();
                    }
                };
            }
        ]);

/**
 * Controlleur de la page de liste des profiles
 */
angular.module('app')
    .controller('ProfilesControllerList',
        ['$scope', '$state', 'ProfilesService', 'uiNotif', 'utils',
            function ($scope, $state, ProfilesService, uiNotif, utils) {

                /**
                 * Initialisation des données
                 */
                $scope.states = utils.states;

                $scope.profiles = [];

                $scope.profilesSelected = [];

                $scope.query = utils.initPagination;

                var initFilterForm = {
                    profil: {
                        nom: '',
                        state: null
                    },
                    page: $scope.query.page - 1,
                    size: $scope.query.limit
                };

                $scope.filterForm = angular.copy(initFilterForm);

                $scope.isResearch = false;

                /*********************************************************************************/

                /**
                 * Liste des profiles
                 */
                $scope.getAll = function (page, limit) {

                    if ($scope.isResearch) {

                        $scope.filterForm.page = page - 1;
                        $scope.filterForm.size = limit;
                        $scope.doSearch();

                    } else {
                        $scope.promise = ProfilesService.findAll({page: page - 1, size: limit}).$promise;
                        $scope.promise.then(function (response) {

                                $scope.profiles = response.data.content;
                                $scope.query.count = response.data.totalElements;

                            },
                            function (error) {
                                uiNotif.info(error.data.message);
                            });
                    }
                };

                /*********************************************************************************/
                /**
                 * Appel de la liste des profiles
                 */
                $scope.getAll($scope.query.page, $scope.query.limit);


                /*********************************************************************************/

                /**
                 * Méthode de suppression d'un profile
                 */
                $scope.remove = function (idProfiles, ev) {

                    var title = utils.dialogTitleRemoval;
                    var message = 'Ce profil sera définitivement supprimé';

                    uiNotif.mdDialog(ev, title, message).then(function () {

                        $scope.promise = ProfilesService.remove({profilId: idProfiles}).$promise;
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
                 * Méthode de changement d'état d'un profile
                 */
                $scope.changeState = function (idProfiles, newState, ev) {

                    var state = {state: newState};

                    var msg = utils.iTranslate(newState).toLowerCase();

                    var title = utils.dialogTitleChangeState;
                    var message = 'Ce profil sera ' + msg;

                    uiNotif.mdDialog(ev, title, message).then(function () {

                        $scope.promise = ProfilesService.changeState({profilId: idProfiles}, state).$promise;
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
                 * Méthode de recherche des profiles
                 */
                $scope.doSearch = function () {

                    $scope.isResearch = true;

                    $scope.promise = ProfilesService.search($scope.filterForm).$promise;
                    $scope.promise.then(function (response) {

                            $scope.profiles = response.data.content;
                            $scope.query.count = response.data.totalElements;

                        },
                        function (error) {
                            uiNotif.info(error.data.message);
                        });
                };
                /*********************************************************************************/

                /**
                 * Méthode pour rafraichir la liste des profiles
                 */
                $scope.refresh = function () {
                    $scope.isResearch = false;
                    $scope.getAll($scope.query.page, $scope.query.limit);
                    $scope.filterForm = angular.copy(initFilterForm);
                };
            }
        ]);

