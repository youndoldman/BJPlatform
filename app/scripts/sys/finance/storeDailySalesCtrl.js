/**
 * Created by Administrator on 2018/3/28.
 */
'use strict';

financeApp.controller('storeDailySalesCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'sessionStorage','FinanceService',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                    rootService, pager, udcModal,sessionStorage,FinanceService) {

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
            yesterdayYuejiejieyu:[]
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
            liableDepartmentCode: null
        };


        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchData();
        };



        var searchData = function () {
            if(($scope.q.liableDepartmentCode.length>0)&&($scope.dailyData.startTime.length>0)&& ($scope.dailyData.endTime.length>0)
                &&($scope.monthlyData.startTime.length>0)&& ($scope.monthlyData.endTime.length>0))
            {
                //现金
                var queryParams1 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    payType:1,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                //console.info(queryParams1);
                FinanceService.searchSalesByBayType(queryParams1).then(function (salesByPay) {
                    var salesByPayCashList = salesByPay.items;
                    console.info(salesByPayCashList);
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayCashList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayCashList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayCash = salesByPayCashList[k];
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
                    //console.log(salesByPayOnlineList);
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayOnlineList.length; k++) {
                                    //console.log($scope.data.goodsList[i].detail[j].code+"    "+salesByPayOnlineList[k].specCode);
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayOnlineList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayOnline = salesByPayOnlineList[k];
                                        //console.log($scope.data.goodsList[i].detail[j]);
                                    }
                                }
                            }
                        }
                    }
                    //console.log($scope.data.goodsList);
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
                    //console.info(salesByPayCreditList);
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayCreditList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayCreditList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayCredit = salesByPayCreditList[k];
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
                   // console.info(salesByPayMonthlySumList);
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayMonthlySumList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayMonthlySumList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayMonthlySum = salesByPayMonthlySumList[k];
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
                    //console.info(salesByPayTicketList);
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayTicketList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayTicketList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayTicket = salesByPayTicketList[k];
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
                   // console.info(salesByPayCouponList);
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayCouponList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayCouponList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayCoupon = salesByPayCouponList[k];
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
                    console.info("月累计销售");
                    console.info(salesByPayMonthlySumSalesList);
                    if(salesByPay.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayMonthlySumSalesList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayMonthlySumSalesList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByMonthlySumSales = salesByPayMonthlySumSalesList[k];
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
                console.info(queryParams7);
                FinanceService.searchSaleContacts(queryParams7).then(function (SaleContacts) {
                    $scope.data.todayShexiaohuikuan = SaleContacts.items[0];
                    console.info("今日赊销回款");
                    console.info($scope.data.todayShexiaohuikuan);

                })
                //销售往来日报表查询:今日赊销结余
                var queryParams8 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:0,
                    writeOffType:1,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                console.info(queryParams8);
                FinanceService.searchSaleContacts(queryParams8).then(function (SaleContacts) {
                    $scope.data.todayShexiaojieyu = SaleContacts.items[0];
                    console.info("今日赊销结余");
                    console.info($scope.data.todayShexiaojieyu);

                })

                //销售往来日报表查询:今日月结回款
                var queryParams9 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:1,
                    writeOffType:1,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                console.info(queryParams9);
                FinanceService.searchSaleContacts(queryParams9).then(function (SaleContacts) {
                    $scope.data.todayYuejiehuikuan = SaleContacts.items[0];
                    console.info("今日月结回款");
                    console.info($scope.data.todayYuejiehuikuan);
                })

                //销售往来日报表查询:今日月结结余
                var queryParams10 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:1,
                    writeOffType:0,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                console.info(queryParams10);
                FinanceService.searchSaleContacts(queryParams10).then(function (SaleContacts) {
                    $scope.data.todayYuejiejieyu = SaleContacts.items[0];
                    console.info("今日月结结余");
                    console.info( $scope.data.todayYuejiejieyu);
                })


                //销售往来日报表查询:上日赊销结余
                var queryParams11 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:0,
                    writeOffType:1,
                    startTime:$scope.yesterdayData.startTime,
                    endTime:$scope.yesterdayData.endTime,
                };
                console.info(queryParams11);
                FinanceService.searchSaleContacts(queryParams11).then(function (SaleContacts) {
                    $scope.data.yesterdayShexiaojieyu = SaleContacts.items[0];
                    console.info("上日赊销结余");
                    console.info($scope.data.yesterdayShexiaojieyu);
                })

                //销售往来日报表查询:上日月结结余
                var queryParams12 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    creditType:1,
                    writeOffType:0,
                    startTime:$scope.yesterdayData.startTime,
                    endTime:$scope.yesterdayData.endTime,
                };
                console.info(queryParams12);
                FinanceService.searchSaleContacts(queryParams12).then(function (SaleContacts) {
                    $scope.data.yesterdayYuejiejieyu = SaleContacts.items[0];
                    console.info("上日月结结余");
                    console.info($scope.data.yesterdayYuejiejieyu);
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
            //var currentDate = new Date();
            //var currentMonth = currentDate.getMonth()+1;
            //$scope.data.currentTime = currentDate.getFullYear()+"  年  "+currentMonth+"  月  "+currentDate.getDate()+"  日  "
            //    +currentDate.getHours()+"   时  "+currentDate.getMinutes()+"  分  "+currentDate.getSeconds()+"  秒  ";

            //console.log($scope.data.currentTime);
            //默认结束时间为当前时间
            //$scope.q.currentTime = $sgoodsTypesListcope.data.currentTime;

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
            });
        };
        init();


        var ipts ;
        $('#tb').change(function () {
           var sum = 0, v;
           $('#tb tr').each(function () {
                //ipts = $(this).find('input');
                v = (parseFloat(ipts.eq(0).val()) || 0) * (parseFloat(ipts.eq(1).val()) || 0)
                ipts.eq(2).val(v);
               sum += v;
            });
            $('#total').val(sum);
        });
    }]);
