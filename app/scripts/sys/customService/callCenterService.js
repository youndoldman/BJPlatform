'use strict';

customServiceApp.service('CallCenterService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {

    var ordersUri = URI.resources.orders;


    //提交订气订单
    this.createOrder = function (order){
        return promise.wrap($http.post(ordersUri, order));
    };



}]);

