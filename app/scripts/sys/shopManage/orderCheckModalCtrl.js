'use strict';

shopManageApp.controller('OrderCheckModalCtrl', ['$scope', 'close', 'OrderCheckService', 'title', 'initVal','udcModal','sessionStorage',function ($scope, close, OrderCheckService, title, initVal, udcModal,sessionStorage) {
    $scope.modalTitle = title;


    $scope.vm = {
        currentOrder: {
        },
        curUser:null
    };

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (order) {

        if ($scope.isModify) {
            var dealparams = {
                businessKey: $scope.vm.currentOrder.orderSn,
                candiUser   : $scope.vm.curUser.userId,
                orderStatus: 3
            };
            OrderCheckService.checkOrder(dealparams,$scope.vm.currentOrder.taskId).then(function () {
                udcModal.info({"title": "处理结果", "message": "订单确认成功："});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "订单确认失败 "+value.message});
                $scope.close(false);
            })
        }
    };

    var init = function () {
        $scope.vm.currentOrder = _.clone(initVal);
        if(title == "确认订单") {
            $scope.isModify = true;
        }
        else {
            $scope.isModify = false;
        }
        $scope.vm.curUser = sessionStorage.getCurUser();
    };
    init();

    //四舍五入，取两位小数
    $scope.getFixData = function (data) {
        if(data==null){
            return "";
        }
        var f =  Math.round(data * 100) / 100;
        var s = f.toString();
        var rs = s.indexOf('.');
        if (rs < 0) {
            rs = s.length;
            s += '.';
        }
        while (s.length <= rs + 2) {
            s += '0';
        }
        return s;

    };
}]);