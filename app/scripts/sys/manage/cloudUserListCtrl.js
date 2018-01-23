'use strict';

manageApp.controller('CloudUserListCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'CloudUserService', function ($scope, $rootScope, $filter, $location, Constants,
                                                                   rootService, pager, udcModal, CloudUserService) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchCloudUsers();
        };
        $scope.pager = pager.init('CloudUserListCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            cloudUserId: historyQ.cloudUserId || ""
        };

        $scope.vm = {
            cloudUserList: []
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchCloudUsers();
        };

        $scope.initPopUp = function () {
            udcModal.show({
                templateUrl: "./manage/cloudUserModal.htm",
                controller: "CloudUserModalCtrl",
                inputs: {
                    title: '新增云客服账号',
                    initVal: {}
                }
            }).then(function (result) {
                if (result) {
                    searchCloudUsers();
                }
            })
        };



        $scope.modify = function (cloudUser) {
            udcModal.show({
                templateUrl: "./manage/cloudUserModal.htm",
                controller: "CloudUserModalCtrl",
                inputs: {
                    title: '修改云客服账号',
                    initVal: cloudUser
                }
            }).then(function (result) {
                if (result) {
                    searchCloudUsers();
                }
            })
        };

        $scope.delete = function (cloudUser) {
            udcModal.confirm({"title": "删除", "message": "是否永久删除云客服帐号信息 " + cloudUser.code})
                .then(function (result) {
                    if (result) {
                        CloudUserService.deleteCloudUser(cloudUser).then(function () {
                            search();
                        });
                    }
                })
        };

        var searchCloudUsers = function () {
            var queryParams = {
                cloudUserId: $scope.q.cloudUserId,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            CloudUserService.retrieveCloudUsers(queryParams).then(function (cloudUsers) {
                $scope.pager.update($scope.q, cloudUsers.total, queryParams.pageNo);
                $scope.vm.cloudUserList = cloudUsers.items;
            });


        };



        var init = function () {
            //查询云客服账号
            searchCloudUsers();
        };

        init();

    }]);

manageApp.controller('CloudUserModalCtrl', ['$scope', 'close', 'CloudUserService', 'title', 'initVal', 'udcModal',function ($scope, close, CloudUserService, title, initVal, udcModal) {
    $scope.modalTitle = title;
    $scope.isModify = false;
    $scope.isBinded = false;//是否绑定
    $scope.vm = {
        cloudUser: {},
    };

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (cloudUser) {
        if (!$scope.isModify) {
            CloudUserService.createCloudUser(cloudUser).then(function () {
                $scope.close(true);
            })
        } else {
            //去除百江用户字段，才能提交
            cloudUser.panvaUser = null;
            CloudUserService.modifyCloudUser(cloudUser).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改云客服信息成功"});
                $scope.close(true);
            })
        }
    };

    var init = function () {
        $scope.vm.cloudUser = _.clone(initVal);
        if (title == "修改云客服账号"){
            $scope.isModify = true;
        } else {
            $scope.isModify = false;
        }
        //没有绑定
        if ($scope.vm.cloudUser.panvaUser==null){
            $scope.isBinded = false;
        } else{
            $scope.isBinded = true;
        }

    };
    //解除绑定
    $scope.unBind = function () {
        CloudUserService.unBindUser($scope.vm.cloudUser, $scope.vm.cloudUser.panvaUser.userId).then(function () {
            $scope.vm.cloudUser.panvaUser = null;
            $scope.isBinded = false;
            udcModal.info({"title": "处理结果", "message": "解除绑定成功 "});
        }, function(value) {
            udcModal.info({"title": "错误信息", "message": value.message});
        })
    };
    //绑定
    $scope.bind = function () {
        CloudUserService.bindUser($scope.vm.cloudUser, $scope.vm.cloudUser.panvaUser.userId).then(function () {
            $scope.isBinded = true;
            udcModal.info({"title": "处理结果", "message": "绑定成功 "});
        }, function(value) {
            udcModal.info({"title": "错误信息", "message": value.message});
        })
    };

    init();
}]);