'use strict';

shopManageApp.service('ShopStockService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {
    var bottlesUri = URI.resources.bottle;//钢瓶接口
    var bottleHandOverUri = URI.resources.bottleHandOver;//钢瓶交接接口
    var usersUri = URI.resources.users;//用户信息查询接口

    var writeOffUri = URI.resources.writeOff;//回款
    var writeOffDetailUri = URI.resources.writeOffDetail;//回款明细

    var departmentUri = URI.resources.department;//部门查询接口

    var usersUri = URI.resources.users;
    var goodsUri = URI.resources.goods;
    var goodsTypesUri = URI.resources.goodsTypes;//商品规格
    //查询商品
    this.retrieveGoodsTypes = function (params) {
        return promise.wrap($http.get(goodsTypesUri, {params: params}));
    };

    //查询商品规格
    this.retrieveGoods = function (params) {
        return promise.wrap($http.get(goodsUri, {params: params}));
    };

    this.retrieveUsers = function (params) {
        return promise.wrap($http.get(usersUri, {params: params}));
    };

    this.toViewModel = function (userFromApi) {
        return {
            id: userFromApi.id,
            userId: userFromApi.userId,
            name: userFromApi.name,
            password: userFromApi.password,
            identity: userFromApi.identity,
            userGroup: userFromApi.userGroup,
            department: userFromApi.department,
            jobNumber: userFromApi.jobNumber,
            mobilePhone: userFromApi.mobilePhone,
            officePhone: userFromApi.officePhone,
            email: userFromApi.email,
            createTime: userFromApi.createTime
        }
    };

    //查询部门信息(下级递归)
    this.retrieveDepartmentLower = function (code) {
        return promise.wrap($http.get(departmentUri + "/Lower"+"?code=" +code));
    };

    this.retrieveBottles = function (params) {
        return promise.wrap($http.get(bottlesUri, {params: params}));
    };

    this.handOverBottle = function (number,srcUserId,targetUserId,serviceStatus, note) {
        return promise.wrap($http.put(bottleHandOverUri+"/"+number+"?srcUserId="+srcUserId
            +"&targetUserId="+targetUserId+"&serviceStatus="+serviceStatus+"&note="+note));
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

    var salesByBayTypeUri = URI.resources.salesByBayType;//查询销售日报表(按支付类型查询)
    //查询销售日报表(按支付类型查询)
    this.searchSalesByBayType = function (params) {
        return promise.wrap($http.get(salesByBayTypeUri, {params: params}));
    };
}]);

