/**
 * Created by T470P on 2017/9/15.
 */
'use strict';

customerApp.controller('customerCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', '$http', 'URI', 'promiseWrapper','sessionStorage',function ($scope, $rootScope, $filter, $location, Constants,
                                                                 rootService, pager, udcModal, $http, URI, promise, sessionStorage) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);

        };
        $scope.userInfo = {
            userId: ""
        };
        $scope.pager = pager.init('customerCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            ID: historyQ.ID || ""
        };

        $scope.taskId = "";
        $scope.customerOrder = {
                "userId":""
        };

        $scope.vm = {
            customerOrderList: []
        };

        $scope.imageUrl = "";

        $scope.submit = function () {
            promise.wrap($http.post("../../api/orders", $scope.customerOrder));
            alert("订单提交成功！");
        };


        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchOrder();
        };

        var toViewModel = function (orderFromApi) {
            return {
                userId: orderFromApi.userId,
                userName: orderFromApi.userName,
                phone: orderFromApi.phone,
                style: orderFromApi.style,
                quantity: orderFromApi.quantity,
                location: orderFromApi.location,
                state: orderFromApi.state,
                remark: orderFromApi.remark
            }
        };

        var searchOrder = function () {
            console.log($scope.vm.customerOrder);
            return promise.wrap($http.post("../../api/orders", $scope.vm.customerOrder));

            var queryParams = {
                name: $scope.q.userName,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            searchOrder.retrieveUsers(queryParams).then(function (customerOrder) {
                $scope.pager.update($scope.q, customerOrder.total, queryParams.pageNo);
                $scope.vm.userList = _.map(customerOrder.items, toViewModel);
            });
        };

        var getCurUser = function()
        {
            var curUser = sessionStorage.getCurUser();
            $scope.customerOrder.userId = curUser.id;
            $scope.userInfo.userId = curUser.id;
        };



        var getTask = function()
        {
            promise.wrap($http.get("../../api/orders/mytask?userId="+$scope.userInfo.userId)).then(function(value){
                $scope.vm.customerOrderList = value.items;
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
            $scope.imageUrl = "../../api/orders/myorder?userId="+$scope.customerOrder.userId;
        };
        init();

    }]);

