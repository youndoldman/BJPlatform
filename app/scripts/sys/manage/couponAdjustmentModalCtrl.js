/**
 * Created by Administrator on 2018/4/2.
 */
'use strict';

manageApp.controller('couponAdjustmentModalCtrl', ['$scope', 'close', 'GoodsService', 'title', 'initVal','udcModal','$timeout',function ($scope, close, GoodsService, title, initVal, udcModal,$timeout) {
    $scope.modalTitle = title;

    var currentTime = new Date();
    $timeout(function() {
        $(function () {
            $('#datetimepickerStartCoupon').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
                minDate:new Date(),
            });
            $('#datetimepickerEndCoupon').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
                minDate:new Date(),
            });
        });


        $(function () {
            $('#datetimepickerStartCoupon').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    //var dateNum = date.getTime();
                    //var currentTime = new Date();
                    //var currentTimeNum = currentTime.getTime();
                    //
                    //if(dateNum < currentTimeNum)
                    //{
                    //    udcModal.info({"title": "提醒", "message": "优惠策略开始时间不能早于当前时间"});
                    //    var date = new Date();
                    //}

                    var month = date.getMonth()+1;
                    $scope.q.startTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#datetimepickerEndCoupon').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
        });
    },300);

    $scope.config = {
        provinces: [],
        citys: [],
        countys: [],
    };

    $scope.isCreate = true;

    $scope.vm = {
        discountType:["直减","百分比折扣"],
        discountConditionType:["用户等级优惠","用户类别优惠"],
        //discountConditionType:["按用户级别","按用户类型"],
        useType:["排他型","叠加型"],

        discountText:"优惠：",
        currentPriceAdjustment: {
            name:null,
            effectTime:null,
            adjustPriceDetailList:[],
        },

        goods: {
            area:{
                "province":"",
                "city":"",
                "county":"",
            }
        },
    };

    $scope.q={
        startTime:null,
        endTime:null,
        discountType:null,
        discountConditionType:{},
        discountConditionValue:null,
        useType:null,
        discountDetails:[],
    };
    $scope.temp = {
        selectedGoodsType:{},
        selectedGoods:{},
        adjustGoodsPrice:null,
        goodsTypesList:[],
        goodsList:[],
//查询客户类型或者客户级别返回
        selectCustomer:{},
        customersList:[],
    };
    $scope.discountType = null,
        $scope.useType = null,
        $scope.type = null,
        $scope.value = null,


        $scope.useTypeSelectChange = function(){
            if($scope.useType == "排他型")
            {
                $scope.q.useType = 0;
            }
            else if($scope.useType == "叠加型")
            {
                $scope.q.useType = 1;
            }
            console.info($scope.q.useType);
        }

    $scope.discountTypeChange = function(){
        console.info($scope.discountType);
        if($scope.discountType == "直减")
        {
            $scope.q.discountType = 0;
            $scope.vm.discountText = "优惠直减金额"
        }
        else if($scope.discountType == "百分比折扣")
        {
            $scope.q.discountType = 1;
            $scope.vm.discountText = "优惠百分比(%)"
        }
    }

    $scope.selectChange = function () {
        console.info($scope.type);
        $scope.q.discountConditionType.code = $scope.type;

        //查询客户类型或者级别

        if($scope.type == "用户等级优惠")
        {
            $scope.q.discountConditionType.code = "00001";
            var queryParams = {};
            GoodsService.retrieveCustomerLevel(queryParams).then(function (customerLevel){
                //console.info(customerLevel.items);
                $scope.temp.customersList = customerLevel.items;
                $scope.value = $scope.temp.customersList[0].code;
                console.info($scope.value);
            });
        }
        else if($scope.type == "用户类别优惠")
        {
            $scope.q.discountConditionType.code = "00002";
            var queryParams = {};
            GoodsService.retrieveCustomerTypes(queryParams).then(function (customerType){
                //console.info(customerType.items)
                $scope.temp.customersList = customerType.items;
                $scope.value = $scope.temp.customersList[0].code;
                console.info($scope.value);
                //$scope.customersLevelTypeChange();
            });
        }
    }

    //客户类型或者级别选择改变discountConditionValue赋值
    $scope.customersLevelTypeChange = function () {
        //for (var i = 0; i < $scope.temp.customersList.length; i++)
        //{
        //if($scope.value == $scope.temp.customersList[i].code)
        //{
        $scope.q.discountConditionValue = $scope.value ;
        //console.info($scope.q.discountConditionValue);
        //}
        //}
    }

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (adjustDiscountStrategies) {
        console.info(adjustDiscountStrategies);
        if ($scope.isModify) {
            adjustDiscountStrategies.status = null;

            var adjustCoupon = {};
            adjustCoupon.name = adjustDiscountStrategies.name;
            adjustCoupon.startTime = adjustDiscountStrategies.startTime;
            adjustCoupon.endTime = adjustDiscountStrategies.endTime;
            adjustCoupon.discountType = adjustDiscountStrategies.discountType.index;
            adjustCoupon.discountConditionValue = adjustDiscountStrategies.discountConditionValue;
            adjustCoupon.discountConditionType = adjustDiscountStrategies.discountConditionType;
            adjustCoupon.useType = adjustDiscountStrategies.useType.index;
            adjustCoupon.discountDetails = adjustDiscountStrategies.discountDetails;


            GoodsService.modifyDiscountStrategies(adjustDiscountStrategies.id,adjustCoupon).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改优惠策略成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "修改优惠策略失败 "+value.message});
            })
        }else{

            GoodsService.createDiscountStrategies(adjustDiscountStrategies).then(function () {
                udcModal.info({"title": "处理结果", "message": "增加优惠策略成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "增加优惠策略失败 "+value.message});
            })
        }
    };

    $scope.provincesChange = function () {
        //获取市
        getCitysConfig($scope.vm.goods.area.province);
        $scope.config.countys = [];
        //$scope.goodsTypeChange();

        //var queryParams = {
        //    typeName: $scope.temp.selectedGoodsType.name,
        //    province:$scope.vm.goods.area.province,
        //    city:$scope.vm.goods.area.city,
        //    county:$scope.vm.goods.area.county,
        //};
        //GoodsService.retrieveGoods(queryParams).then(function (goods) {
        //    $scope.temp.goodsList = goods.items;
        //    $scope.temp.selectedGoods = $scope.temp.goodsList[0];
        //});
    }

    $scope.citysChange = function () {
        //获取区
        if ($scope.vm.goods.area.city==null) {
            return;
        };
        getCountysConfig($scope.vm.goods.area.city);
       //$scope.goodsTypeChange();
       //
       // var queryParams = {
       //     typeName: $scope.temp.selectedGoodsType.name,
       //     province:$scope.vm.goods.area.province,
       //     city:$scope.vm.goods.area.city,
       //     county:$scope.vm.goods.area.county,
       // };
       // GoodsService.retrieveGoods(queryParams).then(function (goods) {
       //     $scope.temp.goodsList = goods.items;
       //     $scope.temp.selectedGoods = $scope.temp.goodsList[0];
       // });
    }

    $scope.county = null;
    $scope.countysChange = function () {
        //获取区
        $scope.vm.goods.area.county = $scope.county;
        //$scope.goodsTypeChange();

        //var queryParams = {
        //    typeName: $scope.temp.selectedGoodsType.name,
        //    province:$scope.vm.goods.area.province,
        //    city:$scope.vm.goods.area.city,
        //    county:$scope.vm.goods.area.county,
        //};
        //GoodsService.retrieveGoods(queryParams).then(function (goods) {
        //    $scope.temp.goodsList = goods.items;
        //    $scope.temp.selectedGoods = $scope.temp.goodsList[0];
        //});

    }

    var getProvincesConfig = function(param){
        GoodsService.retrieveSubdistrict(param).then(function (params) {
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
        GoodsService.retrieveSubdistrict(param).then(function (params) {
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
        $scope.config.countys[0] = "全部区";
        GoodsService.retrieveSubdistrict(param).then(function (params) {
            var originalCountys = params.districts[0].districts;
            var countys = [];
            for (var i=0; i<originalCountys.length; i++){
                var county = originalCountys[i].name;
                $scope.config.countys.push(county);
                //countys.push(county);
            }
            //$scope.config.countys = countys;
        }, function(value) {
            udcModal.info({"title": "错误信息", "message": "请求高德地图服务失败 "+value.message});
        })
    }


    var init = function () {
        $scope.q.discountType = 0;
        $scope.discountType = $scope.vm.discountType[0];
        $scope.q.discountConditionType.code = $scope.type;
        $scope.useType = $scope.vm.useType[0];
        $scope.q.useType = 0;
        //$scope.type = $scope.vm.discountConditionType[0];


        getProvincesConfig("中国");

        if(title == "修改优惠策略") {
            $scope.q = _.clone(initVal);
            $scope.isModify = true;
            $scope.isCreate = false;

            $scope.type = $scope.q.discountConditionType.name;
            console.info($scope.type);

            if($scope.q.discountConditionType.code == "00001")
            {
                //按照用户级别请求
                var queryParams = {};
                GoodsService.retrieveCustomerLevel(queryParams).then(function (customerLevel){
                    $scope.temp.customersList = customerLevel.items;

                    for(var i = 0; i < customerLevel.items.length; i++)
                    {
                        if($scope.q.discountConditionValue == customerLevel.items[i].code)
                        {
                            $scope.value = customerLevel.items[i].code;
                            console.info($scope.value)
                        }
                    }
                });
            }
            else{
                //按照用户类别请求
                var queryParams = {};
                GoodsService.retrieveCustomerTypes(queryParams).then(function (customerType){

                    $scope.temp.customersList = customerType.items;
                    for(var i = 0; i < customerType.items.length; i++)
                    {
                        if($scope.q.discountConditionValue == customerType.items[i].code)
                        {
                            $scope.value = customerType.items[i].code;
                            console.info($scope.value)
                        }
                    }
                });
            }

            $scope.county =  $scope.vm.goods.area.county;
            if($scope.vm.goods.area.county.length==0){
                $scope.county = "全部区";
            }
        }
        else {
            $scope.isModify = false;
            $scope.isCreate = true;
            //按照用户级别先做一次条件取值请求
            $scope.q.discountConditionType.code = "00001";
            var queryParams = {};
            GoodsService.retrieveCustomerLevel(queryParams).then(function (customerLevel){

                $scope.temp.customersList = customerLevel.items;
                console.info(customerLevel.items);
                $scope.value = $scope.temp.customersList[0].code;
                $scope.q.discountConditionValue =  $scope.value;
                //console.info($scope.value);
            });


            $scope.type = $scope.vm.discountConditionType[0];

            $scope.vm.goods.area.province = "云南省";
            getCitysConfig($scope.vm.goods.area.province);
            $scope.vm.goods.area.city = "昆明市";
            getCountysConfig($scope.vm.goods.area.city);

            $scope.county = "全部区";
            $scope.vm.goods.area.county = "全部区";

        }
        $scope.vm.currentPriceAdjustment.adjustPriceDetailList =  $scope.q.discountDetails;
        var queryParams = {};
        //$scope.timeChange();
        GoodsService.retrieveGoodsTypes(queryParams).then(function (goodsTypes) {
            $scope.temp.goodsTypesList = goodsTypes.items;
            $scope.temp.selectedGoodsType = $scope.temp.goodsTypesList[0];
            $scope.goodsTypeChange();
        });
    };

    //商品选择改变
    $scope.goodsChange = function () {
        $scope.temp.adjustGoodsPrice = null;
    };



    //商品类型改变
    $scope.goodsTypeChange = function () {
        //获取区
        if ($scope.temp.selectedGoodsType==null) {
            return;
        };
        var queryParams = {
            typeCode: $scope.temp.selectedGoodsType.code,
            //province:$scope.vm.goods.area.province,
            //city:$scope.vm.goods.area.city,
            //county:$scope.vm.goods.area.county,
        };
        GoodsService.retrieveGoods(queryParams).then(function (goods) {
            $scope.temp.goodsList = goods.items;
            $scope.temp.selectedGoods = $scope.temp.goodsList[0];
        });
    };

    //添加购物车
    $scope.addToCart = function () {
        if($scope.temp.adjustGoodsPrice == null){
            udcModal.info({"title": "错误信息", "message": "请输入调整后的价格 "});
            return;
        }
        var adjustmentDetail = {};
        adjustmentDetail.goods = $scope.temp.selectedGoods;
        adjustmentDetail.discount = $scope.temp.adjustGoodsPrice;
        $scope.vm.currentPriceAdjustment.adjustPriceDetailList.push(adjustmentDetail);

        console.info($scope.vm.currentPriceAdjustment.adjustPriceDetailList);

        var adjustmentDetail1 = {};
        adjustmentDetail1.goods = $scope.temp.selectedGoods;
        adjustmentDetail1.discount = $scope.temp.adjustGoodsPrice;

        //$scope.q.discountDetails.push(adjustmentDetail1);

        //if($scope.vm.currentPriceAdjustment.adjustPriceDetailList.length > 0)
        //{
        //    $scope.q.discountDetails.goods =$scope.temp.selectedGoods;
        //    $scope.q.discountDetails.discount = $scope.temp.adjustGoodsPrice;
        //}
    };

    //删除购物车
    $scope.deleteGoodsFromCart = function (adjustmentDetail) {
        for(var i=0; i<$scope.vm.currentPriceAdjustment.adjustPriceDetailList.length; i++){
            if ($scope.vm.currentPriceAdjustment.adjustPriceDetailList[i] == adjustmentDetail) {
                $scope.vm.currentPriceAdjustment.adjustPriceDetailList.splice(i, 1);

                //$scope.q.discountDetails.splice(i, 1);
                break;
            }
        }
    };

    init();



}]);