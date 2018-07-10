/**
 * Created by Administrator on 2018/5/2.
 */

'use strict';

financeApp.controller('checkStockCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'sessionStorage','FinanceService',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                    rootService, pager, udcModal,sessionStorage,FinanceService) {
        $(function () {
            $('#datetimepickerDailyTimeStart').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            $('#datetimepickerDailyTimeEnd').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
            });

            $('#datetimepickerMonthlyTimeStart').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss',
                locale: moment.locale('zh-cn'),
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            $('#datetimepickerMonthlyTimeEnd').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss',
                locale: moment.locale('zh-cn'),
                showTodayButton:true,
                toolbarPlacement:'top',
            });
        });

        $(function () {
            $('#datetimepickerDailyTimeStart').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.dailyData.startTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#datetimepickerDailyTimeEnd').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.dailyData.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#datetimepickerMonthlyTimeStart').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.monthlyData.startTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#datetimepickerMonthlyTimeEnd').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.monthlyData.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
        });


        $scope.dailyData={
            startTime:null,
            endTime:null,
        }

        $scope.monthlyData={
            startTime:null,
            endTime:null,
        }

        $scope.q = {
            liableDepartmentCode: null,
            liableDepartmentName:null,

            sumCurrentKucun:null,
            sumTodayIn:null,
            sumTodayOut:null,
            sumMonthlyIn:null,
        };
        $scope.data = {
            gasCylinderSpecList:[],
        };

        $scope.initDepartmentSelect = function () {
            udcModal.show({
                templateUrl: "./finance/departmentSelectModal.htm",
                controller: "DepartmentSelectModalCtrl",
                inputs: {
                    title: '百江燃气组织架构',
                    initVal: {}
                }
            }).then(function (result) {
                if (result!=null) {
                    $scope.q.liableDepartmentCode = result.code;
                    $scope.q.liableDepartmentName = result.name;
                    console.info($scope.q.liableDepartmentCode);
                }
            })
        };


        $scope.pager = pager.init('dailyMonthlySalesCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                $scope.data.gasCylinderSpecList[i].currentKucun = null;
                $scope.data.gasCylinderSpecList[i].todayStockIn = null;
                $scope.data.gasCylinderSpecList[i].todayStockOut = null;
                $scope.data.gasCylinderSpecList[i].monthlyStockIn = null;

            }
            searchData();
        };

        $scope.printPage = function () {
            window.print();
        };

        var searchData = function () {
            if(($scope.q.liableDepartmentCode!=null)&&($scope.dailyData.startTime!=null)&& ($scope.dailyData.endTime!=null)
                &&($scope.monthlyData.startTime!=null)&& ($scope.monthlyData.endTime!=null))
            {
                //查询当前库存
                var params = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    loadStatus:1,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                console.info("查询今日库存");
                console.info(params);
                FinanceService.searchStock(params).then(function (stocks) {
                    $scope.q.sumCurrentKucun = null;

                    var stockList = stocks.items;
                    console.info(stockList);
                    if(stocks.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            //for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < stockList.length; k++) {
                                    if ($scope.data.gasCylinderSpecList[i].code==stockList[k].specCode) {
                                        $scope.data.gasCylinderSpecList[i].currentKucun = stockList[k];

                                        $scope.q.sumCurrentKucun +=  $scope.data.gasCylinderSpecList[i].currentKucun.amount;

                                    //}
                                }
                            }
                        }
                        console.info($scope.data.gasCylinderSpecList);
                    }
                })


                //查询今日调入数量
                var params1 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    loadStatus:1,
                    stockType:0,//入库为0
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                console.info("查询今日调入数量");

                FinanceService.searchStockInOut(params1).then(function (stocksIn) {
                    $scope.q.sumTodayIn = null;

                    var stockInList = stocksIn.items;
                    //console.info(stockInList);

                    if(stocksIn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            //for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < stockInList.length; k++) {
                                    if ($scope.data.gasCylinderSpecList[i].code==stockInList[k].specCode) {
                                        $scope.data.gasCylinderSpecList[i].todayStockIn = stockInList[k];


                                        $scope.q.sumTodayIn +=  $scope.data.gasCylinderSpecList[i].todayStockIn.amount;
                                    //}
                                }
                            }
                        }
                        console.info($scope.data.gasCylinderSpecList);
                    }
                })

                //查询今日出库数量
                var params2 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    loadStatus:1,
                    stockType:1,//出库为0
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                //console.info("查询今日出库数量");

                FinanceService.searchStockInOut(params2).then(function (stocksOut) {
                    $scope.q.sumTodayOut = null;

                    var stockOutList = stocksOut.items;
                    //console.info(stockOutList);

                    if(stocksOut.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            //for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < stockOutList.length; k++) {
                                    if ($scope.data.gasCylinderSpecList[i].code==stockOutList[k].specCode) {
                                        $scope.data.gasCylinderSpecList[i].todayStockOut = stockOutList[k];

                                        $scope.q.sumTodayOut +=  $scope.data.gasCylinderSpecList[i].todayStockOut.amount;

                                   // }
                                }
                            }
                        }
                        console.info($scope.data.gasCylinderSpecList);
                    }
                })

                //查本月累计调入数量
                var params3 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    loadStatus:1,
                    stockType:0,//入库为0
                    startTime:$scope.monthlyData.startTime,
                    endTime:$scope.monthlyData.endTime,
                };
                console.info("查本月累计调入数量");

                FinanceService.searchStockInOut(params3).then(function (stocksIn) {
                    $scope.q.sumMonthlyIn= null;
                    var stockInList = stocksIn.items;

                    if(stocksIn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            //for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < stockInList.length; k++) {
                                    if ($scope.data.gasCylinderSpecList[i].code==stockInList[k].specCode) {
                                        $scope.data.gasCylinderSpecList[i].monthlyStockIn = stockInList[k];

                                        $scope.q.sumMonthlyIn += $scope.data.gasCylinderSpecList[i].monthlyStockIn.amount;
                                   // }
                                }
                            }
                        }
                        console.info($scope.data.gasCylinderSpecList);
                    }
                })
            }
            else {
                udcModal.info({"title": "提醒", "message": "请选择编报部门、今日销售时间和月累计销售时间"});
            }

        };

        var init = function () {
            $scope.pager.pageSize=10;
            //查询钢瓶类型规格
            var queryParams = {};
            FinanceService.retrieveGasCylinderSpecUri(queryParams).then(function (GasCylinderSpec) {
                $scope.data.gasCylinderSpecList = GasCylinderSpec.items;
                //$scope.data.selectedGoodsType = $scope.data.goodsTypesList[0];
                console.info($scope.data.gasCylinderSpecList);

            });


        };
        init();

    }]);
