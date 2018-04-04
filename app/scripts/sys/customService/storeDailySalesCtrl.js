/**
 * Created by Administrator on 2018/3/28.
 */
'use strict';

customServiceApp.controller('storeDailySalesCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'KtyService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                 rootService, pager, udcModal, KtyService,sessionStorage) {


        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchData();
        };

        $scope.pager = pager.init('storeDailySalesCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.data = {
            currentTime:null,
        };

        $scope.q = {
        };

        $scope.vm = {
            goods:{name:null, specification:null},
            yesterdayInventory: null,
            todayInQuantity:null,
            LPGSales:{
                thisMonthInQuantity:null,
                cashSales:{quantity:null, amount:null},
                onlinePaySales:{quantity:null, amount:null},
                onCreditSales:{quantity:null, amount:null},
                monthlySettle:{quantity:null, amount:null},
                gasTicket:{quantity:null, amount:null},
                coupon:null,
                quantitySum:null,
                amountSum:null,
                todayAvgPrice:null,
                monthSales:{quantity:null, amount:null},
                todayRealInventory:null
            },
            LPGSalesBalanceManagement:{
                yesterdayCreditSurplus:null,
                todayCreditPayback:null,
                todayCreditSurplus:null,
                yesterdayMonthlySurplus:null,
                todayMonthlyPayback:null,
                todayMonthlySurplus:null
            },
            LPGSalesCashManagement:{
                yesterdayCashInventory:null,
                cashXiaokuan:null,
                yesterdayCredit:null,
                yesterdayMonthlySettle:null,
                gasTicketSum:null,
                todayGoInBankSum:null,
                todayCashOnHand:null
            }
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchData();
        };

        $scope.printPage = function () {
            window.print();
        };

        var searchData = function () {
            //先登录授权，拿到token、orgId、userId
            KtyService.authenticate("58531181@qq.com","123456").then(function (response) {
                $scope.q.token = response.data.token;
                var queryParams = {
                    agentUserIds: $scope.q.userId,
                    //agentUserName: $scope.q.userName,
                    type: $scope.q.type,
                    interval: $scope.q.interval,
                    begin: $scope.q.startTime,
                    end: $scope.q.endTime
                };
                KtyService.retrieveReport2(queryParams, $scope.q.token).then(function (response) {
                    $scope.vm.dataList = response.data;
                    $scope.vm.lastDataList = $scope.vm.dataList[$scope.vm.dataList.length-1];
                    $scope.vm.dataList .splice($scope.vm.dataList.length-1, 1);
                }, function(value) {
                    //if(value.code == "40007")
                    //{
                    //    $scope.vm.dataList = null;
                    //    udcModal.info({"title": "查询失败", "message": "未找到呼叫记录"});
                    //}
                    //else if(value.code == "40021")
                    //{
                    //    $scope.vm.dataList = null;
                    //    udcModal.info({"title": "查询失败", "message": "未找到指定部门人员信息"});
                    //}
                    //else if(value.code == "40026")
                    //{
                    //    $scope.vm.dataList = null;
                    //    udcModal.info({"title": "查询失败", "message": "时间范围长度超出限制（最大三个月间隔）"});
                    //}
                    //else if(value.code == "50000")
                    //{
                    //    $scope.vm.dataList = null;
                    //    udcModal.info({"title": "查询失败", "message": "系统内部错误"});
                    //}
                });
            }, function(value) {
                //if(value.code == "40001")
                //{
                //    $scope.vm.dataList = null;
                //    udcModal.info({"title": "连接结果", "message": "用户名或密码不正确"});
                //}
                //else if(value.code == "40002")
                //{
                //    $scope.vm.dataList = null;
                //    udcModal.info({"title": "连接结果", "message": "用户名或密码为空"});
                //}
                //else if(value.code == "40003")
                //{
                //    $scope.vm.dataList = null;
                //    udcModal.info({"title": "连接结果", "message": "用户权限不正确"});
                //}
                //else if(value.code == "40004")
                //{
                //    $scope.vm.dataList = null;
                //    udcModal.info({"title": "连接结果", "message": "用户认证不存在或已过期"});
                //}
                //else if(value.code == "50000")
                //{
                //    $scope.vm.dataList = null;
                //    udcModal.info({"title": "连接结果", "message": "系统内部错误"});
                //}
            })
        };

        var init = function () {
            searchData();
            var currentDate = new Date();
            var currentMonth = currentDate.getMonth()+1;
            $scope.data.currentTime = currentDate.getFullYear()+"  年  "+currentMonth+"  月  "+currentDate.getDate()+"  日  "
                +currentDate.getHours()+"   时  "+currentDate.getMinutes()+"  分  "+currentDate.getSeconds()+"  秒  ";
            console.log($scope.data.currentTime);
            //默认结束时间为当前时间
            $scope.q.currentTime = $scope.data.currentTime;
        };
        init();
    }]);
