/**
 * Created by Administrator on 2017/10/10.
 */
'use strict';

manageApp.service('GoodsService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {

    var goodsUri = URI.resources.goods;
    var goodsTypesUri = URI.resources.goodsTypes;
    var goodsPriceAdjustmentUri = URI.resources.adjustPriceSchedules;

    var customerTypeUri = URI.resources.customerType;
    var customerLevelUri = URI.resources.customerLevel;

    var discountStrategiesUri = URI.resources.discountStrategies;
    var key = MISC.keys.gaodeKey;
    var subdistrictUri = URI.resources.subdistrict;
    //行政区域查询
    this.retrieveSubdistrict = function (keyWords) {
        return promise.wrap($http.get(subdistrictUri+"?key="+key+"&keywords="+keyWords));
    };


    this.toViewModelGoods = function (goodsFromApi) {
        return {
            id: goodsFromApi.id,
            goodName: goodsFromApi.goodName,
            picture: goodsFromApi.picture,
            price: goodsFromApi.price,
            createTime: goodsFromApi.createTime,
            updateTime: goodsFromApi.updateTime,

        }
    };

    this.toViewModelGoodsTypes = function (goodsTypesFromApi) {
        return {
            id: goodsTypesFromApi.id,
            code: goodsTypesFromApi.code,
            name: goodsTypesFromApi.name,
            note: goodsTypesFromApi.note,
            createTime: goodsTypesFromApi.createTime,
            updateTime: goodsTypesFromApi.updateTime,

        }
    };


    this.retrieveGoods = function (params) {
        return promise.wrap($http.get(goodsUri, {params: params}));
    };

    this.retrieveGoodsTypes = function (params) {
        return promise.wrap($http.get(goodsTypesUri, {params: params}));
    };
//客户类型查询
    this.retrieveCustomerTypes = function (params) {
        return promise.wrap($http.get(customerTypeUri, {params: params}));
    };
//客户级别查询
    this.retrieveCustomerLevel= function (params) {
        return promise.wrap($http.get(customerLevelUri, {params: params}));
    };

    this.deleteGoods = function (goods) {
        return promise.wrap($http.delete(goodsUri + "/" + goods.code));
    };

    this.deleteGoodsType = function (goodsType) {
        return promise.wrap($http.delete(goodsTypesUri + "/" + goodsType.code));
    };

    this.createGoodsType = function (goodsType) {
        return promise.wrap($http.post(goodsTypesUri, goodsType));
    };

    this.modifyGoodsType = function (goodsType, oldCode) {
        return promise.wrap($http.put(goodsTypesUri + "/" + goodsType.code, goodsType));
    };

    this.createGoods = function (goods) {
        return promise.wrap($http.post(goodsUri, goods));
    };

    this.modifyGoods = function (goods) {
        return promise.wrap($http.put(goodsUri + "/" + goods.code, goods));
    };

    //添加调价策略
    this.createAdjustPriceSchedules = function (adjustPriceSchedules) {
        return promise.wrap($http.post(goodsPriceAdjustmentUri, adjustPriceSchedules));
    };

    //修改调价策略
    this.modifyAdjustPriceSchedules = function (adjustPriceSchedules) {
        return promise.wrap($http.put(goodsPriceAdjustmentUri + "/" + adjustPriceSchedules.id, adjustPriceSchedules));
    };

    //删除调价策略
    this.deleteAdjustPriceSchedules = function (adjustPriceSchedules) {
        return promise.wrap($http.delete(goodsPriceAdjustmentUri + "/" + adjustPriceSchedules.id));
    };

    //查询调价策略
    this.retrieveAdjustPriceSchedules = function (params) {
        return promise.wrap($http.get(goodsPriceAdjustmentUri, {params: params}));
    };

    //添加优惠策略
    this.createDiscountStrategies = function (adjustDiscountStrategies) {
        return promise.wrap($http.post(discountStrategiesUri, adjustDiscountStrategies));
    };
    //修改优惠策略
    this.modifyDiscountStrategies = function (id, adjustDiscountStrategies) {
        return promise.wrap($http.put(discountStrategiesUri + "/" + id, adjustDiscountStrategies));
    };
    //删除优惠策略
    this.deleteDiscountStrategies = function (adjustDiscountStrategies) {
        return promise.wrap($http.delete(discountStrategiesUri + "/" + adjustDiscountStrategies.id));
    };
    //查询优惠策略
    this.retrieveDiscountStrategies = function (params) {
        return promise.wrap($http.get(discountStrategiesUri,{params: params}));
    };
}]);

