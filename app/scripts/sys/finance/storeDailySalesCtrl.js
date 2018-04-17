/**
 * Created by Administrator on 2018/3/28.
 */
'use strict';

financeApp.controller('storeDailySalesCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'sessionStorage','FinanceService','$interval',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                    rootService, pager, udcModal,sessionStorage,FinanceService,$interval) {

        Date.prototype.format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }

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

                    date.setDate(date.getDate() - 1);
                    var s1 = date.format("yyyy-MM-dd");
                   //console.info("昨日开始");
                    $scope.yesterdayData.startTime = s1 +" "+ date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                    //console.info($scope.yesterdayData.startTime)

                });
            $('#datetimepickerDailyTimeEnd').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.dailyData.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();

                    date.setDate(date.getDate() - 1);
                    var s1 = date.format("yyyy-MM-dd");
                    //console.info("昨日结束");
                    $scope.yesterdayData.endTime = s1 +" "+ date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                    //console.info($scope.yesterdayData.endTime)

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


        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            //searchData();
        };

        $scope.pager = pager.init('storeDailySalesCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.data = {
            currentTime:null,
            selectedGoodsType:{},
            selectedGoods:{},
            goodsTypesList:[],
            goodsList:[],

            todayShexiaohuikuan:[],
            todayShexiaojieyu:[],
            todayYuejiehuikuan:[],
            todayYuejiejieyu:[],
            yesterdayShexiaojieyu:[],
            yesterdayYuejiejieyu:[],
            todaySalesCash:[],
            yesterdaySalesCash:[],
        };

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

        $scope.q = {
            number: null,
            liableUserId: null,
            liableDepartmentCode: null,

            sumYesterdayKucun:null,
            sumTodayDiaoruShuliang:null,
            sumMonthDiaoru:null,

            sumCashSum:null,sumCashCount:null,
            sumPayOnlineSum:null,sumPayOnlineCount:null,
            sumCreditSum:null,sumCreditCount:null,
            sumYuejieCount:null,sumYuejieSum:null,
            sumTicketCount:null,sumTicketSum:null,
            sumCouponCount:null,

            sumCount:null,sumSum:null,

            sumTodayAvegPrice:null,

            sumYueleijiCount:null,sumYueleijiSum:null,

            sumCurrentKucun:null,sumTodayIn:null,sumTodayOut:null,
            sumMonthlyIn:null,


        };


        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchData();
        };

        var searchData = function () {
            if(($scope.q.liableDepartmentCode!=null)&&($scope.dailyData.startTime!=null)&& ($scope.dailyData.endTime!=null)
                &&($scope.monthlyData.startTime!=null)&& ($scope.monthlyData.endTime!=null))
            //if(($scope.q.liableDepartmentCode.length>0)&&($scope.dailyData.startTime.length>0)&& ($scope.dailyData.endTime.length>0)
            //    &&($scope.monthlyData.startTime.length>0)&& ($scope.monthlyData.endTime.length>0))
            {
                //查询当前库存
                var params = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    loadStatus:1,
                };
                console.info("查询当前库存");
                FinanceService.searchStock(params).then(function (stocks) {
                    var stockList = stocks.items;
                    console.info(stockList);
                    if(stocks.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < stockList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==stockList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].currentKucun = stockList[k];

                                        $scope.q.sumCurrentKucun +=  $scope.data.goodsList[i].detail[j].currentKucun.amount;

                                    }
                                }
                            }
                        }
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
                    var stockInList = stocksIn.items;
                    console.info(stockInList);

                    if(stocksIn.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < stockInList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==stockInList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].todayStockIn = stockInList[k];


                                        $scope.q.sumTodayIn +=  $scope.data.goodsList[i].detail[j].todayStockIn.amount;
                                    }
                                }
                            }
                        }
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
                console.info("查询今日出库数量");

                FinanceService.searchStockInOut(params2).then(function (stocksOut) {
                    var stockOutList = stocksOut.items;
                    console.info(stockOutList);

                    if(stocksOut.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < stockOutList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==stockOutList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].todayStockOut = stockOutList[k];

                                        $scope.q.sumTodayOut +=  $scope.data.goodsList[i].detail[j].todayStockOut.amount;

                                    }
                                }
                            }
                        }
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
                    var stockInList = stocksIn.items;
                    console.info(stockInList);

                    if(stocksIn.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < stockInList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==stockInList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].monthlyStockIn = stockInList[k];

                                        $scope.q.sumMonthlyIn += $scope.data.goodsList[i].detail[j].monthlyStockIn.amount;
                                    }
                                }
                            }
                        }
                    }
                })

                //现金
                var queryParams1 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    payType:1,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };

                FinanceService.searchSalesByBayType(queryParams1).then(function (salesByPay) {
                    var salesByPayCashList = salesByPay.items;

                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayCashList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayCashList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayCash = salesByPayCashList[k];


                                        $scope.q.sumCashSum +=  $scope.data.goodsList[i].detail[j].salesByPayCash.sum;
                                        $scope.q.sumCashCount += $scope.data.goodsList[i].detail[j].salesByPayCash.count;
                                    }
                                }
                            }
                        }
                    }
                })

                //电子支付（扫码）
                var queryParams = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    payType:0,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                FinanceService.searchSalesByBayType(queryParams).then(function (salesByPay) {
                    var salesByPayOnlineList = salesByPay.items;

                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayOnlineList.length; k++) {

                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayOnlineList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayOnline = salesByPayOnlineList[k];

                                        $scope.q.sumPayOnlineCount += $scope.data.goodsList[i].detail[j].salesByPayOnline.count;
                                        $scope.q.sumPayOnlineSum += $scope.data.goodsList[i].detail[j].salesByPayOnline.sum;
                                    }
                                }
                            }
                        }
                    }
                })

                //赊销
                var queryParams2 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    payType:2,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                FinanceService.searchSalesByBayType(queryParams2).then(function (salesByPay) {
                    var salesByPayCreditList = salesByPay.items;

                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayCreditList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayCreditList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayCredit = salesByPayCreditList[k];

                                        $scope.q.sumCreditCount += $scope.data.goodsList[i].detail[j].salesByPayCredit.count;
                                        $scope.q.sumCreditSum += $scope.data.goodsList[i].detail[j].salesByPayCredit.sum;
                                    }
                                }
                            }
                        }
                    }
                })

                //月结
                var queryParams3 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    payType:3,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                FinanceService.searchSalesByBayType(queryParams3).then(function (salesByPay) {
                    var salesByPayMonthlySumList = salesByPay.items;

                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayMonthlySumList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayMonthlySumList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayMonthlySum = salesByPayMonthlySumList[k];

                                        $scope.sumYuejieCount +=  $scope.data.goodsList[i].detail[j].salesByPayMonthlySum.count;
                                        $scope.sumYuejieSum += $scope.data.goodsList[i].detail[j].salesByPayMonthlySum.sum;

                                    }
                                }
                            }
                        }
                    }
                })

                //气票
                var queryParams4 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    payType:4,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };

                FinanceService.searchSalesByBayType(queryParams4).then(function (salesByPay) {
                    var salesByPayTicketList = salesByPay.items;

                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayTicketList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayTicketList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayTicket = salesByPayTicketList[k];

                                        $scope.q.sumTicketCount +=  $scope.data.goodsList[i].detail[j].salesByPayTicket.count;
                                        $scope.q.sumTicketSum += $scope.data.goodsList[i].detail[j].salesByPayTicket.sum;
                                    }
                                }
                            }
                        }
                    }
                })

                //优惠券
                var queryParams5 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    payType:5,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };

                FinanceService.searchSalesByBayType(queryParams5).then(function (salesByPay) {
                    var salesByPayCouponList = salesByPay.items;

                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayCouponList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayCouponList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayCoupon = salesByPayCouponList[k];

                                        $scope.q.sumCouponCount += $scope.data.goodsList[i].detail[j].salesByPayCoupon.count;

                                    }
                                }
                            }
                        }
                    }
                })

                //月累计销售 no payType
                var queryParams6 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    //payType:5,
                    startTime:$scope.monthlyData.startTime,
                    endTime:$scope.monthlyData.endTime,
                };

                FinanceService.searchSalesByBayType(queryParams6).then(function (salesByPay) {
                    var salesByPayMonthlySumSalesList = salesByPay.items;
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayMonthlySumSalesList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayMonthlySumSalesList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByMonthlySumSales = salesByPayMonthlySumSalesList[k];

                                        $scope.q.sumYueleijiCount += $scope.data.goodsList[i].detail[j].salesByMonthlySumSales.count;
                                        $scope.q.sumYueleijiSum += $scope.data.goodsList[i].detail[j].salesByMonthlySumSales.sum;
                                    }
                                }
                            }
                        }
                    }
                })

                //销售往来日报表查询:今日赊销回款
                var queryParams7 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:0,
                    writeOffType:0,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };

                FinanceService.searchSaleContacts(queryParams7).then(function (SaleContacts) {
                    $scope.data.todayShexiaohuikuan = SaleContacts.items;
                   // console.info("test");
                    console.info( $scope.data.todayShexiaohuikuan);
                })
                //销售往来日报表查询:今日赊销结余
                var queryParams8 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:0,
                    writeOffType:1,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };

                FinanceService.searchSaleContacts(queryParams8).then(function (SaleContacts) {
                    $scope.data.todayShexiaojieyu = SaleContacts.items[0];
                })

                //销售往来日报表查询:今日月结回款
                var queryParams9 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:1,
                    writeOffType:1,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };

                FinanceService.searchSaleContacts(queryParams9).then(function (SaleContacts) {
                    $scope.data.todayYuejiehuikuan = SaleContacts.items[0];
                })

                //销售往来日报表查询:今日月结结余
                var queryParams10 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:1,
                    writeOffType:0,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };

                FinanceService.searchSaleContacts(queryParams10).then(function (SaleContacts) {
                    $scope.data.todayYuejiejieyu = SaleContacts.items[0];
                })


                //销售往来日报表查询:上日赊销结余
                var queryParams11 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:0,
                    writeOffType:1,
                    startTime:$scope.yesterdayData.startTime,
                    endTime:$scope.yesterdayData.endTime,
                };

                FinanceService.searchSaleContacts(queryParams11).then(function (SaleContacts) {
                    $scope.data.yesterdayShexiaojieyu = SaleContacts.items[0];
                })

                //销售往来日报表查询:上日月结结余
                var queryParams12 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:1,
                    writeOffType:0,
                    startTime:$scope.yesterdayData.startTime,
                    endTime:$scope.yesterdayData.endTime,
                };

                FinanceService.searchSaleContacts(queryParams12).then(function (SaleContacts) {
                    $scope.data.yesterdayYuejiejieyu = SaleContacts.items[0];
                })

                //今日销售现金查询
                var paramsCash1 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                console.info("今日现金销售");
                console.info(paramsCash1);
                FinanceService.searchSalesCash(paramsCash1).then(function (salesCash) {
                    $scope.data.todaySalesCash = salesCash;
                    console.info($scope.data.todaySalesCash);
                })

                //昨日销售现金查询
                var paramsCash2 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    startTime:$scope.yesterdayData.startTime,
                    endTime:$scope.yesterdayData.endTime,
                };
                console.info("昨日现金销售");
                console.info(paramsCash2);
                FinanceService.searchSalesCash(paramsCash2).then(function (salesCash) {
                    $scope.data.yesterdaySalesCash = salesCash;
                    console.info($scope.data.yesterdaySalesCash);
                })
            }
            else
            {
                udcModal.info({"title": "提醒", "message": "请选择编报部门、今日销售时间和月累计销售时间"});
            }

        }

        $scope.printPage = function () {
            window.print();
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
                    console.info($scope.q.liableDepartmentCode);
                    //searchBottles();
                }
            })
        };

        var init = function () {
            var queryParams = {};
            FinanceService.retrieveGoodsTypes(queryParams).then(function (goodsTypes) {
                $scope.data.goodsTypesList = goodsTypes.items;
                $scope.data.selectedGoodsType = $scope.data.goodsTypesList[0];
                //console.info($scope.data.goodsTypesList);

                for(var i = 0; i < $scope.data.goodsTypesList.length; i++)
                {
                    //$scope.data.goodsTableContext.goodsName = $scope.data.goodsTypesList[i].name
                    var queryParams = {
                        typeName: $scope.data.goodsTypesList[i].name,
                    };
                    FinanceService.retrieveGoods(queryParams).then(function (goods) {
                        var tempList = {type:null,detail:[]};
                        tempList.detail = goods.items;
                        if(goods.items.length > 0){
                            tempList.type = goods.items[0].goodsType.name;
                        }
                        $scope.data.goodsList.push(tempList);
                    });
                }
            })

            $scope.timer = $interval(function(){
                calculateCountSumSum()
            }, 50);

        };
        init();

        //计算数量、金额合计
        var calculateCountSumSum = function () {
            //console.log("calculateCountSumSum");
            for(var i = 0; i < $scope.data.goodsList.length; i++) {
                for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                    if(($scope.data.goodsList[i].detail[j].salesByPayCash!=null)&&($scope.data.goodsList[i].detail[j].salesByPayOnline!=null)
                        &&($scope.data.goodsList[i].detail[j].salesByPayCredit!=null)&&($scope.data.goodsList[i].detail[j].salesByPayMonthlySum!=null)
                        &&($scope.data.goodsList[i].detail[j].salesByPayTicket!=null)&&($scope.data.goodsList[i].detail[j].salesByPayCoupon!=null)
                        &&($scope.data.goodsList[i].detail[j].salesByMonthlySumSales!=null))
                    {

                        $scope.sumCount = $scope.data.goodsList[i].detail[j].salesByPayCash.count + $scope.data.goodsList[i].detail[j].salesByPayOnline.count
                            + $scope.data.goodsList[i].detail[j].salesByPayCredit.count + $scope.data.goodsList[i].detail[j].salesByPayMonthlySum.count
                            + $scope.data.goodsList[i].detail[j].salesByPayTicket.count + $scope.data.goodsList[i].detail[j].salesByPayCoupon.count
                            + $scope.data.goodsList[i].detail[j].salesByMonthlySumSales.count;

                        $scope.sumSum = $scope.data.goodsList[i].detail[j].salesByPayCash.sum + $scope.data.goodsList[i].detail[j].salesByPayOnline.sum
                            + $scope.data.goodsList[i].detail[j].salesByPayCredit.sum + $scope.data.goodsList[i].detail[j].salesByPayMonthlySum.sum
                            + $scope.data.goodsList[i].detail[j].salesByPayTicket.sum + $scope.data.goodsList[i].detail[j].salesByPayCoupon.sum
                            + $scope.data.goodsList[i].detail[j].salesByMonthlySumSales.sum;
                    }
                }
            }
        };


    }]);
