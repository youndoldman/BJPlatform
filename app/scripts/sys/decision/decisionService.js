/**
 * Created by Administrator on 2017/10/10.
 */
'use strict';

decisionApp.service('DecisionService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var goodsPriceHistoryUri = URI.resources.goodsPriceHistory;
    var gasCynTrayHistoryUri = URI.resources.GasCynTrayHistory;
    //查询调价历史
    this.retrieveGoodsPriceHistory = function () {
        return promise.wrap($http.get(goodsPriceHistoryUri));
    };

    //查询调价历史
    this.retrieveGasCynTrayHistory = function (params) {
        return promise.wrap($http.get(gasCynTrayHistoryUri,{params:params}));
    };

}]);

