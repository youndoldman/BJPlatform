'use strict';

bottleApp.service('CenterStockService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {
    var bottlesUri = URI.resources.bottle;//钢瓶接口
    var bottleHandOverUri = URI.resources.bottleHandOver;//钢瓶交接接口


    this.retrieveBottles = function (params) {
        return promise.wrap($http.get(bottlesUri, {params: params}));
    };

    this.handOverBottle = function (number,params) {
        return promise.wrap($http.put(bottleHandOverUri+"/"+number, {params: params}));
    };



}]);

