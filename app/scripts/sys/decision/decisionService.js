/**
 * Created by Administrator on 2017/10/10.
 */
'use strict';

decisionApp.service('DecisionService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var goodsPriceHistoryUri = URI.resources.goodsPriceHistory;
    var gasCynTrayHistoryUri = URI.resources.GasCynTrayHistory;
    var ordersCountUri = URI.resources.ordersCount;


    //查询调价历史
    this.retrieveGoodsPriceHistory = function () {
        return promise.wrap($http.get(goodsPriceHistoryUri));
    };

    //查询托盘历史
    this.retrieveGasCynTrayHistory = function (params) {
        return promise.wrap($http.get(gasCynTrayHistoryUri,{params:params}));
    };

    //查询订单数量
    this.retrieveOrdersCount = function (params) {
        return promise.wrap($http.get(ordersCountUri,{params:params}));
    };

}]);

