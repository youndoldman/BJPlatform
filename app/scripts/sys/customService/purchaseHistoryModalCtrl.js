'use strict';

customServiceApp.controller('purchaseHistoryModalCtrl', ['$scope', 'close', 'OrderService', 'title', 'initVal','udcModal','pager','CustomerManageService',function ($scope, close, OrderService, title, initVal, udcModal,pager,CustomerManageService) {
    $scope.modalTitle = title;

    var gotoPage = function (pageNo) {
        $scope.pager.setCurPageNo(pageNo);
        searchOrder();
    };
    $scope.pager = pager.init('purchaseHistoryModalCtrl', gotoPage);
    var historyQ = $scope.pager.getQ();
    $scope.vm = {
        customer:{},
        orderList:[],
        currentCustomerCredit:{}
    };


    $scope.close = function (result) {
        close(result, 500);
    };


    var init = function () {
        $scope.vm.customer = _.clone(initVal);
        $scope.pager.pageSize=5;
        $scope.pager.setCurPageNo(1);
        searchOrder();
        retrieveCustomerCredit();

    };

    var searchOrder = function () {
        var queryParams = {
            userId:$scope.vm.customer.userId,
            pageNo: $scope.pager.getCurPageNo(),
            pageSize: $scope.pager.pageSize,
            orderStatus:3,
            orderBy:"id desc"
        };

        OrderService.retrieveOrders(queryParams).then(function (orders) {
            $scope.pager.update($scope.q, orders.total, queryParams.pageNo);
            $scope.vm.orderList = orders.items;

        }, function(value) {
            udcModal.info({"title": "错误信息", "message": "查询当前用户消费记录失败 "+value.message});
        })
    };
//查询当前用户欠款
    var retrieveCustomerCredit = function(){
        var queryParams = {
            userId:$scope.vm.customer.userId
        };
        CustomerManageService.retrieveCustomerCredit(queryParams).then(function (credit) {
            var CreditList = credit.items;
            if(CreditList.length==0){
                $scope.vm.currentCustomerCredit = 0+"元";
            }else{
                $scope.vm.currentCustomerCredit = credit.items[0].amount+"元";
            }

        }, function(value) {
            udcModal.info({"title": "错误信息", "message": "查询当前用户欠款信息失败 "+value.message});
        })
    };

    init();
}]);