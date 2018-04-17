/**
 * Service de la gestion de l'authentification
 *
 * @param $resource
 * @returns
 */
angular.module("app")
    .factory("Authservice", function ($http) {
        var response = {};

        var headers = {
            'Content-Type': 'application/x-www-form-urlencoded',
            "Authorization": "Basic d2ViX2FwcDp3ZWJfYXBw"
        };

        response.returnData = function (requestData) {
            return $http({
                url: 'oauth/token',
                method: 'post',
                data: requestData,
                headers: headers,
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj) {
                        if (obj.hasOwnProperty(p)) {
                            str.push(encodeURIComponent(p) + '=' + encodeURIComponent(obj[p]));
                        }
                    }
                    return str.join('&');
                }
            });
        };

        return response;
    });