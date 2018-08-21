'use strict';

comprehensiveQueryApp.controller('CouponCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'TicketService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                                        rootService, pager, udcModal, TicketService,sessionStorage) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchCoupon();
        };

        $(function () {
            $('#datetimepickerStart').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            $('#datetimepickerEnd').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',

            });
        });
        $(function () {
            $('#datetimepickerStart').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.startTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                    console.log("changed")
                });
            $('#datetimepickerEnd').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
        });
        //清除时间范围
        $scope.deleteTimeRange = function () {
            $scope.q.startTime = null;
            $scope.q.endTime = null;
        };



        $scope.pager = pager.init('CouponCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();



        $scope.vm = {
            couponList: [],
            useStatusList:[{index:null,name:"全部"},{index:"0",name:"待使用"},{index:"1",name:"已使用"}]
        };
        $scope.q = {
            startTime:null,
            endTime:null,
            couponSn:null,
            customerUserId:null,
            specCode:null,
            useStatus:{}

        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchCoupon();
        };

        $scope.initPopUp = function () {

        };

        var searchCoupon = function () {
            var queryParams = {
                couponSn:$scope.q.couponSn,
                saleStartTime:$scope.q.startTime,
                saleEndTime:$scope.q.endTime,
                customerUserId:$scope.q.customerUserId,
                useStatus:$scope.q.useStatus.index,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
                orderBy:"id desc"
            };

            TicketService.retrieveCoupon(queryParams).then(function (complaints) {
                $scope.pager.update($scope.q, complaints.total, queryParams.pageNo);
                $scope.vm.couponList = complaints.items;
            });
        };

        var init = function () {
            //查询投诉单状态初始化为全部
            $scope.q.useStatus = $scope.vm.useStatusList[0];
            searchCoupon();
        };

        init();
        //优惠券状态查询改变
        $scope.couponStatusChange = function () {
            if ($scope.q.useStatus==null) {
                return;
            };
            $scope.pager.setCurPageNo(1);
            searchCoupon();
        };
    }]);
