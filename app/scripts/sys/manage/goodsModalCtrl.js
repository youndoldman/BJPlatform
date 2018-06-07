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

    $scope.originalGoodsCode = null;
    $scope.config = {
        unitList:["公斤","瓶"],

        goodsStatusList:[{name:"正常上市",value:0},{name:"暂停销售",value:1},{name:"商品下架",value:2}],
        provinces: [],
        citys: [],
        countys: [],
    };
    $scope.unit = null,

    $scope.isModify = false;

    $scope.isCreate = true;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.unitChange = function(){
        console.info($scope.unit);
        $scope.vm.goods.unit = $scope.unit;
        console.log($scope.vm.goods.unit);
    }

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

    $scope.county = null;
    $scope.countysChange = function () {
        //获取区
        $scope.vm.goods.area.county = $scope.county;

    }


    $scope.submit = function (goods) {

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
            //console.log("修改商品");
            //console.log(goods);
            //console.log($scope.originalGoodsCode);
            GoodsService.modifyGoods($scope.originalGoodsCode,goods).then(function () {
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
            $scope.unit = $scope.config.unitList[0];
            $scope.vm.goods.unit = $scope.config.unitList[0];
            console.info($scope.unit);
            $scope.vm.goods.area.province = "云南省";
            getCitysConfig($scope.vm.goods.area.province);
            $scope.vm.goods.area.city = "昆明市";
            getCountysConfig($scope.vm.goods.area.city);

            $scope.county = "全部区";
            $scope.vm.goods.area.county = "全部区";

        }else {
            $scope.vm.goods = _.clone(initVal);
            $scope.isCreate = false;
            getCitysConfig($scope.vm.goods.area.province);
            getCountysConfig($scope.vm.goods.area.city);
        }

        if(title == "修改商品") {
            $scope.isModify = true;
            console.log($scope.vm.goods);
            $scope.originalGoodsCode = $scope.vm.goods.code;
          //  console.log($scope.originalGoodsCode);

            $scope.unit = $scope.vm.goods.unit;

            $scope.county =  $scope.vm.goods.area.county;
            if($scope.vm.goods.area.county.length==0){
                $scope.county = "全部区";
            }
        }
        else {
            $scope.isModify = false;
            //商品状态初始化
            $scope.vm.goods.status = $scope.config.goodsStatusList[0].value;

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

    init();
}]);