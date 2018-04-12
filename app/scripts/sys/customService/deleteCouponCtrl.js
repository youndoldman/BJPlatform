/**
 * Created by Administrator on 2018/4/11.
 */
'use strict';
customServiceApp.controller('DeleteCouponModalCtrl', ['$scope', 'close', 'CustomerManageService', 'title', 'initVal','udcModal','sessionStorage',function ($scope, close, CustomerManageService, title, initVal, udcModal,sessionStorage) {
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
        CustomerManageService.searchCoupon(queryParams).then(function (coupons) {
            $scope.vm.currentCustomerTickets = coupons.items;
            console.info( $scope.vm.currentCustomerTickets);
            if($scope.vm.currentCustomerTickets.length==0)
            {
                udcModal.info({"title": "提示信息", "message": "该客户暂时没绑定的优惠券！"});
            }
        });
    }
    //删除气票
    $scope.deleteTicket = function (couponDetail) {
        udcModal.confirm({"title": "删除优惠券", "message": "是否永久删除该优惠券，规格为：" + couponDetail.specName})
            .then(function (result) {
                if (result) {
                    CustomerManageService.deleteCoupon(couponDetail).then(function () {
                        udcModal.info({"title": "处理结果", "message": "删除优惠券成功 "});
                        $scope.searchTickets();
                    }, function(value) {
                        udcModal.info({"title": "处理结果", "message": "删除优惠券失败 "+value.message});
                    })
                }
            })
    };

    init();
}])