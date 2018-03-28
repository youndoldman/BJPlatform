/**
 * Created by Administrator on 2017/10/10.
 */
'use strict';

decisionApp.service('DecisionService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var goodsPriceHistory = URI.resources.goodsPriceHistory;
    //查询调价历史
    this.retrieveGoodsPriceHistory = function () {
        return promise.wrap($http.get(goodsPriceHistory));
    };

}]);

