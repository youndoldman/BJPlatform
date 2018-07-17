'use strict';

comprehensiveSituationApp.service('GisWatchService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {


    var usersUri = URI.resources.users;

    //查询员工
    this.retrieveUsers = function (params) {
        return promise.wrap($http.get(usersUri, {params: params}));
    };

}]);

