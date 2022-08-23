angular.module('my-app')
    .component('login', {
        templateUrl: '/app/template/login.html',
        controller: function ($scope, $routeParams, UserApi, $location) {


            $scope.login = function () {
                UserApi.login($scope.loginRequest, function (authenticationResponse) {
                    if (authenticationResponse.code == 0) {
                        $location.path("/my-drive");
                    } else {

                        //pass
                    }
                });
            }

            $scope.init = function () {
                $scope.loginRequest = {};
            };

            $scope.init();
        }

    });