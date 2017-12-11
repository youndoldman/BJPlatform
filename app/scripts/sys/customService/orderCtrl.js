'use strict';

customServiceApp.controller('OrderCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'OrderService', function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal, OrderService) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchOrder();
        };

        $(function () {

            $('#datetimepickerStart').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',

            });
            $('#datetimepickerEnd').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',

            });
        });
        $(function () {
            $('#datetimepickerStart').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.startTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#datetimepickerEnd').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
        });

        $scope.pager = pager.init('OrderCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();



        $scope.vm = {
            orderList: [],
            orderStatus:[{key:999,value:"全部订单"},{key:0,value:"待派送"},{key:1,value:"派送中"},{key:2,value:"已签收"},
                {key:3,value:"已完成"},{key:4,value:"已作废"}],
        };
        $scope.q = {
            startTime:null,
            endTime:null,
            orderSn:null,
            callInPhone:null,
            userId:null,
            orderStatus:$scope.vm.orderStatus[0],
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchOrder();
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
                    searchOrder();
                }
            })
        };

        $scope.delete = function (order) {
            udcModal.confirm({"title": "作废", "message": "是否作废该订单信息,订单编号: " + order.orderSn})
                .then(function (result) {
                    if (result) {
                        //OrderService.deleteCustomer(customer).then(function () {
                        //    searchCustomer();
                        //    udcModal.info({"title": "处理结果", "message": "删除客户信息成功 "});
                        //});
                    }
                })
        };

        var searchOrder = function () {
            var queryParams = {
                //startTime:$scope.q.startTime,
                //endTime:$scope.q.endTime,
                callInPhone:$scope.q.callInPhone,
                userId:$scope.q.userId,
                orderSn:$scope.q.orderSn,
                //orderStatus:$scope.q.orderStatus.key,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            OrderService.retrieveOrders(queryParams).then(function (orders) {
                $scope.pager.update($scope.q, orders.total, queryParams.pageNo);
                $scope.vm.orderList = orders.items;
            });
        };

        var init = function () {
            searchOrder();
        };

        init();

    }]);
