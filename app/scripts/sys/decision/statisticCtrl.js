'use strict';

decisionApp.controller('StatisticCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'DecisionService','sessionStorage', '$interval',function ($scope, $rootScope, $filter, $location, Constants,

                                                                                                  rootService, pager, udcModal, DecisionService, sessionStorage,$interval) {


        $scope.q = {
            code: null,
            startTime:"",
            endTime:"",

        };

        $scope.vm = {
            dataDealed:[],
            unitData : {legend:null, items:[]},

            //订单来源图表
            orderAccessTypeChart:null,
            orderAccessTypeOption:{
                title: {
                    text: '订单来源统计'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{b} : {c} ({d}%)"
                },
                "textStyle": {
                    "fontSize": 18
                },
                series : [
                    {
                        type: 'pie',
                        radius : '65%',
                        center: ['50%', '50%'],
                        data:[],
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            },
            //订单支付方式图表
            orderPayTypeChart:null,
            orderPayTypeOption:{
                title: {
                    text: '支付方式统计'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{b} : {c} ({d}%)"
                },
                "textStyle": {
                    "fontSize": 18
                },
                series : [
                    {
                        type: 'pie',
                        radius : '65%',
                        center: ['50%', '50%'],
                        data:[],
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            },
            //日订单数量统计图表
            orderDayCountChart:null,
            orderDayCountOption: {
                title: {
                    text: '日订单统计'
                },
                toolbox: {
                    show : true,
                    feature : {
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                tooltip: {
                    trigger: 'axis',
                    formatter: function (params) {
                        params = params[0];
                        return params.value[0] + ' : ' + params.value[1];
                    },
                    axisPointer: {
                        animation: true
                    }
                },
                xAxis: {
                    type: 'time',
                    splitLine: {
                        show: true
                    }
                },
                yAxis: {
                    type: 'value',
                    boundaryGap: [0, '100%'],
                    splitLine: {
                        show: true
                    }
                },
                // 数据内容数组
                series:[{
                    legend:"订单统计",
                    name: "订单统计",
                    type: 'bar',
                    data: [],
                    showSymbol: false,
                    hoverAnimation: false
                }]
            }





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
            function p(s) {
                return s < 10 ? '0' + s: s;
            }

            $('#datetimepickerStart').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.startTime = date.getFullYear()+"-"+p(month)+"-"+p(date.getDate())+" "
                        +p(date.getHours())+":"+p(date.getMinutes())+":"+p(date.getSeconds());
                });
            $('#datetimepickerEnd').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.endTime = date.getFullYear()+"-"+p(month)+"-"+p(date.getDate())+" "
                        +p(date.getHours())+":"+p(date.getMinutes())+":"+p(date.getSeconds());
                });
        });
        //查询订单来源进行统计
        var searchOrderAccessType = function () {
            //微信
            orderAccessQuery("ATWeixin", "微信");
            //客服
            orderAccessQuery("ATCustomService", "客服");
        };

        //查询订单结算进行统计
        var searchOrderPayType = function () {

            //微信线上支付
            orderPayQuery("PTOnLine", "微信线上支付");
            orderPayQuery("PTCash", "现金支付");
            orderPayQuery("PTDebtCredit", "赊销支付");
            orderPayQuery("PTMonthlyCredit", "月结支付");
            orderPayQuery("PTTicket", "气票支付");
            orderPayQuery("PTWxOffLine", "微信线下支付");
        };

        //查询日订单数量进行统计
        var searchOrderDayCount = function () {
            var dateStart = new Date($scope.q.startTime);
            var dateEnd = new Date($scope.q.endTime);

            var tempDate =  new Date(dateStart);
            while(1){
                var month = tempDate.getMonth()+1;
                var startTime = tempDate.getFullYear()+"-"+month+"-"+tempDate.getDate()+" 00:00:00";
                var endTime = tempDate.getFullYear()+"-"+month+"-"+tempDate.getDate()+" 23:59:59";

                orderDayCountQuery(startTime, endTime);
                if((tempDate.getFullYear()==dateEnd.getFullYear())&&
                    (tempDate.getMonth()==dateEnd.getMonth())&&
                    (tempDate.getDate()==dateEnd.getDate())){
                    return;

                }
                tempDate.setDate(tempDate.getDate()+1);
            }


            //微信线上支付
            orderPayQuery("PTOnLine", "微信线上支付");
            orderPayQuery("PTCash", "现金支付");
            orderPayQuery("PTDebtCredit", "赊销支付");
            orderPayQuery("PTMonthlyCredit", "月结支付");
            orderPayQuery("PTTicket", "气票支付");
            orderPayQuery("PTWxOffLine", "微信线下支付");
        };

        var init = function () {
            //初始化为当前时间
            //初始化时间为当天
            var date = new Date();
            var month = date.getMonth()+1;
            $scope.q.startTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" 00:00:00";
            $scope.q.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" 23:59:59";
            chartInitial();

            $scope.search();
        };

        var chartInitial = function () {
            $scope.vm.orderAccessTypeChart = echarts.init(document.getElementById('orderAccessTypeChart'));//p 标签id
            $scope.vm.orderPayTypeChart = echarts.init(document.getElementById('orderPayTypeChart'));//p 标签id
            $scope.vm.orderAccessTypeChart.setOption($scope.vm.orderAccessTypeOption);
            $scope.vm.orderPayTypeChart.setOption($scope.vm.orderPayTypeOption);

            $scope.vm.orderDayCountChart = echarts.init(document.getElementById('orderDayCountChart'));//p 标签id
            $scope.vm.orderDayCountChart.setOption($scope.vm.orderDayCountOption);


            $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                $scope.vm.orderAccessTypeChart.resize();
                $scope.vm.orderPayTypeChart.resize();
                $scope.vm.orderDayCountChart.resize();
            });
        };
        $scope.search = function () {
            $scope.vm.orderPayTypeOption.series[0].data = [];
            $scope.vm.orderAccessTypeOption.series[0].data = [];
            $scope.vm.orderDayCountOption.series[0].data = [];


            searchOrderAccessType();
            searchOrderPayType();
            searchOrderDayCount();
        };

        var orderAccessReflesh = function () {
            $scope.vm.orderAccessTypeChart.setOption($scope.vm.orderAccessTypeOption);
        };
        var orderPayReflesh = function () {
            $scope.vm.orderPayTypeChart.setOption($scope.vm.orderPayTypeOption);
        };
        var orderDayCountReflesh = function () {
            $scope.vm.orderDayCountChart.setOption($scope.vm.orderDayCountOption);
        };

        //订单来源查询
        var orderAccessQuery = function (AccessType, name) {
            var queryParams = {
                accessType:AccessType,
                startTime:$scope.q.startTime,
                endTime:$scope.q.endTime
            };
            DecisionService.retrieveOrdersCount(queryParams).then(function (count) {
                var tempData = {value:count, name: name};
                $scope.vm.orderAccessTypeOption.series[0].data.push(tempData);
                orderAccessReflesh();
            });
        };

        //订单支付情况查询
        var orderPayQuery = function (PayType, name) {
            var queryParams = {
                payType:PayType,
                startTime:$scope.q.startTime,
                endTime:$scope.q.endTime
            };
            DecisionService.retrieveOrdersCount(queryParams).then(function (count) {
                var tempData = {value:count, name: name};
                $scope.vm.orderPayTypeOption.series[0].data.push(tempData);
                orderPayReflesh();
            });
        };
        //日订单数量查询
        var orderDayCountQuery = function (startTime, endTime) {
            var queryParams = {
                startTime:startTime,
                endTime:endTime
            };
            DecisionService.retrieveOrdersCount(queryParams).then(function (count) {
                var tempData = {name:null,value:null};
                var date = new Date(queryParams.startTime);
                tempData.name = date.toString();
                tempData.value = [queryParams.startTime,count];


                $scope.vm.orderDayCountOption.series[0].data.push(tempData);
                orderDayCountReflesh();
            });
        };


        init();

    }]);
