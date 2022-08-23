angular.module('my-app')
    .component('sharedWithMe', {
        templateUrl: '/app/template/shared-with-me.html',
        controller: function ($scope, $routeParams, UserApi, $location) {


            $scope.icons = {
                "rar": "bi bi-file-zip-fill",
                "jpg": "bi bi-filetype-jpg ",
                "png": "bi bi-filetype-png ",
                "doc": "bi bi-filetype-doc ",
                "xlsx": "bi bi-filetype-xlsx ",
                "csv": "bi bi-filetype-csv ",
                "pdf": "bi bi-filetype-pdf ",

            }
            $scope.init = function () {
                $scope.gridView = true;
                UserApi.sharedWithMe(function (response){
                    $scope.files = response;
                });
            };


            $scope.init();
        }

    });