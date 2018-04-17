angular.module('app')
    .controller('TresorerieGrdLivreController',
        ['$scope', 'TresoreriesService', 'uiNotif', '$locale',
            function ($scope, TresoreriesService, uiNotif, $locale) {
                $locale.NUMBER_FORMATS.GROUP_SEP = ' ';
                $scope.grandLivreTresorerieList = {
                    operationDetails: [],
                    soldeDebitTotal: null,
                    soldeCreditTotal: null
                };

                $scope.dateDebut = null;
                $scope.dateFin = null;

                $scope.tresorerieForSearch = {
                    identifiant: null,
                    libelle: null
                };

                $scope.doSearch = function () {
                    if ($scope.tresorerieForSearch.identifiant !== null) {
                        $scope.promise = TresoreriesService.recupererLeGrandLivre(
                            {tresorerieId: $scope.tresorerieForSearch.identifiant},
                            {dateDebut: $scope.dateDebut, dateFin: $scope.dateFin}).$promise;
                        $scope.promise.then(function (response) {
                                $scope.grandLivreTresorerieList = response.data;
                            },
                            function (error) {
                                uiNotif.info(error.data.message);
                            });
                    } else {
                        uiNotif.info('La tresorerie est obligatoire');
                    }
                };

                $scope.fetchTresorerie = function ($select, $event) {

                    $scope.max = 10;

                    if (!$event) {
                        $scope.page = 1;
                        $scope.tresoreries = [];
                    } else {
                        $event.stopPropagation();
                        $event.preventDefault();
                        $scope.page++;
                    }

                    var tresorerieForSearch = {
                        tresorerie: {
                            libelle: $select.search,
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

                    TresoreriesService.search(tresorerieForSearch).$promise
                        .then(function (response) {
                                var content = response.data.content;
                                $scope.tresoreries = $scope.tresoreries.concat(content).unique();
                                $scope.endOfList = (response.data.totalElements === $scope.tresoreries.length);
                                $scope.loading = false;
                            },
                            function (error) {
                                uiNotif.info(error.data.message);
                                $scope.loading = false;
                            });
                };
            }
        ]);

