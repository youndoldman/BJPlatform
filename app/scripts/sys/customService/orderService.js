'use strict';

customServiceApp.service('OrderService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {

    var ordersUri = URI.resources.orders;


    //提交订气订单
    this.createOrder = function (order){
        return promise.wrap($http.post(ordersUri, order));
    };


    //查询所有订单
    this.retrieveOrders = function (params) {
        return promise.wrap($http.get(ordersUri, {params: params}));
    };

    //修改订单
    this.modifyOrder = function (order) {
        return promise.wrap($http.put(ordersUri + "/" + order.orderSn, order));
    };


}]);

