'use strict';

customServiceApp.controller('CustomerModalCtrl', ['$scope', 'close', 'CustomerManageService', 'title', 'initVal','udcModal',function ($scope, close, CustomerManageService, title, initVal, udcModal) {
    $scope.modalTitle = title;


    $scope.vm = {
        currentCustomer: {
            phone:null,
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

        settlementTypes:[],

        haveCylinders:[{name:"是",code:true},{name:"否",code:false}],
        customerStatus:[{name:"正常",code:0},{name:"注销",code:1}],

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
            //console.info(customer);
            CustomerManageService.createCustomer(customer).then(function () {
                udcModal.info({"title": "处理结果", "message": "新增客户信息成功 "});

            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "新增客户失败 "+value.message});
            })
        } else if (customer.name != "" && title == "修改客户") {
            CustomerManageService.modifyCustomer(customer).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改客户信息成功 "});
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "修改客户失败 "+value.message});
            })
        }
    };

    $scope.closeModal = function () {
        //新建客户的资料传到母页
        if($scope.vm.currentCustomer.userId!=null){
            var result = {value:true, param:$scope.vm.currentCustomer.userId};
            $scope.close(result);
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
            console.log(initVal)
            $scope.vm.currentCustomer.phone = _.clone(initVal);
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
        var company = null;
        if(title == "修改客户") {
            company = {name:""};
            if ($scope.vm.currentCustomer.customerCompany!=null){
                company.name = $scope.vm.currentCustomer.customerCompany.name;
            }
            $scope.isModify = true;
        }
        $scope.vm.currentCustomer.customerCompany = company;

        if(title == "新增客户") {
            $scope.vm.currentCustomer.haveCylinder = false;
            $scope.vm.currentCustomer.status = 0;
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

//结算类型信息查询
        CustomerManageService.retrieveSettlementType().then(function (settlementTypes) {
            $scope.config.settlementTypes = _.map(settlementTypes.items, CustomerManageService.toViewModelCustomType);
            if(title == "新增客户") {
                $scope.vm.currentCustomer.settlementType = $scope.config.settlementTypes[0];
            }else {
                for(var i=0; i<$scope.config.settlementTypes.length; i++){
                    if($scope.vm.currentCustomer.settlementType.id == $scope.config.settlementTypes[i].id){
                        $scope.vm.currentCustomer.settlementType = $scope.config.settlementTypes[i];
                        break;
                    }
                }
            }
        });
    };
    init();
}]);