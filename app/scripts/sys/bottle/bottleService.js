'use strict';

bottleApp.service('BottleService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {


    var bottlesUri = URI.resources.bottle;//钢瓶接口
    var gpsBindUri = URI.resources.gpsBind;//定位终端绑定接口
    var gpsUnBindUri = URI.resources.gpsUnBind;//定位终端解除绑定接口
    var bottleSpecQueryUri = URI.resources.bottleSpecQuery;//钢瓶规格查询
    var departmentUri = URI.resources.department;//部门查询接口

    var gasCylinderPositionUri = URI.resources.gasCylinderPosition;//钢瓶轨迹查询
    var gasCylinderTakeOverHistoryUri = URI.resources.gasCylinderTakeOverHistory;//钢瓶交接记录查询

    var gasCylinderWarnUri = URI.resources.GasCylinderWarn;//钢瓶告警信息
    var gasCylinderFillingUri = URI.resources.GasFillingMerge;//充装数据查询

    var bottleByRangeUri = URI.resources.bottleByRange;//范围获取钢瓶

    var bottleLocationsUri = URI.resources.bottleLocations;//获取钢瓶位置


    this.toViewModel = function (bottleFromApi) {
        return {
            id: bottleFromApi.id,
        }
    };

    this.retrieveBottlesLocation = function (params) {
        return promise.wrap($http.get(bottleLocationsUri, {params: params}));
    };

    this.retrieveBottlesByRange = function (params) {
        return promise.wrap($http.get(bottleByRangeUri, {params: params}));
    };

    this.retrieveBottles = function (params) {
        return promise.wrap($http.get(bottlesUri, {params: params}));
    };

    this.createBottle = function (bottle) {
        return promise.wrap($http.post(bottlesUri, bottle));
    };

    this.modifyBottle = function (bottle) {
        return promise.wrap($http.put(bottlesUri + "/" + bottle.number, bottle));
    };

    this.deleteBottle = function (bottle) {
        return promise.wrap($http.delete(bottlesUri + "/" + bottle.number));
    };

    //绑定定位终端至钢瓶
    this.bindBottle = function (bottle, locationDevNumber) {
        return promise.wrap($http.put(gpsBindUri + "/" + bottle.number+"?locationDevNumber="+locationDevNumber));
    };
    //解除绑定定位终端
    this.unBindBottle = function (bottle, locationDevNumber) {
        return promise.wrap($http.put(gpsUnBindUri + "/" + bottle.number+"?locationDevNumber="+locationDevNumber));
    };

    //钢瓶规格查询
    this.retrieveBottleSpecs = function () {
        return promise.wrap($http.get(bottleSpecQueryUri));
    };

    //查询部门信息(下级递归)
    this.retrieveDepartmentLower = function (code) {
        return promise.wrap($http.get(departmentUri + "/Lower"+"?code=" +code));
    };

    //查询钢瓶轨迹
    this.retrieveGasCylinderPosition = function (params) {
        return promise.wrap($http.get(gasCylinderPositionUri,{params: params}));
    };

    //查询钢瓶交接记录
    this.retrieveGasCylinderTakeOverHistory = function (number, params) {
        return promise.wrap($http.get(gasCylinderTakeOverHistoryUri+"/"+number,{params: params}));
    };

    //查询钢瓶告警信息
    this.retrieveGasCylinderWarn = function (params) {
        return promise.wrap($http.get(gasCylinderWarnUri,{params: params}));
    };

    //处理钢瓶告警信息
    this.modifyGasCylinderWarn = function (warn) {
        return promise.wrap($http.put(gasCylinderWarnUri + "/" + warn.id, warn));
    };

    //查询钢瓶灌装数据
    this.retrieveGasCylindeFilling = function (params) {
        return promise.wrap($http.get(gasCylinderFillingUri,{params: params}));
    };


}]);

