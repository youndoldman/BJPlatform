/**
 * Created by Administrator on 2018/7/2.
 */
'use strict';

shopManageApp.controller('CalculateDeliveryCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'ShopStockService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                                   rootService, pager, udcModal, ShopStockService,sessionStorage) {
        $(function () {
            $('#datetimepickerDailyTimeStart').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss',
                locale: moment.locale('zh-cn'),
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            $('#datetimepickerDailyTimeEnd').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss',
                locale: moment.locale('zh-cn'),
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
            deliveryWorkerId:null,
            liableDepartmentCode: null,
            liableDepartmentName:null,
            sumDailyCount:null,
            sumMonthlyCount:null,

        };
        $scope.data = {
            currentTime:null,
            selectedGoodsType:{},
            selectedGoods:{},
            goodsTypesList:[],

            goodsList:[],


        };
        $scope.vm = {
            userList: [],
            selectedUser:{},
            curUser:null,
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
                    console.info(result);
                    searchUsers($scope.q.liableDepartmentCode);
                }
            })
        };


        $scope.deliveryWorkerIdChange = function () {
            console.info($scope.vm.selectedUser);
        };

        $scope.search = function () {
            for(var i = 0; i < $scope.data.goodsList.length; i++) {
                for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                    $scope.data.goodsList[i].detail[j].salesByPayDaily= null;
                    $scope.data.goodsList[i].detail[j].salesByPayMonthly= null;
                }
            }
            $scope.q.sumDailyCount = null;
            $scope.q.sumMonthlyCount = null;
            searchData();
        };

        var searchData = function () {
            if(($scope.q.liableDepartmentCode!=null)&&($scope.vm.selectedUser!=null)&&($scope.dailyData.startTime!=null)&& ($scope.dailyData.endTime!=null)
                &&($scope.monthlyData.startTime!=null)&& ($scope.monthlyData.endTime!=null))
            {
                //查询配送工今日配送量
                var queryParams1 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    startTime:$scope.dailyData.startTime,
                    endTime:$scope.dailyData.endTime,
                    dispatchUserId:$scope.vm.selectedUser.userId,
                };

                ShopStockService.searchSalesByBayType(queryParams1).then(function (salesByPay) {
                    $scope.q.sumDailyCount = null;

                    var salesByPayList = salesByPay.items;
                    if(salesByPay.items.length>0)
                    {
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayDaily = salesByPayList[k];
                                        //数量
                                        $scope.q.sumDailyCount +=  $scope.data.goodsList[i].detail[j].salesByPayDaily.count;
                                    }
                                }
                            }
                        }
                    }
                    //console.log($scope.data.goodsList);
                })

                //查询配送工月累计配送量
                var queryParams2 = {
                    departmentCode:$scope.q.liableDepartmentCode,
                    startTime:$scope.monthlyData.startTime,
                    endTime:$scope.monthlyData.endTime,
                    dispatchUserId:$scope.vm.selectedUser.userId,
                };

                ShopStockService.searchSalesByBayType(queryParams2).then(function (salesByPay) {
                    $scope.q.sumMonthlyCount = null

                    var salesByPayList = salesByPay.items;
                    if(salesByPay.items.length>0)
                    {
                        for(var i = 0; i < $scope.data.goodsList.length; i++) {
                            for(var j = 0; j < $scope.data.goodsList[i].detail.length; j++) {
                                for (var k = 0; k < salesByPayList.length; k++) {
                                    if ($scope.data.goodsList[i].detail[j].code==salesByPayList[k].specCode) {
                                        $scope.data.goodsList[i].detail[j].salesByPayMonthly = salesByPayList[k];
                                        //数量
                                        $scope.q.sumMonthlyCount +=  $scope.data.goodsList[i].detail[j].salesByPayMonthly.count;

                                    }
                                }
                            }
                        }
                    }
                   // console.log($scope.data.goodsList);
                })

            }
            else {
                udcModal.info({"title": "提醒", "message": "请选择部门、配送工ID、今日起始时间和月累计起始时间！"});
            }

        };

        var searchUsers = function (param) {
            var searchParam = {
                groupCode:"00003",//配送工组号
                departmentCode:param
            }

            ShopStockService.retrieveUsers(searchParam).then(function (users) {
                $scope.vm.userList = _.map(users.items, ShopStockService.toViewModel);
                console.log($scope.vm.userList);
                $scope.vm.selectedUser = $scope.vm.userList[0];
            });
        };

        var init = function () {
            $scope.vm.curUser = sessionStorage.getCurUser();
            console.info($scope.vm.curUser);
            $scope.q.liableDepartmentCode = $scope.vm.curUser.department.code;
            $scope.q.liableDepartmentName = $scope.vm.curUser.department.name;

            var queryParams = {};
            ShopStockService.retrieveGoodsTypes(queryParams).then(function (goodsTypes) {
                $scope.data.goodsTypesList = goodsTypes.items;
                $scope.data.selectedGoodsType = $scope.data.goodsTypesList[0];
                //console.info($scope.data.goodsTypesList);

                for(var i = 0; i < $scope.data.goodsTypesList.length; i++)
                {
                    var queryParams = {
                        typeCode: $scope.data.goodsTypesList[i].code,
                    };
                    ShopStockService.retrieveGoods(queryParams).then(function (goods) {
                        var tempList = {type:null,detail:[]};
                        tempList.detail = goods.items;
                        if(goods.items.length > 0){
                            tempList.type = goods.items[0].goodsType.name;
                        }
                        $scope.data.goodsList.push(tempList);
                       // console.info($scope.data.goodsList)
                    });
                }
                //console.log($scope.data.goodsList);
            })

            searchUsers($scope.q.liableDepartmentCode);

        };
        init();


    }]);
