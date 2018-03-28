/**
 * Created by Administrator on 2017/10/10.
 */
'use strict';

manageApp.service('GoodsService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var goodsUri = URI.resources.goods;
    var goodsTypesUri = URI.resources.goodsTypes;
    var goodsPriceAdjustmentUri = URI.resources.adjustPriceSchedules;



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

    //修改调价策略
    this.deleteAdjustPriceSchedules = function (adjustPriceSchedules) {
        return promise.wrap($http.delete(goodsPriceAdjustmentUri + "/" + adjustPriceSchedules.id));
    };

    //查询调价策略
    this.retrieveAdjustPriceSchedules = function (params) {
        return promise.wrap($http.get(goodsPriceAdjustmentUri, {params: params}));
    };

}]);

