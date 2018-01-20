'use strict';

shopManageApp.service('ShopStockService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {
    var bottlesUri = URI.resources.bottle;//钢瓶接口
    var bottleHandOverUri = URI.resources.bottle;//钢瓶交接接口


    this.retrieveBottles = function (params) {
        return promise.wrap($http.get(bottlesUri, {params: params}));
    };

    this.handOverBottle = function (params) {
        return promise.wrap($http.post(bottleHandOverUri, {params: params}));
    };



}]);

