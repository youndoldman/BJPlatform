/**
 * Created by Administrator on 2018/3/28.
 */
'use strict';

financeApp.controller('storeDailySalesByWeightCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'sessionStorage','FinanceService','$interval',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                                rootService, pager, udcModal,sessionStorage,FinanceService,$interval) {

        $scope.data = {
            customerTypesList:[],
            reportDayCash:[],//日报表缓存
            reportMonthCash:[],//月报表缓存
        };

        var init = function () {

        };


        $scope.search = function (m) {
            $scope.data.reportDayCash = [];

            //日报表

            //微信线上
            var queryParams1 = {
                payType:"PTOnLine",
                departmentCode: m.departmentCode,
                startTime:m.dayStartTime,
                endTime:m.dayEndTime,
                payStatus:"PSPaied"
            };
            FinanceService.retrieveSaleByWeightReport(queryParams1).then(function (report) {
                $scope.data.reportDayCash[0] = reportSortByCustomerType(report);
            });
            //微信线下
            var queryParams2 = {
                payType:"PTWxOffLine",
                departmentCode: m.departmentCode,
                startTime:m.dayStartTime,
                endTime:m.dayEndTime,
                payStatus:"PSPaied"
            };
            FinanceService.retrieveSaleByWeightReport(queryParams2).then(function (report) {
                $scope.data.reportDayCash[1] = reportSortByCustomerType(report);
            });
            //现金
            var queryParams3 = {
                payType:"PTCash",
                departmentCode: m.departmentCode,
                startTime:m.dayStartTime,
                endTime:m.dayEndTime,
                payStatus:"PSPaied"
            };
            FinanceService.retrieveSaleByWeightReport(queryParams3).then(function (report) {
                $scope.data.reportDayCash[2] = reportSortByCustomerType(report);
            });
            //赊销
            var queryParams4 = {
                payType:"PTDebtCredit",
                departmentCode: m.departmentCode,
                startTime:m.dayStartTime,
                endTime:m.dayEndTime,
                payStatus:"PSPaied"
            };
            FinanceService.retrieveSaleByWeightReport(queryParams4).then(function (report) {
                $scope.data.reportDayCash[3] = reportSortByCustomerType(report);
            });
            //月结
            var queryParams5 = {
                payType:"PTMonthlyCredit",
                departmentCode: m.departmentCode,
                startTime:m.dayStartTime,
                endTime:m.dayEndTime,
                payStatus:"PSPaied"
            };
            FinanceService.retrieveSaleByWeightReport(queryParams5).then(function (report) {
                $scope.data.reportDayCash[4] = reportSortByCustomerType(report);
            });

            //月报表


            //微信线上
            var queryParams11 = {
                payType:"PTOnLine",
                departmentCode: m.departmentCode,
                startTime:m.monthStartTime,
                endTime:m.monthEndTime,
                payStatus:"PSPaied"
            };
            FinanceService.retrieveSaleByWeightReport(queryParams11).then(function (report) {
                $scope.data.reportMonthCash[0] = reportSortByCustomerType(report);
            });

            //微信线下
            var queryParams12 = {
                payType:"PTWxOffLine",
                departmentCode: m.departmentCode,
                startTime:m.monthStartTime,
                endTime:m.monthEndTime,
                payStatus:"PSPaied"
            };
            FinanceService.retrieveSaleByWeightReport(queryParams12).then(function (report) {
                $scope.data.reportMonthCash[1] = reportSortByCustomerType(report);
            });
            //现金
            var queryParams13 = {
                payType:"PTCash",
                departmentCode: m.departmentCode,
                startTime:m.monthStartTime,
                endTime:m.monthEndTime,
                payStatus:"PSPaied"
            };
            FinanceService.retrieveSaleByWeightReport(queryParams13).then(function (report) {
                $scope.data.reportMonthCash[2] = reportSortByCustomerType(report);
            });
            //赊销
            var queryParams14 = {
                payType:"PTDebtCredit",
                departmentCode: m.departmentCode,
                startTime:m.monthStartTime,
                endTime:m.monthEndTime,
                payStatus:"PSPaied"
            };
            FinanceService.retrieveSaleByWeightReport(queryParams14).then(function (report) {
                $scope.data.reportMonthCash[3] = reportSortByCustomerType(report);
            });
            //月结
            var queryParams15 = {
                payType:"PTMonthlyCredit",
                departmentCode: m.departmentCode,
                startTime:m.monthStartTime,
                endTime:m.monthEndTime,
                payStatus:"PSPaied"
            };
            FinanceService.retrieveSaleByWeightReport(queryParams15).then(function (report) {
                $scope.data.reportMonthCash[4] = reportSortByCustomerType(report);
            });
        };

        var reportSortByCustomerType = function (report) {
            var sortedReport = {};
            if(report.items.length==0){
                return sortedReport;
            }

            var sumReport = {"saleWeight":0,"saleSum":0,"refoundWeight":0,"refoundSum":0};
            for(var i=0; i<$scope.data.customerTypesList.length;i++){
                var find = false;
                for(var j=0; j<report.items.length;j++){
                    if(report.items[j].customerTypeCode == $scope.data.customerTypesList[i].code){
                        sortedReport[report.items[j].customerTypeCode] = report.items[j];
                        find = true;
                    }
                }
                if(!find){
                    continue;
                }
                sumReport.saleWeight+=sortedReport[$scope.data.customerTypesList[i].code].saleWeight;
                sumReport.saleSum+=sortedReport[$scope.data.customerTypesList[i].code].saleSum;
                sumReport.refoundWeight+=sortedReport[$scope.data.customerTypesList[i].code].refoundWeight;
                sumReport.refoundSum+=sortedReport[$scope.data.customerTypesList[i].code].refoundSum;
            }
            sortedReport["sum"] = sumReport;
            console.log(sortedReport);

            //累加合计

            return sortedReport;
        };


        $scope.printPage = function () {
            window.print();
        };



        var init = function () {
            var queryParams = {};
            FinanceService.searchCustomerType(queryParams).then(function (customerTypes) {
                $scope.data.customerTypesList = customerTypes.items;
            });

        };
        init();



//监听来电事件
        $scope.$on("Event_DailySalesQuery", function(e, m) {
            $scope.search(m);
        });



        //四舍五入，取两位小数
        $scope.getFixData = function (data) {
            if(data==null){
                return "";
            }
            var f =  Math.round(data * 100) / 100;
            var s = f.toString();
            var rs = s.indexOf('.');
            if (rs < 0) {
                rs = s.length;
                s += '.';
            }
            while (s.length <= rs + 2) {
                s += '0';
            }
            return s;

        };
    }]);
