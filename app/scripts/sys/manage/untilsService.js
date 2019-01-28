'use strict';

manageApp.service('UntilsService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var sendMsUri = URI.resources.sendMs;



    //短信推送
    this.SendBatchSms = function (params) {
        return promise.wrap($http.get(sendMsUri, {params: params}));
    };




}]);

