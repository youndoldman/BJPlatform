'use strict';
shopManageApp.controller('MendDealModalCtrl', ['$scope', 'close', 'MendSecurityComplaintService', 'title', 'initVal','udcModal', 'sessionStorage',function ($scope, close, MendSecurityComplaintService, title, initVal,udcModal,sessionStorage) {
    $scope.modalTitle = title;
    $scope.vm = {
        mend: {}
    };

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (mend) {
        if ((mend.resloveInfo != null) && (mend.resloveInfo != "")) {

            mend.processStatus = "PTSolved";//将报修单标记成已处理
            var curUser = sessionStorage.getCurUser();
            mend.dealedUser = {userId:curUser.userId};//本登陆用户处理
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
        };

        init();
    }]);