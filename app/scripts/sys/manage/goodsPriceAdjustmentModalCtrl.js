'use strict';

manageApp.controller('GoodsPriceAdjustmentModalCtrl', ['$scope', 'close', 'GoodsService', 'title', 'initVal','udcModal','$timeout',function ($scope, close, GoodsService, title, initVal, udcModal,$timeout) {
    $scope.modalTitle = title;
    $timeout(function(){
        $(function () {

            $('#datetimepickerStartExcute').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                showTodayButton: true,
                toolbarPlacement: 'top',
                //minDate:new Date(),
            });

        });
        $(function () {
            $('#datetimepickerStartExcute').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth() + 1;
                    $scope.vm.currentPriceAdjustment.effectTime = date.getFullYear() + "-" + month + "-" + date.getDate() + " "
                        + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                });
        });
    },500);

    $scope.inputHidden_effectTime = true;

    $scope.isCreate = true;

    $scope.vm = {
        currentPriceAdjustment: {
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

    $scope.submit = function (currentPriceAdjustment) {
        console.info(currentPriceAdjustment);

        var effectTime = new Date(currentPriceAdjustment.effectTime);
        var effectTimeNum = effectTime.getTime();

        var currentTime = new Date();
        var currentTimeNum = currentTime.getTime();

        if(effectTimeNum < currentTimeNum)
        {
            udcModal.info({"title": "提醒", "message": "调价策略的执行时间不能早于当前时间！"});
        }
        else
        {
            if ($scope.isModify)
            {
                currentPriceAdjustment.status = null;
                GoodsService.modifyAdjustPriceSchedules(currentPriceAdjustment).then(function () {
                    udcModal.info({"title": "处理结果", "message": "修改调价策略成功 "});
                    $scope.close(true);
                }, function(value) {
                    udcModal.info({"title": "处理结果", "message": "修改调价策略失败 "+value.message});
                })
            }
            else
            {
                if(currentPriceAdjustment.adjustPriceDetailList.length != 0)
                {
                    GoodsService.createAdjustPriceSchedules(currentPriceAdjustment).then(function () {
                        udcModal.info({"title": "处理结果", "message": "增加调价策略成功 "});
                        $scope.close(true);
                    }, function(value) {
                        udcModal.info({"title": "处理结果", "message": "增加调价策略失败 "+value.message});
                    })
                }
                else
                {
                    udcModal.info({"title": "错误信息", "message": "请先添加调价列表再提交保存"});
                    return;
                }
            }
        }

    };

    $scope.provincesChange = function () {
        $scope.temp.goodsList = null;
        //获取市
        getCitysConfig($scope.vm.goods.area.province);
        $scope.config.countys = [];

        var queryParams = {
            typeCode: $scope.temp.selectedGoodsType.code,
            province:$scope.vm.goods.area.province,
            city:$scope.vm.goods.area.city,
            county:$scope.vm.goods.area.county,
        };

        GoodsService.retrieveGoods(queryParams).then(function (goods) {
            $scope.temp.goodsList = goods.items;
            console.info($scope.temp.goodsList);
            $scope.temp.selectedGoods = $scope.temp.goodsList[0];
        });
    }

    $scope.citysChange = function () {
        $scope.temp.goodsList = null;
        //获取区
        if ($scope.vm.goods.area.city==null) {
            return;
        };
        getCountysConfig($scope.vm.goods.area.city);

         var queryParams = {
             typeCode: $scope.temp.selectedGoodsType.code,
             province:$scope.vm.goods.area.province,
             city:$scope.vm.goods.area.city,
             county:$scope.vm.goods.area.county,
         };
         GoodsService.retrieveGoods(queryParams).then(function (goods) {
             $scope.temp.goodsList = goods.items;
             console.info($scope.temp.goodsList);
             $scope.temp.selectedGoods = $scope.temp.goodsList[0];
         });
    }

    $scope.county = null;
    $scope.countysChange = function () {
        $scope.temp.goodsList = null;
        //获取区
      $scope.vm.goods.area.county = $scope.county;
        var queryParams = {
            typeCode: $scope.temp.selectedGoodsType.code,
            province:$scope.vm.goods.area.province,
            city:$scope.vm.goods.area.city,
            county:$scope.vm.goods.area.county,
        };
        GoodsService.retrieveGoods(queryParams).then(function (goods) {
            $scope.temp.goodsList = goods.items;
            console.info($scope.temp.goodsList);
            $scope.temp.selectedGoods = $scope.temp.goodsList[0];
        });

    };

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
    };

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
            $scope.vm.currentPriceAdjustment = _.clone(initVal);
            console.info( $scope.vm.currentPriceAdjustment);
            $scope.isModify = true;
            $scope.isCreate = false;

            //$scope.inputHidden_effectTime = false;
            //$scope.county =  $scope.vm.goods.area.county;
            //if($scope.vm.goods.area.county.length==0){
            //    $scope.county = "全部区";
            //}

            //$scope.temp.selectedGoodsType = $scope.vm.currentPriceAdjustment.adjustPriceDetailList[0].goods.goodsType.name;
            //console.info($scope.temp.selectedGoodsType);
            //$scope.temp.selectedGoods = $scope.vm.currentPriceAdjustment.adjustPriceDetailList[0].goods.name;
            //console.info($scope.temp.selectedGoods);
            $scope.vm.goods.area.province = $scope.vm.currentPriceAdjustment.adjustPriceDetailList[0].goods.area.province;
            getCitysConfig($scope.vm.goods.area.province);
            $scope.vm.goods.area.city = $scope.vm.currentPriceAdjustment.adjustPriceDetailList[0].goods.area.city;
            getCountysConfig($scope.vm.goods.area.city);
            $scope.county = $scope.vm.currentPriceAdjustment.adjustPriceDetailList[0].goods.area.county;

        }
        else {
            $scope.isModify = false;
            $scope.isCreate = true;

            $scope.inputHidden_effectTime = true;

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
        $scope.temp.goodsList = null;
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
            province:$scope.vm.goods.area.province,
            city:$scope.vm.goods.area.city,
            county:$scope.vm.goods.area.county,
        };

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
        var adjustmentDetail = {};
        adjustmentDetail.goods = $scope.temp.selectedGoods;
        adjustmentDetail.price = $scope.temp.adjustGoodsPrice;
       // $scope.vm.currentPriceAdjustment.adjustPriceDetailList.push(adjustmentDetail)
       // console.info($scope.vm.currentPriceAdjustment.adjustPriceDetailList.length);

        if($scope.vm.currentPriceAdjustment.adjustPriceDetailList.length == 0)
        {
            $scope.vm.currentPriceAdjustment.adjustPriceDetailList.push(adjustmentDetail)

            //console.info($scope.vm.currentPriceAdjustment.adjustPriceDetailList);
        }
        else
        {
            var findFlag = false;
            for(var i=0; i< $scope.vm.currentPriceAdjustment.adjustPriceDetailList.length; i++) {
                if(adjustmentDetail.goods.code == $scope.vm.currentPriceAdjustment.adjustPriceDetailList[i].goods.code)
                {
                    findFlag = true;
                    break;
                }
            }
            if(!findFlag){
                $scope.vm.currentPriceAdjustment.adjustPriceDetailList.push(adjustmentDetail);
            }

            //console.info($scope.vm.currentPriceAdjustment.adjustPriceDetailList);
        }



    };


    //删除购物车
    $scope.deleteGoodsFromCart = function (adjustmentDetail) {
        for(var i=0; i<$scope.vm.currentPriceAdjustment.adjustPriceDetailList.length; i++){
            if ($scope.vm.currentPriceAdjustment.adjustPriceDetailList[i] == adjustmentDetail) {
                $scope.vm.currentPriceAdjustment.adjustPriceDetailList.splice(i, 1);
                break;
            }
        }
    };

    init();
}]);