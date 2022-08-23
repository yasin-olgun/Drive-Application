angular.module('my-app')
    .component('register',{
        templateUrl:'/app/template/register.html',
        controller: function($scope,$location,UserApi){

            $scope.register = function (){
                UserApi.register($scope.user);
                $location.path("/login");

            }

            $scope.init = function (){
            }

            $scope.init();
        }


    });



