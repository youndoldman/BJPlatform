'use strict';

manageApp.service('UserService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var usersUri = URI.resources.users;

    this.toViewModel = function (userFromApi) {
        return {
            id: userFromApi.id,
            name: userFromApi.name,
            password: userFromApi.password,
            role: userFromApi.role,
            createTime: userFromApi.createTime,
            updateTime: userFromApi.updateTime
        }
    };

    this.retrieveUsers = function (params) {
        return promise.wrap($http.get(usersUri, {params: params}));
    };

    this.getUserInfoById = function (userId) {
        return promise.wrap($http.get(usersUri + '/' + userId));
    };

    this.createUser = function (user) {
        return promise.wrap($http.post(usersUri, user));
    };

    this.modifyUser = function (user) {
        return promise.wrap($http.put(usersUri + "/" + user.id, user));
    };

    this.deleteUser = function (user) {
        return promise.wrap($http.delete(usersUri + "/" + user.id));
    };

}]);

