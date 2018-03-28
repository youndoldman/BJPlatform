'use strict';
decisionApp.directive('price', ['DecisionService', function(DecisionService)  {

    return {
        scope: {
            id: "@",
            legend: "=",
            item: "=",
            data: "="
        },
        restrict: 'E',
        template: '<div style="padding-top:100px; height:500px;position=relative;"></div>',
        replace: true,
        link: function($scope, element, attrs, controller) {
            var goodsPriceHistoryList = [];
            DecisionService.retrieveGoodsPriceHistory().then(function (goodsPriceHistory) {
                var dataDealed = [];//结果数据
                //加工数据，按商品号分开
                goodsPriceHistoryList = goodsPriceHistory.items;
                var lastCode = null;
                var unitData = {legend:null, items:[]};
                for (var i=0;i<goodsPriceHistoryList.length;i++){
                    var code = goodsPriceHistoryList[i].code;
                    if((lastCode!=null)&&(lastCode!=code)){
                        dataDealed.push(unitData);
                        unitData.legend = lastCode;
                        unitData = {legend:null, items:[]};
                    }

                    var tempData = {name:null,value:null};
                    var date = new Date(goodsPriceHistoryList[i].effectTime);
                    tempData.name = date.toString();
                    tempData.value = [goodsPriceHistoryList[i].effectTime,goodsPriceHistoryList[i].price];
                    unitData.items.push(tempData);
                    lastCode = code;
                }

                if(unitData.items.length!=0){
                    unitData.legend = lastCode;
                    dataDealed.push(unitData);
                }
                drawChart(dataDealed);
            });

            var drawChart = function(dataDealed) {
                var legend = [];
                for (var i=0;i<dataDealed.length;i++) {
                    legend.push(dataDealed[i].legend);
                }
                var option = {
                    // 提示框，鼠标悬浮交互时的信息提示
                    title: {
                        text: '商品价格调整',
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
                var myChart = echarts.init(element[0]);
                myChart.setOption(option);
                $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                    myChart.resize();
                });
            }
        }

    };
}]);