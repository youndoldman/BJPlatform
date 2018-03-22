'use strict';

shopManageApp.controller('OrderCheckCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'OrderCheckService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal, OrderCheckService,sessionStorage) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
                //查询需要进行确认的订单
            searchTaskOrder();
        };



        $scope.pager = pager.init('OrderCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();



        $scope.vm = {
            taskList:[],
            orderList: [],
            orderStatus:[{key:null,value:"全部订单"},{key:0,value:"待派送"},{key:1,value:"派送中"},{key:2,value:"待核单"},
                {key:3,value:"已完成"},{key:4,value:"作废"}],
            orderStatusDisplay:["待派送","派送中","待核单","已完成","作废"]
        };
        $scope.q = {
            startTime:null,
            endTime:null,
            orderSn:null,
            callInPhone:null,
            userId:null,
            orderStatus:$scope.vm.orderStatus[3],
        };



        $scope.initPopUp = function () {

        };

        $scope.viewDetails = function (order) {
            udcModal.show({
                templateUrl: "./shopManage/orderCheckModal.htm",
                controller: "OrderCheckModalCtrl",
                inputs: {
                    title: '订单详情',
                    initVal: order
                }
            }).then(function (result) {
            })
        };

        $scope.check = function (order) {
            udcModal.show({
                templateUrl: "./shopManage/orderCheckModal.htm",
                controller: "OrderCheckModalCtrl",
                inputs: {
                    title: '确认订单',
                    initVal: order
                }
            }).then(function (result) {
                if (result) {
                    searchTaskOrder();
                }
            })
        };

        var searchTaskOrder = function () {
            //查询需要进行强制派单的订单
            var queryParams = {
                orderStatus:2,//用户已经签收
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };
            var curUser = sessionStorage.getCurUser();
            OrderCheckService.retrieveTaskOrders(curUser.userId, queryParams).then(function (tasks) {
                $scope.pager.update($scope.q, tasks.total, queryParams.pageNo);
                $scope.vm.taskList = tasks.items;
                //任务到订单数组的结构转换
                $scope.vm.orderList= _.map(tasks.items, OrderCheckService.toViewModelTaskOrders);;

            });
        };


        var init = function () {
            searchTaskOrder();
        };

        init();


    }]);
