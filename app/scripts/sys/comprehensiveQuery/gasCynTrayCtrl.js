'use strict';

comprehensiveQueryApp.controller('GasCynTrayCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'TicketService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                                        rootService, pager, udcModal, TicketService,sessionStorage) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchGasCynTray();
        };

        //$(function () {
        //    $('#datetimepickerStart').datetimepicker({
        //        format: 'YYYY-MM-DD HH:mm',
        //        locale: moment.locale('zh-cn'),
        //        //sideBySide:true,
        //        showTodayButton:true,
        //        toolbarPlacement:'top',
        //    });
        //    $('#datetimepickerEnd').datetimepicker({
        //        format: 'YYYY-MM-DD HH:mm',
        //        locale: moment.locale('zh-cn'),
        //        //sideBySide:true,
        //        showTodayButton:true,
        //        toolbarPlacement:'top',
        //
        //    });
        //});
        //$(function () {
        //    $('#datetimepickerStart').datetimepicker()
        //        .on('dp.change', function (ev) {
        //            var date = ev.date._d;
        //            var month = date.getMonth()+1;
        //            $scope.q.startTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
        //                +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
        //            console.log("changed")
        //        });
        //    $('#datetimepickerEnd').datetimepicker()
        //        .on('dp.change', function (ev) {
        //            var date = ev.date._d;
        //            var month = date.getMonth()+1;
        //            $scope.q.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
        //                +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
        //        });
        //});
        ////清除时间范围
        //$scope.deleteTimeRange = function () {
        //    $scope.q.startTime = null;
        //    $scope.q.endTime = null;
        //};



        $scope.pager = pager.init('GasCynTrayCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();



        $scope.vm = {
            gasCynTrayList: []

        };
        $scope.q = {
            //startTime:null,
            //endTime:null,
            gasCynTraySn:null,
            customerUserId:null,

        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchGasCynTray();
        };

        $scope.initPopUp = function () {

        };

        var searchGasCynTray = function () {
            var queryParams = {
                number:$scope.q.gasCynTraySn,
                userId:$scope.q.customerUserId,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
                orderBy:"id desc"
            };

            TicketService.retrieveGasCynTray(queryParams).then(function (gasCynTrays) {
                $scope.pager.update($scope.q, gasCynTrays.total, queryParams.pageNo);
                $scope.vm.gasCynTrayList = gasCynTrays.items;
            });
        };

        var init = function () {
            searchGasCynTray();
        };

        init();

    }]);
