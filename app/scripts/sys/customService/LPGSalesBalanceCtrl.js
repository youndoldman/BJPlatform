/**
 * Created by Administrator on 2018/3/30.
 */

'use strict';

customServiceApp.controller('LPGSalesBalanceCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
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
        });

        $(function () {
            $('#datetimepickerStart').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.startTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
        });

        //var gotoPage = function (pageNo) {
        //    $scope.pager.setCurPageNo(pageNo);
        //    searchData();
        //};
        //
        //$scope.pager = pager.init('LPGCtrl', gotoPage);
        //var historyQ = $scope.pager.getQ();

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
            goodsTypes: [{"name":"液化气","type":"45kg/瓶"},{"name":"液化气","type":"12kg/瓶"},{"name":"液化气","type":"3.5kg/瓶"},
                {"name":"专卖瓶","type":"15kg/只"},{"name":"专卖瓶","type":"5kg/只"}],
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
            //KtyService.authenticate("58531181@qq.com","123456").then(function (response) {
            //    $scope.q.token = response.data.token;
            //    var queryParams = {
            //        agentUserIds: $scope.q.userId,
            //        //agentUserName: $scope.q.userName,
            //        type: $scope.q.type,
            //        interval: $scope.q.interval,
            //        begin: $scope.q.startTime,
            //        end: $scope.q.endTime
            //    };
            //    KtyService.retrieveReport1(queryParams, $scope.q.token).then(function (response) {
            //        $scope.vm.dataList = response.data;
            //        $scope.vm.lastDataList = $scope.vm.dataList[$scope.vm.dataList.length-1];
            //        //console.log($scope.vm.lastDataList);
            //        $scope.vm.dataList .splice($scope.vm.dataList.length-1, 1);
            //    }, function(value) {
            //        //$scope.vm.dataList = null;
            //        //udcModal.info({"title": "查询失败", "message": "请输入相关的查询条件"});
            //        if(value.code == "40007")
            //        {
            //            $scope.vm.dataList = null;
            //            udcModal.info({"title": "查询失败", "message": "未找到呼叫记录"});
            //        }
            //        else if(value.code == "40021")
            //        {
            //            $scope.vm.dataList = null;
            //            udcModal.info({"title": "查询失败", "message": "未找到指定部门人员信息"});
            //        }
            //        else if(value.code == "40025")
            //        {
            //            $scope.vm.dataList = null;
            //            udcModal.info({"title": "查询失败", "message": "参数异常"});
            //        }
            //        else if(value.code == "40026")
            //        {
            //            $scope.vm.dataList = null;
            //            udcModal.info({"title": "查询失败", "message": "时间范围长度超出限制（最大三个月间隔）"});
            //        }
            //        else if(value.code == "50000")
            //        {
            //            $scope.vm.dataList = null;
            //            udcModal.info({"title": "查询失败", "message": "系统内部错误"});
            //        }
            //    });
            //}, function(value) {
            //    if(value.code == "40001")
            //    {
            //        $scope.vm.dataList = null;
            //        udcModal.info({"title": "连接结果", "message": "用户名或密码不正确"});
            //    }
            //    else if(value.code == "40002")
            //    {
            //        $scope.vm.dataList = null;
            //        udcModal.info({"title": "连接结果", "message": "用户名或密码为空"});
            //    }
            //    else if(value.code == "40003")
            //    {
            //        $scope.vm.dataList = null;
            //        udcModal.info({"title": "连接结果", "message": "用户权限不正确"});
            //    }
            //    else if(value.code == "40004")
            //    {
            //        $scope.vm.dataList = null;
            //        udcModal.info({"title": "连接结果", "message": "用户认证不存在或已过期"});
            //    }
            //    else if(value.code == "50000")
            //    {
            //        $scope.vm.dataList = null;
            //        udcModal.info({"title": "连接结果", "message": "系统内部错误"});
            //    }
            //})
        };

        var init = function () {
            searchData();
            //var currentDate = new Date();
            //var currentMonth = currentDate.getMonth()+1;
            //$scope.data.endTime = currentDate.getFullYear()+"-"+currentMonth+"-"+currentDate.getDate()+" "
            //    +currentDate.getHours()+":"+currentDate.getMinutes()+":"+currentDate.getSeconds();
            ////console.log($scope.data.endTime);
            ////默认结束时间为当前时间
            //$scope.q.endTime = $scope.data.endTime;
        };
        init();
    }]);
