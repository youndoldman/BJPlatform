'use strict';

shopManageApp.controller('OrderCheckModalCtrl', ['$scope', 'close', 'OrderCheckService', 'title', 'initVal','udcModal',function ($scope, close, OrderCheckService, title, initVal, udcModal) {
    $scope.modalTitle = title;


    $scope.vm = {
        currentOrder: {
        }
    };

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (order) {

        console.log(order);
        if ($scope.isModify) {
            var dealparams = {
                businessKey: $scope.vm.currentOrder.orderSn,
                candiUser   : "",
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
        if(title == "修改订单") {
            $scope.isModify = true;
        }
        else {
            $scope.isModify = false;
        }
        console.log($scope.vm.currentOrder);
    };
    init();
}]);