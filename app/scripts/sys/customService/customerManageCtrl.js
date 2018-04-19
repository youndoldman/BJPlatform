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
            CustomerName: historyQ.CustomerName || "",
            CustomerID: historyQ.CustomerName || ""
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

        //增加优惠券
        $scope.addCoupon = function(customer){
            udcModal.show({
                templateUrl: "./customService/addCouponModal.html",
                controller: "AddCouponModalCtrl",
                inputs: {
                    title: '增加优惠券',
                    initVal: customer
                }
            }).then(function (result) {
                if (result) {
                    searchCustomer();
                }
            })
        }

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

        //删除气票
        $scope.deleteGasTicket = function (customer) {
            udcModal.show({
                templateUrl: "./customService/deleteGasTicketModal.html",
                controller: "DeleteGasTicketModalCtrl",
                inputs: {
                    title: '删除气票',
                    initVal: customer
                }
            }).then(function (result) {
                if (result) {
                    searchCustomer();
                }
            })
        };

        //删除优惠券
        $scope.deleteCoupon = function(customer)
        {
            udcModal.show({
                templateUrl: "./customService/deleteCouponModal.html",
                controller: "DeleteCouponModalCtrl",
                inputs: {
                    title: '删除优惠券',
                    initVal: customer
                }
            }).then(function (result) {
                if (result) {
                    searchCustomer();
                }
            })
        }
        var searchCustomer = function () {
            var queryParams = {
                userId: $scope.q.CustomerID,
                userName: $scope.q.CustomerName,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            CustomerManageService.retrieveCustomers(queryParams).then(function (customers) {
                $scope.pager.update($scope.q, customers.total, queryParams.pageNo);
                $scope.vm.customerList = _.map(customers.items, CustomerManageService.toViewModel);
            });
        };

        var init = function () {
            $scope.pager.pageSize=25;
            searchCustomer();



        }
        //判断是不是气票用户
        $scope.isTicketUser =  function(customer){
            if(customer.settlementType.code == "00003")
            {
                return true;
            }else{
                return false;
            }
        }
        init();

    }]);
