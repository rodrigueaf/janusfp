/**
 * Controlleur de gestion des comptes utilisateur
 */
angular.module('app')
    .controller('AccountController',
        ['$scope', '$state', '$cookies', '$stateParams', 'AccountService', 'uiNotif', 'Authservice',
            function ($scope, $state, $cookies, $stateParams, AccountService, uiNotif, Authservice) {

                /**
                 * Initialisation des données
                 */
                var initKeyAndPasswordVM = {
                    key: $state.params.key,
                    newPassword: '',
                    confirmPassword: ''
                };

                $cookies.remove('token');
                $cookies.remove('refresh_token');

                $scope.keyAndPasswordVM = angular.copy(initKeyAndPasswordVM);

                /*********************************************************************************/
                /**
                 * Activation du compte
                 */
                $scope.reset = function () {

                    if ($scope.keyAndPasswordVM.newPassword === $scope.keyAndPasswordVM.confirmPassword) {
                        $scope.promise = AccountService.resetPassword($scope.keyAndPasswordVM).$promise;
                        $scope.promise.then(function (response) {

                            var dataForPasswordGrantType = {
                                username: response.data,
                                password: $scope.keyAndPasswordVM.newPassword,
                                grant_type: "password"
                            };

                            Authservice.returnData(dataForPasswordGrantType)
                                .then(function (data) {
                                    var accessToken = data.data["access_token"];
                                    var refreshToken = data.data["refresh_token"];

                                    /*
                                     * on stocke le jeton dans le cookie
                                     */
                                    $cookies.put('token', accessToken);
                                    $cookies.put('refresh_token', refreshToken);

                                    $state.go("home");
                                });

                        }, function (error) {
                            uiNotif.info(error.message);
                        });
                    } else {
                        $scope.msgError = 'Les mots de passe ne correspondent pas';
                    }
                };
                /*********************************************************************************/
            }
        ]);