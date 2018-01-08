'use strict';

bottleApp.service('BottleService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var bottlesUri = URI.resources.bottles;
    var bottleMap;

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

    this.getBottleInfoById = function (bottleId) {
        return promise.wrap($http.get(bottlesUri + '/' + bottleId));
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

    this.location = function(lon, lan)
    {
        var point = new BMap.Point(lon, lan);
        bottleMap.centerAndZoom(point, 15);
    }


}]);

