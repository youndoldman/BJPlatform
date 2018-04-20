'use strict';
shopManageApp.controller('ComplaintDealModalCtrl', ['$scope', 'close', 'MendSecurityComplaintService', 'title', 'initVal','udcModal', 'sessionStorage',function ($scope, close, MendSecurityComplaintService, title, initVal,udcModal,sessionStorage) {
    $scope.modalTitle = title;
    $scope.vm = {
        complaint: {}
    };

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (complaint) {
        if ((complaint.resloveInfo != null) && (complaint.resloveInfo != "")) {

            complaint.processStatus = "PTSolved";//将报修单标记成已处理
            var curUser = sessionStorage.getCurUser();
            complaint.dealedUser = {userId:curUser.userId};//本登陆用户处理
            MendSecurityComplaintService.modifyComplaint(complaint).then(function () {
                udcModal.info({"title": "处理结果", "message": "投诉单处理成功 "});
                $scope.close(true);
            }, function (value) {
                udcModal.info({"title": "处理结果", "message": "投诉单处理失败 " + value.message});
            })
        }
    };

        var init = function () {
            $scope.vm.complaint = _.clone(initVal);
            if(title == "投诉单处理") {
                $scope.isModify = true;
            }
            else {
                $scope.isModify = false;
            }
        };

        init();
    }]);