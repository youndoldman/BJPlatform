'use strict';

customServiceApp.service('TicketService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {

    var ticketUri = URI.resources.ticket;
    var couponUri = URI.resources.coupon;

    //请求气票表
    this.retrieveTicket = function (params) {
        return promise.wrap($http.get(ticketUri, {params: params}));
    };

    //请求优惠券
    this.retrieveCoupon = function (params) {
        return promise.wrap($http.get(couponUri, {params: params}));
    };

}]);

