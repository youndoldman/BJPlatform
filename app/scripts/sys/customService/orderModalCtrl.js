'use strict';

customServiceApp.controller('OrderModalCtrl', ['$scope', 'close', 'OrderService', 'title', 'initVal','udcModal',function ($scope, close, OrderService, title, initVal, udcModal) {
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
            OrderService.modifyOrder(order).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改订单信息成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "修改订单信息失败 "+value.message});
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