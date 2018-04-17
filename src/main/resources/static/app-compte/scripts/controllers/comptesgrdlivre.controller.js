angular.module('app')
    .controller('CompteGrdLivreController',
        ['$scope', 'ComptesService', 'uiNotif', '$locale',
            function ($scope, ComptesService, uiNotif, $locale) {
                $locale.NUMBER_FORMATS.GROUP_SEP = ' ';
                $scope.grandLivreList = {
                    operationDetails: [],
                    soldeTotal: null
                };

                $scope.dateDebut = null;
                $scope.dateFin = null;

                $scope.compteForSearch = {
                    identifiant: null,
                    libelle: null
                };

                $scope.doSearch = function () {
                    if ($scope.compteForSearch.identifiant !== null) {
                        $scope.promise = ComptesService.recupererLeGrandLivre(
                            {compteId: $scope.compteForSearch.identifiant},
                            {dateDebut: $scope.dateDebut, dateFin: $scope.dateFin}).$promise;
                        $scope.promise.then(function (response) {
                                $scope.grandLivreList = response.data;
                            },
                            function (error) {
                                uiNotif.info(error.data.message);
                            });
                    } else {
                        uiNotif.info('Le compte est obligatoire');
                    }
                };

                $scope.fetchCompte = function ($select, $event) {

                    $scope.max = 10;

                    if (!$event) {
                        $scope.page = 1;
                        $scope.comptes = [];
                    } else {
                        $event.stopPropagation();
                        $event.preventDefault();
                        $scope.page++;
                    }

                    var compteForSearch = {
                        compte: {
                            libelle: $select.search,
                            utilisable: true,
                            state: null,
                            compteType: null
                        },
                        compteTypeDifferent: 'TRESORERIE',
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

                    ComptesService.search(compteForSearch).$promise
                        .then(function (response) {
                                var content = response.data.content;
                                $scope.comptes = $scope.comptes.concat(content).unique();
                                $scope.endOfList = (response.data.totalElements === $scope.comptes.length);
                                $scope.loading = false;
                            },
                            function (error) {
                                uiNotif.info(error.data.message);
                                $scope.loading = false;
                            });
                };
            }
        ]);

