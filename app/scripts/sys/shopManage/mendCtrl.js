'use strict';

shopManageApp.controller('MendCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'MendSecurityComplaintService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal, MendSecurityComplaintService,sessionStorage) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchMend();
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
                    console.log($scope.q.endTime);
                });
        });
        //清除时间范围
        $scope.deleteTimeRange = function () {
            $scope.q.startTime = null;
            $scope.q.endTime = null;
        };



        $scope.pager = pager.init('MendCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();



        $scope.vm = {
            curUser:null,
            mendList: [],
            mendStatusList:[{index:-1,name:"全部"},{index:"PTSuspending",name:"待处理"},{index:"PTHandling",name:"正在处理"},{index:"PTSolved",name:"已处理"}]
        };
        $scope.q = {
            department:null,
            startTime:null,
            endTime:null,
            mendSn:null,
            processStatus:{}

        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchMend();
        };

        $scope.initPopUp = function () {

        };

        $scope.viewDetails = function (mend) {
            udcModal.show({
                templateUrl: "./shopManage/mendDealModal.htm",
                controller: "MendDealModalCtrl",
                inputs: {
                    title: '报修单详情',
                    initVal: mend
                }
            }).then(function (result) {
            })
        };
        //处理报修单
        $scope.deal = function (mend) {
            udcModal.show({
                templateUrl: "./shopManage/mendDealModal.htm",
                controller: "MendDealModalCtrl",
                inputs: {
                    title: '报修单处理',
                    initVal: mend
                }
            }).then(function (result) {
                if (result) {
                    searchMend();
                }
            })
        };

        var searchMend = function () {

            var queryParams = {
                liableDepartmentCode:$scope.q.department.code,
                startTime:$scope.q.startTime,
                endTime:$scope.q.endTime,
                mendSn:$scope.q.mendSn,
                processStatus:$scope.q.processStatus.index,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
                orderBy:"id desc"
            };
            if($scope.q.processStatus.index==-1){
                queryParams.processStatus = null;
            }

            MendSecurityComplaintService.retrieveMend(queryParams).then(function (mends) {
                $scope.pager.update($scope.q, mends.total, queryParams.pageNo);
                $scope.vm.mendList = mends.items;
            });
        };

        var init = function () {
            //初始化当前用户
            $scope.vm.curUser = sessionStorage.getCurUser();
            $scope.q.department = $scope.vm.curUser.department;
            //查询报修单状态初始化为全部
            $scope.q.processStatus = $scope.vm.mendStatusList[0];
            searchMend();
        };

        init();
        //订单状态查询改变
        $scope.MendStatusSearchChange = function () {
            if ($scope.q.processStatus==null) {
                return;
            };
            $scope.pager.setCurPageNo(1);
            searchMend();
        };
    }]);
