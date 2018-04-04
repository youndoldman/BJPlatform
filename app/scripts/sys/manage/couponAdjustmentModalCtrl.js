/**
 * Created by Administrator on 2018/4/2.
 */
'use strict';

manageApp.controller('couponAdjustmentModalCtrl', ['$scope', 'close', 'GoodsService', 'title', 'initVal','udcModal',function ($scope, close, GoodsService, title, initVal, udcModal) {
    $scope.modalTitle = title;

    $(function () {
        $('#datetimepickerStartCoupon').datetimepicker({
            format: 'YYYY-MM-DD HH:mm',
            locale: moment.locale('zh-cn'),
            //sideBySide:true,
            showTodayButton:true,
            toolbarPlacement:'top',
        });
        $('#datetimepickerEndCoupon').datetimepicker({
            format: 'YYYY-MM-DD HH:mm',
            locale: moment.locale('zh-cn'),
            //sideBySide:true,
            showTodayButton:true,
            toolbarPlacement:'top',
        });

    });
    $(function () {
        $('#datetimepickerStartCoupon').datetimepicker()
            .on('dp.change', function (ev) {
                var date = ev.date._d;
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
    $scope.vm = {
        discountType:["直减","百分比折扣"],
        discountConditionType:["按用户级别","按用户类型"],
        useType:["排他型","叠加型"],

        discountText:"优惠：",

        currentPriceAdjustment: {
            name:null,
            effectTime:null,
            adjustPriceDetailList:[],
        }
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
        var queryParams = {};
        if($scope.type == "按用户级别")
        {
            $scope.q.discountConditionType.code = "00001";
            GoodsService.retrieveCustomerLevel(queryParams).then(function (customerLevel){
                console.info(customerLevel.items);
                $scope.temp.customersList = customerLevel.items;
                $scope.temp.selectCustomer = $scope.temp.customersList[0];
                //$scope.customersLevelTypeChange();
            });
        }
        else if($scope.type == "按用户类型")
        {
            $scope.q.discountConditionType.code = "00002";
            GoodsService.retrieveCustomerTypes(queryParams).then(function (customerType){
                console.info(customerType.items)
                $scope.temp.customersList = customerType.items;
                $scope.temp.selectCustomer = $scope.temp.customersList[0];
                //$scope.customersLevelTypeChange();

            });
        }
    }

    //客户类型或者级别选择改变discountConditionValue赋值
    $scope.customersLevelTypeChange = function () {
        console.info($scope.value.name);
        if($scope.value.name == "未定义")
        {
            $scope.q.discountConditionValue ="00000";
        }
        else if(($scope.value.name == "一级客户") || ($scope.value.name == "普通住宅客户"))
        {
            $scope.q.discountConditionValue ="00001";
        }
        else if(($scope.value.name == "2级客户") || ($scope.value.name == "餐饮客户1"))
        {
            $scope.q.discountConditionValue ="00002";
        }
        else if($scope.value.name == "4级客户")
        {
            $scope.q.discountConditionValue ="00004";
        }

        //console.info($scope.q.discountConditionValue);
    }

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (adjustDiscountStrategies) {
        if ($scope.isModify) {
            adjustDiscountStrategies.status = null;
            console.info(adjustDiscountStrategies);
            var adjustCoupon = {};
            adjustCoupon.name = adjustDiscountStrategies.name;
            adjustCoupon.startTime = adjustDiscountStrategies.startTime;
            adjustCoupon.endTime = adjustDiscountStrategies.endTime;
            adjustCoupon.discountType = adjustDiscountStrategies.discountType;
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
            console.info(adjustDiscountStrategies);
            GoodsService.createDiscountStrategies(adjustDiscountStrategies).then(function () {
                udcModal.info({"title": "处理结果", "message": "增加优惠策略成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "增加优惠策略失败 "+value.message});
            })

        }
    };

    var init = function () {
        if(title == "修改优惠策略") {
            $scope.q = _.clone(initVal);
            //$scope.discountType = $scope.q.discountType.name;
            $scope.isModify = true;
        }
        else {
            $scope.isModify = false;
        }
        $scope.vm.currentPriceAdjustment.adjustPriceDetailList =  $scope.q.discountDetails;
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
        var adjustmentDetail = {};
        adjustmentDetail.goods = $scope.temp.selectedGoods;
        adjustmentDetail.price = $scope.temp.adjustGoodsPrice;
        $scope.vm.currentPriceAdjustment.adjustPriceDetailList.push(adjustmentDetail);

        var adjustmentDetail1 = {};
        adjustmentDetail1.goods = $scope.temp.selectedGoods;
        adjustmentDetail1.discount = $scope.temp.adjustGoodsPrice;

            $scope.q.discountDetails.push(adjustmentDetail1);



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

                //$scope.q.discountDetails.goods =$scope.vm.currentPriceAdjustment.adjustPriceDetailList.goods;
                $scope.q.discountDetails.splice(i, 1);
                break;
            }
        }
    };

    init();
}]);