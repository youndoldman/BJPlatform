/**
 * Created by Administrator on 2018/3/31.
 */

'use strict';

financeApp.controller('dailyMonthlySalesCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
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
            number: null,
            liableUserId: null,
            liableDepartmentCode: null
        };
        $scope.data = {
            currentTime:null,
            selectedGoodsType:{},
            selectedGoods:{},
            goodsTypesList:[],
            goodsList:[],
            customerTypesList:[],
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
                }
            })
        };
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            //searchData();
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchData();
        };

        $scope.printPage = function () {
            window.print();
        };

        var searchData = function () {
            if(($scope.q.liableDepartmentCode.length>0)&&($scope.dailyData.startTime.length>0)&& ($scope.dailyData.endTime.length>0)
                &&($scope.monthlyData.startTime.length>0)&& ($scope.monthlyData.endTime.length>0))
            {

                //今日实际销售 居民
                var queryParams1 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    cstTypeCode:1,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                console.info("今日实际销售 居民");
                console.info(queryParams1);
                FinanceService.searchSalesByCustomerType(queryParams1).then(function (salesByCustomerType) {
                    var salesByCustomerTypeList = salesByCustomerType.items;
                    console.info(salesByCustomerTypeList);
                    if(salesByCustomerType.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByCustomerTypeList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByCustomerTypeList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByCustomerTypeDailyJuming = salesByCustomerTypeList[k];
                                    }
                                }
                            }
                        }
                    }
                })

                //今日实际销售 餐饮
                var queryParams2 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    cstTypeCode:1,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                console.info("今日实际销售 餐饮");
                console.info(queryParams2);
                FinanceService.searchSalesByCustomerType(queryParams2).then(function (salesByCustomerType) {
                    var salesByCustomerTypeList = salesByCustomerType.items;
                    console.info(salesByCustomerTypeList);
                    if(salesByCustomerType.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByCustomerTypeList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByCustomerTypeList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByCustomerTypeDailyCanyin = salesByCustomerTypeList[k];
                                    }
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
            //searchData();
            //查询商品类型规格
            var queryParams = {};
            FinanceService.retrieveGoodsTypes(queryParams).then(function (goodsTypes) {
                $scope.data.goodsTypesList = goodsTypes.items;
                //$scope.data.selectedGoodsType = $scope.data.goodsTypesList[0];
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

            //查询客户类型
            var Params = {};
            FinanceService.searchCustomerType(Params).then(function (customerTypes) {
                $scope.data.customerTypesList = customerTypes.items;
                console.info( $scope.data.customerTypesList);
            });
        };
        init();
    }]);
