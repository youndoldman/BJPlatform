'use strict';
bottleApp.controller('BottleTakeOverModalCtrl', ['$scope', 'close', 'BottleService', 'title', 'initVal', 'udcModal','$timeout',
    'pager', 'MapService',function ($scope, close, BottleService, title, initVal, udcModal,$timeout, pager,　MapService) {
    $scope.modalTitle = title;
    $scope.isModify = false;
    $scope.vm = {
        bottle:null,
        historyListSplitByPage:[],//分页显示的交接记录
    };


    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function () {
        $scope.close(true);
    };


    var init = function () {
        $scope.pagerOpsLog.pageSize=10;
        $scope.pagerOpsLog.setCurPageNo(1);
        $scope.vm.bottle = _.clone(initVal);
        if (title == "交接记录"){
            $scope.isModify = true;
        } else {
            $scope.isModify = false;
        }
        searchOpsLog();

    };


    var gotoPageOpsLog = function (pageNo) {
        $scope.pagerOpsLog.setCurPageNo(pageNo);
        searchOpsLog();
    };

    $scope.pagerOpsLog = pager.init('BottleTakeOverModalCtrl', gotoPageOpsLog);


    $scope.q = {
        bottleCode: null,
        startTime:"",
        endTime:""
    };




        //查询钢瓶的生命历程
        var searchOpsLog = function () {
            $scope.vm.historyListSplitByPage = [];
            var queryParams = {
                startTime: $scope.q.startTime,
                endTime: $scope.q.endTime,
                pageNo: $scope.pagerOpsLog.getCurPageNo(),
                pageSize: $scope.pagerOpsLog.pageSize,
                orderBy:"id desc"
            };

            BottleService.retrieveGasCylinderTakeOverHistory($scope.vm.bottle.number, queryParams).then(function (historys) {
                $scope.pagerOpsLog.update($scope.q, historys.total, queryParams.pageNo);
                for (var i = 0; i < historys.items.length; i++) {
                    var tempHistory = {
                        srcUser: "",
                        destUser: "",
                        detail: "",
                        createTime: "",
                        longititude: "",
                        latitude: ""
                    };
                    tempHistory.id = historys.items[i].id;
                    tempHistory.srcUser = historys.items[i].srcUser.userId + "(" + historys.items[i].srcUser.name + ")";
                    tempHistory.destUser = historys.items[i].targetUser.userId + "(" + historys.items[i].targetUser.name + ")";
                    tempHistory.detail = historys.items[i].note;
                    tempHistory.createTime = historys.items[i].optime;

                    tempHistory.longititude = historys.items[i].longitude;
                    tempHistory.latitude = historys.items[i].latitude;
                    $scope.vm.historyListSplitByPage.push(tempHistory);
                }
            });
        };
    init();

}]);