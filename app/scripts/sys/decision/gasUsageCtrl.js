'use strict';

decisionApp.controller('GasUsageCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'DecisionService','sessionStorage', '$interval',function ($scope, $rootScope, $filter, $location, Constants,

                                                                      rootService, pager, udcModal, DecisionService, sessionStorage,$interval) {
        $scope.q = {
            code: null,
            startTime:"",
            endTime:""
        };
        $scope.search = function () {
            //启动绘制托盘重量的定时器
            $interval.cancel($scope.timer);
            $scope.timer = $interval( function(){
                retrieveGasCynTrayHistory();
            }, 1000);
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
        var drawChart = function(dataDealed) {
            var legend = [];
            for (var i=0;i<dataDealed.length;i++) {
                legend.push(dataDealed[i].legend);
            }
            var option = {
                // 提示框，鼠标悬浮交互时的信息提示
                title: {
                    text: '托盘气量变化',
                    x:'left',
                    y:'top',

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

                // 图例
                legend: {
                    data: legend,
                    x:'center'
                },
                tooltip: {
                    trigger: 'axis',
                    formatter: function (params) {
                        params = params[0];
                        return params.value[0] + ' : ' + params.value[1];
                    },
                    axisPointer: {
                        animation: false
                    }
                },
                xAxis: {
                    type: 'time',
                    splitLine: {
                        show: false
                    }
                },
                yAxis: {
                    type: 'value',
                    boundaryGap: [0, '100%'],
                    splitLine: {
                        show: false
                    }
                },
                // 数据内容数组
                series: function () {
                    var serie=[];
                    for (var i = 0; i < dataDealed.length; i++) {
                        var item = {
                            name: dataDealed[i].legend,
                            type: 'line',
                            data: dataDealed[i].items,
                            showSymbol: false,
                            hoverAnimation: false,
                        };
                        serie.push(item);
                    }
                    return serie;
                }()
            };
            var myChart = echarts.init(document.getElementById('id0001'));//p 标签id
            myChart.setOption(option);

            $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                myChart.resize();
            });
        };
        var retrieveGasCynTrayHistory = function () {
            var gasCynTrayHistoryList = [];
            var queryParams = {
                startTime: $scope.q.startTime,
                endTime: $scope.q.endTime,
                number: $scope.q.code
            };
            DecisionService.retrieveGasCynTrayHistory(queryParams).then(function (gasCynTrayHistory) {
                var dataDealed = [];//结果数据
                //加工数据，按商品号分开
                gasCynTrayHistoryList = gasCynTrayHistory.items;
                var name = null;
                var unitData = {legend:null, items:[]};
                if(gasCynTrayHistoryList.length==0){
                    $interval.cancel($scope.timer);
                    udcModal.info({"title": "查询结果", "message": "无数据"});
                }
                for (var i=0;i<gasCynTrayHistoryList.length;i++){
                    var code = gasCynTrayHistoryList[i].number;

                    var tempData = {name:null,value:null};
                    var date = new Date(gasCynTrayHistoryList[i].timestamp);
                    tempData.name = date.toString();
                    tempData.value = [gasCynTrayHistoryList[i].timestamp,gasCynTrayHistoryList[i].weight];
                    unitData.items.push(tempData);
                    name = gasCynTrayHistoryList[i].number;
                }

                if(unitData.items.length!=0){
                    unitData.legend = name;
                    dataDealed.push(unitData);
                }
                drawChart(dataDealed);
            });
        };
        var init = function () {
            var dataDealed = [];//结果数据
            drawChart(dataDealed);
        };

        init();

    }]);
