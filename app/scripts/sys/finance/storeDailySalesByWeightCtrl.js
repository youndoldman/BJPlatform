/**
 * Created by Administrator on 2018/3/28.
 */
'use strict';

financeApp.controller('storeDailySalesByWeightCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'sessionStorage','FinanceService','$interval',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                                rootService, pager, udcModal,sessionStorage,FinanceService,$interval) {





        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
        };

        $scope.pager = pager.init('storeDailySalesByWeightCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.data = {
            customerTypesList:[],
        };

        $scope.q = {

        };


        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
        };


        $scope.printPage = function () {
            window.print();
        };



        var init = function () {
            var queryParams = {};
            FinanceService.searchCustomerType(queryParams).then(function (customerTypes) {
                $scope.data.customerTypesList = customerTypes.items;
            });
        };
        init();



//监听来电事件
        $scope.$on("Event_DailySalesQuery", function(e, m) {
            $scope.search();
        });
    }]);
