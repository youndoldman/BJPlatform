'use strict';

customServiceApp.controller('OrderDeliveryCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'OrderService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal, OrderService,sessionStorage) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchTaskOrder();
        };


        $scope.pager = pager.init('OrderCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();



        $scope.vm = {
            taskList:[],
            orderList: [],
            orderStatusDisplay:["待派送","派送中","待核单","已完成","已作废"]
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchTaskOrder();
        };

        $scope.initPopUp = function () {

        };

        $scope.viewDetails = function (order) {
            udcModal.show({
                templateUrl: "./customService/orderModal.htm",
                controller: "OrderModalCtrl",
                inputs: {
                    title: '订单详情',
                    initVal: order
                }
            }).then(function (result) {
            })
        };

        $scope.modify = function (order) {
            udcModal.show({
                templateUrl: "./customService/orderModal.htm",
                controller: "OrderModalCtrl",
                inputs: {
                    title: '修改订单',
                    initVal: order
                }
            }).then(function (result) {
                if (result) {
                    searchTaskOrder();
                }
            })
        };

        //指派订单
        $scope.assign = function (order) {
            udcModal.show({
                templateUrl: "./customService/assignModal.htm",
                controller: "AssignModalCtrl",
                inputs: {
                    title: '指派订单',
                    initVal: order
                }
            }).then(function (result) {
                if (result) {
                    $scope.orderStatusSearchChange();
                }
            })
        };

        $scope.delete = function (order) {
            udcModal.confirm({"title": "作废", "message": "是否作废该订单信息,订单编号: " + order.orderSn})
                .then(function (result) {
                    if (result) {
                        OrderService.ordelCancel(order.orderSn).then(function (value) {
                            searchTaskOrder();
                            udcModal.info({"title": "处理结果", "message": "作废订单成功"});
                        }, function(value) {
                            udcModal.info({"title": "处理结果", "message": "作废订单失败 "+value.message});
                        })
                    }
                })
        };

        var searchTaskOrder = function () {
            //查询需要进行强制派单的订单
            var queryParams = {
                orderStatus:0,//待派送
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };
            console.info(queryParams);
            var curUser = sessionStorage.getCurUser();
            OrderService.retrieveTaskOrders(curUser.userId, queryParams).then(function (tasks) {
                $scope.pager.update($scope.q, tasks.total, queryParams.pageNo);
                $scope.vm.taskList = tasks.items;
                //任务到订单数组的结构转换
                $scope.vm.orderList= _.map(tasks.items, OrderService.toViewModelTaskOrders);;

            });
        };

        var init = function () {
            searchTaskOrder();
        };
        init();

    }]);
