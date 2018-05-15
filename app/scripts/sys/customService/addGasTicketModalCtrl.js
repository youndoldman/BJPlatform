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

            $('#startTimePicker1').datetimepicker({
                format: 'YYYY-MM-DD HH:mm:ss',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            $('#endTimePicker1').datetimepicker({
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

            $('#startTimePicker1').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.startDate1 = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#endTimePicker1').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.endDate1 = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
        });
    },500);

    $scope.q = {
        customer:{userId:null},
        operator:{userId:null},
        specCode:null,
        ticketSn:null,
        startDate:null,
        endDate:null,
        note:null,

        specCode1:null,
        quantity:null,
        startDate1:null,
        endDate1:null,
        note1:null
    };

    $scope.vm = {
        ticketStates:["待使用","已使用"],
        goodsList:[],
        selectedGoods:{},
        selectedGoods1:{},
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

    $scope.ticketSubmit = function () {
        var ticketInfo = {
            customer:$scope.q.customer,
            operator:$scope.q.operator,
            ticketSn:$scope.q.ticketSn,
            specCode:$scope.q.specCode,
            ticketStatus:0,
            startDate:$scope.q.startDate,
            endDate:$scope.q.endDate,
            note:$scope.q.note,
        };
        console.info(ticketInfo);

        if (title == "增加气票") {
            CustomerManageService.addTicketInfo(ticketInfo).then(function () {
                udcModal.info({"title": "处理结果", "message": "新增气票信息成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "新增气票失败 "+value.message});
            })
        }
    };

    $scope.couponSubmit = function (couponInfo) {
        var couponInfo = {
            customer:$scope.q.customer,
            operator:$scope.q.operator,
            specCode:$scope.q.specCode1,
            couponStatus:0,
            startDate:$scope.q.startDate1,
            endDate:$scope.q.endDate1,
            note:$scope.q.note1,
        };
        console.info(couponInfo);
        for(var i = 0; i< $scope.q.quantity; i++)
        {
            CustomerManageService.addCoupon(couponInfo).then(function () {
                udcModal.info({"title": "处理结果", "message": "新增优惠券成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "新增优惠券失败 "+value.message});
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

            $scope.vm.selectedGoods = $scope.vm.goodsList[0];
            $scope.q.specCode = $scope.vm.selectedGoods.code;
            $scope.vm.selectedGoods1 = $scope.vm.goodsList[0];
            $scope.q.specCode1 = $scope.vm.selectedGoods1.code;

        });
    };

    $scope.specCodeChange = function () {
        $scope.q.specCode = $scope.vm.selectedGoods.code;
        console.info( $scope.q.specCode)
    }

    $scope.specCodeChange1 = function () {
        $scope.q.specCode1 = $scope.vm.selectedGoods1.code;
        console.info( $scope.q.specCode1)
    }

    init();
}]);