'use strict';

shopManageApp.controller('OrderCheckCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'OrderCheckService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal, OrderCheckService,sessionStorage) {
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
            taskList:[],
            orderList: [],
            orderStatus:[{key:null,value:"全部订单"},{key:0,value:"待派送"},{key:1,value:"派送中"},{key:2,value:"已签收"},
                {key:3,value:"已完成"},{key:4,value:"作废"}],
        };
        $scope.q = {
            startTime:null,
            endTime:null,
            orderSn:null,
            callInPhone:null,
            userId:null,
            orderStatus:$scope.vm.orderStatus[3],
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
                    searchOrder();
                }
            })
        };

        var searchOrder = function () {
            var queryParams = {
                startTime:$scope.q.startTime,
                endTime:$scope.q.endTime,
                callInPhone:$scope.q.callInPhone,
                userId:$scope.q.userId,
                orderSn:$scope.q.orderSn,
                orderStatus:$scope.q.orderStatus.key,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };
            console.log(queryParams);

            OrderCheckService.retrieveOrders(queryParams).then(function (orders) {
                $scope.pager.update($scope.q, orders.total, queryParams.pageNo);
                $scope.vm.orderList = orders.items;
            });
        };


        //订单状态查询改变
        $scope.orderStatusSearchChange = function () {
            if ($scope.q.orderStatus==null) {
                return;
            };
            if($scope.q.orderStatus.key==2){
            //查询需要进行确认的订单
                var queryParams = {
                    pageNo: $scope.pager.getCurPageNo(),
                    pageSize: $scope.pager.pageSize
                };
                var curUser = sessionStorage.getCurUser();
                OrderCheckService.retrieveTaskOrders(curUser.userId).then(function (tasks) {
                    $scope.pager.update($scope.q, tasks.total, queryParams.pageNo);
                    $scope.vm.taskList = tasks.items;
                    //任务到订单数组的结构转换
                    $scope.vm.orderList= _.map(tasks.items, OrderCheckService.toViewModelTaskOrders);;

                });

            } else{
                searchOrder();
            }

        };
        var init = function () {
            $scope.orderStatusSearchChange();
        };

        init();


    }]);
