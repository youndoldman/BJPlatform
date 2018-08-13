'use strict';

bottleApp.controller('BottleFillingCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'BottleService', function ($scope, $rootScope, $filter, $location, Constants,

                                                                   rootService, pager, udcModal, BottleService) {

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
                });
        });
        //清除时间范围
        $scope.deleteTimeRange = function () {
            $scope.q.startTime = null;
            $scope.q.endTime = null;
        };

        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchFillings();
        };

        $scope.pager = pager.init('BottleFillingCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            cynNumber:null,
            station: null,
            machine: null,
            warningStatus:null,
            startTime:null,
            endTime:null
        };

        $scope.vm = {
            fillingList: [],
            stationList:[{key:"丽江龙山气站",value:"丽江龙山气站"}],
            machineList:[{key:null,value:"全部"},{key:1,value:"1号秤"},{key:2,value:"2号秤"},{key:3,value:"3号秤"},{key:4,value:"4号秤"}],
            warningStatusList:[{key:null,value:"全部"},{key:0,value:"正常"},{key:1,value:"异常"}],
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchFillings();
        };



        $scope.modify = function (gasCylindCode) {
            var gasCylinder = {"number":gasCylindCode};
            var warning = {"gasCylinder":gasCylinder};//这里临时借用报警单处理流程
            udcModal.show({
                templateUrl: "./bottle/bottleWarningModal.htm",
                controller: "BottleWarningModalCtrl",
                inputs: {
                    title: '告警处理',
                    initVal: warning
                }
            }).then(function (result) {
                if (result) {
                    //searchFillings();
                }
            })
        };

        var searchFillings = function () {
            var queryParams = {
                stationNumber: $scope.q.station.key,
                machineNumber: $scope.q.machine.key,
                cynNumber: $scope.q.cynNumber,
                warningStatus:$scope.q.warningStatus.key,
                startTime: $scope.q.startTime,
                endTime: $scope.q.endTime,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
            };
            BottleService.retrieveGasCylindeFilling(queryParams).then(function (warnings) {
                $scope.pager.update($scope.q, warnings.total, queryParams.pageNo);
                $scope.vm.fillingList = warnings.items;
            });

        };



        var init = function () {
            //查询告警
            $scope.q.station = $scope.vm.stationList[0];
            $scope.q.machine = $scope.vm.machineList[0];
            $scope.q.warningStatus = $scope.vm.warningStatusList[0];
            searchFillings();

        };

        init();

    }]);
