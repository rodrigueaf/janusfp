/**
 * Controlleur d'édition des tresoreries
 */
angular.module('app')
    .controller('TresoreriesControllerEdit',
        ['$scope', '$state', '$stateParams', 'TresoreriesService', 'uiNotif', 'utils', 'ComptesService',
            function ($scope, $state, $stateParams, TresoreriesService, uiNotif, utils, ComptesService) {

                /**
                 * Initialisation des données
                 */
                var initTresoreries = {
                    identifiant: null,
                    comptePhysique: null,
                    compteLogique: null
                };

                if ($stateParams.initData === null) {
                    $scope.tresorerie = angular.copy(initTresoreries);
                } else {
                    $scope.tresorerie = angular.copy($stateParams.initData);
                }

                /*********************************************************************************/
                /**
                 * Ajout d'un tresorerie
                 */
                var create = function () {

                    $scope.promise = TresoreriesService.save($scope.tresorerie).$promise;
                    $scope.promise.then(function (response) {

                        $scope.tresorerie = angular.copy(initTresoreries);
                        uiNotif.info(response.message);

                    }, function (error) {
                        uiNotif.info(error.data.message);
                    });
                };
                /*********************************************************************************/

                /**
                 * Mise à jour d'un tresorerie
                 */
                var update = function () {

                    $scope.promise = TresoreriesService.update($scope.tresorerie).$promise;
                    $scope.promise.then(function (response) {

                        $scope.tresorerie = angular.copy(initTresoreries);
                        uiNotif.info(response.message);
                        $state.go('ui.tresoreries.list');

                    }, function (error) {
                        $scope.tresorerie = angular.copy($stateParams.initData);
                        uiNotif.info(error.data.message);
                    });
                };
                /*********************************************************************************/

                /**
                 * Edition de tresorerie
                 */
                $scope.doEdit = function () {

                    if ($scope.tresorerie.identifiant === null) {
                        create();
                    } else {
                        update();
                    }
                };

                /**
                 * récupérer dynamiquement les tresoreries
                 * par lot de <code>$scope.max</code>
                 *
                 * @param $select
                 * @param $event
                 */
                $scope.fetchComptePhysique = function ($select, $event) {

                    $scope.max = 10;

                    if (!$event) {
                        $scope.page = 1;
                        $scope.comptePhysique = [];
                    } else {
                        $event.stopPropagation();
                        $event.preventDefault();
                        $scope.page++;
                    }

                    var compteToSearch = {
                        compte: {
                            libelle: $select.search,
                            compteForme: 'PHYSIQUE',
                            compteType: 'TRESORERIE',
                            utilisable : true,
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
                    ComptesService.search(compteToSearch).$promise.then(function (response) {
                        var content = response.data.content;

                        $scope.comptePhysique = $scope.comptePhysique.concat(content).unique();

                        $scope.endOfList = (response.data.totalElements === $scope.comptePhysique.length);
                        $scope.loading = false;
                    }, function (error) {
                        uiNotif.info(error.data.message);
                        $scope.loading = false;
                    });
                };

                /**
                 * récupérer dynamiquement les tresoreries
                 * par lot de <code>$scope.max</code>
                 *
                 * @param $select
                 * @param $event
                 */
                $scope.fetchCompteLogique = function ($select, $event) {

                    $scope.max = 10;

                    if (!$event) {
                        $scope.page = 1;
                        $scope.comptesLogique = [];
                    } else {
                        $event.stopPropagation();
                        $event.preventDefault();
                        $scope.page++;
                    }

                    var compteToSearch = {
                        compte: {
                            libelle: $select.search,
                            compteForme: 'LOGIQUE',
                            compteType: 'TRESORERIE',
                            utilisable : true,
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
                    ComptesService.search(compteToSearch).$promise.then(function (response) {
                        var content = response.data.content;

                        $scope.comptesLogique = $scope.comptesLogique.concat(content).unique();

                        $scope.endOfList = (response.data.totalElements === $scope.comptesLogique.length);
                        $scope.loading = false;
                    }, function (error) {
                        uiNotif.info(error.data.message);
                        $scope.loading = false;
                    });
                };
            }
        ]);

/**
 * Controlleur de la page de liste des tresoreries
 */
angular.module('app')
    .controller('TresoreriesControllerList',
        ['$scope', '$state', 'TresoreriesService', 'uiNotif', 'utils', '$locale',
            function ($scope, $state, TresoreriesService, uiNotif, utils, $locale) {
                $locale.NUMBER_FORMATS.GROUP_SEP = ' ';
                $scope.tresoreries = [];

                $scope.tresoreriesSelected = [];

                $scope.query = utils.initPagination;

                var initFilterForm = {
                    tresorerie: {
                        comptePhysique: {
                            libelle: null
                        },
                        compteLogique: {
                            libelle: null
                        }
                    },
                    page: $scope.query.page - 1,
                    size: $scope.query.limit
                };

                $scope.filterForm = angular.copy(initFilterForm);

                $scope.isResearch = false;

                $scope.soldeTotal = 0;

                /*********************************************************************************/

                /**
                 * Liste des tresoreries
                 */
                $scope.getAll = function (page, limit) {

                    if ($scope.isResearch) {

                        $scope.filterForm.page = page - 1;
                        $scope.filterForm.size = limit;
                        $scope.doSearch();

                    } else {
                        $scope.promise = TresoreriesService.findAll({page: page - 1, size: limit}).$promise;
                        $scope.promise.then(function (response) {

                                $scope.tresoreries = response.data.content;

                                for(var i = 0; i < $scope.tresoreries.length; i++){
                                    $scope.soldeTotal += $scope.tresoreries[i].soldeCourant;
                                }

                                $scope.query.count = response.data.totalElements;

                            },
                            function (error) {
                                uiNotif.info(error.data.message);
                            });
                    }
                };

                /*********************************************************************************/
                /**
                 * Appel de la liste des tresoreries
                 */
                $scope.getAll($scope.query.page, $scope.query.limit);


                /*********************************************************************************/

                /**
                 * Méthode de suppression d'un tresorerie
                 */
                $scope.remove = function (idTresorerie, ev) {

                    var title = utils.dialogTitleRemoval;
                    var message = 'Cette tresorerie sera définitivement supprimée';

                    uiNotif.mdDialog(ev, title, message).then(function () {

                        $scope.promise = TresoreriesService.remove({tresorerieId: idTresorerie}).$promise;
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
                 * Méthode de recherche des tresoreries
                 */
                $scope.doSearch = function () {

                    $scope.isResearch = true;

                    $scope.promise = TresoreriesService.search($scope.filterForm).$promise;
                    $scope.promise.then(function (response) {

                            $scope.tresoreries = response.data.content;
                            $scope.query.count = response.data.totalElements;

                        },
                        function (error) {
                            uiNotif.info(error.data.message);
                        });
                };
                /*********************************************************************************/

                /**
                 * Méthode pour rafraichir la liste des tresoreries
                 */
                $scope.refresh = function () {
                    $scope.isResearch = false;
                    $scope.getAll($scope.query.page, $scope.query.limit);
                    $scope.filterForm = angular.copy(initFilterForm);
                };
            }
        ]);

