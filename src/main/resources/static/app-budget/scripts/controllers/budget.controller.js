/**
 * Controlleur d'édition des budgets
 */
angular.module('app')
    .controller('BudgetsControllerEdit',
        ['$scope', '$state', '$stateParams', 'BudgetsService', 'uiNotif', 'utils',
            function ($scope, $state, $stateParams, BudgetsService, uiNotif, utils) {

                /**
                 * Initialisation des données
                 */
                var initBudgets = {
                    identifiant: null,
                    libelle: '',
                    dateDebut: null,
                    dateFin: null
                };

                if ($stateParams.initData === null) {
                    $scope.budget = angular.copy(initBudgets);
                } else {
                    $scope.budget = angular.copy($stateParams.initData);
                }

                /*********************************************************************************/
                /**
                 * Ajout d'un budget
                 */
                var create = function () {

                    $scope.promise = BudgetsService.save($scope.budget).$promise;
                    $scope.promise.then(function (response) {

                        $scope.budget = angular.copy(initBudgets);
                        uiNotif.info(response.message);

                    }, function (error) {
                        uiNotif.info(error.data.message);
                    });
                };
                /*********************************************************************************/

                /**
                 * Mise à jour d'un budget
                 */
                var update = function () {

                    $scope.promise = BudgetsService.update($scope.budget).$promise;
                    $scope.promise.then(function (response) {

                        $scope.budget = angular.copy(initBudgets);
                        uiNotif.info(response.message);
                        $state.go('ui.budgets.list');

                    }, function (error) {
                        $scope.budget = angular.copy($stateParams.initData);
                        uiNotif.info(error.data.message);
                    });
                };
                /*********************************************************************************/

                /**
                 * Edition de budget
                 */
                $scope.doEdit = function () {

                    if ($scope.budget.identifiant === null) {
                        create();
                    } else {
                        update();
                    }
                };
            }
        ]);

/**
 * Controlleur de la page de liste des budgets
 */
angular.module('app')
    .controller('BudgetsControllerList',
        ['$scope', '$state', 'BudgetsService', 'uiNotif', 'utils', '$locale',
            function ($scope, $state, BudgetsService, uiNotif, utils, $locale) {

                $locale.NUMBER_FORMATS.GROUP_SEP = ' ';

                $scope.budgets = [];

                $scope.budgetsSelected = [];

                $scope.query = utils.initPagination;

                var initFilterForm = {
                    budget: {
                        libelle: ''
                    },
                    page: $scope.query.page - 1,
                    size: $scope.query.limit
                };

                $scope.filterForm = angular.copy(initFilterForm);

                $scope.isResearch = false;

                /*********************************************************************************/

                /**
                 * Liste des budgets
                 */
                $scope.getAll = function (page, limit) {

                    if ($scope.isResearch) {

                        $scope.filterForm.page = page - 1;
                        $scope.filterForm.size = limit;
                        $scope.doSearch();

                    } else {
                        $scope.promise = BudgetsService.findAll({page: page - 1, size: limit}).$promise;
                        $scope.promise.then(function (response) {

                                $scope.budgets = response.data.content;
                                $scope.query.count = response.data.totalElements;

                            },
                            function (error) {
                                uiNotif.info(error.data.message);
                            });
                    }
                };

                /*********************************************************************************/
                /**
                 * Appel de la liste des budgets
                 */
                $scope.getAll($scope.query.page, $scope.query.limit);


                /*********************************************************************************/

                /**
                 * Méthode de suppression d'un budget
                 */
                $scope.remove = function (idBudget, ev) {

                    var title = utils.dialogTitleRemoval;
                    var message = 'Ce budget sera définitivement supprimé';

                    uiNotif.mdDialog(ev, title, message).then(function () {

                        $scope.promise = BudgetsService.remove({budgetId: idBudget}).$promise;
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
                 * Méthode de recherche des budgets
                 */
                $scope.doSearch = function () {

                    $scope.isResearch = true;

                    $scope.promise = BudgetsService.search($scope.filterForm).$promise;
                    $scope.promise.then(function (response) {

                            $scope.budgets = response.data.content;
                            $scope.query.count = response.data.totalElements;

                        },
                        function (error) {
                            uiNotif.info(error.data.message);
                        });
                };
                /*********************************************************************************/

                /**
                 * Méthode pour rafraichir la liste des budgets
                 */
                $scope.refresh = function () {
                    $scope.isResearch = false;
                    $scope.getAll($scope.query.page, $scope.query.limit);
                    $scope.filterForm = angular.copy(initFilterForm);
                };
            }
        ]);

