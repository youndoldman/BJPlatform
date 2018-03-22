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
            types:["daily","hourly"],
            intervals:["1","2","3","4","5"]
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchData();
        };

        $scope.printPage = function () {
            window.print();
        };
        //$scope.printPage=function()
        //{
        //    //var oper=1;
        //    //if (oper < 10) {
        //        var bdhtml=window.document.body.innerHTML;//获取当前页的html代码
        //        var sprnstr="<!--startprint-->";//设置打印开始区域
        //        var eprnstr="<!--endprint-->";//设置打印结束区域
        //        var prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+18); //从开始代码向后取html
        //    //console.log(prnhtml);
        //        prnhtml=prnhtml.substr(0,prnhtml.indexOf(eprnstr));//从结束代码向前取html
        //        window.document.body.innerHTML=prnhtml;
        //        window.print();
        //        window.document.body.innerHTML=bdhtml;
        //    //} else {
        //    //    window.print();
        //    //}
        //}

        var searchData = function () {
            //先登录授权，拿到token、orgId、userId
            KtyService.authenticate("58531181@qq.com","123456").then(function (response) {
                $scope.q.token = response.data.token;
                var queryParams = {
                    agentUserId: $scope.q.userId,
                    agentUserName: $scope.q.userName,
                    type: $scope.q.type,
                    interval: $scope.q.interval,
                    begin: $scope.q.startTime,
                    end: $scope.q.endTime
                };
                KtyService.retrieveReport1(queryParams, $scope.q.token).then(function (response) {
                    $scope.vm.dataList = response.data;
                }, function(value) {
                    $scope.vm.dataList = null;
                    //udcModal.info({"title": "查询失败", "message": value.message});
                    udcModal.info({"title": "查询失败", "message": "请输入相关的查询条件"});
                });
            }, function(value) {
                udcModal.info({"title": "连接结果", "message": "认证失败 "+ value.message});
            })
        };

        var init = function () {
            //searchData();
            var currentDate = new Date();
            var currentMonth = currentDate.getMonth()+1;
            $scope.data.endTime = currentDate.getFullYear()+"-"+currentMonth+"-"+currentDate.getDate()+" "
                +currentDate.getHours()+":"+currentDate.getMinutes()+":"+currentDate.getSeconds();
            //console.log($scope.data.endTime);
            //默认结束时间为当前时间
            $scope.q.endTime = $scope.data.endTime;
        };
        init();
    }]);
