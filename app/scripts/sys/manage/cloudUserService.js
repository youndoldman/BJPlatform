'use strict';

manageApp.service('CloudUserService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var CloudUserUri = URI.resources.cloudUser;
    var cloudUserBindUri = URI.resources.cloudUserBind;
    var cloudUserUnBindUri = URI.resources.cloudUserUnBind;



    this.retrieveCloudUsers = function (params) {
        return promise.wrap($http.get(CloudUserUri, {params: params}));
    };

    this.createCloudUser = function (cloudUser) {
        return promise.wrap($http.post(CloudUserUri, cloudUser));
    };

    this.modifyCloudUser = function (cloudUser) {
        return promise.wrap($http.put(CloudUserUri + "?userId=" + cloudUser.userId, cloudUser));
    };

    this.deleteCloudUser = function (cloudUser) {
        return promise.wrap($http.delete(CloudUserUri + "?userId=" + cloudUser.userId));
    };

    //绑定用户
    this.bindUser = function (cloudUser, panvaUserId) {
        return promise.wrap($http.put(cloudUserBindUri + "?userId=" + cloudUser.userId+"?panvaUserId="+panvaUserId));
    };
    //解除绑定
    this.unBindUser = function (cloudUser, panvaUserId) {
        return promise.wrap($http.put(cloudUserUnBindUri + "?userId=" + cloudUser.userId+"?panvaUserId="+panvaUserId));
    };


}]);

