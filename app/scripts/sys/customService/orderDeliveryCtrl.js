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
        $scope.q = {
            orderStatus:null
        };


        $scope.vm = {
            taskList:[],
            orderList: [],
            orderStatus:[{key:0,value:"待派送"},{key:1,value:"派送中"}],
            orderStatusDisplay:["待派送","派送中","待核单","已完成","已作废"]
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchTaskOrder();
        };

        $scope.initPopUp = function () {

        };
//订单状态查询改变
        $scope.orderStatusSearchChange = function () {
            if ($scope.q.orderStatus==null) {
                return;
            };
            $scope.pager.setCurPageNo(1);
            searchTaskOrder();
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


        //强派订单
        $scope.assign = function (order) {
            udcModal.show({
                templateUrl: "./customService/assignModal.htm",
                controller: "AssignModalCtrl",
                inputs: {
                    title: '强派订单',
                    initVal: order
                }
            }).then(function (result) {
                if (result) {
                    $scope.orderStatusSearchChange();
                }
            })
        };

        //转派订单
        $scope.transfer = function (order) {
            udcModal.show({
                templateUrl: "./customService/assignModal.htm",
                controller: "AssignModalCtrl",
                inputs: {
                    title: '转派订单',
                    initVal: order
                }
            }).then(function (result) {
                if (result) {
                    $scope.orderStatusSearchChange();
                }
            })
        };


        //$scope.delete = function (order) {
        //    udcModal.confirm({"title": "作废", "message": "是否作废该订单信息,订单编号: " + order.orderSn})
        //        .then(function (result) {
        //            if (result) {
        //                OrderService.ordelCancel(order.orderSn).then(function (value) {
        //                    searchTaskOrder();
        //                    udcModal.info({"title": "处理结果", "message": "作废订单成功"});
        //                }, function(value) {
        //                    udcModal.info({"title": "处理结果", "message": "作废订单失败 "+value.message});
        //                })
        //            }
        //        })
        //};

        var searchTaskOrder = function () {
            //查询需要进行强制派单的订单
            var queryParams = {
                orderStatus:$scope.q.orderStatus.key,//待派送,配送中
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
            //初始化为待配送订单
            $scope.q.orderStatus = $scope.vm.orderStatus[0],
            searchTaskOrder();
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
