'use strict';
shopManageApp.controller('MendDealModalCtrl', ['$scope', 'close', 'MendSecurityComplaintService', 'title', 'initVal','udcModal', 'sessionStorage',function ($scope, close, MendSecurityComplaintService, title, initVal,udcModal,sessionStorage) {
    $scope.modalTitle = title;
    $scope.vm = {
        selectedUser:null,
        userList:[],
        mend: {}
    };

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (mend) {
        if ((mend.resloveInfo != null) && (mend.resloveInfo != "")) {

            mend.processStatus = "PTSolved";//将报修单标记成已处理
            mend.dealedUser = {userId:$scope.vm.selectedUser};//本登陆用户处理
            MendSecurityComplaintService.modifyMend(mend).then(function () {
                udcModal.info({"title": "处理结果", "message": "报修单处理成功 "});
                $scope.close(true);
            }, function (value) {
                udcModal.info({"title": "处理结果", "message": "报修单处理失败 " + value.message});
            })
        }
    };

        var init = function () {
            $scope.vm.mend = _.clone(initVal);
            if(title == "报修单处理") {
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
            departmentCode:$scope.vm.mend.department.code
        };
        MendSecurityComplaintService.retrieveUsers(searchParam).then(function (users) {
            $scope.vm.userList = users.items;
            $scope.vm.selectedUser = $scope.vm.userList[0].userId;
        });
    };


        init();
    }]);