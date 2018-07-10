/**
 * Created by Administrator on 2018/3/29.
 */
'use strict';

financeApp.controller('checkBottleCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'sessionStorage','FinanceService','$interval',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                 rootService, pager, udcModal,sessionStorage,FinanceService,$interval) {

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

        $scope.yesterdayData={
            startTime:null,
            endTime:null,
        }

        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchData();
        };

        $scope.pager = pager.init('checkBottleCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.data = {
            gasCylinderSpecList:[],
        };


        $scope.q = {
            liableDepartmentCode: null,
            liableDepartmentName:null,
            sumCurrentKucun:null,
            sumTodayIn:null,
            sumTodayOut:null,
            sumMonthlyIn:null,
            sumTodayLingyong:null,
            sumThisMonthLingyong:null,
            sumTodaySongjian:null,
            sumThisMonthSongjian:null,
            sumToadyTuiweixiuping:null,
            sumThisMonthTuiweixiuping:null,
            sumTodayShouqu:null,
            sumThisMonthShouqu:null,
            sumTodayTuibaofeiping:null,
            sumThisMonthTuibaofeiping:null,
            sumTodayTuiyajinping:null,
            sumThisMonthTuiyajinping:null,
            sumTodayYaping:null,
            sumThisMonthYaping:null,
            sumBottleCheckFee:null,
            sumBottleMortgageFee:null,
            sumGangjianFee:null,
            sumPingYajingFee:null,
            sumReturnYajingFee:null,
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                $scope.data.gasCylinderSpecList[i].currentKucun = null;
                $scope.data.gasCylinderSpecList[i].todayStockIn = null;
                $scope.data.gasCylinderSpecList[i].todayStockOut = null;

                $scope.data.gasCylinderSpecList[i].todayLingyong = null;
                $scope.data.gasCylinderSpecList[i].thisMonthLingyong = null;
                $scope.data.gasCylinderSpecList[i].todaySongjian = null;
                $scope.data.gasCylinderSpecList[i].thisMonthSongjian = null;
                $scope.data.gasCylinderSpecList[i].todayTuiweixiuping = null;
                $scope.data.gasCylinderSpecList[i].thisMonthTuiweixiuping = null;
                $scope.data.gasCylinderSpecList[i].todayShouqu = null;
                $scope.data.gasCylinderSpecList[i].thisMonthShouqu = null;
                $scope.data.gasCylinderSpecList[i].todayTuibaofeiping = null;
                $scope.data.gasCylinderSpecList[i].thisMonthTuibaofeiping = null;
                $scope.data.gasCylinderSpecList[i].todayTuiyajinping = null;

                $scope.data.gasCylinderSpecList[i].thisMonthTuiyajinping = null;
                $scope.data.gasCylinderSpecList[i].todayYaping = null;
                $scope.data.gasCylinderSpecList[i].thisMonthYaping = null;
                $scope.data.gasCylinderSpecList[i].bottleCheckFee = null;
                $scope.data.gasCylinderSpecList[i].bottleMortgageFee = null;






            }
            searchData();
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
               // console.info("查询今日库存");
                //console.info(params);
                FinanceService.searchStock(params).then(function (stocks) {
                    $scope.q.sumCurrentKucun = null;

                    var stockList = stocks.items;
                   // console.info(stockList);
                    if(stocks.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < stockList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==stockList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].currentKucun = stockList[k];

                                    $scope.q.sumCurrentKucun +=  $scope.data.gasCylinderSpecList[i].currentKucun.amount;

                                }
                            }

                        }
                        //console.info( $scope.q.sumCurrentKucun);
                        //console.info($scope.data.gasCylinderSpecList);
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


                FinanceService.searchStockInOut(params1).then(function (stocksIn) {
                    $scope.q.sumTodayIn = null;

                    var stockInList = stocksIn.items;
                    //console.info(stockInList);

                    if(stocksIn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < stockInList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==stockInList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].todayStockIn = stockInList[k];


                                    $scope.q.sumTodayIn +=  $scope.data.gasCylinderSpecList[i].todayStockIn.amount;
                                }

                            }
                        }
                        //console.info("查询今日调入数量");
                        //console.info($scope.q.sumTodayIn);
                        //console.info($scope.data.gasCylinderSpecList);
                    }
                })

                //查询今日出库数量
                var params2 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    loadStatus:1,
                    stockType:1,//出库为1
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };

                FinanceService.searchStockInOut(params2).then(function (stocksOut) {
                    $scope.q.sumTodayOut = null;

                    var stockOutList = stocksOut.items;
                    //console.info(stockOutList);

                    if(stocksOut.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < stockOutList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==stockOutList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].todayStockOut = stockOutList[k];

                                    $scope.q.sumTodayOut +=  $scope.data.gasCylinderSpecList[i].todayStockOut.amount;

                                }
                            }

                        }
                       // console.info("查询今日出库数量");
                       // console.info($scope.q.sumTodayOut);
                       //console.info($scope.data.gasCylinderSpecList);
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
               // console.info("查本月累计调入数量");

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
                        //console.info($scope.data.gasCylinderSpecList);
                    }
                })


                //查本日领用数量
                var p0 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:0,//领用为0
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };

                //console.info(p0);
                FinanceService.retrieveReportGasCyrDyn(p0).then(function (reportGasCyrDyn) {
                   $scope.q.sumTodayLingyong= null;

                    var bottleList = reportGasCyrDyn.items;
                    //console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].todayLingyong = bottleList[k];

                                    $scope.q.sumTodayLingyong += $scope.data.gasCylinderSpecList[i].todayLingyong.amount;
                                }
                            }
                        }
                        //console.info("查本日领用数量");
                        //console.info($scope.q.sumTodayLingyong)
                        //console.info($scope.data.gasCylinderSpecList);
                    }

                })

                //查本月累计领用数量
                var p1 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:0,//领用为0
                    startTime:$scope.monthlyData.startTime,
                    endTime:$scope.monthlyData.endTime,
                };
                //console.info("查本月累计领用数量");
                //console.info(p1);
                FinanceService.retrieveReportGasCyrDyn(p1).then(function (reportGasCyrDyn) {
                    $scope.q.sumThisMonthLingyong= null;

                    var bottleList = reportGasCyrDyn.items;
                   // console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].thisMonthLingyong= bottleList[k];

                                    $scope.q.sumThisMonthLingyong += $scope.data.gasCylinderSpecList[i].thisMonthLingyong.amount;
                                }
                            }
                        }

                        //console.info($scope.data.gasCylinderSpecList);
                    }
                })

                //查本日送检数量
                var p2 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:1,//送检为1
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                //console.info("查本日送检数量");
                //console.info(p2);
                FinanceService.retrieveReportGasCyrDyn(p2).then(function (reportGasCyrDyn) {
                    $scope.q.sumTodaySongjian= null;

                    var bottleList = reportGasCyrDyn.items;
                   // console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].todaySongjian = bottleList[k];

                                    $scope.q.sumTodaySongjian += $scope.data.gasCylinderSpecList[i].todaySongjian.amount;
                                }
                            }
                        }
                    }
                })

                //查本月累计送检数量
                var p3 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:1,//送检为1
                    startTime:$scope.monthlyData.startTime,
                    endTime:$scope.monthlyData.endTime,
                };
                //console.info("查本月累计送检数量");
                //console.info(p3);
                FinanceService.retrieveReportGasCyrDyn(p3).then(function (reportGasCyrDyn) {

                    $scope.q.sumThisMonthSongjian= null;
                    var bottleList = reportGasCyrDyn.items;
                    //console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].thisMonthSongjian = bottleList[k];

                                    $scope.q.sumThisMonthSongjian += $scope.data.gasCylinderSpecList[i].thisMonthSongjian.amount;
                                }
                            }
                        }
                    }
                })

                //查本日退维修瓶数量
                var p4 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:3,//退维修瓶为3
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                //console.info("本日退维修瓶数量");
                //console.info(p4);
                FinanceService.retrieveReportGasCyrDyn(p4).then(function (reportGasCyrDyn) {

                    $scope.q.sumToadyTuiweixiuping= null;
                    var bottleList = reportGasCyrDyn.items;
                    //console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].todayTuiweixiuping = bottleList[k];

                                    $scope.q.sumToadyTuiweixiuping += $scope.data.gasCylinderSpecList[i].todayTuiweixiuping.amount;
                                }
                            }
                        }
                    }
                })


                //查本月累计退维修瓶数量
                var p5 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:3,//退维修瓶为3
                    startTime:$scope.monthlyData.startTime,
                    endTime:$scope.monthlyData.endTime,
                };
                //console.info("查本月累计退维修瓶数量");
                //console.info(p5);
                FinanceService.retrieveReportGasCyrDyn(p5).then(function (reportGasCyrDyn) {

                    $scope.q.sumThisMonthTuiweixiuping= null;
                    var bottleList = reportGasCyrDyn.items;
                   // console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].thisMonthTuiweixiuping = bottleList[k];

                                    $scope.q.sumThisMonthTuiweixiuping += $scope.data.gasCylinderSpecList[i].thisMonthTuiweixiuping.amount;
                                }
                            }
                        }
                    }
                })

                //查本日收取钢检瓶数量
                var p6 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:2,//收取钢检瓶为2
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                //console.info("查本日收取钢检瓶数量");
                //console.info(p6);
                FinanceService.retrieveReportGasCyrDyn(p6).then(function (reportGasCyrDyn) {

                    $scope.q.sumTodayShouqu= null;
                    var bottleList = reportGasCyrDyn.items;
                   // console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].todayShouqu = bottleList[k];

                                    $scope.q.sumTodayShouqu += $scope.data.gasCylinderSpecList[i].todayShouqu.amount;
                                }
                            }
                        }
                    }
                })

                //查本月累计收取钢检瓶数量
                var p7 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:2,//收取钢检瓶为2
                    startTime:$scope.monthlyData.startTime,
                    endTime:$scope.monthlyData.endTime,
                };
                //console.info("查本月累计收取钢检瓶数量");
                //console.info(p7);
                FinanceService.retrieveReportGasCyrDyn(p7).then(function (reportGasCyrDyn) {

                    $scope.q.sumThisMonthShouqu= null;
                    var bottleList = reportGasCyrDyn.items;
                    //console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].thisMonthShouqu = bottleList[k];

                                    $scope.q.sumThisMonthShouqu += $scope.data.gasCylinderSpecList[i].thisMonthShouqu.amount;
                                }
                            }
                        }
                    }
                })

                //查本日退报废瓶数量
                var p8 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:4,//退报废瓶为4
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                //console.info("查本日退报废瓶数量");
                //console.info(p8);
                FinanceService.retrieveReportGasCyrDyn(p8).then(function (reportGasCyrDyn) {

                    $scope.q.sumTodayTuibaofeiping= null;
                    var bottleList = reportGasCyrDyn.items;
                   // console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].todayTuibaofeiping = bottleList[k];

                                    $scope.q.sumTodayTuibaofeiping += $scope.data.gasCylinderSpecList[i].todayTuibaofeiping.amount;
                                }
                            }
                        }
                    }
                })

                //查本月累计退报废瓶数量
                var p9 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:4,//退报废瓶为4
                    startTime:$scope.monthlyData.startTime,
                    endTime:$scope.monthlyData.endTime,
                };
                //console.info("查本月累计退报废瓶数量");
                //console.info(p9);
                FinanceService.retrieveReportGasCyrDyn(p9).then(function (reportGasCyrDyn) {

                    $scope.q.sumThisMonthTuibaofeiping= null;
                    var bottleList = reportGasCyrDyn.items;
                    //console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].thisMonthTuibaofeiping = bottleList[k];

                                    $scope.q.sumThisMonthTuibaofeiping += $scope.data.gasCylinderSpecList[i].thisMonthTuibaofeiping.amount;
                                }
                            }
                        }
                    }
                })

                //查本日退押金瓶数量
                var p9 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:6,//退押金瓶为6
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                //console.info("查本日退押金瓶数量");
                //console.info(p9);
                FinanceService.retrieveReportGasCyrDyn(p9).then(function (reportGasCyrDyn) {

                    $scope.q.sumTodayTuiyajinping= null;
                    var bottleList = reportGasCyrDyn.items;
                    //console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].todayTuiyajinping = bottleList[k];

                                    $scope.q.sumTodayTuiyajinping += $scope.data.gasCylinderSpecList[i].todayTuiyajinping.amount;
                                }
                            }
                        }
                    }
                })

                //查本月累计退押金瓶数量
                var p10 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:6,//退押金瓶为6
                    startTime:$scope.monthlyData.startTime,
                    endTime:$scope.monthlyData.endTime,
                };
                //console.info("查本月累计退押金瓶数量");
                //console.info(p10);
                FinanceService.retrieveReportGasCyrDyn(p10).then(function (reportGasCyrDyn) {

                    $scope.q.sumTodayTuiyajinping= null;
                    var bottleList = reportGasCyrDyn.items;
                    //console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].thisMonthTuiyajinping = bottleList[k];

                                    $scope.q.sumTodayTuiyajinping += $scope.data.gasCylinderSpecList[i].thisMonthTuiyajinping.amount;
                                }
                            }
                        }
                    }
                })

                //查本日押瓶数量
                var p11 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:5,//押瓶为5
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                //console.info("查本日押瓶数量");
                //console.info(p11);
                FinanceService.retrieveReportGasCyrDyn(p11).then(function (reportGasCyrDyn) {

                    $scope.q.sumTodayYaping= null;
                    var bottleList = reportGasCyrDyn.items;
                    //console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].todayYaping = bottleList[k];

                                    $scope.q.sumTodayYaping += $scope.data.gasCylinderSpecList[i].todayYaping.amount;
                                }
                            }
                        }
                    }
                })

                //查本月押瓶数量
                var p12 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    operType:5,//押瓶为5
                    startTime:$scope.monthlyData.startTime,
                    endTime:$scope.monthlyData.endTime,
                };
                //console.info("查本月押瓶数量");
                //console.info(p12);
                FinanceService.retrieveReportGasCyrDyn(p12).then(function (reportGasCyrDyn) {

                    $scope.q.sumThisMonthYaping= null;
                    var bottleList = reportGasCyrDyn.items;
                    //console.info(bottleList);

                    if(reportGasCyrDyn.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < bottleList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==bottleList[k].specCode) {
                                    $scope.data.gasCylinderSpecList[i].thisMonthYaping = bottleList[k];

                                    $scope.q.sumThisMonthYaping += $scope.data.gasCylinderSpecList[i].thisMonthYaping.amount;
                                }
                            }
                        }
                    }
                })

                //查钢检费标准
                var para0 = {
                    chargeType:0,//钢瓶检验为0
                };
                //console.info("查钢检费标准");
                //console.info(para0);
                FinanceService.searchGasCyrChargeSpec(para0).then(function (charge) {

                    $scope.q.sumBottleCheckFee= null;

                    var chargeList = charge.items;
                  //  console.info(chargeList);

                    if(charge.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < chargeList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==chargeList[k].gasCyrSpecCode) {
                                    $scope.data.gasCylinderSpecList[i].bottleCheckFee = chargeList[k];

                                    $scope.q.sumBottleCheckFee += $scope.data.gasCylinderSpecList[i].bottleCheckFee.price;
                                }
                            }
                        }
                    }
                })

                //查押瓶费标准
                var para1 = {
                    chargeType:1,//押瓶为1
                };
                //console.info("查押瓶费标准");
                //console.info(para1);
                FinanceService.searchGasCyrChargeSpec(para1).then(function (charge) {

                    $scope.q.sumBottleMortgageFee= null;

                    var chargeList = charge.items;
                  //  console.info(chargeList);

                    if(charge.items.length>0){
                        for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                            for (var k = 0; k < chargeList.length; k++) {
                                if ($scope.data.gasCylinderSpecList[i].code==chargeList[k].gasCyrSpecCode) {
                                    $scope.data.gasCylinderSpecList[i].bottleMortgageFee = chargeList[k];

                                    $scope.q.sumBottleMortgageFee += $scope.data.gasCylinderSpecList[i].bottleMortgageFee.price;
                                }
                            }
                        }
                    }
                })
            }
            else {
                udcModal.info({"title": "提醒", "message": "请选择编报部门、今日销售时间和月累计销售时间"});
            }
        };

        var init = function () {
            $scope.pager.pageSize=10;
            //searchData();
            //查询钢瓶类型规格
            var queryParams = {};
            FinanceService.retrieveGasCylinderSpecUri(queryParams).then(function (GasCylinderSpec) {
                $scope.data.gasCylinderSpecList = GasCylinderSpec.items;
                //$scope.data.selectedGoodsType = $scope.data.goodsTypesList[0];
                //console.info($scope.data.gasCylinderSpecList);
            });

            $scope.timer = $interval(function(){
                calculateFeeSum()
            }, 50);

        };
        init();

        var calculateFeeSum = function () {
            var fee1 = null;
            var fee2 = null;
            var fee3 = null;
            for(var i = 0; i < $scope.data.gasCylinderSpecList.length; i++) {
                if(($scope.data.gasCylinderSpecList[i].todayShouqu!=null) && ($scope.data.gasCylinderSpecList[i].bottleCheckFee!=null)){
                    fee1 += $scope.data.gasCylinderSpecList[i].todayShouqu.amount * $scope.data.gasCylinderSpecList[i].bottleCheckFee.price;
                }
                if(($scope.data.gasCylinderSpecList[i].todayYaping!=null) && ($scope.data.gasCylinderSpecList[i].bottleMortgageFee!=null)){
                    fee2 += $scope.data.gasCylinderSpecList[i].todayYaping.amount * $scope.data.gasCylinderSpecList[i].bottleMortgageFee.price;
                }
                if(($scope.data.gasCylinderSpecList[i].todayTuiyajinping!=null) && ($scope.data.gasCylinderSpecList[i].bottleMortgageFee!=null)){
                    fee3 += $scope.data.gasCylinderSpecList[i].todayTuiyajinping.amount * $scope.data.gasCylinderSpecList[i].bottleMortgageFee.price;
                }
            }
            $scope.q.sumGangjianFee = fee1;
            $scope.q.sumPingYajingFee = fee2;
            $scope.q.sumReturnYajingFee = fee3;
        }
    }]);
