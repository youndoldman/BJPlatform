/**
 * Created by Administrator on 2018/4/9.
 */
'use strict';

customServiceApp.controller('AddGasTicketModalCtrl', ['$scope', 'close', 'CustomerManageService', 'title', 'initVal','udcModal','sessionStorage','$timeout',function ($scope, close, CustomerManageService, title, initVal, udcModal,sessionStorage,$timeout) {
    $scope.modalTitle = title;

    $scope.currentUser = {};

    $timeout(function() {
        $(function () {
            $('#startTimePicker').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            $('#endTimePicker').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
            });
        });

        $(function () {
            $('#startTimePicker').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.startDate = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#endTimePicker').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.endDate = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
        });
    },300);

    $scope.q = {
        customer:{userId:null},
        operator:{userId:null},
        specCode:null,
        ticketStatus:null,
        startDate:null,
        endDate:null,
        note:null
    };

    $scope.vm = {
        ticketStates:["待使用","已使用"],
        goodsList:[],
        currentGasTicket:{},
        currentCustomer: {
            userId:null,
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

    $scope.submit = function (ticketInfo) {
        console.info(ticketInfo);

        if (title == "增加气票") {
            CustomerManageService.addTicketInfo(ticketInfo).then(function () {
                udcModal.info({"title": "处理结果", "message": "新增气票信息成功 "});
                $scope.close(true);
            }, function(value) {
                // failure
                udcModal.info({"title": "处理结果", "message": "新增气票失败 "+value.message});
            })
        }
        //else if (customer.name != "" && title == "修改客户") {
        //    CustomerManageService.modifyCustomer(customer).then(function () {
        //        udcModal.info({"title": "处理结果", "message": "修改客户信息成功 "});
        //        $scope.close(true);
        //    }, function(value) {
        //        udcModal.info({"title": "处理结果", "message": "修改客户失败 "+value.message});
        //    })
        //}
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
        $scope.vm.currentCustomer = _.clone(initVal);

        $scope.currentUser = sessionStorage.getCurUser();
        $scope.q.operator.userId = $scope.currentUser.userId;
        $scope.q.customer.userId = $scope.vm.currentCustomer.userId;
        $scope.searchGoods();


        //获取省
        getProvincesConfig("中国");
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

    $scope.searchGoods = function () {
        //查询商品名字为液化气的规格
        var queryParams = {
            typeName: "液化气",
        };
        CustomerManageService.retrieveGoods(queryParams).then(function (goods) {
            $scope.vm.goodsList = goods.items;
            //console.info($scope.vm.goodsList)
        });
    };

    $scope.specCodeChange = function () {
        //console.info($scope.specCode.name);
        for (var i = 0; i < $scope.vm.goodsList.length; i++) {
            if ($scope.specCode.name == $scope.vm.goodsList[i].name) {
                $scope.q.specCode = $scope.vm.goodsList[i].code;
                console.info($scope.q.specCode);
            }
        }
    }

    $scope.ticketStatusChange = function(){
        if($scope.state=="待使用")
        {
            $scope.q.ticketStatus = 0;
        }
        else if($scope.state=="已使用")
        {
            $scope.q.ticketStatus = 1;
        }
    }
    init();
}]);