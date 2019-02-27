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
            isControled:false

        };
        $scope.data = {
            currentTime:null,
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
                        for(var i = 0; i < $scope.data.gasCylinderSpecsList.length; i++) {

                                for (var k = 0; k < salesByPayList.length; k++) {
                                    if ($scope.data.gasCylinderSpecsList[i].code==salesByPayList[k].specCode) {
                                        $scope.data.gasCylinderSpecsList[i].salesByPayDaily = salesByPayList[k];
                                        //数量
                                        $scope.q.sumDailyCount +=  $scope.data.gasCylinderSpecsList[i].salesByPayDaily.count;
                                    }
                                }

                        }
                    }

                });

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
                        for(var i = 0; i < $scope.data.gasCylinderSpecsList.length; i++) {
                                for (var k = 0; k < salesByPayList.length; k++) {
                                    if ($scope.data.gasCylinderSpecsList[i].code==salesByPayList[k].specCode) {
                                        $scope.data.gasCylinderSpecsList[i].salesByPayMonthly = salesByPayList[k];
                                        //数量
                                        $scope.q.sumMonthlyCount +=  $scope.data.gasCylinderSpecsList[i].salesByPayMonthly.count;

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
            ShopStockService.retrieveGasCylinderSpecUri(queryParams).then(function (gasCylinderSpecs) {
                $scope.data.gasCylinderSpecsList = gasCylinderSpecs.items;
            });

            searchUsers($scope.q.liableDepartmentCode);

            //权限控制代码
            rolesControl();

            //初始化时间为当天
            var date = new Date();
            var month = date.getMonth()+1;
            $scope.dailyData.startTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" 00:00:00";
            $scope.dailyData.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" 23:59:59";

            $scope.monthlyData.startTime = date.getFullYear()+"-"+month+"-"+"01"+" 00:00:00";
            $scope.monthlyData.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" 23:59:59";

        };

        //权限控制代码
        var rolesControl = function () {
            //初始化当前用户
            var curUser = sessionStorage.getCurUser();
            if(curUser.userGroup.id==5){
                $scope.q.isControled = true;
            }
        };


        init();


    }]);
