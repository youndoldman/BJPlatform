'use strict';

customServiceApp.service('OrderService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {
    var ordersUri = URI.resources.orders;
    var taskOrdersUri = URI.resources.taskOrders;
    var usersUri = URI.resources.users;

    this.toViewModelTaskOrders = function (taskOrdersFromApi) {
        if(taskOrdersFromApi.object==null)
        {
            return {
                id: null,
                orderSn: taskOrdersFromApi.process.buinessKey,
                callInPhone: null,
                payType: null,
                accessType: null,
                customer: null,
                orderAmount: null,
                orderStatus: null,
                urgent: null,
                reserveTime: null,
                recvAddr: null,
                recvLongitude: null,
                recvLatitude: null,
                recvName: null,
                recvPhone: null,
                comment: null,
                orderDetailList: null,
                note: null,
                createTime: null,
                updateTime: null,
                taskId: taskOrdersFromApi.id
            }
        }
        return {
            id: taskOrdersFromApi.object.id,
            orderSn: taskOrdersFromApi.object.orderSn,
            callInPhone: taskOrdersFromApi.object.callInPhone,
            payType: taskOrdersFromApi.object.payType,
            accessType: taskOrdersFromApi.object.accessType,
            customer: taskOrdersFromApi.object.customer,
            orderAmount: taskOrdersFromApi.object.orderAmount,
            orderStatus: taskOrdersFromApi.object.orderStatus,
            urgent: taskOrdersFromApi.object.urgent,
            reserveTime: taskOrdersFromApi.object.reserveTime,
            recvAddr: taskOrdersFromApi.object.recvAddr,
            recvLongitude: taskOrdersFromApi.object.recvLongitude,
            recvLatitude: taskOrdersFromApi.object.recvLatitude,
            recvName: taskOrdersFromApi.object.recvName,
            recvPhone: taskOrdersFromApi.object.recvPhone,
            comment: taskOrdersFromApi.object.comment,
            orderDetailList: taskOrdersFromApi.object.orderDetailList,
            note: taskOrdersFromApi.object.note,
            createTime: taskOrdersFromApi.object.createTime,
            updateTime: taskOrdersFromApi.object.updateTime,
            taskId: taskOrdersFromApi.id
        }
    };




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


    //查询需要进行处理的任务订单
    this.retrieveTaskOrders = function (userId) {
        return promise.wrap($http.get(taskOrdersUri+'/'+userId));
    };

    //查询在线的配送工
    this.retrieveDistributionworkers = function (params) {
        return promise.wrap($http.get(usersUri, {params: params}));

    }
}]);

