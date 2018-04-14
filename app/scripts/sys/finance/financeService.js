/**
 * Created by Administrator on 2018/4/13.
 */
'use strict';

financeApp.service('FinanceService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var departmentUri = URI.resources.department;//部门查询接口
    var goodsTypesUri = URI.resources.goodsTypes;//商品规格
    var goodsUri = URI.resources.goods;
    var salesByBayTypeUri = URI.resources.salesByBayType;//查询销售日报表(按支付类型查询)
    var saleContactsUri = URI.resources.saleContacts;//销售往来日报表查询
    var salesByCustomerTypeUri = URI.resources.salesByCustomerType;//查询销售日报表(按客户类型查询)
    var customerTypeUri = URI.resources.customerType;//客户类型

    //查询客户类型
    this.searchCustomerType = function (params) {
        return promise.wrap($http.get(customerTypeUri, {params: params}));
    };
    //查询商品
    this.retrieveGoodsTypes = function (params) {
        return promise.wrap($http.get(goodsTypesUri, {params: params}));
    };
    //查询商品规格
    this.retrieveGoods = function (params) {
        return promise.wrap($http.get(goodsUri, {params: params}));
    };
    //查询部门信息(下级递归)
    this.retrieveDepartmentLower = function (code) {
        return promise.wrap($http.get(departmentUri + "/Lower"+"?code=" +code));
    };

    //查询销售日报表(按支付类型查询)
    this.searchSalesByBayType = function (params) {
        return promise.wrap($http.get(salesByBayTypeUri, {params: params}));
    };

    //销售往来日报表查询
    this.searchSaleContacts = function (params) {
        return promise.wrap($http.get(saleContactsUri, {params: params}));
    };

    //查询销售日报表(按客户类型查询)
    this.searchSalesByCustomerType = function (params) {
        return promise.wrap($http.get(salesByCustomerTypeUri, {params: params}));
    };
}]);

