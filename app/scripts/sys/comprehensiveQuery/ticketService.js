'use strict';

comprehensiveQueryApp.service('TicketService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {

    var ticketUri = URI.resources.ticket;
    var couponUri = URI.resources.coupon;
    var userCardUri = URI.resources.UserCard;
    var gasCynTrayUri = URI.resources.GasCynTray;
    var sysUserFindByUserIdUri = URI.resources.sysUserFindByUserId;
    //请求气票表
    this.retrieveTicket = function (params) {
        return promise.wrap($http.get(ticketUri, {params: params}));
    };

    //请求优惠券
    this.retrieveCoupon = function (params) {
        return promise.wrap($http.get(couponUri, {params: params}));
    };

    //请求用户卡
    this.retrieveUserCard = function (params) {
        return promise.wrap($http.get(userCardUri, {params: params}));
    };


    //请求托盘信息
    this.retrieveGasCynTray = function (params) {
        return promise.wrap($http.get(gasCynTrayUri, {params: params}));
    };

    //修改气票表
    this.modifyTicket = function (ticket) {
        return promise.wrap($http.put(ticketUri + "/" + ticket.ticketSn, ticket));
    };

    //精确查询系统客户
    this.FindSysUserUri = function (params) {
        return promise.wrap($http.get(sysUserFindByUserIdUri, {params: params}));
    };

}]);

