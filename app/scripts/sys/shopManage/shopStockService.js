'use strict';

shopManageApp.service('ShopStockService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {
    var bottlesUri = URI.resources.bottle;//钢瓶接口
    var bottleHandOverUri = URI.resources.bottleHandOver;//钢瓶交接接口

    var writeOffUri = URI.resources.writeOff;//回款
    var writeOffDetailUri = URI.resources.writeOffDetail;//回款明细

    this.retrieveBottles = function (params) {
        return promise.wrap($http.get(bottlesUri, {params: params}));
    };

    this.handOverBottle = function (number,srcUserId,targetUserId,serviceStatus) {
        return promise.wrap($http.put(bottleHandOverUri+"/"+number+"?srcUserId="+srcUserId
            +"&targetUserId="+targetUserId+"&serviceStatus="+serviceStatus));
    };

    //增加回款
    this.addWriteOff = function (params) {
        return promise.wrap($http.post(writeOffUri, params));
    };
    //回款明细查询
    this.searchWriteOffDetail = function (params) {
        return promise.wrap($http.get(writeOffDetailUri, {params: params}));
    };
}]);

