'use strict';

shopManageApp.controller('ComplaintCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'MendSecurityComplaintService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                                        rootService, pager, udcModal, MendSecurityComplaintService,sessionStorage) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchComplaint();
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



        $scope.pager = pager.init('ComplaintCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();



        $scope.vm = {
            complaintList: [],
            complaintStatusList:[{index:-1,name:"全部"},{index:"PTSuspending",name:"待处理"},{index:"PTHandling",name:"正在处理"},{index:"PTSolved",name:"已处理"}]
        };
        $scope.q = {
            department:null,
            startTime:null,
            endTime:null,
            complaintSn:null,
            processStatus:{}

        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchComplaint();
        };

        $scope.initPopUp = function () {

        };

        $scope.viewDetails = function (complaint) {
            udcModal.show({
                templateUrl: "./shopManage/complaintDealModal.htm",
                controller: "ComplaintDealModalCtrl",
                inputs: {
                    title: '投诉单详情',
                    initVal: complaint
                }
            }).then(function (result) {
            })
        };
        //指派投诉单
        $scope.assign = function (complaint) {
            udcModal.show({
                templateUrl: "./shopManage/complaintAssignModal.htm",
                controller: "ComplaintAssignModalCtrl",
                inputs: {
                    title: '投诉单指派',
                    initVal: complaint
                }
            }).then(function (result) {
                if (result) {
                    searchComplaint();
                }
            })
        };

        //处理投诉单
        $scope.deal = function (complaint) {
            udcModal.show({
                templateUrl: "./shopManage/complaintDealModal.htm",
                controller: "ComplaintDealModalCtrl",
                inputs: {
                    title: '投诉单处理',
                    initVal: complaint
                }
            }).then(function (result) {
                if (result) {
                    searchComplaint();
                }
            })
        };

        var searchComplaint = function () {


            var queryParams = {
                liableDepartmentCode:$scope.q.department.code,
                startTime:$scope.q.startTime,
                endTime:$scope.q.endTime,
                complaintSn:$scope.q.complaintSn,
                processStatus:$scope.q.processStatus.index,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
                orderBy:"id desc"
            };
            if($scope.q.processStatus.index==-1){
                queryParams.processStatus = null;
            }

            MendSecurityComplaintService.retrieveComplaint(queryParams).then(function (complaints) {
                $scope.pager.update($scope.q, complaints.total, queryParams.pageNo);
                $scope.vm.complaintList = complaints.items;
            });
        };

        var init = function () {
            //初始化当前用户
            $scope.vm.curUser = sessionStorage.getCurUser();
            $scope.q.department = $scope.vm.curUser.department;
            //查询投诉单状态初始化为全部
            $scope.q.processStatus = $scope.vm.complaintStatusList[0];
            searchComplaint();
        };

        init();
        //订单状态查询改变
        $scope.complaintStatusSearchChange = function () {
            if ($scope.q.processStatus==null) {
                return;
            };
            $scope.pager.setCurPageNo(1);
            searchComplaint();
        };
    }]);
