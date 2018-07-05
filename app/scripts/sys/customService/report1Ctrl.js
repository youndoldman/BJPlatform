'use strict';

customServiceApp.controller('Report1Ctrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'KtyService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal, KtyService,sessionStorage) {
        $(function () {
            $('#datetimepickerStart').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            $('#datetimepickerEnd').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss',
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

        $scope.pager = pager.init('Report1Ctrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.data = {
            startTime:null,
            endTime:null
        };

        $scope.q = {
            userName: null,
            userId: null,
            token:null,
            orgId:null,
            interval:"1",
            type:"daily",
            startTime:null,
            endTime:null
        };

        $scope.vm = {
            dataList: [],
            lastDataList:[],
            types:["daily","hourly"],
            intervals:["1","2","3","4","5"]
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            $scope.vm.dataList = null;
            $scope.vm.lastDataList = null;
            searchData();
        };

        $scope.printPage = function () {
            window.print();
        };

        var searchData = function () {
            //先登录授权，拿到token、orgId、userId
            var userName = $scope.currentKTYUser.items[0].userId;
            var password = $scope.currentKTYUser.items[0].password;
            if(($scope.q.userId!=null) && ($scope.q.startTime!=null) && ($scope.q.endTime!=null) && ($scope.q.type!=null) && ($scope.q.interval!=null))
            {
                KtyService.authenticate(userName,password).then(function (response) {
                    $scope.q.token = response.data.token;
                    var queryParams = {
                        agentNames: $scope.q.userId,
                        //agentUserName: $scope.q.userName,
                        type: $scope.q.type,
                        interval: $scope.q.interval,
                        begin: $scope.q.startTime,
                        end: $scope.q.endTime
                    };
                    KtyService.retrieveReport1(queryParams, $scope.q.token).then(function (response) {
                        $scope.vm.dataList = response.data;
                        $scope.vm.lastDataList = $scope.vm.dataList[$scope.vm.dataList.length-1];
                        //console.log($scope.vm.lastDataList);
                        $scope.vm.dataList .splice($scope.vm.dataList.length-1, 1);
                    }, function(value) {
                        udcModal.info({"title": "查询失败", "message": value.message});
                    });
                }, function(value) {
                    udcModal.info({"title": "连接结果", "message": value.message});
                })
            }
            else
            {
                udcModal.info({"title": "提醒", "message": "请填写坐席工号，选择日期范围、间隔类型和间隔数"});
            }
        };

        var init = function () {
            $scope.currentKTYUser = sessionStorage.getKTYCurUser();
        };
        init();
    }]);
