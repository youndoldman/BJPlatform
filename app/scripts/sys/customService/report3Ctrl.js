/**
 * Created by Administrator on 2018/3/14.
 */
'use strict';

customServiceApp.controller('Report3Ctrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
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

        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchData();
        };

        $scope.pager = pager.init('Report3Ctrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            //userName: null,
            //userId: null,
            //token:null,
            //orgId:null,
            workSet:null,
            type:"daily",
            interval:"1",
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
                    //agentUserId: $scope.q.userId,
                    //agentUserName: $scope.q.userName,
                    workSets: $scope.q.workSet,
                    type: $scope.q.type,
                    interval: $scope.q.interval,
                    begin: $scope.q.startTime,
                    end: $scope.q.endTime
                };
                //KtyService.retrieveReport1(queryParams, $scope.q.token).then(function (response) {
                //    $scope.vm.dataList = response.data;
                //}, function(value) {
                //    udcModal.info({"title": "查询失败", "message": value.message});
                //});

                KtyService.retrieveReport3(queryParams, $scope.q.token).then(function (response) {
                    $scope.vm.dataList = response.data;
                    $scope.vm.lastDataList = $scope.vm.dataList[$scope.vm.dataList.length-1];
                        //console.log($scope.vm.lastDataList);
                    $scope.vm.dataList .splice($scope.vm.dataList.length-1, 1);
                }, function(value) {
                    if(value.code == "40007")
                    {
                        $scope.vm.dataList = null;
                        udcModal.info({"title": "查询失败", "message": "未找到呼叫记录"});
                    }
                    else if(value.code == "40021")
                    {
                        $scope.vm.dataList = null;
                        udcModal.info({"title": "查询失败", "message": "未找到指定部门人员信息"});
                    }
                    else if(value.code == "40026")
                    {
                        $scope.vm.dataList = null;
                        udcModal.info({"title": "查询失败", "message": "时间范围长度超出限制（最大三个月间隔）"});
                    }
                    else if(value.code == "50000")
                    {
                        $scope.vm.dataList = null;
                        udcModal.info({"title": "查询失败", "message": "系统内部错误"});
                    }
                });
            }, function(value) {
                if(value.code == "40001")
                {
                    $scope.vm.dataList = null;
                    udcModal.info({"title": "连接结果", "message": "用户名或密码不正确"});
                }
                else if(value.code == "40002")
                {
                    $scope.vm.dataList = null;
                    udcModal.info({"title": "连接结果", "message": "用户名或密码为空"});
                }
                else if(value.code == "40003")
                {
                    $scope.vm.dataList = null;
                    udcModal.info({"title": "连接结果", "message": "用户权限不正确"});
                }
                else if(value.code == "40004")
                {
                    $scope.vm.dataList = null;
                    udcModal.info({"title": "连接结果", "message": "用户认证不存在或已过期"});
                }
                else if(value.code == "50000")
                {
                    $scope.vm.dataList = null;
                    udcModal.info({"title": "连接结果", "message": "系统内部错误"});
                }
            })
        };

        var init = function () {
            //searchData();
        };
        init();
    }]);
