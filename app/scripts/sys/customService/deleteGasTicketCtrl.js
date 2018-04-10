/**
 * Created by Administrator on 2018/4/10.
 */
'use strict';
customServiceApp.controller('DeleteGasTicketModalCtrl', ['$scope', 'close', 'CustomerManageService', 'title', 'initVal','udcModal','sessionStorage',function ($scope, close, CustomerManageService, title, initVal, udcModal,sessionStorage) {
    $scope.modalTitle = title;

    $scope.vm = {
        currentCustomer: {
            userId:null,
        },
        currentCustomerTickets: {
        }
    };

    //$scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    var init = function () {
        $scope.vm.currentCustomer = _.clone(initVal);
        //console.info($scope.vm.currentCustomer);
        $scope.searchTickets();
    };

    $scope.searchTickets= function(){
        var queryParams = {};
        queryParams.customerUserId = $scope.vm.currentCustomer.userId;
        CustomerManageService.searchTicket(queryParams).then(function (tickets) {
            $scope.vm.currentCustomerTickets = tickets.items;
            //console.info( $scope.vm.currentCustomerTickets);
            if($scope.vm.currentCustomerTickets.length==0)
            {
                udcModal.info({"title": "提示信息", "message": "该客户暂时没绑定的气票！"});
            }
        });
}
    //删除气票
    $scope.deleteTicket = function (ticketDetail) {
        udcModal.confirm({"title": "删除气票", "message": "是否永久删除该气票，编号为：" + ticketDetail.ticketSn})
            .then(function (result) {
                if (result) {
                    CustomerManageService.deleteTicket(ticketDetail).then(function () {
                        udcModal.info({"title": "处理结果", "message": "删除气票成功 "});
                        $scope.searchTickets();
                    }, function(value) {
                        udcModal.info({"title": "处理结果", "message": "删除气票失败 "+value.message});
                    })
                }
            })
    };

    init();
}])