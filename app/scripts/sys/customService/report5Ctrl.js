/**
 * Created by Administrator on 2018/3/14.
 */
'use strict';

customServiceApp.controller('Report5Ctrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'KtyService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                 rootService, pager, udcModal, KtyService,sessionStorage) {
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
                });
            $('#datetimepickerEnd').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
        });

        $scope.currentKTYUser = {};

        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);

            searchData();
        };

        $scope.pager = pager.init('Report5Ctrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            //userName: null,
            //userId: null,
            //token:null,
            //orgId:null,
            workSet:null,
            type:"daily",
            interval:"1",
            cutPoint:"12",
            startTime:null,
            endTime:null
        };

        $scope.vm = {
            dataList: [],
            types:["daily","hourly"],
            intervals:["1","2","3","4","5"],
            cutPoints:["1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"]
        };
        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            $scope.vm.dataList = null;

            searchData();
        };

        $scope.printPage = function () {
            window.print();
        };

        var searchData = function () {
            //先登录授权，拿到token、orgId、userId
            var userName = $scope.currentKTYUser.items[0].userId;
            var password = $scope.currentKTYUser.items[0].password;
            if(($scope.q.workSet!=null) && ($scope.q.startTime!=null) && ($scope.q.endTime!=null)
                && ($scope.q.type!=null) && ($scope.q.interval!=null) && ($scope.q.cutPoint!=null))
            {
                KtyService.authenticate(userName,password).then(function (response) {
                    $scope.q.token = response.data.token;
                    var queryParams = {
                        //agentUserId: $scope.q.userId,
                        //agentUserName: $scope.q.userName,
                        workSet: $scope.q.workSet,
                        type: $scope.q.type,
                        interval: $scope.q.interval,
                        cutPoint:$scope.q.cutPoint,
                        begin: $scope.q.startTime,
                        end: $scope.q.endTime
                    };

                    KtyService.retrieveReport5( queryParams, $scope.q.token).then(function (response){
                        $scope.vm.dataList = response.data;

                    }, function(value) {
                        udcModal.info({"title": "查询失败", "message": value.message});
                    });
                }, function(value) {

                    udcModal.info({"title": "连接结果", "message": value.message});
                })
            }
            else
            {
                udcModal.info({"title": "提醒", "message": "请填写坐席工号，选择日期范围、间隔类型、间隔数和等待时长分割点"});
            }

        };


        var init = function () {
            //searchData();
            $scope.currentKTYUser = sessionStorage.getKTYCurUser();
        };
        init();
    }]);
