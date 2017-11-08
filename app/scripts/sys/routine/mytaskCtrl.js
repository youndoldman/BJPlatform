/**
 * Created by T470P on 2017/9/15.
 */
'use strict';

routineApp.controller('mytaskCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', '$http', 'URI', 'promiseWrapper','sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                                 rootService, pager, udcModal, $http, URI, promise, sessionStorage) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
        };
        $scope.userInfo = {
            userId: ""
        };
        $scope.pager = pager.init('mytaskCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            ID: historyQ.ID || ""
        };

        $scope.vm = {
            customerOrderList: []
        };


        var getCurUser = function()
        {
            var curUser = sessionStorage.getCurUser();
            $scope.userInfo.userId = curUser.id;
        };



        var getTask = function()
        {
            promise.wrap($http.get("../../api/orders/mytask?userId="+$scope.userInfo.userId)).then(function(value){
                $scope.vm.customerOrderList = value.items;
                console.log($scope.vm.customerOrderList);
            });
        };


        $scope.confirm = function(taskId)
        {
            promise.wrap($http.post("../../api/orders/done?taskId="+taskId));
            alert("确认成功！");
        };
        var init = function () {
            getCurUser();
            getTask();
        };
        init();

    }]);

