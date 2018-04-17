function profilesControllerPermission($scope, $stateParams, ProfilesService, uiNotif, $state) {

    $scope.profil = $stateParams.profil;

    $scope.treeInstance = {};
    $scope.selected = {
        authorities: []
    };

    var convertToJsTreeData = function (dataDomain) {
        var parents = [];

        var lengthModules = dataDomain.length;
        for (var i = 0; i < lengthModules; i++) {

            var domain = {
                'name': '',
                'libelleDefault': ''
            };

            /*
             * récupéré les information du domaine pour une permission données
             */
            domain.libelleDefault = dataDomain[i].name;

            var dataAuthorities = dataDomain[i].permissions;
            var lengthAuthorities = dataAuthorities.length;

            if (lengthAuthorities > 0) {

                var children = [];
                var data = [];

                var openParent = false;

                for (var j = 0; j < lengthAuthorities; j++) {

                    openParent = ($scope.selected.authorities.indexOf(dataAuthorities[j].nom) !== -1);

                    var child = {
                        'text': '',
                        'data': {},
                        'type': 'authority',
                        'name': '',
                        'state': {
                            'selected': openParent
                        }
                    };

                    /*
                     * construire un enfants d'un parent pour la jstree
                     */
                    child.text = dataAuthorities[j].libelleParDefaut;
                    child.name = dataAuthorities[j].nom;

                    //contient les informations relative à une fonctionnalité
                    //les attribut doivent être conforme à ceux de l'entité Feature côté backend
                    child.data = {
                        'name': dataAuthorities[j].nom,
                        'libelleDefault': dataAuthorities[j].libelleParDefaut,
                        'domain': domain
                    };

                    /*
                     * liste des enfants d'un parent pour la jstree
                     */
                    children.push(child);
                    data.push(child.data.name);
                }
            }

            var parent = {
                'text': '',
                'type': 'domain',
                'children': [],
                'data': [],
                'state': {
                    'opened': openParent
                }
            };

            /*
             * construire le noeud parent pour la jstree
             */
            parent.text = domain.libelleDefault;
            parent.children = children;
            parent.data = data;
            /*
             * liste des parents pour la jstree
             */
            parents.push(parent);
        }
        return parents;
    };

    var reCreateTree = function () {
        $scope.treeConfig.version++;
    };

    /*
     * récupérer la liste des droits du profil
     */
    if ($stateParams.profil != null) {
        ProfilesService.getAuthorities({nom: $stateParams.profil.nom})
            .$promise.then(function (response) {
                $scope.selected.authorities = response.data;

                /*
                 * récupérer la liste des domaines et des droits associés
                 */
                ProfilesService.getDomains()
                    .$promise.then(function (response) {

                        $scope.jsTreeData = convertToJsTreeData(response.data);
                        reCreateTree();
                    },
                    function (error) {
                        uiNotif.info(error.data.message);
                    });

            },
            function (error) {
                uiNotif.info(error.data.message);
            });
    }

    /*
     * détails de configuration pour la customisation de la jstree
     */
    $scope.treeConfig = {
        checkbox: {"keep_selected_style": false},
        types: {
            "domain": {/*pas de customisation*/},
            "authority": {"icon": "glyphicon glyphicon-file"}
        },
        plugins: ["checkbox", "sort", "types"],
        core: {
            multiple: true,
            animation: true,
            error: function () {
            },
            check_callback: true,
            worker: true
        },
        version: 1
    };



    $scope.selectNodeCB = function (e, item) {
        if (item.node.type === 'domain') {
            $scope.selected.authorities.push.apply($scope.selected.authorities, item.node.data);
        } else {
            $scope.selected.authorities.push(item.node.data.name);
        }
    };

    $scope.deselectNodeCB = function (e, item) {
        if (item.node.type === 'domain') {
            for (var i = 0; i < item.node.data.length; i++) {
                while ($scope.selected.authorities.indexOf(item.node.data[i]) !== -1) {
                    $scope.selected.authorities.splice(
                        $scope.selected.authorities.indexOf(item.node.data[i]), 1);
                }
            }
        } else {
            while ($scope.selected.authorities.indexOf(item.node.data.name) !== -1){
                $scope.selected.authorities.splice(
                    $scope.selected.authorities.indexOf(item.node.data.name), 1);
            }
        }
    };

    $scope.ajouter = function () {
        ProfilesService.createAuthorities({nom: $stateParams.profil.nom}, $scope.selected.authorities)
            .$promise.then(function (response) {

            uiNotif.info(response.message);
            $state.go('ui.profiles.list');

        }, function (error) {

            uiNotif.info(error.data.message);
        });
    };
}

/**
 * Controlleur d'édition des permissions des profiles
 */
angular.module('app')
/*
 * récupérer les données de tous les noeuds sélectionner
 * dans la jstree
 */
    .directive("getchecked", function () {
        return {
            restrict: "A",
            link: function (scope, elem) {
                $(elem).on('changed.jstree', function (e, data) {
                    var i, size = data.selected.length, checkArrayFeature = [];
                    for (i = 0; i < size; i++) {
                        checkArrayFeature.push(data.instance.get_node(data.selected[i]).data);
                    }
                    scope.selected.authorities = checkArrayFeature;
                });
            }
        }
    })
    .controller('ProfilesControllerPermission', profilesControllerPermission)
;
