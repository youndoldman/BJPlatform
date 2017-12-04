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
            currentCustomer:null,
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
            if (customer==null) {
                udcModal.info({"title": "错误信息", "message": "请选择客户 "});
                return;
            };
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
            //呼叫中心初始化
            callCenterLogin();
            searchCustomer();
        };

        init();

    }]);

customServiceApp.controller('CustomerModalCtrl', ['$scope', 'close', 'CustomerManageService', 'title', 'initVal','udcModal', function ($scope, close, CustomerManageService, title, initVal, udcModal) {
    $scope.modalTitle = title;
    $scope.vm = {
        currentCustomer: {
            address:{
                "province":"",
                "city":"",
                "county":"",
            }
        }
    };


    $scope.config = {
        customerSources: [],
        customerLevels: [],
        customerTypes: [],
        haveCylinders:[true, false],

        provinces: [],
        citys: [],
        countys: [],
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
                udcModal.info({"title": "处理结果", "message": "新增客户信息成功 "});
                $scope.close(true);
            }, function(value) {
                // failure
                udcModal.info({"title": "处理结果", "message": "新增客户失败 "+value.message});
            })
        } else if (customer.name != "" && title == "修改客户") {
            CustomerManageService.modifyCustomer(customer).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改客户信息成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "修改客户失败 "+value.message});
            })
        }
    };

    $scope.provincesChange = function () {
        //获取市
        getCitysConfig($scope.vm.currentCustomer.address.province);
        $scope.config.countys = [];

    }

    $scope.citysChange = function () {
        //获取区
        if ($scope.vm.currentCustomer.address.city==null) {
            return;
        };
        getCountysConfig($scope.vm.currentCustomer.address.city);

    }

    var getProvincesConfig = function(param){
        CustomerManageService.retrieveSubdistrict(param).then(function (params) {
            var originalProvinces = params.districts[0].districts;
            var provinces = [];
            for (var i=0; i<originalProvinces.length; i++){
                var province = originalProvinces[i].name;
                provinces.push(province);
            }
            $scope.config.provinces = provinces;
        }, function(value) {
            udcModal.info({"title": "错误信息", "message": "请求高德地图服务失败 "+value.message});
        })
    }

    var getCitysConfig = function(param){
        CustomerManageService.retrieveSubdistrict(param).then(function (params) {
            var originalCitys = params.districts[0].districts;
            var citys = [];
            for (var i=0; i<originalCitys.length; i++){
                var city = originalCitys[i].name;
                citys.push(city);
            }
            $scope.config.citys = citys;
        }, function(value) {
            udcModal.info({"title": "错误信息", "message": "请求高德地图服务失败 "+value.message});
        })
    }

    var getCountysConfig = function(param){
        CustomerManageService.retrieveSubdistrict(param).then(function (params) {
            var originalCountys = params.districts[0].districts;
            var countys = [];
            for (var i=0; i<originalCountys.length; i++){
                var county = originalCountys[i].name;
                countys.push(county);
            }
            $scope.config.countys = countys;
        }, function(value) {
            udcModal.info({"title": "错误信息", "message": "请求高德地图服务失败 "+value.message});
        })
    }


    var init = function () {
        //获取省
        getProvincesConfig("中国");
        //初始化地址信息
        if(title == "新增客户") {
            $scope.vm.currentCustomer.address.province = "云南省";
            getCitysConfig($scope.vm.currentCustomer.address.province);
            $scope.vm.currentCustomer.address.city = "昆明市";
            getCountysConfig($scope.vm.currentCustomer.address.city);
        }else {
            $scope.vm.currentCustomer = _.clone(initVal);
            getCitysConfig($scope.vm.currentCustomer.address.province);
            getCountysConfig($scope.vm.currentCustomer.address.city);
            console.log($scope.vm.currentCustomer );
        }

        //修改客户公司的结构
        var company = {name:""};
        if(title == "修改客户") {
            if ($scope.vm.currentCustomer.customerCompany!=null){
                company.name = $scope.vm.currentCustomer.customerCompany.name;
            }
            $scope.isModify = true;
        }
        $scope.vm.currentCustomer.customerCompany = company;

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