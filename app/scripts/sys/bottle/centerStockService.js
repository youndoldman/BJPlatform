'use strict';

bottleApp.service('CenterStockService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {
    var bottlesUri = URI.resources.bottle;//钢瓶接口
    var bottleHandOverUri = URI.resources.bottleHandOver;//钢瓶交接接口


    this.retrieveBottles = function (params) {
        return promise.wrap($http.get(bottlesUri, {params: params}));
    };

    this.handOverBottle = function (number,srcUserId,targetUserId,serviceStatus) {
        return promise.wrap($http.put(bottleHandOverUri+"/"+number+"?srcUserId="+srcUserId
        +"&targetUserId="+targetUserId+"&serviceStatus="+serviceStatus));
    };

    this.modifyBottle = function (bottle) {
        return promise.wrap($http.put(bottlesUri + "/" + bottle.number, bottle));
    };


}]);

