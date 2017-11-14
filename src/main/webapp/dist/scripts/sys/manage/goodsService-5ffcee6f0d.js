/**
 * Created by Administrator on 2017/10/10.
 */
'use strict';

manageApp.service('GoodsService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var goodsUri = URI.resources.goods;

    this.toViewModel = function (goodsFromApi) {
        return {
            id: goodsFromApi.id,
            goodName: goodsFromApi.goodName,
            picture: goodsFromApi.picture,
            price: goodsFromApi.price,
            createTime: goodsFromApi.createTime,
            updateTime: goodsFromApi.updateTime,

        }
    };

    this.retrieveGoods = function (params) {
        return promise.wrap($http.get(goodsUri, {params: params}));
    };


    this.deleteGoods = function (goods) {
        return promise.wrap($http.delete(goodsUri + "/" + goods.id));
    };

}]);

