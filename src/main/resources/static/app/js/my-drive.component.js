angular.module('my-app')
    .component('myDrive', {
        templateUrl: '/app/template/my-drive.html',
        controller: function ($scope, UserApi, $uibModal,$scope, Upload) {

            $scope.icons = {
                "rar": "bi bi-file-zip-fill",
                "jpg": "bi bi-filetype-jpg ",
                "png": "bi bi-filetype-png ",
                "doc": "bi bi-filetype-doc ",
                "xlsx": "bi bi-filetype-xlsx ",
                "csv": "bi bi-filetype-csv ",
                "pdf": "bi bi-filetype-pdf ",

            }
            $scope.shareInModal = function (file) {


                var modalInstance = $uibModal.open({

                    templateUrl: '/app/template/share-modal.html',
                    controller: 'FileShareModalController',
                    size: 'md',
                    resolve: {
                        File: function () {
                            return angular.copy(file);
                        }
                    }
                });


            };

            $scope.fileSelected = function (file) {
                console.log($scope.file);
                if (file != null) {
                    UserApi.upload(file, function (data) {
                        $scope.files.push(data);
                    });
                }
            }

            $scope.delete = function (id) {
                UserApi.delete({id: id}, function (data) {
                    _.remove($scope.files, {id: id})
                });
            }


            $scope.init = function () {
                $scope.gridView = true;

                UserApi.files(function (response) {

                    $scope.files = response;

                    $scope.user = UserApi.getUser();
                });

                console.log("interval");
            };

            $scope.init();
//            $interval($scope.init(), 5000);

        }

    })
    .controller("FileShareModalController", function ($scope, $uibModalInstance, File, UserApi) {

        $scope.ok = function (username, fileId) { // userapi share
            UserApi.share({fileId: fileId, username: username}, {});
            // UserApi ile paylasim islemi
            $uibModalInstance.close();

        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.unshare = function (fileId, username) {
            UserApi.unshare({fileId: fileId, username: username}, {});
            _.remove($scope.users, {username: username})

        };


        $scope.init = function () {
            $scope.file = File;
            $scope.users = UserApi.userList($scope.file.sharedUserIds);
        };
        $scope.init();
    });