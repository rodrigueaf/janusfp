/**
 * Controlleur d'édition des comptes
 */
angular.module('app')
    .controller('ComptesControllerEdit',
        ['$scope', '$state', '$stateParams', 'ComptesService', 'uiNotif', 'utils',
            function ($scope, $state, $stateParams, ComptesService, uiNotif, utils) {

                $scope.compteTypes = ["TRESORERIE", "ENTREE", "SORTIE", "AUTRE_COMPTE"];
                $scope.compteFormes = ["LOGIQUE", "PHYSIQUE"];

                /**
                 * Initialisation des données
                 */
                var initComptes = {
                    identifiant: null,
                    libelle: '',
                    compteType: null,
                    compteForme: null,
                    utilisable: false,
                    compteParent: null,
                    state: utils.states[0].value
                };

                if ($stateParams.initData === null) {
                    $scope.compte = angular.copy(initComptes);
                } else {
                    $scope.compte = angular.copy($stateParams.initData);
                }

                /*********************************************************************************/
                /**
                 * Ajout d'un compte
                 */
                var create = function () {

                    $scope.promise = ComptesService.save($scope.compte).$promise;
                    $scope.promise.then(function (response) {

                        $scope.compte = angular.copy(initComptes);
                        uiNotif.info(response.message);

                    }, function (error) {
                        uiNotif.info(error.data.message);
                    });
                };
                /*********************************************************************************/

                /**
                 * Mise à jour d'un compte
                 */
                var update = function () {

                    $scope.promise = ComptesService.update($scope.compte).$promise;
                    $scope.promise.then(function (response) {

                        $scope.compte = angular.copy(initComptes);
                        uiNotif.info(response.message);
                        $state.go('ui.comptes.list');

                    }, function (error) {
                        $scope.compte = angular.copy($stateParams.initData);
                        uiNotif.info(error.data.message);
                    });
                };
                /*********************************************************************************/

                /**
                 * Edition de compte
                 */
                $scope.doEdit = function () {

                    if ($scope.compte.identifiant === null) {
                        create();
                    } else {
                        update();
                    }
                };

                /**
                 * récupérer dynamiquement les comptes
                 * par lot de <code>$scope.max</code>
                 *
                 * @param $select
                 * @param $event
                 */
                $scope.fetch = function ($select, $event) {

                    $scope.max = 10;

                    if (!$event) {
                        $scope.page = 1;
                        $scope.comptes = [];
                    } else {
                        $event.stopPropagation();
                        $event.preventDefault();
                        $scope.page++;
                    }

                    var compteToSearch = {
                        compte: {
                            libelle: $select.search,
                            compteType: null,
                            state: null
                        },
                        size: $scope.max,
                        page: $scope.page - 1
                    };
                    $scope.loading = true;

                    Array.prototype.unique = function () {
                        var a = this.concat();
                        for (var i = 0; i < a.length; ++i) {
                            for (var j = i + 1; j < a.length; ++j) {
                                if (a[i].identifiant === a[j].identifiant)
                                    a.splice(j--, 1);
                            }
                        }

                        return a;
                    };
                    //appel vers le serveur
                    ComptesService.search(compteToSearch).$promise.then(function (response) {
                        var content = response.data.content;

                        $scope.comptes = $scope.comptes.concat(content).unique();

                        $scope.endOfList = (response.data.totalElements === $scope.comptes.length);
                        $scope.loading = false;
                    }, function (error) {
                        uiNotif.info(error.data.message);
                        $scope.loading = false;
                    });
                };
            }
        ]);

/**
 * Controlleur de la page de liste des comptes
 */
angular.module('app')
    .controller('ComptesControllerList',
        ['$scope', '$state', 'ComptesService', 'uiNotif', 'utils', '$locale',
            function ($scope, $state, ComptesService, uiNotif, utils, $locale) {
                $locale.NUMBER_FORMATS.GROUP_SEP = ' ';
                /**
                 * Initialisation des données
                 */
                $scope.states = utils.states;
                $scope.compteTypes = ["TRESORERIE", "ENTREE", "SORTIE", "AUTRE_COMPTE"];
                $scope.compteFormes = ["LOGIQUE", "PHYSIQUE"];

                $scope.comptes = [];

                $scope.comptesSelected = [];

                $scope.query = utils.initPagination;

                var initFilterForm = {
                    compte: {
                        libelle: '',
                        compteType: null,
                        compteForme: null,
                        state: null,
                        utilisable: null
                    },
                    page: $scope.query.page - 1,
                    size: $scope.query.limit
                };

                $scope.filterForm = angular.copy(initFilterForm);

                $scope.isResearch = false;

                /*********************************************************************************/

                /**
                 * Liste des comptes
                 */
                $scope.getAll = function (page, limit) {

                    if ($scope.isResearch) {

                        $scope.filterForm.page = page - 1;
                        $scope.filterForm.size = limit;
                        $scope.doSearch();

                    } else {
                        $scope.promise = ComptesService.findAll({page: page - 1, size: limit}).$promise;
                        $scope.promise.then(function (response) {

                                $scope.comptes = response.data.content;
                                $scope.query.count = response.data.totalElements;

                            },
                            function (error) {
                                uiNotif.info(error.data.message);
                            });
                    }
                };

                /*********************************************************************************/
                /**
                 * Appel de la liste des comptes
                 */
                $scope.getAll($scope.query.page, $scope.query.limit);


                /*********************************************************************************/

                /**
                 * Méthode de suppression d'un compte
                 */
                $scope.remove = function (idCompte, ev) {

                    var title = utils.dialogTitleRemoval;
                    var message = 'Ce compte sera définitivement supprimé';

                    uiNotif.mdDialog(ev, title, message).then(function () {

                        $scope.promise = ComptesService.remove({compteId: idCompte}).$promise;
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
                 * Méthode de changement d'état d'un compte
                 */
                $scope.changeState = function (idCompte, newState, ev) {

                    var state = {state: newState};

                    var msg = utils.iTranslate(newState).toLowerCase();

                    var title = utils.dialogTitleChangeState;
                    var message = 'Ce compte sera ' + msg;

                    uiNotif.mdDialog(ev, title, message).then(function () {

                        $scope.promise = ComptesService.changeState({compteId: idCompte}, state).$promise;
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
                 * Méthode de recherche des comptes
                 */
                $scope.doSearch = function () {

                    $scope.isResearch = true;

                    $scope.promise = ComptesService.search($scope.filterForm).$promise;
                    $scope.promise.then(function (response) {

                            $scope.comptes = response.data.content;
                            $scope.query.count = response.data.totalElements;

                        },
                        function (error) {
                            uiNotif.info(error.data.message);
                        });
                };
                /*********************************************************************************/

                /**
                 * Méthode pour rafraichir la liste des comptes
                 */
                $scope.refresh = function () {
                    $scope.isResearch = false;
                    $scope.getAll($scope.query.page, $scope.query.limit);
                    $scope.filterForm = angular.copy(initFilterForm);
                };
            }
        ]);

