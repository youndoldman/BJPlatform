/**
 * Created by Administrator on 2018/5/22.
 */
'use strict';
shopManageApp.controller('MendBillCtrl', ['$scope', 'close', 'MendSecurityComplaintService', 'title', 'initVal','udcModal', 'sessionStorage',function ($scope, close, MendSecurityComplaintService, title, initVal,udcModal,sessionStorage) {
    $scope.modalTitle = title;
    $scope.vm = {
        mend: {}
    };

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.printPage = function () {
        window.print();
    };


    var init = function () {

    };

    init();
}]);