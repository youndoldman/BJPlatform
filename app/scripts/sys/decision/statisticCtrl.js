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
            unitData : {legend:null, items:[]}
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
            var weatherIcons = {
                'Sunny': './data/asset/img/weather/sunny_128.png',
                'Cloudy': './data/asset/img/weather/cloudy_128.png',
                'Showers': './data/asset/img/weather/showers_128.png'
            };

            var option = {
                title: {
                    text: '天气情况统计',
                    subtext: '虚构数据',
                    left: 'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    // orient: 'vertical',
                    // top: 'middle',
                    bottom: 10,
                    left: 'center',
                    data: ['西凉', '益州','兖州','荆州','幽州']
                },
                series : [
                    {
                        type: 'pie',
                        radius : '65%',
                        center: ['50%', '50%'],
                        selectedMode: 'single',
                        data:[
                            {
                                value:1548,
                                name: '幽州',
                                label: {
                                    normal: {
                                        formatter: [
                                            '{title|{b}}{abg|}',
                                            '  {weatherHead|天气}{valueHead|天数}{rateHead|占比}',
                                            '{hr|}',
                                            '  {Sunny|}{value|202}{rate|55.3%}',
                                            '  {Cloudy|}{value|142}{rate|38.9%}',
                                            '  {Showers|}{value|21}{rate|5.8%}'
                                        ].join('\n'),
                                        backgroundColor: '#eee',
                                        borderColor: '#777',
                                        borderWidth: 1,
                                        borderRadius: 4,
                                        rich: {
                                            title: {
                                                color: '#eee',
                                                align: 'center'
                                            },
                                            abg: {
                                                backgroundColor: '#333',
                                                width: '100%',
                                                align: 'right',
                                                height: 25,
                                                borderRadius: [4, 4, 0, 0]
                                            },
                                            Sunny: {
                                                height: 30,
                                                align: 'left',
                                                backgroundColor: {
                                                    image: weatherIcons.Sunny
                                                }
                                            },
                                            Cloudy: {
                                                height: 30,
                                                align: 'left',
                                                backgroundColor: {
                                                    image: weatherIcons.Cloudy
                                                }
                                            },
                                            Showers: {
                                                height: 30,
                                                align: 'left',
                                                backgroundColor: {
                                                    image: weatherIcons.Showers
                                                }
                                            },
                                            weatherHead: {
                                                color: '#333',
                                                height: 24,
                                                align: 'left'
                                            },
                                            hr: {
                                                borderColor: '#777',
                                                width: '100%',
                                                borderWidth: 0.5,
                                                height: 0
                                            },
                                            value: {
                                                width: 20,
                                                padding: [0, 20, 0, 30],
                                                align: 'left'
                                            },
                                            valueHead: {
                                                color: '#333',
                                                width: 20,
                                                padding: [0, 20, 0, 30],
                                                align: 'center'
                                            },
                                            rate: {
                                                width: 40,
                                                align: 'right',
                                                padding: [0, 10, 0, 0]
                                            },
                                            rateHead: {
                                                color: '#333',
                                                width: 40,
                                                align: 'center',
                                                padding: [0, 10, 0, 0]
                                            }
                                        }
                                    }
                                }
                            },
                            {value:535, name: '荆州'},
                            {value:510, name: '兖州'},
                            {value:634, name: '益州'},
                            {value:735, name: '西凉'}
                        ],
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            };
            var myChart = echarts.init(document.getElementById('id0001'));//p 标签id
            myChart.setOption(option);

            $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                myChart.resize();
            });
        };
        var retrieveGasCynTrayHistory = function (startTime, endTime, code) {
            var gasCynTrayHistoryList = [];
            var queryParams = {
                startTime: startTime,
                endTime: endTime,
                number: code
            };
            DecisionService.retrieveGasCynTrayHistory(queryParams).then(function (gasCynTrayHistory) {

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
        var init = function () {
            var dataDealed = [];//结果数据
            drawChart(dataDealed);
        };

        init();

    }]);
