/**
 * Created by Administrator on 2018/4/10.
 */
'use strict';
customServiceApp.controller('DeleteGasTicketModalCtrl', ['$scope', 'close', 'CustomerManageService', 'title', 'initVal','udcModal','sessionStorage','pager',function ($scope, close, CustomerManageService, title, initVal, udcModal,sessionStorage,pager) {
    $scope.modalTitle = title;

    var gotoPage = function (pageNo) {
        $scope.pager.setCurPageNo(pageNo);
        $scope.searchTickets();
    };

    var gotoPagePagerCoupon = function (pageNo) {
        $scope.pagerCoupon.setCurPageNo(pageNo);
        $scope.searchCoupons();
    };

    $scope.pager = pager.init('DeleteGasTicketModalCtrl', gotoPage);
    $scope.pagerCoupon = pager.init('DeleteGasTicketModalCtrl', gotoPagePagerCoupon);

    var historyQ = $scope.pager.getQ();

    $scope.vm = {
        currentCustomer: {
            userId:null,
        },
        currentCustomerTickets: {},
        currentCustomerCoupons:{},
    };

    $scope.close = function (result) {
        close(result, 500);
    };

    var init = function () {
        $scope.pager.pageSize=5;
        $scope.pagerCoupon.pageSize=5;
        $scope.vm.currentCustomer = _.clone(initVal);

        $scope.searchTickets();
        $scope.searchCoupons();
    };
//查找气票
    $scope.searchTickets= function(){
        var queryParams = {
            customerUserId:$scope.vm.currentCustomer.userId,
            useStatus:0,//未使用
            pageNo: $scope.pager.getCurPageNo(),
            pageSize: $scope.pager.pageSize
        };

        CustomerManageService.searchTicket(queryParams).then(function (tickets) {
            $scope.pager.update($scope.q, tickets.total, queryParams.pageNo);
            $scope.vm.currentCustomerTickets = tickets.items;
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
//查找优惠券
    $scope.searchCoupons= function(){
        var queryParams = {
            customerUserId:$scope.vm.currentCustomer.userId,
            useStatus:0,//未使用
            pageNo: $scope.pagerCoupon.getCurPageNo(),
            pageSize: $scope.pagerCoupon.pageSize
        };

        CustomerManageService.searchCoupon(queryParams).then(function (coupons) {
            $scope.pagerCoupon.update($scope.q, coupons.total, queryParams.pageNo);
            $scope.vm.currentCustomerCoupons = coupons.items;

        });
    };

    //删除优惠券
    $scope.deleteCoupons = function (couponDetail) {
        udcModal.confirm({"title": "删除优惠券", "message": "是否永久删除该优惠券，规格为：" + couponDetail.specName})
            .then(function (result) {
                if (result) {
                    CustomerManageService.deleteCoupon(couponDetail).then(function () {
                        udcModal.info({"title": "处理结果", "message": "删除优惠券成功 "});

                        $scope.searchCoupons();
                    }, function(value) {
                        udcModal.info({"title": "处理结果", "message": "删除优惠券失败 "+value.message});
                    })
                }
            })
    };

    init();
}])