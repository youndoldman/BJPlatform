manageApp.controller('GoodsModalCtrl', ['$scope', 'close', 'GoodsService', 'title', 'initVal','Upload','URI','udcModal', function ($scope, close, GoodsService, title, initVal,Upload,URI,udcModal) {
    $scope.modalTitle = title;
    $scope.vm = {
        goods: {
            area:{
            "province":"",
            "city":"",
            "county":"",
            }
        },
        goodsTypesList:[],

        //currentGoods: {
        //    address:{
        //        "province":"",
        //        "city":"",
        //        "county":"",
        //    }
        //}
    };
    $scope.config = {
        goodsStatusList:[{name:"正常上市",value:0},{name:"暂停销售",value:1},{name:"商品下架",value:2}],
        provinces: [],
        citys: [],
        countys: [],
    };

    $scope.isModify = false;

    $scope.isCreate = true;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.provincesChange = function () {
        //获取市
        getCitysConfig($scope.vm.goods.area.province);
        $scope.config.countys = [];

    }

    $scope.citysChange = function () {
        //获取区
        if ($scope.vm.goods.area.city==null) {
            return;
        };
        getCountysConfig($scope.vm.goods.area.city);

    }

    $scope.submit = function (goods) {
        //$scope.vm.goods.address.province = $scope.vm.currentGoods.address.province;
        //$scope.vm.goods.address.city = $scope.vm.currentGoods.address.city;
        //$scope.vm.goods.address.county = $scope.vm.currentGoods.address.county;
        //
        //var goods = $scope.vm.goods;
        console.info(goods);
        //修改商品类型结构
        if (title == "新增商品") {
            GoodsService.createGoods(goods).then(function () {
                udcModal.info({"title": "处理结果", "message": "新增商品成功 "});
                $scope.close(true);
            }, function (value) {
                udcModal.info({"title": "处理结果", "message": "新增商品失败 " + value.message});
            })
        } else if (title == "修改商品") {
            GoodsService.modifyGoods(goods).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改商品成功 "});
                $scope.close(true);
            }, function (value) {
                udcModal.info({"title": "处理结果", "message": "修改商品失败 " + value.message});
            })
        }
    };




    var init = function () {

        //获取省
        getProvincesConfig("中国");
        //初始化地址信息
        if(title == "新增商品") {
            //console.log(initVal)
            $scope.isCreate = true;
            $scope.vm.goods.area.province = "云南省";
            getCitysConfig($scope.vm.goods.area.province);
            $scope.vm.goods.area.city = "昆明市";
            getCountysConfig($scope.vm.goods.area.city);
        }else {
            $scope.vm.goods = _.clone(initVal);
            $scope.isCreate = false;
            getCitysConfig($scope.vm.goods.area.province);
            getCountysConfig($scope.vm.goods.area.city);
            console.log($scope.vm.goods );
        }

        if(title == "修改商品") {
            $scope.isModify = true;
            console.log($scope.vm.goods.status);
        }
        else {
            $scope.isModify = false;
            //商品状态初始化
            $scope.vm.goods.status = $scope.config.goodsStatusList[0].value;
            console.log($scope.vm.goods.status);
        }
        //查询商品类型
        var queryParams = {
        };
        GoodsService.retrieveGoodsTypes(queryParams).then(function (goodsTypes) {
            $scope.vm.goodsTypesList = _.map(goodsTypes.items, GoodsService.toViewModelGoodsTypes);
            if(!$scope.isModify){
                $scope.vm.goods.goodsType = $scope.vm.goodsTypesList[0];
            }else{
                for(var tempGoods in $scope.vm.goodsTypesList){
                    if($scope.vm.goods.goodsType.code==$scope.vm.goodsTypesList[tempGoods].code){
                        $scope.vm.goods.goodsType = $scope.vm.goodsTypesList[tempGoods];
                    }
                }

            }
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
        GoodsService.retrieveSubdistrict(param).then(function (params) {
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

    init();
}]);