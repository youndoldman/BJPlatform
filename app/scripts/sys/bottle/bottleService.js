'use strict';

bottleApp.service('BottleService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {


    var bottleUri = URI.resources.bottle;//钢瓶接口
    var gpsBindUri = URI.resources.gpsBind;//定位终端绑定接口
    var gpsUnBindUri = URI.resources.gpsUnBind;//定位终端解除绑定接口

    this.toViewModel = function (bottleFromApi) {
        return {
            id: bottleFromApi.id,
            name: bottleFromApi.name,
            facture: bottleFromApi.facture,
            location: bottleFromApi.location,
            createTime: bottleFromApi.createTime,
            updateTime: bottleFromApi.updateTime
        }
    };

    this.retrieveBottles = function (params) {
        return promise.wrap($http.get(bottlesUri, {params: params}));
    };

    this.createBottle = function (bottle) {
        return promise.wrap($http.post(bottlesUri, bottle));
    };

    this.modifyBottle = function (bottle) {
        return promise.wrap($http.put(bottlesUri + "/" + bottle.id, bottle));
    };

    this.deleteBottle = function (bottle) {
        return promise.wrap($http.delete(bottlesUri + "/" + bottle.id));
    };

    //绑定定位终端至钢瓶
    this.bindBottle = function (bottle, deviceId) {
        return promise.wrap($http.delete(bottlesUri + "/" + bottle.id));
    };
    //解除绑定定位终端
    this.unBindBottle = function (bottle) {
        return promise.wrap($http.delete(bottlesUri + "/" + bottle.id));
    };

}]);

