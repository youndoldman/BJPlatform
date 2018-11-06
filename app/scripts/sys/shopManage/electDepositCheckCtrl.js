'use strict';

shopManageApp.controller('ElectDepositCheckCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'OrderCheckService', 'sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal, OrderCheckService,sessionStorage) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
                //查询需要进行确认的订单
            searchElectDeposit();
        };



        $scope.pager = pager.init('OrderCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();



        $scope.vm = {
            electDepositList: [],
            electDepositStatusList:[{key:null,value:"全部"},{key:0,value:"待核单"},{key:1,value:"已核单"}],
        };
        $scope.q = {
            electDepositStatus:null,
            department:null
        };



        $scope.initPopUp = function () {

        };


        $scope.check = function (electDeposit) {
            var newElectDeposit = {id:electDeposit.id, electDepositStatus:"EDSChecked"};
            udcModal.confirm({"title": "电子押金单核实", "message": electDeposit.depositSn + " | 实收："+electDeposit.actualAmount+"元"})
                .then(function (result) {
                    if (result) {
                        OrderCheckService.modifyElectDeposit(newElectDeposit).then(function () {
                            udcModal.info({"title": "处理结果", "message": "电子押金单核实成功 "});
                            searchElectDeposit();
                        }, function (value) {
                            udcModal.info({"title": "处理结果", "message": "电子押金单核实失败 " + value.message});
                        })
                    }
                })
        };

        var searchElectDeposit = function () {
            //查询需要进行核单的电子押金单
            var queryParams = {
                electDepositStatus:$scope.q.electDepositStatus.key,
                departmentCode:$scope.q.department.code,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            OrderCheckService.retrieveElectDeposit(queryParams).then(function (electDeposit) {
                $scope.pager.update($scope.q, electDeposit.total, queryParams.pageNo);
                $scope.vm.electDepositList= electDeposit.items;

            });
        };


        var init = function () {
            var curUser = sessionStorage.getCurUser();
            $scope.q.department = curUser.department;
            $scope.q.electDepositStatus = $scope.vm.electDepositStatusList[2];
            searchElectDeposit();
        };

        init();


    }]);
