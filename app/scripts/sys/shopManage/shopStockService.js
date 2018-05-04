'use strict';

shopManageApp.service('ShopStockService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {
    var bottlesUri = URI.resources.bottle;//钢瓶接口
    var bottleHandOverUri = URI.resources.bottleHandOver;//钢瓶交接接口
    var usersUri = URI.resources.users;//用户信息查询接口

    var writeOffUri = URI.resources.writeOff;//回款
    var writeOffDetailUri = URI.resources.writeOffDetail;//回款明细

    this.retrieveBottles = function (params) {
        return promise.wrap($http.get(bottlesUri, {params: params}));
    };

    this.handOverBottle = function (number,srcUserId,targetUserId,serviceStatus) {
        return promise.wrap($http.put(bottleHandOverUri+"/"+number+"?srcUserId="+srcUserId
            +"&targetUserId="+targetUserId+"&serviceStatus="+serviceStatus));
    };

    this.retrieveUsers = function (params) {
        return promise.wrap($http.get(usersUri, {params: params}));
    };

    this.retrieveUsers = function (params) {
        return promise.wrap($http.get(usersUri, {params: params}));
    };

    this.modifyBottle = function (bottle) {
        return promise.wrap($http.put(bottlesUri + "/" + bottle.number, bottle));
    };

    //增加回款
    this.addWriteOff = function (params) {
        return promise.wrap($http.post(writeOffUri, params));
    };
    //回款明细查询
    this.searchWriteOffDetail = function (params) {
        return promise.wrap($http.get(writeOffDetailUri, {params: params}));
    };

    var gasCylinderSpecUri = URI.resources.gasCylinderSpec;//钢瓶规格


    //查询钢瓶规格
    this.retrieveGasCylinderSpecUri = function (params) {
        return promise.wrap($http.get(gasCylinderSpecUri, {params: params}));
    };

    var addGasCylinderSpecUri = URI.resources.addGasCylinderSpec;//钢检瓶动态增加
    this.addGasCylinderSpecUri = function (params) {
        return promise.wrap($http.post(addGasCylinderSpecUri, params));
    };

    this.searchGasCylinder = function (params) {
        return promise.wrap($http.get(addGasCylinderSpecUri, {params: params}));
    };
}]);

