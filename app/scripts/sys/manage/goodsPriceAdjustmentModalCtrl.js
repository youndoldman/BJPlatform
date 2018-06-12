'use strict';

manageApp.controller('GoodsPriceAdjustmentModalCtrl', ['$scope', 'close', 'GoodsService', 'title', 'initVal','udcModal','$timeout',function ($scope, close, GoodsService, title, initVal, udcModal,$timeout) {
    $scope.modalTitle = title;
    $timeout(function(){
        $(function () {

            $('#datetimepickerStartExcute').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton: true,
                toolbarPlacement: 'top',
                minDate:new Date(),
            });

        });
        $(function () {
            $('#datetimepickerStartExcute').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth() + 1;
                    $scope.vm.currentPriceAjustment.effectTime = date.getFullYear() + "-" + month + "-" + date.getDate() + " "
                        + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                });
        });
    },300);

    $scope.isCreate = true;

    $scope.vm = {
        currentPriceAjustment: {
            name:null,
            effectTime:null,
            adjustPriceDetailList:[]
        },
        goods: {
            area:{
                "province":"",
                "city":"",
                "county":"",
            }
        },
    };

    $scope.temp = {
        selectedGoodsType:{},
        selectedGoods:{},
        adjustGoodsPrice:null,
        goodsTypesList:[],
        goodsList:[],

    };

    $scope.config = {
        provinces: [],
        citys: [],
        countys: [],
    };

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (adjustPriceSchedules) {

        if ($scope.isModify) {
            adjustPriceSchedules.status = null;
            GoodsService.modifyAdjustPriceSchedules(adjustPriceSchedules).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改调价策略成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "修改调价策略失败 "+value.message});

            })
        }else{
            GoodsService.createAdjustPriceSchedules(adjustPriceSchedules).then(function () {
                udcModal.info({"title": "处理结果", "message": "增加调价策略成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "增加调价策略失败 "+value.message});
            })

        }
    };

    $scope.provincesChange = function () {
        //获取市
        getCitysConfig($scope.vm.goods.area.province);
        $scope.config.countys = [];
        //$scope.goodsTypeChange();

        var queryParams = {
            typeCode: $scope.temp.selectedGoodsType.code,
            province:$scope.vm.goods.area.province,
            city:$scope.vm.goods.area.city,
            county:$scope.vm.goods.area.county,
        };
        console.info("省选择")
        console.info(queryParams);
        GoodsService.retrieveGoods(queryParams).then(function (goods) {
            $scope.temp.goodsList = goods.items;
            console.info($scope.temp.goodsList);
            $scope.temp.selectedGoods = $scope.temp.goodsList[0];
        });
    }

    $scope.citysChange = function () {
        //获取区
        if ($scope.vm.goods.area.city==null) {
            return;
        };
        getCountysConfig($scope.vm.goods.area.city);
        //$scope.goodsTypeChange();
        //
         var queryParams = {
             typeCode: $scope.temp.selectedGoodsType.code,
             province:$scope.vm.goods.area.province,
             city:$scope.vm.goods.area.city,
             county:$scope.vm.goods.area.county,
         };
        console.info("城市选择")
        console.info(queryParams);
         GoodsService.retrieveGoods(queryParams).then(function (goods) {
             $scope.temp.goodsList = goods.items;
             console.info($scope.temp.goodsList);
             $scope.temp.selectedGoods = $scope.temp.goodsList[0];
         });
    }

    $scope.county = null;
    $scope.countysChange = function () {
        //获取区
      $scope.vm.goods.area.county = $scope.county;
        //$scope.goodsTypeChange();

        var queryParams = {
            typeCode: $scope.temp.selectedGoodsType.code,
            province:$scope.vm.goods.area.province,
            city:$scope.vm.goods.area.city,
            county:$scope.vm.goods.area.county,
        };
        console.info("区县选择");
        console.info(queryParams);
        //GoodsService.retrieveGoods(queryParams).then(function (goods) {
        //    $scope.temp.goodsList = goods.items;
        //    console.info($scope.temp.goodsList);
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
        getProvincesConfig("中国");

        if(title == "修改调价策略") {
            $scope.vm.currentPriceAjustment = _.clone(initVal);
            $scope.isModify = true;
            $scope.isCreate = false;
            $scope.county =  $scope.vm.goods.area.county;
            if($scope.vm.goods.area.county.length==0){
                $scope.county = "全部区";
            }

        }
        else {
            $scope.isModify = false;
            $scope.isCreate = true;
            $scope.vm.goods.area.province = "云南省";
            getCitysConfig($scope.vm.goods.area.province);
            $scope.vm.goods.area.city = "昆明市";
            getCountysConfig($scope.vm.goods.area.city);

            $scope.county = "全部区";
            $scope.vm.goods.area.county = "全部区";


        }
        //查询商品类型
        var queryParams = {};
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
        //console.info($scope.vm.goods);
        if ($scope.temp.selectedGoodsType==null) {
            return;
        };
        if($scope.vm.goods == null)
        {
            return;
        }

        var queryParams = {
            typeCode: $scope.temp.selectedGoodsType.code,
            //province:$scope.vm.goods.area.province,
            //city:$scope.vm.goods.area.city,
            //county:$scope.vm.goods.area.county,
        };
        //console.info("搜索区域商品");
        //console.info(queryParams);
        GoodsService.retrieveGoods(queryParams).then(function (goods) {
            $scope.temp.goodsList = goods.items;
            console.info($scope.temp.goodsList );
            $scope.temp.selectedGoods = $scope.temp.goodsList[0];
        });
    };

    //添加购物车
    $scope.addToCart = function () {
        if($scope.temp.adjustGoodsPrice == null){
            udcModal.info({"title": "错误信息", "message": "请输入调整后的价格 "});
            return;
        }
        var ajustmentDetail = {};
        ajustmentDetail.goods = $scope.temp.selectedGoods;
        ajustmentDetail.price = $scope.temp.adjustGoodsPrice;
        $scope.vm.currentPriceAjustment.adjustPriceDetailList.push(ajustmentDetail);
    };


    //删除购物车
    $scope.deleteGoodsFromCart = function (ajustmentDetail) {
        for(var i=0; i<$scope.vm.currentPriceAjustment.adjustPriceDetailList.length; i++){
            if ($scope.vm.currentPriceAjustment.adjustPriceDetailList[i] == ajustmentDetail) {
                $scope.vm.currentPriceAjustment.adjustPriceDetailList.splice(i, 1);
                break;
            }
        }
    };

    init();
}]);