'use strict';

comprehensiveQueryApp.controller('GasCynTrayUsageCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'TicketService','sessionStorage', '$interval','initVal','close',function ($scope, $rootScope, $filter, $location, Constants,

                                                                      rootService, pager, udcModal, TicketService, sessionStorage,$interval,initVal,close) {

        $scope.q = {
            code: null,
            startTime:"",
            endTime:""


        };

        $scope.vm = {
            GasCynTray:null,
            dataDealed:[],
            unitData : {legend:null, items:[]},
            myChart:null
        };
        $scope.search = function () {
            //启动绘制托盘重量的定时器
            $scope.vm.dataDealed = [];//结果数据
            $scope.vm.unitData = {legend:null, items:[]};
            retrieveGasCynTrayHistory($scope.q.startTime, $scope.q.endTime,$scope.q.code);
            //$interval.cancel($scope.timer);
            //$scope.timer = $interval( function(){
            //    retrieveGasCynTrayHistory();
            //}, 1000);
        };

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
            $scope.vm.myChart = echarts.init(document.getElementById('id0001'));//p 标签id

            $scope.vm.myChart.setOption(option);

            $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                $scope.vm.myChart.resize();
            });
        };
        var retrieveGasCynTrayHistory = function (startTime, endTime, code) {
            var gasCynTrayHistoryList = [];
            var queryParams = {
                startTime: startTime,
                endTime: endTime,
                number: code
            };
            TicketService.retrieveGasCynTrayHistory(queryParams).then(function (gasCynTrayHistory) {

                //加工数据，按商品号分开
                gasCynTrayHistoryList = gasCynTrayHistory.items;
                var name = null;

                if(gasCynTrayHistoryList.length==0){
                    //$interval.cancel($scope.timer);
                    udcModal.info({"title": "查询结果", "message": "无数据"});
                }
                for (var i=0;i<gasCynTrayHistoryList.length;i++){
                    var code = gasCynTrayHistoryList[i].number;

                    var tempData = {name:null,value:null};
                    var date = new Date(gasCynTrayHistoryList[i].timestamp);
                    tempData.name = date.toString();
                    tempData.value = [gasCynTrayHistoryList[i].timestamp,gasCynTrayHistoryList[i].weight];
                    $scope.vm.unitData.items.push(tempData);
                    name = gasCynTrayHistoryList[i].number;
                }

                if(gasCynTrayHistory.items.length == 5000){
                    retrieveGasCynTrayHistory(gasCynTrayHistoryList[gasCynTrayHistoryList.length-1].timestamp, $scope.q.endTime, code);
                }else{
                    if($scope.vm.unitData.items.length!=0){
                        $scope.vm.unitData.legend = name;
                        $scope.vm.dataDealed.push($scope.vm.unitData);
                    }
                    console.log($scope.vm.dataDealed);
                    drawChart($scope.vm.dataDealed);
                }
            });
        };

        var dateFillZero = function (number) {
            if(number < 10){
                return '0'+number;
            }else{
                return number;
            }

        };
        var init = function () {
            $scope.vm.GasCynTray = _.clone(initVal);

            $scope.q.code = $scope.vm.GasCynTray.number;
            var date1 = new Date();
            var month1 = date1.getMonth()+1;



            var date2= new Date(new Date().setDate(new Date().getDate() - 30)); //30天前的日期
            var month2 = date2.getMonth()+1;
            $scope.q.endTime = date1.getFullYear()+"-"+dateFillZero(month1)+"-"+dateFillZero(date1.getDate())+" 23:59:59";
            $scope.q.startTime = date2.getFullYear()+"-"+dateFillZero(month2)+"-"+dateFillZero(date2.getDate())+" 00:00:00";
            $scope.search();

        };
        $scope.close = function (result) {

            close(result, 500);
        };




        init();

    }]);
