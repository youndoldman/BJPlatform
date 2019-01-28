'use strict';

manageApp.controller('SendMsListCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'UntilsService', function ($scope, $rootScope, $filter, $location, Constants,
                                                                   rootService, pager, udcModal, UntilsService) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchSendMs();
        };
        $scope.pager = pager.init('SendMsListCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {

        };

        $scope.vm = {
            sendMsList: []
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchSendMs();
        };

        $scope.initPopUp = function () {
            udcModal.show({
                templateUrl: "./manage/sendMsModal.htm",
                controller: "SendMsModalCtrl",
                inputs: {
                    title: '短信推送',
                    initVal: {}
                }
            }).then(function (result) {
                if (result) {
                    searchSendMs();
                }
            })
        };




        var searchSendMs = function () {
            var queryParams = {
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            //UntilsService.retrieveCloudUsers(queryParams).then(function (cloudUsers) {
            //    $scope.pager.update($scope.q, cloudUsers.total, queryParams.pageNo);
            //    $scope.vm.sendMsList = cloudUsers.items;
            //});


        };



        var init = function () {
            //查询云客服账号
            searchSendMs();
        };

        init();

    }]);

manageApp.controller('SendMsModalCtrl', ['$scope', 'close', 'UntilsService', 'title', 'initVal', 'udcModal',function ($scope, close, UntilsService, title, initVal, udcModal) {
    $scope.modalTitle = title;
    $scope.isModify = false;
    $scope.vm = {
        sendMs: {},
        province:"",
        city:""
    };

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (cloudUser) {
        if (!$scope.isModify) {
            var queryParams = {
                text: $scope.vm.text,
                province: $scope.vm.province,
                city: $scope.vm.city,
            };
            UntilsService.SendBatchSms(queryParams).then(function (count) {
                udcModal.info({"title": "推送用户数", "message": count});
            }, function(value) {
                udcModal.info({"title": "错误信息", "message": value});
            })
        } else {

        }
    };

    var init = function () {
        $scope.vm.sendMs = _.clone(initVal);
        if (title == "短信推送"){
            $scope.isModify = false;
        } else {
            $scope.isModify = true;
        }


    };


    init();
}]);