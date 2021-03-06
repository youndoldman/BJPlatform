'use strict';
shopManageApp.controller('SecurityDealModalCtrl', ['$scope', 'close', 'MendSecurityComplaintService', 'title', 'initVal','udcModal', 'sessionStorage',function ($scope, close, MendSecurityComplaintService, title, initVal,udcModal,sessionStorage) {
    $scope.modalTitle = title;
    $scope.vm = {
        selectedUser:null,
        userList:[],
        security: {}
    };

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (security) {
        if ((security.resloveInfo != null) && (security.resloveInfo != "")) {

            security.processStatus = "PTSolved";//将安检单标记成已处理
            security.dealedUser = {userId:$scope.vm.selectedUser};//本登陆用户处理
            MendSecurityComplaintService.modifySecurity(security).then(function () {
                udcModal.info({"title": "处理结果", "message": "安检单处理成功 "});
                $scope.close(true);
            }, function (value) {
                udcModal.info({"title": "处理结果", "message": "安检单处理失败 " + value.message});
            })
        }
    };

        var init = function () {
            $scope.vm.security = _.clone(initVal);
            if(title == "安检单处理") {
                $scope.isModify = true;
            }
            else {
                $scope.isModify = false;
            }

            searchUsers();
        };

    //查询本部门员工
    var searchUsers = function () {
        var searchParam = {
            departmentCode:$scope.vm.security.department.code
        };
        MendSecurityComplaintService.retrieveUsers(searchParam).then(function (users) {
            $scope.vm.userList = users.items;
            $scope.vm.selectedUser = $scope.vm.userList[0].userId;
        });
    };


    init();
    }]);