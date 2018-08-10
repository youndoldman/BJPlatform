'use strict';

comprehensiveSituationApp.service('GisWatchService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var bottlesUri = URI.resources.bottle;//钢瓶接口
    var usersUri = URI.resources.users;
    var gasTrayUri = URI.resources.GasCynTray;
    var customerUri = URI.resources.customers;

    //查询员工
    this.retrieveUsers = function (params) {
        return promise.wrap($http.get(usersUri, {params: params}));
    };
    //查询钢瓶
    this.retrieveBottles = function (params) {
        return promise.wrap($http.get(bottlesUri, {params: params}));
    };

    //查询托盘
    this.retrieveGasTray = function (params) {
        return promise.wrap($http.get(gasTrayUri, {params: params}));
    };

    //查询客户
    this.retrieveCustomers = function (params) {
        return promise.wrap($http.get(customerUri, {params: params}));
    };

}]);

