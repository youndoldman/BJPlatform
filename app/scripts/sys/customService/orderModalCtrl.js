'use strict';

customServiceApp.controller('OrderModalCtrl', ['$scope', 'close', 'OrderService', 'title', 'initVal','udcModal','$timeout',function ($scope, close, OrderService, title, initVal, udcModal,$timeout) {
    $scope.modalTitle = title;


    $timeout(function(){
        drawChart($scope.vm.currentOrder.orderStatus,$scope.vm.currentOrder.orderOpHistoryList);
    },500);

    $scope.vm = {
        currentOrder: {
        }
    };

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (order) {

        console.log(order);
        if ($scope.isModify) {
            OrderService.modifyOrder(order).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改订单信息成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "修改订单信息失败 "+value.message});
            })
        }
    };

    var init = function () {
        $scope.vm.currentOrder = _.clone(initVal);
        if(title == "修改订单") {
            $scope.isModify = true;
        }
        else {
            $scope.isModify = false;
        }
    };



    var drawChart = function(orderStatus,orderOpHistoryList) {
        var  dataOrderStatus = [{
            value:"",
            name: '开始',
            x: 0,
            y: 300,
            itemStyle : {//===============图形样式，有 normal 和 emphasis 两个状态。normal 是图形在默认状态下的样式；emphasis 是图形在高亮状态下的样式，比如在鼠标悬浮或者图例联动高亮时。
                normal : { //默认样式
                    color : 'rgba(255,0,0,1)'
                    // 图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。默认0.5
                },
                emphasis : {//高亮状态
                }
            }
        },{
            value:"",
            name: '客户下单',
            x: 100,
            y: 300,
            itemStyle : {//===============图形样式，有 normal 和 emphasis 两个状态。normal 是图形在默认状态下的样式；emphasis 是图形在高亮状态下的样式，比如在鼠标悬浮或者图例联动高亮时。
                normal : { //默认样式
                    color : 'rgba(255,0,0,1)'
                    // 图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。默认0.5
                },
                emphasis : {//高亮状态
                }
            }
        }, {
            value:"",
            name: '百江接单',
            x: 200,
            y: 300,
            itemStyle : {//===============图形样式，有 normal 和 emphasis 两个状态。normal 是图形在默认状态下的样式；emphasis 是图形在高亮状态下的样式，比如在鼠标悬浮或者图例联动高亮时。
                normal : { //默认样式
                    color : 'rgba(255,0,0,1)'
                    // 图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。默认0.5
                },
                emphasis : {//高亮状态
                }
            }
        }, {
            value:"",
            name: '客户签收',
            x: 300,
            y: 300,
            itemStyle : {//===============图形样式，有 normal 和 emphasis 两个状态。normal 是图形在默认状态下的样式；emphasis 是图形在高亮状态下的样式，比如在鼠标悬浮或者图例联动高亮时。
                normal : { //默认样式
                    color : 'rgba(255,0,0,1)'
                    // 图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。默认0.5
                },
                emphasis : {//高亮状态
                }
            }
        }, {
            value:"",
            name: '门店核单',
            x: 400,
            y: 300,
            itemStyle : {//===============图形样式，有 normal 和 emphasis 两个状态。normal 是图形在默认状态下的样式；emphasis 是图形在高亮状态下的样式，比如在鼠标悬浮或者图例联动高亮时。
                normal : { //默认样式
                    color : 'rgba(255,0,0,1)'
                    // 图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。默认0.5
                },
                emphasis : {//高亮状态
                }
            }
        }];

        var  linkOrderDetail = [];
        for(var k=0;  k<4;k++) {
            var linkOrderDetailItems = {source: k,
                target: k+1,
                value:"",
                label : { //=============图形上的文本标签
                    normal : {
                        show : true,
                        formatter: '{c}',
                        fontSize:15
                    }
                }
            };
            linkOrderDetail.push(linkOrderDetailItems);
        }

        console.log(linkOrderDetail);

        for(var i=orderStatus+2; i<5; i++){
            dataOrderStatus[i].itemStyle.normal.color = 'rgba(144,144,144,1)';
        }

        for(var j=0; j<orderOpHistoryList.length; j++){
            var itemorderOp = orderOpHistoryList[j];
            var indexOrderStatus = itemorderOp.orderStatus.index;
            linkOrderDetail[indexOrderStatus].value = itemorderOp.userId+"\r\n"+itemorderOp.updateTime;

        }


        var option = {
            title: {
                text: '订单流程图'
            },
            tooltip : {
                formatter: '{c}'
            },
            animationDurationUpdate: 500,
            animationEasingUpdate: 'quinticInOut',
            series : [
                {
                    type: 'graph',
                    layout: 'none',
                    symbolSize: 100,
                    roam: true,
                    label : { //=============图形上的文本标签
                        normal : {
                            show : true,
                            textStyle: {
                                fontSize: 20,
                            }
                        }
                    },
                    edgeSymbol: ['circle', 'arrow'],
                    edgeSymbolSize: [4, 10],
                    edgeLabel: {
                        normal: {
                            textStyle: {
                                fontSize: 40
                            }
                        }
                    },
                    data:dataOrderStatus,
                    // links: [],
                    links: linkOrderDetail,
                    lineStyle: {
                        normal: {
                            opacity: 0.9,
                            width: 2,
                            curveness: 0
                        }
                    }
                }
            ]
        };


        var myChart = echarts.init(document.getElementById('orderChart'));//p 标签id
        myChart.setOption(option);
        myChart.resize()
    };

    init();
}]);