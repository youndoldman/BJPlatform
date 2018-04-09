'use strict';

customServiceApp.service('CustomerManageService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {

    var customersUri = URI.resources.customers;
    var customerSourceUri = URI.resources.customerSource;
    var customerLevelUri = URI.resources.customerLevel;
    var customerTypeUri = URI.resources.customerType;
    var goodsTypesUri = URI.resources.goodsTypes;
    var goodsUri = URI.resources.goods;
    var subdistrictUri = URI.resources.subdistrict;
    var geocodeUri = URI.resources.geocode;
    var customerCallinUri = URI.resources.customerCallin;
    var key = MISC.keys.gaodeKey;
    var CloudUserUri = URI.resources.cloudUser;

    var settlementTypeUri = URI.resources.settlementType;

    var goodsUri = URI.resources.goods;

    var ticketUri = URI.resources.ticket;


    this.retrieveGoods = function (params) {
        return promise.wrap($http.get(goodsUri, {params: params}));
    };


    //查询云客服账号
    this.retrieveCloudUsers = function (params) {
        return promise.wrap($http.get(CloudUserUri, {params: params}));
    };




    this.toViewModel = function (customerFromApi) {
        return {
            id: customerFromApi.id,
            userId: customerFromApi.userId,
            name: customerFromApi.name,
            password: customerFromApi.password,
            identity: customerFromApi.identity,
            number: customerFromApi.number,
            haveCylinder: customerFromApi.haveCylinder,
            status: customerFromApi.status,
            customerType: customerFromApi.customerType,
            customerLevel: customerFromApi.customerLevel,

            settlementType:customerFromApi.settlementType,

            customerSource: customerFromApi.customerSource,
            customerCompany: customerFromApi.customerCompany,
            address: customerFromApi.address,
            phone: customerFromApi.phone,
            note: customerFromApi.note,
            createTime: customerFromApi.createTime,
            updateTime: customerFromApi.updateTime
        }
    };

    this.toViewModelCustomSource = function (customerSourceFromApi) {
        return {
            id: customerSourceFromApi.id,
            code: customerSourceFromApi.code,
            name: customerSourceFromApi.name,
        }
    };
    this.toViewModelCustomLevel = function (customerLevelFromApi) {
        return {
            id: customerLevelFromApi.id,
            code: customerLevelFromApi.code,
            name: customerLevelFromApi.name,
        }
    };
    this.toViewModelCustomType = function (customerTypeFromApi) {
        return {
            id: customerTypeFromApi.id,
            code: customerTypeFromApi.code,
            name: customerTypeFromApi.name,
        }
    };

    this.retrieveCustomers = function (params) {
        return promise.wrap($http.get(customersUri, {params: params}));
    };

    this.getCustomerInfoById = function (customerId) {
        return promise.wrap($http.get(customersUri + '/' + customerId));
    };

    this.createCustomer = function (customer) {
        return promise.wrap($http.post(customersUri, customer));
    };

    this.modifyCustomer = function (customer) {
        return promise.wrap($http.put(customersUri + "/" + customer.userId, customer));
    };

    this.deleteCustomer = function (customer) {
        return promise.wrap($http.delete(customersUri + "/" + customer.userId));
    };



    this.retrieveGroups = function () {
        return promise.wrap($http.get(groupsUri));
    };

    this.retrieveCustomerSource = function (params) {
        return promise.wrap($http.get(customerSourceUri, {params: params}));
    };

    this.retrieveCustomerLevel = function (params) {
        return promise.wrap($http.get(customerLevelUri, {params: params}));
    };

    this.retrieveCustomerType = function (params) {
        return promise.wrap($http.get(customerTypeUri, {params: params}));
    };

    //结算类型信息查询
    this.retrieveSettlementType = function (params) {
        return promise.wrap($http.get(settlementTypeUri, {params: params}));
    };

    //行政区域查询
    this.retrieveSubdistrict = function (keyWords) {
        return promise.wrap($http.get(subdistrictUri+"?key="+key+"&keywords="+keyWords));
    };

    //地理编码经纬度查询
    this.retrieveLocation = function (address) {
        return promise.wrap($http.get(geocodeUri+"?key="+key+"&address="+address));
    };

    //请求呼叫记录关联的用户
    this.retrieveCustomerCallin = function (params) {
        return promise.wrap($http.get(customerCallinUri,{params: params}));
    };

    //请求商品的类型
    this.retrieveGoodsTypes = function (params) {
        return promise.wrap($http.get(goodsTypesUri, {params: params}));
    };

    //请求商品
    this.retrieveGoods = function (params) {
        return promise.wrap($http.get(goodsUri, {params: params}));
    };

    //增加气票信息
    this.addTicketInfo = function (ticketInfo) {
        return promise.wrap($http.post(ticketUri, ticketInfo));
    };

}]);

