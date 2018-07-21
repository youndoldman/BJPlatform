'use strict';

comprehensiveSituationApp.service('GisWatchService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var bottlesUri = URI.resources.bottle;//钢瓶接口
    var usersUri = URI.resources.users;

    //查询员工
    this.retrieveUsers = function (params) {
        return promise.wrap($http.get(usersUri, {params: params}));
    };
    //查询钢瓶
    this.retrieveBottles = function (params) {
        return promise.wrap($http.get(bottlesUri, {params: params}));
    };

}]);

