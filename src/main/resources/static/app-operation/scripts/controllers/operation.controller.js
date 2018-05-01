/**
 * Controlleur d'édition des schémas comptables
 */
angular.module('app')
    .controller('OperationsControllerEdit',
        ['$scope', '$filter', '$state', '$stateParams',
            'OperationsService', 'ComptesService', 'uiNotif', 'utils',
            'TresoreriesService', 'ExploitationsService', 'BudgetsService',
            function ($scope, $filter, $state, $stateParams,
                      OperationsService, ComptesService, uiNotif, utils,
                      TresoreriesService, ExploitationsService, BudgetsService) {

                $scope.operationVM = {
                    operation: {
                        identifiant: null,
                        budget: null,
                        operationType: null,
                        exploitation: null,
                        dateEnregistement: null
                    },
                    operationDetails: []
                };

                $scope.filtreDataCompte = $stateParams.filtreDataCompte;
                $scope.filtreDataTresorerie = $stateParams.filtreDataTresorerie;

                $scope.goToList = function () {
                    if ($scope.filtreDataCompte !== null) {
                        $state.go('ui.comptes.grdlivre', {
                            filtreDataCompte: $scope.filtreDataCompte
                        });
                    }else if ($scope.filtreDataTresorerie !== null) {
                        $state.go('ui.tresoreries.grdlivre', {
                            filtreDataTresorerie: $scope.filtreDataTresorerie
                        });
                    } else {
                        $state.go('ui.operations.list', {
                            budget: $scope.operationVM.operation.budget,
                            exploitation: $scope.operationVM.operation.exploitation
                        });
                    }
                };

                $scope.isMonoLigne = $stateParams.isMonoLigne;
                $scope.montantOperation = 0;

                $scope.operationTypes = ["DEPENSE", "PRODUIT", "PRET", "EMPRUNT", "VIREMENT"];

                var isAjout = true;

                $scope.budgetForSearch = null;

                $scope.operationVM.operation.budget = $stateParams.budget;
                $scope.operationVM.operation.exploitation = $stateParams.exploitation;
                $scope.operationVM.operation.dateEnregistement = new Date();

                if ($stateParams.operation !== null) {
                    $scope.operationVM.operation = $stateParams.operation;
                    isAjout = false;
                }

                $scope.loadSchemaByOperationBudget = function (item) {
                    $scope.promise = OperationsService.recupererOperationAvecMontantRestant(
                        {operationId: item.identifiant}).$promise;
                    $scope.promise.then(function (response) {
                        var data = response.data.operationDetails;
                        $scope.operationVM.operation.operationType = item.operationType;
                        for (var i = 0; i < data.length; i++) {
                            if (data[i].compte !== null) {
                                data[i].isTresorerie = false;
                            } else if (data[i].tresorerie !== null) {
                                data[i].isTresorerie = true;
                            }
                            data[i].identifiant = null;
                        }
                        $scope.operationVM.operationDetails = data;
                        $scope.montantOperation = response.data.montantRestant;

                    }, function (error) {
                        uiNotif.info(error.data.message);
                    });
                };

                $scope.loadSchemaByTypeOperation = function (item) {
                    $scope.operationVM.operation.operationType = item;
                    switch (item) {
                        case 'PRET':
                        case 'DEPENSE':
                            $scope.operationVM.operationDetails = [
                                {
                                    compte: null,
                                    tresorerie: null,
                                    operationSens: 'CREDIT',
                                    montant: 0,
                                    isTresorerie: true
                                }, {
                                    tresorerie: null,
                                    compte: null,
                                    operationSens: 'DEBIT',
                                    montant: 0,
                                    isTresorerie: false
                                }
                            ];
                            break;
                        case 'EMPRUNT':
                        case 'PRODUIT':
                            $scope.operationVM.operationDetails = [
                                {
                                    tresorerie: null,
                                    compte: null,
                                    operationSens: 'CREDIT',
                                    montant: 0,
                                    isTresorerie: false
                                }, {
                                    compte: null,
                                    tresorerie: null,
                                    operationSens: 'DEBIT',
                                    montant: 0,
                                    isTresorerie: true
                                }
                            ];
                            break;
                        case 'VIREMENT':
                            $scope.operationVM.operationDetails = [
                                {
                                    compte: null,
                                    tresorerie: null,
                                    operationSens: 'CREDIT',
                                    montant: 0,
                                    isTresorerie: true
                                }, {
                                    compte: null,
                                    tresorerie: null,
                                    operationSens: 'DEBIT',
                                    montant: 0,
                                    isTresorerie: true
                                }
                            ];
                    }
                };

                // Chargement des lignes des détails en fonction de l'opération
                if ($scope.operationVM.operation !== null && $scope.operationVM.operation.identifiant !== null) {
                    $scope.promise = OperationsService.recupererLaListeDesDetailsDOperations(
                        {operationId: $scope.operationVM.operation.identifiant}).$promise;
                    $scope.promise.then(function (response) {
                        var data = response.data;
                        if (data.length === 0) {
                            isAjout = true;
                            $scope.operationVM.operationDetails = [];
                        } else {
                            isAjout = false;
                            $scope.operationVM.operationDetails = data;
                        }
                        for (var i = 0; i < $scope.operationVM.operationDetails.length; i++) {
                            $scope.operationVM.operationDetails[i].isTresorerie
                                = $scope.operationVM.operationDetails[i].tresorerie !== null;
                        }
                    }, function (error) {
                        uiNotif.info(error.data.message);
                    });
                }

                /**
                 * Les sens
                 */
                $scope.sens = [{'value': 'DEBIT'},
                    {'value': 'CREDIT'}];

                // Supprimer une ligne du tableau
                $scope.removeSchema = function (index) {
                    $scope.operationVM.operationDetails.splice(index, 1);
                };

                // Ajouter une ligne
                $scope.addSchema = function () {
                    $scope.inserted = {
                        id: $scope.operationVM.operationDetails.length + 1,
                        operationSens: null,
                        compte: null,
                        montant: null,
                        tresorerie: null
                    };
                    $scope.operationVM.operationDetails.push($scope.inserted);
                };

                // filter operationDetail to show
                $scope.filterSchema = function (operationDetail) {
                    return operationDetail.isDeleted !== true;
                };

                // mark schema as deleted
                $scope.removeSchema = function (id) {
                    var filtered = $filter('filter')($scope.operationVM.operationDetails, {identifiant: id});
                    if (filtered.length) {
                        filtered[0].isDeleted = true;
                    }
                };

                // cancel all changes
                $scope.cancel = function () {
                    for (var i = $scope.operationVM.operationDetails.length; i--;) {
                        var schema = $scope.operationVM.operationDetails[i];
                        // undelete
                        if (schema.isDeleted) {
                            delete schema.isDeleted;
                        }
                        // remove new
                        if (schema.isNew) {
                            $scope.operationVM.operationDetails.length.splice(i, 1);
                        }
                    }
                };

                $scope.budgetSelected = {};

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
                            compteType: ($scope.operationVM.operation.operationType === 'DEPENSE'
                                || $scope.operationVM.operation.operationType === 'PRET') ? 'SORTIE' : 'ENTREE',
                            utilisable: true,
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

                    var tresorerieToSearch = {
                        tresorerie: {
                            compteLogique: {
                                libelle: $select.search,
                                compteType: null,
                                state: null
                            },
                            comptePhysique: {
                                libelle: $select.search,
                                compteType: null,
                                state: null
                            }
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
                    TresoreriesService.search(tresorerieToSearch).$promise.then(function (response) {
                        var content = response.data.content;

                        $scope.tresoreries = $scope.tresoreries.concat(content).unique();

                        $scope.endOfList = (response.data.totalElements === $scope.tresoreries.length);
                        $scope.loading = false;
                    }, function (error) {
                        uiNotif.info(error.data.message);
                        $scope.loading = false;
                    });
                };
                $scope.fetchOperationBudget = function ($select, $event) {

                    $scope.max = 10;

                    if (!$event) {
                        $scope.page = 1;
                        $scope.operationsBudget = [];
                    } else {
                        $event.stopPropagation();
                        $event.preventDefault();
                        $scope.page++;
                    }

                    var operationToSearch = {
                        operation: {
                            budget: $scope.budgetSelected,
                            description: $select.search
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
                    OperationsService.search(operationToSearch).$promise.then(function (response) {
                        var content = response.data.content;

                        $scope.operationsBudget = $scope.operationsBudget.concat(content).unique();

                        $scope.endOfList = (response.data.totalElements === $scope.operationsBudget.length);
                        $scope.loading = false;
                    }, function (error) {
                        uiNotif.info(error.data.message);
                        $scope.loading = false;
                    });
                };
                $scope.fetchExploitation = function ($select, $event) {

                    $scope.max = 10;

                    if (!$event) {
                        $scope.page = 1;
                        $scope.exploitations = [];
                    } else {
                        $event.stopPropagation();
                        $event.preventDefault();
                        $scope.page++;
                    }

                    var exploitationToSearch = {
                        exploitation: {
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
                    //appel vers le serveur
                    ExploitationsService.search(exploitationToSearch).$promise.then(function (response) {
                        var content = response.data.content;

                        $scope.exploitations = $scope.exploitations.concat(content).unique();

                        $scope.endOfList = (response.data.totalElements === $scope.exploitations.length);
                        $scope.loading = false;
                    }, function (error) {
                        uiNotif.info(error.data.message);
                        $scope.loading = false;
                    });
                };

                $scope.filtrerOperationBudget = function (data) {
                    $scope.budgetSelected = data;
                    $scope.fetchOperationBudget({search: ''}, false);
                };

                if ($stateParams.exploitation !== null) {
                    $scope.budgetForSearch = $stateParams.exploitation.budgetParDefaut;
                    $scope.filtrerOperationBudget($scope.budgetForSearch);
                }

                // Préparer la liste à persister
                var prepareSchmema = function (operationsDetails) {
                    var results = [];
                    for (var i = operationsDetails.length; i--;) {
                        var schema = operationsDetails[i];
                        // actually delete schema
                        if (schema.isDeleted) {
                            operationsDetails.splice(i, 1);
                        }
                        // mark as not new
                        if (schema.isNew) {
                            schema.isNew = false;
                        }
                        // send on server
                        if ($scope.isMonoLigne)
                            schema.montant = $scope.montantOperation;
                        results.push(schema);

                    }
                    return operationsDetails;
                };

                /*********************************************************************************/
                /**
                 * Ajout d'une opération
                 */
                var create = function (ev) {

                    var message = 'Voulez-vous vraiment valider cette opération';

                    uiNotif.mdDialog(ev, "Confirmation", message).then(function () {
                        $scope.operationVM.operationDetails = prepareSchmema($scope.operationVM.operationDetails);

                        $scope.promise = OperationsService.save($scope.operationVM).$promise;
                        $scope.promise.then(function (response) {

                            uiNotif.info(response.message);
                            if ($scope.operationVM.operation.budget !== null)
                                $state.go('ui.operations.list', {budget: $scope.operationVM.operation.budget});
                            else if ($scope.operationVM.operation.exploitation !== null)
                                $state.go('ui.operations.list', {exploitation: $scope.operationVM.operation.exploitation});
                            else
                                $state.go('ui.operations.list');
                        }, function (error) {
                            uiNotif.info(error.data.message);
                        });
                    }, function () {
                        // traitement quand on clique sur 'NON'
                    });
                };
                /*********************************************************************************/

                /**
                 * Mise à jour d'une opération
                 */
                var update = function (ev) {
                    var message = 'Voulez-vous vraiment valider cette opération';

                    uiNotif.mdDialog(ev, "Confirmation", message).then(function () {
                        $scope.operationVM.operationDetails = prepareSchmema($scope.operationVM.operationDetails);

                        $scope.promise = OperationsService.update($scope.operationVM).$promise;
                        $scope.promise.then(function (response) {

                            uiNotif.info(response.message);
                            if ($scope.operationVM.operation.budget !== null)
                                $state.go('ui.operations.list', {budget: $scope.operationVM.operation.budget});
                            else if ($scope.operationVM.operation.exploitation !== null)
                                $state.go('ui.operations.list', {exploitation: $scope.operationVM.operation.exploitation});
                            else
                                $state.go('ui.operations.list');

                        }, function (error) {
                            uiNotif.info(error.data.message);
                        });
                    }, function () {
                        // traitement quand on clique sur 'NON'
                    });
                };
                /*********************************************************************************/

                /**
                 * Edition d'un opérations
                 */
                $scope.doEdit = function (ev) {

                    if ($scope.operationVM.operationDetails.length !== 0) {
                        if (isAjout) {
                            create(ev);
                        } else {
                            update(ev);
                        }
                    } else {
                        uiNotif.info('Vous devez ajouter des lignes à l\'opération');
                    }
                };

            }
        ]);
/**
 * Controlleur de la page de liste des opérations
 */
angular.module('app')
    .controller('OperationsControllerList',
        ['$scope', '$state', 'OperationsService', 'uiNotif', 'utils', '$stateParams', '$locale',
            function ($scope, $state, OperationsService, uiNotif, utils, $stateParams, $locale) {
                $locale.NUMBER_FORMATS.GROUP_SEP = ' ';
                /**
                 * Initialisation des données
                 */
                $scope.operationTypes = ["DEPENSE", "PRODUIT", "PRET", "EMPRUNT", "VIREMENT"];

                $scope.operations = [];

                $scope.showRecap = $stateParams.budget !== null || $stateParams.exploitation !== null;

                $scope.operationsSelected = [];

                $scope.query = utils.initPagination;

                var initFilterForm = {
                    operation: {
                        description: '',
                        operationType: null,
                        budget: null,
                        exploitation: null
                    },
                    page: $scope.query.page - 1,
                    size: $scope.query.limit
                };

                $scope.filterForm = angular.copy(initFilterForm);

                $scope.isResearch = false;

                var recapitulatif = {
                    depense: 0,
                    produit: 0,
                    pret: 0,
                    emprunt: 0,
                    reste: 0
                };

                $scope.recapitulatif = angular.copy(recapitulatif);

                $scope.recapitulatifs = [$scope.recapitulatif];

                var calculerRecap = function (operations) {
                    for (var i = 0; i < operations.length; i++) {
                        if (operations[i].operationType === 'DEPENSE')
                            $scope.recapitulatif.depense += operations[i].montantTotal;
                        else if (operations[i].operationType === 'PRODUIT' || operations[i].operationType === 'VIREMENT')
                            $scope.recapitulatif.produit += operations[i].montantTotal;
                        else if (operations[i].operationType === 'PRET')
                            $scope.recapitulatif.pret += operations[i].montantTotal;
                        else if (operations[i].operationType === 'EMPRUNT')
                            $scope.recapitulatif.emprunt += operations[i].montantTotal;
                    }
                    $scope.recapitulatif.reste =
                        ($scope.recapitulatif.produit + $scope.recapitulatif.emprunt) -
                        ($scope.recapitulatif.depense + $scope.recapitulatif.pret)
                };

                /*********************************************************************************/

                /**
                 * Liste des opérations
                 */
                $scope.getAll = function (page, limit) {

                    if ($scope.isResearch) {

                        $scope.filterForm.page = page - 1;
                        $scope.filterForm.size = limit;
                        $scope.doSearch();

                    } else {
                        if ($stateParams.budget !== null) {
                            $scope.budget = $stateParams.budget;
                            $scope.filterForm.page = 0;
                            $scope.filterForm.size = 1000;
                            $scope.filterForm = angular.copy(initFilterForm);
                            $scope.filterForm.operation.budget = $stateParams.budget;
                            $scope.doSearch();
                            $scope.filterForm = angular.copy(initFilterForm);
                        } else if ($stateParams.exploitation !== null) {
                            $scope.exploitation = $stateParams.exploitation;
                            $scope.filterForm.page = 0;
                            $scope.filterForm.size = 1000;
                            $scope.filterForm = angular.copy(initFilterForm);
                            $scope.filterForm.operation.exploitation = $stateParams.exploitation;
                            $scope.doSearch();
                            $scope.filterForm = angular.copy(initFilterForm);
                        } else {
                            $scope.promise = OperationsService
                                .recupererLaListeDesOperationsNonBudgetiser({page: page - 1, size: limit}).$promise;
                            $scope.promise.then(function (response) {

                                    $scope.operations = response.data.content;
                                    $scope.query.count = response.data.totalElements;

                                },
                                function (error) {
                                    uiNotif.info(error.data.message);
                                });
                        }
                    }
                };

                /**
                 * Méthode de recherche des types d'opération
                 */
                $scope.doSearch = function () {

                    $scope.isResearch = true;

                    if ($stateParams.budget !== null) {
                        $scope.filterForm.operation.budget = $stateParams.budget;
                    } else if ($stateParams.exploitation !== null) {
                        $scope.filterForm.operation.exploitation = $stateParams.exploitation;
                    }

                    $scope.promise = OperationsService.search($scope.filterForm).$promise;
                    $scope.promise.then(function (response) {

                            $scope.operations = response.data.content;

                            if ($stateParams.budget !== null || $stateParams.exploitation !== null) {
                                if ($scope.showRecap) {
                                    $scope.recapitulatif = angular.copy(recapitulatif);
                                    $scope.recapitulatifs = [$scope.recapitulatif];
                                    calculerRecap($scope.operations);
                                }
                            } else {
                                var operationTmp = [];
                                for (var i = 0; i < $scope.operations.length; i++) {
                                    if ($scope.operations[i].budget === null) {
                                        operationTmp.push($scope.operations[i]);
                                    }
                                }
                                $scope.operations = operationTmp;
                            }
                            $scope.query.count = $scope.operations.length;
                        },
                        function (error) {
                            uiNotif.info(error.data.message);
                        });
                };
                /*********************************************************************************/
                /**
                 * Appel de la liste des opérations
                 */
                $scope.getAll($scope.query.page, $scope.query.limit);


                /*********************************************************************************/

                /**
                 * Méthode de suppression d'une opération
                 */
                $scope.remove = function (idOperation, ev) {

                    var title = utils.dialogTitleRemoval;
                    var message = 'Cette opération sera définitivement supprimée';

                    uiNotif.mdDialog(ev, title, message).then(function () {

                        $scope.promise = OperationsService.remove({operationId: idOperation}, null).$promise;
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

                /*********************************************************************************/

                /**
                 * Méthode pour rafraichir la liste des opérations
                 */
                $scope.refresh = function () {
                    $scope.recapitulatif = angular.copy(recapitulatif);
                    $scope.recapitulatifs = [$scope.recapitulatif];
                    $scope.isResearch = false;
                    $scope.getAll($scope.query.page, $scope.query.limit);
                    $scope.filterForm = angular.copy(initFilterForm);
                };
            }
        ]);

