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
            CustomerID: historyQ.CustomerName || "",
            customerType:null,
            settlementType:null,
            customerLevel:null
        };

        $scope.vm = {
            customerStatusDesc:["正常","注销"],
            customerList: [],
            gasTicketEditBoolean:true,
            customerTypeList: [{key:null,value:"全部"},{key:"00001",value:"居民客户"},{key:"00002",value:"餐饮客户"}],
            settlementTypeList: [{key:null,value:"全部"},{key:"00001",value:"普通客户"},{key:"00002",value:"月结客户"},{key:"00003",value:"气票客户"}],
            customerLevelList: [{key:null,value:"全部"},{key:"00001",value:"一级客户"},{key:"00002",value:"二级客户"},{key:"00003",value:"三级客户"}
                ,{key:"00004",value:"四级客户"},{key:"00005",value:"五级客户"},{key:"00006",value:"六级客户"},{key:"00007",value:"七级客户"}
                ,{key:"00008",value:"八级客户"},{key:"00009",value:"九级客户"},{key:"00010",value:"十级客户"}],
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

        $scope.showPurchaseHistory = function (customer) {
            udcModal.show({
                templateUrl: "./customService/purchaseHistoryModal.htm",
                controller: "purchaseHistoryModalCtrl",
                inputs: {
                    title: '消费记录',
                    initVal: customer
                }
            }).then(function (result) {
                if (result) {
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
                        }, function(value) {
                            udcModal.info({"title": "错误信息", "message": "删除客户信息失败 " + value.message});
                        })
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
                settlementTypeCode: $scope.q.settlementType.key,
                customerTypeCode: $scope.q.customerType.key,
                customerLevelCode: $scope.q.customerLevel.key,
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
            $scope.q.settlementType = $scope.vm.settlementTypeList[0];
            $scope.q.customerType= $scope.vm.customerTypeList[0];
            $scope.q.customerLevel = $scope.vm.customerLevelList[0];

            searchCustomer();
        };
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
