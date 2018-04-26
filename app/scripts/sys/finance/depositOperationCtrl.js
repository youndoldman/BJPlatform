/**
 * Created by Administrator on 2018/4/24.
 */
/**
 * Created by Administrator on 2018/3/28.
 */
'use strict';

financeApp.controller('DepositOperationCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'sessionStorage','FinanceService','$interval',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                                rootService, pager, udcModal,sessionStorage,FinanceService,$interval) {

        $scope.currentUser = {};

        $(function () {
            $('#creditPickerStart').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            $('#creditPickerEnd').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                showTodayButton:true,
                toolbarPlacement:'top',
            });

            $('#depositOperTimePicker').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                showTodayButton:true,
                toolbarPlacement:'top',
            });

        });
        $(function () {
            $('#creditPickerStart').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.data.startTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#creditPickerEnd').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.data.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });

            $('#depositOperTimePicker').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.operTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
        });

        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            $scope.searchCredit();
        };

        $scope.pager = pager.init('MoneyReturnCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            amount:null,
            operTime:null,
            note:null,
        };


        $scope.vm = {
            creditList:[],
        };

        $scope.data = {
            operId:null,
            startTime:null,
            endTime:null,
        }


        //$scope.search = function () {
        //    $scope.pager.setCurPageNo(1);
        //    $scope.searchCredit();
        //};


        $scope.saveDeposit = function(param){
            console.info(param);
            FinanceService.addDepositDetail(param).then(function () {
                udcModal.info({"title": "处理结果", "message": "存银行款录入成功 "});

            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "存银行款录入失败 "+value.message});
            })

        }

        $scope.searchCredit = function () {
            var queryParams = {
                userId: $scope.data.operId,
                //creditType:$scope.data.creditType,
                startTime:$scope.data.startTime,
                endTime:$scope.data.endTime,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
            };
            console.info(queryParams);
            FinanceService.searchDepositDetail(queryParams).then(function (detail) {
                $scope.pager.update($scope.q, detail.total, queryParams.pageNo);
                $scope.vm.creditList = detail.items;
                console.info($scope.vm.creditList );
            });

        };


        var init = function () {
            $scope.currentUser = sessionStorage.getCurUser();
            //$scope.data.operId = $scope.currentUser.userId,
            //$scope.data.creditType = 0;

            $scope.searchCredit();
        };

        init();

    }]);
