angular.module('app')
    .controller('ComptePrevRealController',
        ['$scope', 'ComptesService', 'uiNotif', '$locale', 'BudgetsService',
            function ($scope, ComptesService, uiNotif, $locale, BudgetsService) {
                $locale.NUMBER_FORMATS.GROUP_SEP = ' ';
                $scope.realisationParPrevisionList = {
                    realisationParPrevisions: [],
                    totalPrevision: null,
                    totalRealisation: null,
                    totalReste: null
                };

                $scope.budgetForSearch = {
                    identifiant: null,
                    libelle: null
                };

                $scope.doSearch = function () {
                    if ($scope.budgetForSearch.identifiant !== null) {
                        $scope.promise = ComptesService.realisationParPrevision(
                            {budgetId: $scope.budgetForSearch.identifiant}, null).$promise;
                        $scope.promise.then(function (response) {
                                $scope.realisationParPrevisionList = response.data;
                            },
                            function (error) {
                                uiNotif.info(error.data.message);
                            });
                    } else {
                        uiNotif.info('Le budget est obligatoire');
                    }
                };

                $scope.fetchBudget = function ($select, $event) {

                    $scope.max = 10;

                    if (!$event) {
                        $scope.page = 1;
                        $scope.budgets = [];
                    } else {
                        $event.stopPropagation();
                        $event.preventDefault();
                        $scope.page++;
                    }

                    var budgetForSearch = {
                        budget: {
                            libelle: $select.search
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

                    BudgetsService.search(budgetForSearch).$promise
                        .then(function (response) {
                                var content = response.data.content;
                                $scope.budgets = $scope.budgets.concat(content).unique();
                                $scope.endOfList = (response.data.totalElements === $scope.budgets.length);
                                $scope.loading = false;
                            },
                            function (error) {
                                uiNotif.info(error.data.message);
                                $scope.loading = false;
                            });
                };
            }
        ]);

