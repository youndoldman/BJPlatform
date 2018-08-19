'use strict';

customServiceApp.controller('TicketCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'TicketService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                                        rootService, pager, udcModal, TicketService,sessionStorage) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchTicket();
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



        $scope.pager = pager.init('TicketCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();



        $scope.vm = {
            ticketList: [],
            useStatusList:[{index:null,name:"全部"},{index:"0",name:"待使用"},{index:"1",name:"已使用"}]
        };
        $scope.q = {
            startTime:null,
            endTime:null,
            ticketSn:null,
            customerUserId:null,
            specCode:null,
            useStatus:{}

        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchTicket();
        };

        $scope.initPopUp = function () {

        };

        var searchTicket = function () {
            var queryParams = {
                ticketSn:$scope.q.ticketSn,
                customerUserId:$scope.q.customerUserId,
                useStatus:$scope.q.useStatus.index,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
                orderBy:"id desc"
            };

            TicketService.retrieveTicket(queryParams).then(function (complaints) {
                $scope.pager.update($scope.q, complaints.total, queryParams.pageNo);
                $scope.vm.ticketList = complaints.items;
            });
        };

        var init = function () {
            //查询投诉单状态初始化为全部
            $scope.q.useStatus = $scope.vm.useStatusList[0];
            searchTicket();
        };

        init();
        //气票状态查询改变
        $scope.ticketStatusChange = function () {
            if ($scope.q.useStatus==null) {
                return;
            };
            $scope.pager.setCurPageNo(1);
            searchTicket();
        };
    }]);
