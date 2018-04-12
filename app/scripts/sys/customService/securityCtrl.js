'use strict';

customServiceApp.controller('SecurityCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'MendSecurityComplaintService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                                       rootService, pager, udcModal, MendSecurityComplaintService,sessionStorage) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchSecurity();
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



        $scope.pager = pager.init('securityCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();



        $scope.vm = {
            securityList: [],
            securityStatusList:[{index:-1,name:"全部"},{index:"PTSuspending",name:"待处理"},{index:"PTHandling",name:"正在处理"},{index:"PTSolved",name:"已处理"}]
        };
        $scope.q = {
            startTime:null,
            endTime:null,
            securitySn:null,
            processStatus:{}

        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchSecurity();
        };

        $scope.initPopUp = function () {

        };

        $scope.viewDetails = function (security) {
            udcModal.show({
                templateUrl: "./customService/securityDealModal.htm",
                controller: "SecurityDealModalCtrl",
                inputs: {
                    title: '安检单详情',
                    initVal: security
                }
            }).then(function (result) {
            })
        };
        //指派安检单
        $scope.assign = function (security) {
            udcModal.show({
                templateUrl: "./customService/securityAssignModal.htm",
                controller: "SecurityAssignModalCtrl",
                inputs: {
                    title: '安检单指派',
                    initVal: security
                }
            }).then(function (result) {
                if (result) {
                    searchSecurity();
                }
            })
        };

        //处理安检单
        $scope.deal = function (security) {
            udcModal.show({
                templateUrl: "./customService/securityDealModal.htm",
                controller: "SecurityDealModalCtrl",
                inputs: {
                    title: '安检单处理',
                    initVal: security
                }
            }).then(function (result) {
                if (result) {
                    searchSecurity();
                }
            })
        };

        var searchSecurity = function () {


            var queryParams = {
                startTime:$scope.q.startTime,
                endTime:$scope.q.endTime,
                securitySn:$scope.q.securitySn,
                processStatus:$scope.q.processStatus.index,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
                orderBy:"id desc"
            };
            if($scope.q.processStatus.index==-1){
                queryParams.processStatus = null;
            }

            MendSecurityComplaintService.retrieveSecurity(queryParams).then(function (securitys) {
                $scope.pager.update($scope.q, securitys.total, queryParams.pageNo);
                $scope.vm.securityList = securitys.items;
            });
        };

        var init = function () {
            //查询安检单状态初始化为全部
            $scope.q.processStatus = $scope.vm.securityStatusList[0];
            searchSecurity();
        };

        init();
        //订单状态查询改变
        $scope.securityStatusSearchChange = function () {
            if ($scope.q.processStatus==null) {
                return;
            };
            $scope.pager.setCurPageNo(1);
            searchSecurity();
        };
    }]);
