'use strict';

comprehensiveQueryApp.controller('UserCardCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'TicketService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                                        rootService, pager, udcModal, TicketService,sessionStorage) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchUserCard();
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



        $scope.pager = pager.init('UserCardCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();



        $scope.vm = {
            userCardList: []

        };
        $scope.q = {
            //startTime:null,
            //endTime:null,
            userCardSn:null,
            customerUserId:null,

        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchUserCard();
        };

        $scope.initPopUp = function () {

        };

        var searchUserCard = function () {
            var queryParams = {
                number:$scope.q.userCardSn,
                userId:$scope.q.customerUserId,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
                orderBy:"id desc"
            };

            TicketService.retrieveUserCard(queryParams).then(function (userCards) {
                $scope.pager.update($scope.q, userCards.total, queryParams.pageNo);
                $scope.vm.userCardList = userCards.items;
            });
        };

        var init = function () {
            searchUserCard();
        };

        init();

    }]);
