'use strict';

bottleApp.controller('BottleWarningCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'BottleService', function ($scope, $rootScope, $filter, $location, Constants,

                                                                   rootService, pager, udcModal, BottleService) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchWarnings();
        };

        $scope.pager = pager.init('BottleWarningCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            id:null,
            bottleNumber: null,
            srcUserId: null,
            gasCynWarnStatus:null
        };

        $scope.vm = {
            warningList: [],
            gasCynWarnStatusList:[{key:null,value:"全部告警"},{key:0,value:"待处理"},{key:1,value:"处理中"},{key:2,value:"已处理"}],
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchWarnings();
        };



        $scope.modify = function (warning) {
            udcModal.show({
                templateUrl: "./bottle/bottleWarningModal.htm",
                controller: "BottleWarningModalCtrl",
                inputs: {
                    title: '告警处理',
                    initVal: warning
                }
            }).then(function (result) {
                if (result) {
                    searchWarnings();
                }
            })
        };

        var searchWarnings = function () {
            var queryParams = {
                id: $scope.q.id,
                gasNumber: $scope.q.bottleNumber,
                srcUserId: $scope.q.srcUserId,
                gasCynWarnStatus:$scope.q.gasCynWarnStatus.key,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };
            BottleService.retrieveGasCylinderWarn(queryParams).then(function (warnings) {
                $scope.pager.update($scope.q, warnings.total, queryParams.pageNo);
                $scope.vm.warningList = warnings.items;
                console.log($scope.vm.warningList);
            });
            console.log($scope.vm.warningList);


        };



        var init = function () {
            //查询告警
            $scope.q.gasCynWarnStatus = $scope.vm.gasCynWarnStatusList[0];
            searchWarnings();

        };

        init();

    }]);
