'use strict';

manageApp.controller('GoodsPriceAdjustmentModalCtrl', ['$scope', 'close', 'GoodsService', 'title', 'initVal','udcModal',function ($scope, close, GoodsService, title, initVal, udcModal) {
    $scope.modalTitle = title;

    $(function () {

        $('#datetimepickerStartExcute').datetimepicker({
            format: 'YYYY-MM-DD HH:mm',
            locale: moment.locale('zh-cn'),
            //sideBySide:true,
            showTodayButton:true,
            toolbarPlacement:'top',
        });

    });
    $(function () {
        $('#datetimepickerStartExcute').datetimepicker()
            .on('dp.change', function (ev) {
                var date = ev.date._d;
                var month = date.getMonth()+1;
                $scope.vm.currentPriceAjustment.effectTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                    +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
            });
    });

    $scope.vm = {
        currentPriceAjustment: {
            name:null,
            effectTime:null,
            adjustPriceDetailList:[]
        }
    };

    $scope.temp = {
        selectedGoodsType:{},
        selectedGoods:{},
        adjustGoodsPrice:null,
        goodsTypesList:[],
        goodsList:[],

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

    var init = function () {
        if(title == "修改调价策略") {
            $scope.vm.currentPriceAjustment = _.clone(initVal);
            $scope.isModify = true;
        }
        else {
            $scope.isModify = false;
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
        if ($scope.temp.selectedGoodsType==null) {
            return;
        };
        var queryParams = {
            typeName: $scope.temp.selectedGoodsType.name,
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