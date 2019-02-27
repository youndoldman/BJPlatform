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
            yesterdayShexiaohuikuan:[],
            yesterdayYuejiehuikuan:[],

            specTypesList:[],
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
            liableDepartmentName:null,
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

            isControled:false


        };


        $scope.search = function () {
            for(var i = 0; i < $scope.data.specTypesList.length; i++) {
                    $scope.data.specTypesList[i].salesByPayCash= {"count":null,"sum":null};
                    $scope.data.specTypesList[i].salesByPayOnline= {"count":null,"sum":null};
                    $scope.data.specTypesList[i].salesByPayCredit= {"count":null,"sum":null};
                    $scope.data.specTypesList[i].salesByPayMonthlySum= {"count":null,"sum":null};
                    $scope.data.specTypesList[i].salesByPayTicket= {"count":null,"sum":null};
                    $scope.data.specTypesList[i].salesByPayCoupon= {"count":null,"sum":null};
                    $scope.data.specTypesList[i].salesByMonthlySumSales= {"count":null,"sum":null};
            }

            $scope.data.todayShexiaohuikuan = null;
            $scope.data.todayShexiaojieyu =null;
            $scope.data.todayYuejiehuikuan = null;
            $scope.data.todayYuejiejieyu = null;
            $scope.data.yesterdayShexiaojieyu= null;
            $scope.data.yesterdayYuejiejieyu= null;
            $scope.data.todaySalesCash= null;
            $scope.data.yesterdaySalesCash= null;
            $scope.data.yesterdayShexiaohuikuan= null;
            $scope.data.yesterdayYuejiehuikuan= null;
            searchData();
            //传递门店日报表查询事件
            var queryParamsDailySales = {
                departmentCode:$scope.q.liableDepartmentCode,
                dayStartTime:$scope.dailyData.startTime,
                dayEndTime:$scope.dailyData.endTime,
                monthStartTime:$scope.monthlyData.startTime,
                monthEndTime:$scope.monthlyData.endTime,
            };
            emitDailySalesQuery(queryParamsDailySales);
        };

        var searchData = function () {
            if(($scope.q.liableDepartmentCode!=null)&&($scope.dailyData.startTime!=null)&& ($scope.dailyData.endTime!=null)
                &&($scope.monthlyData.startTime!=null)&& ($scope.monthlyData.endTime!=null))
            {
                //现金
                var queryParams1 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    payType:1,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };

                FinanceService.searchSalesByBayType(queryParams1).then(function (salesByPay) {

                    $scope.q.sumCashSum = null;
                    $scope.q.sumCashCount = null;

                    var salesByPayCashList = salesByPay.items;
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.specTypesList.length; i++) {
                            for (var k = 0; k < salesByPayCashList.length; k++) {
                                if ($scope.data.specTypesList[i].code==salesByPayCashList[k].specCode) {
                                    $scope.data.specTypesList[i].salesByPayCash = salesByPayCashList[k];
                                    $scope.q.sumCashSum +=  $scope.data.specTypesList[i].salesByPayCash.sum;
                                    $scope.q.sumCashCount += $scope.data.specTypesList[i].salesByPayCash.count;
                                }
                            }
                        }
                    }
                });

                //电子支付（扫码）
                var queryParams = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    payType:0,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                FinanceService.searchSalesByBayType(queryParams).then(function (salesByPay) {
                    $scope.q.sumPayOnlineCount = null;
                    $scope.q.sumPayOnlineSum = null;

                    var salesByPayOnlineList = salesByPay.items;
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.specTypesList.length; i++) {
                                for (var k = 0; k < salesByPayOnlineList.length; k++) {

                                    if ($scope.data.specTypesList[i].code==salesByPayOnlineList[k].specCode) {
                                        $scope.data.specTypesList[i].salesByPayOnline = salesByPayOnlineList[k];
                                        $scope.q.sumPayOnlineCount += $scope.data.specTypesList[i].salesByPayOnline.count;
                                        $scope.q.sumPayOnlineSum += $scope.data.specTypesList[i].salesByPayOnline.sum;
                                    }
                                }

                        }
                    }
                });

                //赊销
                var queryParams2 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    payType:2,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                FinanceService.searchSalesByBayType(queryParams2).then(function (salesByPay) {
                    $scope.q.sumCreditCount = null;
                    $scope.q.sumCreditSum = null;

                    var salesByPayCreditList = salesByPay.items;
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.specTypesList.length; i++) {

                                for (var k = 0; k < salesByPayCreditList.length; k++) {
                                    if ($scope.data.specTypesList[i].code==salesByPayCreditList[k].specCode) {
                                        $scope.data.specTypesList[i].salesByPayCredit = salesByPayCreditList[k];

                                        $scope.q.sumCreditCount += $scope.data.specTypesList[i].salesByPayCredit.count;
                                        $scope.q.sumCreditSum += $scope.data.specTypesList[i].salesByPayCredit.sum;
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
                    $scope.q.sumYuejieCount = null;
                    $scope.q.sumYuejieSum = null;

                    var salesByPayMonthlySumList = salesByPay.items;
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.specTypesList.length; i++) {
                                for (var k = 0; k < salesByPayMonthlySumList.length; k++) {
                                    if ($scope.data.specTypesList[i].code==salesByPayMonthlySumList[k].specCode) {
                                        $scope.data.specTypesList[i].salesByPayMonthlySum = salesByPayMonthlySumList[k];

                                        $scope.q.sumYuejieCount +=  $scope.data.specTypesList[i].salesByPayMonthlySum.count;
                                        $scope.q.sumYuejieSum += $scope.data.specTypesList[i].salesByPayMonthlySum.sum;

                                    }
                                }

                        }
                    }
                    //console.info("");
                    //console.info( $scope.sumYuejieCount );
                })

                //气票
                var queryParams4 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    payType:4,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };

                FinanceService.searchSalesByBayType(queryParams4).then(function (salesByPay) {
                    $scope.q.sumTicketCount = null;
                    $scope.q.sumTicketSum = null;

                    var salesByPayTicketList = salesByPay.items;
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.specTypesList.length; i++) {

                                for (var k = 0; k < salesByPayTicketList.length; k++) {
                                    if ($scope.data.specTypesList[i].code==salesByPayTicketList[k].specCode) {
                                        $scope.data.specTypesList[i].salesByPayTicket = salesByPayTicketList[k];

                                        $scope.q.sumTicketCount +=  $scope.data.specTypesList[i].salesByPayTicket.count;
                                        $scope.q.sumTicketSum += $scope.data.specTypesList[i].salesByPayTicket.sum;
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
                   $scope.q.sumCouponCount = null;

                    var salesByPayCouponList = salesByPay.items;
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.specTypesList.length; i++) {
                                for (var k = 0; k < salesByPayCouponList.length; k++) {
                                    if ($scope.data.specTypesList[i].code==salesByPayCouponList[k].specCode) {
                                        $scope.data.specTypesList[i].salesByPayCoupon = salesByPayCouponList[k];

                                        $scope.q.sumCouponCount += $scope.data.specTypesList[i].salesByPayCoupon.count;

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
                //console.info(queryParams6);
                FinanceService.searchSalesByBayType(queryParams6).then(function (salesByPay) {
                    $scope.q.sumYueleijiCount = null;
                    $scope.q.sumYueleijiSum = null;

                    var salesByPayMonthlySumSalesList = salesByPay.items;
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.specTypesList.length; i++) {
                                for (var k = 0; k < salesByPayMonthlySumSalesList.length; k++) {
                                    if ($scope.data.specTypesList[i].code==salesByPayMonthlySumSalesList[k].specCode) {
                                        $scope.data.specTypesList[i].salesByMonthlySumSales = salesByPayMonthlySumSalesList[k];

                                        $scope.q.sumYueleijiCount += $scope.data.specTypesList[i].salesByMonthlySumSales.count;
                                        $scope.q.sumYueleijiSum += $scope.data.specTypesList[i].salesByMonthlySumSales.sum;
                                    }
                                }

                        }
                    }
                })

                //销售往来日报表查询:今日赊销回款
                var queryParams7 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:0,
                    writeOffType:1,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };

                FinanceService.searchSaleContacts(queryParams7).then(function (SaleContacts) {
                    $scope.data.todayShexiaohuikuan = SaleContacts.items[0];
                   //console.info("今日赊销回款");
                   // console.info( $scope.data.todayShexiaohuikuan);
                })
                //销售往来日报表查询:今日赊销结余
                var queryParams8 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:0,
                    writeOffType:0,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };

                FinanceService.searchSaleContacts(queryParams8).then(function (SaleContacts) {

                    $scope.data.todayShexiaojieyu = SaleContacts.items[0];
                    //console.info("今日赊销结余");
                    //console.info($scope.data.todayShexiaojieyu);
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
                    //console.info("今日月结回款");
                    //console.info($scope.data.todayYuejiehuikuan);
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
                    //console.info("今日月结结余");
                    //console.info($scope.data.todayYuejiejieyu);
                })

                //销售往来日报表查询:上日赊销回款
                var queryParams20 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:0,
                    writeOffType:1,
                    startTime:$scope.yesterdayData.startTime,
                    endTime:$scope.yesterdayData.endTime,
                };

                FinanceService.searchSaleContacts(queryParams20).then(function (SaleContacts) {

                    $scope.data.yesterdayShexiaohuikuan = SaleContacts.items[0];
                    //console.info("上日赊销回款");
                    //console.info( $scope.data.yesterdayShexiaohuikuan);
                })


                //销售往来日报表查询:上日赊销结余
                var queryParams11 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:0,
                    writeOffType:0,
                    startTime:$scope.yesterdayData.startTime,
                    endTime:$scope.yesterdayData.endTime,
                };

                FinanceService.searchSaleContacts(queryParams11).then(function (SaleContacts) {

                    $scope.data.yesterdayShexiaojieyu = SaleContacts.items[0];
                })

                //销售往来日报表查询:上日月结回款
                var queryParams22 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:1,
                    writeOffType:1,
                    startTime:$scope.yesterdayData.startTime,
                    endTime:$scope.yesterdayData.endTime,
                };

                FinanceService.searchSaleContacts(queryParams22).then(function (SaleContacts) {

                    $scope.data.yesterdayYuejiehuikuan = SaleContacts.items[0];
                    //console.info("上日月结回款");
                    //console.info($scope.data.yesterdayYuejiehuikuan);
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

                FinanceService.searchSalesCash(paramsCash1).then(function (salesCash) {

                    $scope.data.todaySalesCash = salesCash;
                    //console.info($scope.data.todaySalesCash);
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
                    //console.info($scope.data.yesterdaySalesCash);
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
                    $scope.q.liableDepartmentName  = result.name;
                    console.info($scope.q.liableDepartmentCode);
                    //searchBottles();
                }
            })
        };

        //权限控制代码
        var rolesControl = function () {
            //初始化当前用户
            var curUser = sessionStorage.getCurUser();
            if(curUser.userGroup.id==5){
                $scope.q.liableDepartmentCode = curUser.department.code;

                $scope.q.liableDepartmentName = curUser.department.name;
                $scope.q.isControled = true;
            }
        };

        var init = function () {
            var queryParams = {};
            FinanceService.retrieveGasCylinderSpecUri(queryParams).then(function (specTypes) {
                $scope.data.specTypesList = specTypes.items;
            });

            $scope.timer = $interval(function(){
                calculateCountSumSum()
            }, 50);

            //权限控制代码
            rolesControl();

        };
        init();

        //计算数量、金额合计
        var calculateCountSumSum = function () {
            $scope.q.sumCount = 0;
            $scope.q.sumSum = 0;
            for(var i = 0; i < $scope.data.specTypesList.length; i++) {
                    if(($scope.data.specTypesList[i].salesByPayCash!=null)&&($scope.data.specTypesList[i].salesByPayOnline!=null)
                        &&($scope.data.specTypesList[i].salesByPayCredit!=null)&&($scope.data.specTypesList[i].salesByPayMonthlySum!=null)
                        &&($scope.data.specTypesList[i].salesByPayTicket!=null)&&($scope.data.specTypesList[i].salesByPayCoupon!=null)
                        &&($scope.data.specTypesList[i].salesByMonthlySumSales!=null))
                    {
                        $scope.q.sumCount = $scope.q.sumCount+$scope.data.specTypesList[i].salesByPayCash.count + $scope.data.specTypesList[i].salesByPayOnline.count
                            + $scope.data.specTypesList[i].salesByPayCredit.count + $scope.data.specTypesList[i].salesByPayMonthlySum.count
                            + $scope.data.specTypesList[i].salesByPayTicket.count + $scope.data.specTypesList[i].salesByPayCoupon.count
                            + $scope.data.specTypesList[i].salesByMonthlySumSales.count;

                        $scope.q.sumSum = $scope.q.sumSum+$scope.data.specTypesList[i].salesByPayCash.sum + $scope.data.specTypesList[i].salesByPayOnline.sum
                            + $scope.data.specTypesList[i].salesByPayCredit.sum + $scope.data.specTypesList[i].salesByPayMonthlySum.sum
                            + $scope.data.specTypesList[i].salesByPayTicket.sum + $scope.data.specTypesList[i].salesByPayCoupon.sum
                            + $scope.data.specTypesList[i].salesByMonthlySumSales.sum;
                    }

            }
        };


        //传递门店日报表查询事件
        function emitDailySalesQuery(queryParamsDailySales){
            //传递门店日报表查询事件
            $rootScope.$broadcast("Event_DailySalesQuery", queryParamsDailySales);
        };




    }]);
