'use strict';

customServiceApp.controller('CustomerManageCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'CustomerManageService', function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal, CustomerManageService) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchCustomer();
        };

        $scope.pager = pager.init('CustomerListCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            CustomerName: historyQ.CustomerName || ""
        };

        $scope.vm = {
            customerList: [],
            gasTicketEditBoolean:true,
        };
        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchCustomer();
        };

        $scope.initPopUp = function () {
            udcModal.show({
                templateUrl: "./customService/customerModal.htm",
                controller: "CustomerModalCtrl",
                inputs: {
                    title: '新增客户',
                    initVal: {}
                }
            }).then(function (result) {
                if (result) {
                    searchCustomer();
                }
            })
        };

        $scope.viewDetails = function (customer) {
            $location.path('/customer/' + customer.id);
        };

        $scope.modify = function (customer) {
            udcModal.show({
                templateUrl: "./customService/customerModal.htm",
                controller: "CustomerModalCtrl",
                inputs: {
                    title: '修改客户',
                    initVal: customer
                }
            }).then(function (result) {
                if (result) {
                    searchCustomer();
                }
            })
        };

        $scope.delete = function (customer) {
            udcModal.confirm({"title": "删除客户", "message": "是否永久删除客户信息 " + customer.name})
                .then(function (result) {
                    if (result) {
                        CustomerManageService.deleteCustomer(customer).then(function () {
                            searchCustomer();
                            udcModal.info({"title": "处理结果", "message": "删除客户信息成功 "});
                        });
                    }
                })
        };
//增加气票
        $scope.add = function (customer) {
            udcModal.show({
                templateUrl: "./customService/addGasTicketModal.htm",
                controller: "AddGasTicketModalCtrl",
                inputs: {
                    title: '增加气票',
                    initVal: customer
                }
            }).then(function (result) {
                if (result) {
                    searchCustomer();
                }
            })
        };

        var searchCustomer = function () {
            var queryParams = {
                name: $scope.q.CustomerName,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            CustomerManageService.retrieveCustomers(queryParams).then(function (customers) {
                $scope.pager.update($scope.q, customers.total, queryParams.pageNo);
                $scope.vm.customerList = _.map(customers.items, CustomerManageService.toViewModel);
                console.log($scope.vm.customerList);

                for(var i = 0; i <$scope.vm.customerList.length; i++)
                {
                    //console.info($scope.vm.customerList[i].settlementType.name);
                    if($scope.vm.customerList[i].settlementType.name == "气票客户")
                    {
                        $scope.vm.gasTicketEditBoolean = false;
                    }
                    console.info($scope.vm.gasTicketEditBoolean);
                }
            });
        };

        var init = function () {
            $scope.pager.pageSize=25;
            searchCustomer();

        };

        init();

    }]);
