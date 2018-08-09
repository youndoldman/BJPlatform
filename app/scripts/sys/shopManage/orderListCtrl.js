'use strict';

shopManageApp.controller('OrderListCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'OrderCheckService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal, OrderService,sessionStorage) {
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
        //清除时间范围
        $scope.deleteTimeRange = function () {
            $scope.q.startTime = null;
            $scope.q.endTime = null;
        };



        $scope.pager = pager.init('OrderCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();



        $scope.vm = {
            curUser:null,
            taskList:[],
            orderList: [],

            payStatus: [{key:null,value:"全部状态"},{key:"PSUnpaid",value:"待支付"},{key:"PSPaied",value:"已支付"}
                ,{key:"PSRefounding",value:"退款中"},{key:"PSRefounded",value:"已退款"}],
            accessType: [{key:null,value:"全部来源"},{key:"ATWeixin",value:"微信"},{key:"ATCustomService",value:"客服"}],

            payType: [{key:null,value:"全部方式"},{key:"PTOnLine",value:"扫码支付"},{key:"PTCash",value:"现金支付"}
                ,{key:"PTDebtCredit",value:"赊销"},{key:"PTMonthlyCredit ",value:"月结"}],
            orderStatus:[{key:null,value:"全部订单"},{key:0,value:"待派送"},{key:1,value:"派送中"},{key:2,value:"待核单"},
                {key:3,value:"已完成"},{key:4,value:"已作废"}],
            orderStatusDisplay:["待派送","派送中","待核单","已完成","已作废"],
            orderServiceQuality:[{key:null,value:"全部评价"},{key:"OSQPositive",value:"不满意"},{key:"OSQNegative",value:"满意"}],
        };
        $scope.q = {
            dispatcherId:null,
            startTime:null,
            endTime:null,
            orderSn:null,
            callInPhone:null,
            userId:null,
            orderStatus:$scope.vm.orderStatus[0],
            accessType:$scope.vm.accessType[0],
            payStatus:$scope.vm.payStatus[0],
            payType:$scope.vm.payType[0],
            orderServiceQuality:$scope.vm.orderServiceQuality[0],

        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchOrder();
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

        $scope.modify = function (order) {
            udcModal.show({
                templateUrl: "./shopManage/orderCheckModal.htm",
                controller: "OrderCheckModalCtrl",
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
                            searchOrder();
                            udcModal.info({"title": "处理结果", "message": "作废订单成功"});
                        }, function(value) {
                            udcModal.info({"title": "处理结果", "message": "作废订单失败 "+value.message});
                        })
                    }
                })
        };

        var searchOrder = function () {
            var queryParams = {
                departmentCode:$scope.vm.curUser.department.code,
                startTime:$scope.q.startTime,
                endTime:$scope.q.endTime,
                dispatcherId:$scope.q.dispatcherId,

                callInPhone:$scope.q.callInPhone,
                userId:$scope.q.userId,
                orderSn:$scope.q.orderSn,
                orderStatus:$scope.q.orderStatus.key,
                accessType:$scope.q.accessType.key,
                payStatus:$scope.q.payStatus.key,
                payType:$scope.q.payType.key,
                orderServiceQuality:$scope.q.orderServiceQuality.key,

                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
                orderBy:"id desc"
            };

            OrderService.retrieveOrders(queryParams).then(function (orders) {
                $scope.pager.update($scope.q, orders.total, queryParams.pageNo);
                $scope.vm.orderList = orders.items;
            });
        };


        var init = function () {
            $scope.vm.curUser = sessionStorage.getCurUser();
            searchOrder();
        };

        init();

        //订单状态查询改变
        $scope.orderStatusSearchChange = function () {
            if ($scope.q.orderStatus==null) {
                return;
            };
            $scope.pager.setCurPageNo(1);
            searchOrder();
        };
        //订单来源查询改变
        $scope.accessTypeSearchChange = function () {
            if ($scope.q.accessType==null) {
                return;
            };
            $scope.pager.setCurPageNo(1);
            searchOrder();
        };
        //支付状态查询改变
        $scope.payStatusSearchChange = function () {
            if ($scope.q.payStatus==null) {
                return;
            };
            $scope.pager.setCurPageNo(1);
            searchOrder();
        };
        //支付方式查询改变
        $scope.payTypeSearchChange = function () {
            if ($scope.q.payType==null) {
                return;
            };
            $scope.pager.setCurPageNo(1);
            searchOrder();
        };
        //用户评价查询改变
        $scope.orderServiceQualitySearchChange = function () {
            if ($scope.q.orderServiceQuality==null) {
                return;
            };
            $scope.pager.setCurPageNo(1);
            searchOrder();
        };


    }]);
