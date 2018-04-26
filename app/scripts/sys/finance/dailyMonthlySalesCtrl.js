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
            liableDepartmentCode: null,

            dailyCountZhuzhai:null,
            dailySumZhuzhai:null,
            dailyCountCanyin:null,
            dailySumCanyin:null,

            monthlyCountZhuzhai:null,
            monthlySumZhuzhai:null,
            monthlyCountCanyin:null,
            monthlySumCanyin:null,
        };
        $scope.data = {
            currentTime:null,
            selectedGoodsType:{},
            selectedGoods:{},
            goodsTypesList:[],
            goodsList:[],
            customerTypesList:[],
        };
        var rows;
        var cells;

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


        $scope.pager = pager.init('dailyMonthlySalesCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            //searchData();
        };

        $scope.search = function () {
            searchData();
        };

        $scope.printPage = function () {
            window.print();
        };

        var searchData = function () {
            if(($scope.q.liableDepartmentCode.length>0)&&($scope.dailyData.startTime.length>0)&& ($scope.dailyData.endTime.length>0)
                &&($scope.monthlyData.startTime.length>0)&& ($scope.monthlyData.endTime.length>0))
            {

                //今日实际销售 普通住宅客户:00001
                var queryParams1 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    cstTypeCode:'00001',
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                console.info("今日实际销售 普通住宅客户");

                FinanceService.searchSalesByCustomerType(queryParams1).then(function (salesByCustomerType) {
                    var salesByCustomerTypeList = salesByCustomerType.items;
                    //console.info(salesByCustomerTypeList);

                    $scope.q.dailyCountZhuzhai = null;
                    $scope.q.dailySumZhuzhai = null;

                    if(salesByCustomerType.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByCustomerTypeList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByCustomerTypeList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByCustomerTypeDailyZhuzhai = salesByCustomerTypeList[k];

                                        $scope.q.dailyCountZhuzhai += $scope.data.goodsList[i].detail[j].salesByCustomerTypeDailyZhuzhai.count;
                                        $scope.q.dailySumZhuzhai += $scope.data.goodsList[i].detail[j].salesByCustomerTypeDailyZhuzhai.sum;


                                    }
                                }
                            }
                        }
                    }
                })

                //今日实际销售 餐饮用户:00002
                var queryParams2 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    cstTypeCode:'00002',
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                };
                //console.info("今日实际销售 餐饮用户");

                FinanceService.searchSalesByCustomerType(queryParams2).then(function (salesByCustomerType) {
                    $scope.q.dailyCountCanyin = null;
                    $scope.q.dailySumCanyin = null;

                    var salesByCustomerTypeList = salesByCustomerType.items;
                    //console.info(salesByCustomerTypeList);
                    if(salesByCustomerType.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByCustomerTypeList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByCustomerTypeList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByCustomerTypeDailyCanyin = salesByCustomerTypeList[k];

                                        $scope.q.dailyCountCanyin += $scope.data.goodsList[i].detail[j].salesByCustomerTypeDailyCanyin.count;
                                        $scope.q.dailySumCanyin += $scope.data.goodsList[i].detail[j].salesByCustomerTypeDailyCanyin.sum;

                                    }
                                }
                            }
                        }
                    }
                })

                //月累计销售 普通住宅客户:00001
                var queryParams3 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    cstTypeCode:'00001',
                    startTime:$scope.monthlyData.startTime,
                    endTime:$scope.monthlyData.endTime,
                };
                //console.info("月累计 普通住宅客户");

                FinanceService.searchSalesByCustomerType(queryParams3).then(function (salesByCustomerType) {
                    $scope.q.monthlyCountZhuzhai = null;
                    $scope.q.monthlySumZhuzhai = null;
                    var salesByCustomerTypeList = salesByCustomerType.items;
                    //console.info(salesByCustomerTypeList);
                    if(salesByCustomerType.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByCustomerTypeList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByCustomerTypeList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByCustomerTypeMonthlyZhuzhai = salesByCustomerTypeList[k];

                                        $scope.q.monthlyCountZhuzhai += $scope.data.goodsList[i].detail[j].salesByCustomerTypeMonthlyZhuzhai.count;
                                        $scope.q.monthlySumZhuzhai += $scope.data.goodsList[i].detail[j].salesByCustomerTypeMonthlyZhuzhai.sum;

                                    }
                                }
                            }
                        }
                    }
                })

                //月累计 餐饮用户:00002
                var queryParams4 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    cstTypeCode:'00002',
                    startTime:$scope.monthlyData.startTime,
                    endTime:$scope.monthlyData.endTime,
                };
                //console.info("月累计 餐饮用户");

                FinanceService.searchSalesByCustomerType(queryParams4).then(function (salesByCustomerType) {
                    $scope.q.monthlyCountCanyin = null;
                    $scope.q.monthlySumCanyin = null;

                    var salesByCustomerTypeList = salesByCustomerType.items;
                    //console.info(salesByCustomerTypeList);
                    if(salesByCustomerType.items.length>0){
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByCustomerTypeList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByCustomerTypeList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByCustomerTypeMonthlyCanyin = salesByCustomerTypeList[k];

                                        $scope.q.monthlyCountCanyin += $scope.data.goodsList[i].detail[j].salesByCustomerTypeMonthlyCanyin.count;
                                        $scope.q.monthlySumCanyin += $scope.data.goodsList[i].detail[j].salesByCustomerTypeMonthlyCanyin.sum;
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


            var tab = document.getElementById("dailyMonthlySalesTable") ;
            //表格行数
            rows = tab.rows.length ;
            //表格列数
            cells = tab.rows.item(0).cells.length ;
            console.info("行数"+rows+"列数"+cells);
        };
        init();



        /*
         * 表格求和。
         * table 表示当前求和的表格
         * trs  表示表格的所有行
         * startRow  表示开始的行数
         *  startColumn  表示开始的列数
         *  endColumn   表示求和结束的列数
         * */

        function sumFun(table,trs,startRow,startColumn,endColumn,money){
            for(var j = startColumn;j<endColumn;j++){
                sum(table,trs,startRow,j,money);
            }
        }
        function sum(table,trs,startRow,tartColumn,money){
            var total= 0,
                end=trs.length-1;//忽略最后合计的一行;
            for(var i=startRow;i<end;i++){
                var td=trs[i].getElementsByTagName('td')[tartColumn];
                var t=parseFloat(td.innerHTML);
                if(t)total+=t;
            }
            money==true ? (total  = total.toFixed(2)) : total;
            //total = total.toFixed(2);
            if(total!=0){
                trs[end].getElementsByTagName('td')[tartColumn].innerHTML=total;
            }else{
                trs[end].getElementsByTagName('td')[tartColumn].innerHTML="---";
            }

            //console.log(tartColumn+"-------"+total);
        }

    }]);
