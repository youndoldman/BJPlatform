'use strict';

customServiceApp.controller('OrderCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'OrderService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
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
                    console.log("changed")
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
            taskList:[],
            orderList: [],

            payStatus: [{key:null,value:"全部状态"},{key:"PSUnpaid",value:"待支付"},{key:"PSPaied",value:"已支付"}
                ,{key:"PSRefounding",value:"退款中"},{key:"PSRefounded",value:"已退款"}],
            accessType: [{key:null,value:"全部来源"},{key:"ATWeixin",value:"微信"},{key:"ATCustomService",value:"客服"}],

            payType: [{key:null,value:"全部支付方式"},{key:"PTOnLine",value:"微信扫码线上支付"},{key:"PTCash",value:"现金支付"}
                ,{key:"PTDebtCredit",value:"赊销支付"},{key:"PTMonthlyCredit ",value:"月结支付"},{key:"PTTicket ",value:"气票支付"},{key:"PTWxOffLine ",value:"微信扫码线下支付"}],
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

                for(var i=0; i<$scope.vm.orderList.length; i++){
                    $scope.vm.orderList[i].deliveryTime = calDeliveryTime($scope.vm.orderList[i].orderStatus, $scope.vm.orderList[i].orderOpHistoryList)
                }
            });
        };


        var init = function () {
            //初始化时间为当天
            var date = new Date();
            var month = date.getMonth()+1;
            $scope.q.startTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" 00:00:00";
            $scope.q.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" 23:59:59";
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


        //发表状态改变
        $scope.invoiceStatusChanged = function (order) {
            var modifiedOrder = {orderSn:order.orderSn,invoiceStatus:null};
            var message = null;
            if(order.invoiceStatus.index==0){//没开发票
                modifiedOrder.invoiceStatus = "ISInvoiced";
                message = "确认开发票";

            }else{
                modifiedOrder.invoiceStatus = "ISUnInvoice";
                message = "取消发票记录";
            }
            udcModal.confirm({"title": "发票", "message": message+"？,订单编号: " + order.orderSn})
                .then(function (result) {
                    if (result) {
                        OrderService.modifyOrder(modifiedOrder).then(function (value) {
                            searchOrder();
                            udcModal.info({"title": "处理结果", "message": message+"成功"});
                        }, function(value) {
                            searchOrder();
                            udcModal.info({"title": "处理结果", "message": message+"失败 "+value.message});
                        })
                    }else{
                        searchOrder();
                    }
                })
        };

        //计算配送时间
        var calDeliveryTime = function (orderStatus, orderOpHistoryList) {
            var deliveryTime = "";
            if(orderStatus<2){
                return deliveryTime;
            }
            var startTime = null;
            var endTime = null;
            for(var j=0; j<orderOpHistoryList.length; j++){
                var itemorderOp = orderOpHistoryList[j];
                var indexOrderStatus = itemorderOp.orderStatus.index;
                if(indexOrderStatus==1){
                    startTime = Date.parse(new Date(itemorderOp.updateTime));
                }
                if(indexOrderStatus==2){
                    endTime = Date.parse(new Date(itemorderOp.updateTime));
                }
            }
            if(startTime!=null&&endTime!=null){
                var usedTime = endTime - startTime;  //两个时间戳相差的毫秒数
                deliveryTime = usedTime/(60*1000);
                deliveryTime = deliveryTime.toFixed(1)+"分钟";

            }
            return deliveryTime;
        };


    }]);
