'use strict';

customServiceApp.controller('CallCenterCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'CustomerManageService', function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal, CustomerManageService) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchCustomer();
        };

        $scope.searchType = "客户姓名";
        $scope.searchParam = "";



        $scope.pager = pager.init('CustomerListCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            CustomerName: historyQ.CustomerName || "",
            CustomerPhone: historyQ.CustomerPhone || "",
            CustomerAddress: historyQ.CustomerAddress || ""
        };

        $scope.vm = {
            currentCustomer:{},
            CustomerList: []
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
                        });
                    }
                })
        };

        var searchCustomer = function () {
            var queryParams = {};
            if($scope.searchType=="客户姓名"){
                queryParams = {
                    userName: $scope.searchParam,
                    pageNo: $scope.pager.getCurPageNo(),
                    pageSize: $scope.pager.pageSize
                };
            }
            CustomerManageService.retrieveCustomers(queryParams).then(function (customers) {
                $scope.pager.update($scope.q, customers.total, queryParams.pageNo);
                $scope.vm.customerList = _.map(customers.items, CustomerManageService.toViewModel);
            });
        };
        //将列表中的客户信息显示到详情
        $scope.showDetail = function (customer) {
            $scope.vm.currentCustomer = customer;
        };


        var init = function () {
            searchCustomer();
        };

        init();

    }]);

customServiceApp.controller('CustomerModalCtrl', ['$scope', 'close', 'CustomerManageService', 'title', 'initVal', function ($scope, close, CustomerManageService, title, initVal) {
    $scope.modalTitle = title;
    $scope.vm = {
        currentCustomer: {}
    };


    $scope.config = {
        customerSources: [],
        customerLevels: [],
        customerTypes: [],
        haveCylinders:[true, false]
    };



    $scope.userGroups = [];
    $scope.departments = [];

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (customer) {
        if (customer.name != "" && title == "新增客户") {
            CustomerManageService.createCustomer(customer).then(function () {
                $scope.close(true);
            }, function(value) {
                // failure
                console.log(value);
                alert("新建用户失败： "+value.message);
            })
        } else if (customer.name != "" && title == "修改客户") {
            CustomerManageService.modifyCustomer(customer).then(function () {
                $scope.close(true);
            }, function(value) {
                // failure
                console.log(value);
                alert("修改用户失败： "+value.message);
            })
        }
    };

    var init = function () {
        $scope.vm.currentCustomer = _.clone(initVal);
        if(title == "修改客户") {
            $scope.isModify = true;
        }
        if(title == "新增客户") {
            $scope.vm.currentCustomer.haveCylinder = false;
        }else {
        }
        CustomerManageService.retrieveCustomerSource().then(function (customerSources) {
            $scope.config.customerSources = _.map(customerSources.items, CustomerManageService.toViewModelCustomSource);
            if(title == "新增客户") {
                $scope.vm.currentCustomer.customerSource = $scope.config.customerSources[1];
                console.log($scope.vm.currentCustomer);
            }else {
                for(var i=0; i<$scope.config.customerSources.length; i++){
                    if($scope.vm.currentCustomer.customerSource.id == $scope.config.customerSources[i].id){
                        $scope.vm.currentCustomer.customerSource = $scope.config.customerSources[i];
                        break;
                    }
                }
            }

        });
        CustomerManageService.retrieveCustomerLevel().then(function (customerLevels) {
            $scope.config.customerLevels = _.map(customerLevels.items, CustomerManageService.toViewModelCustomLevel);
            if(title == "新增客户") {
                $scope.vm.currentCustomer.customerLevel = $scope.config.customerLevels[0];
            }else {
                for(var i=0; i<$scope.config.customerLevels.length; i++){
                    if($scope.vm.currentCustomer.customerLevel.id == $scope.config.customerLevels[i].id){
                        $scope.vm.currentCustomer.customerLevel = $scope.config.customerLevels[i];
                        break;
                    }
                }
            }

        });
        CustomerManageService.retrieveCustomerType().then(function (customerTypes) {
            $scope.config.customerTypes = _.map(customerTypes.items, CustomerManageService.toViewModelCustomType);
            if(title == "新增客户") {
                $scope.vm.currentCustomer.customerType = $scope.config.customerTypes[0];
            }else {
                for(var i=0; i<$scope.config.customerTypes.length; i++){
                    if($scope.vm.currentCustomer.customerType.id == $scope.config.customerTypes[i].id){
                        $scope.vm.currentCustomer.customerType = $scope.config.customerTypes[i];
                        break;
                    }
                }
            }

        });
    };
    init();
}]);