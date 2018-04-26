/**
 * Created by Administrator on 2018/4/25.
 */
/**
 * Created by Administrator on 2018/3/28.
 */
'use strict';

financeApp.controller('WriteOffCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'sessionStorage','FinanceService','$interval',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                                rootService, pager, udcModal,sessionStorage,FinanceService,$interval) {


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
        });

        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            $scope.searchCredit();
        };

        $scope.pager = pager.init('MoneyReturnCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            userId:null,
            amount:null,
            payType:null,
            note:null,
        };

        $scope.payType = null;
        //$scope.creditType = null;

        $scope.vm = {
            bottleList: [],
            payType:["电子","现金"],
            creditType:["所有类型","普通用户","月结用户"],

            creditList:[],
        };

        $scope.data = {
            userId:null,
            creditType:null,
            startTime:null,
            endTime:null,
        }

        $scope.payTypeChange = function () {
            console.info($scope.payType);
            if($scope.payType == "电子")
            {
                $scope.q.payType = 0;
            }
            else
            {
                $scope.q.payType = 1;
            }
        }

        //$scope.search = function () {
        //    $scope.pager.setCurPageNo(1);
        //    searchCredit();
        //};


        $scope.saveMoneyReturn = function(returnMoney){
            FinanceService.addWriteOff(returnMoney).then(function () {
                udcModal.info({"title": "处理结果", "message": "回款录入成功 "});

            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "回款录入失败 "+value.message});
            })

        }

        $scope.searchCredit = function () {
            console.info("click button");
            var queryParams = {
                userId: $scope.data.userId,
                //creditType:$scope.data.creditType,
                startTime:$scope.data.startTime,
                endTime:$scope.data.endTime,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
            };
            if($scope.data.discountType == "所有类型")
            {
                queryParams.creditType = null;
            }
            else if($scope.data.creditType == "普通用户")
            {
                queryParams.creditType = 0;
            }
            else if($scope.data.creditType == "月结用户")
            {
                queryParams.creditType = 1;
            }
            console.info("回款查询");
            console.info(queryParams);
            FinanceService.searchWriteOffDetail(queryParams).then(function (detail) {
                $scope.pager.update($scope.q, detail.total, queryParams.pageNo);
                $scope.vm.creditList = detail.items;
                console.info($scope.vm.creditList );
            });
        };

        var init = function () {
            $scope.payType = "电子";
            $scope.q.payType = 0;
            $scope.data.creditType = "所有类型";

            $scope.searchCredit();
        };

        init();


    }]);
