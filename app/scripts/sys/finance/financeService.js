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

    var stockUri = URI.resources.stock;//查询库存
    var stockInOutUri = URI.resources.stockInOut;//查询出入库数量
    var salesCashUri = URI.resources.salesCash;//销售现金查询

    var depositDetailUri = URI.resources.depositDetail;//增加存银行款信息

    var writeOffUri = URI.resources.writeOff;//回款
    var writeOffDetailUri = URI.resources.writeOffDetail;//回款明细

    var gasCylinderSpecUri = URI.resources.gasCylinderSpec;//钢瓶规格

    var reportGasCyrDynUri = URI.resources.reportGasCyrDyn;//钢检瓶报表查询

    var gasCyrChargeSpecUri = URI.resources.gasCyrChargeSpec;//钢瓶收费标准查询

    //增加回款
    this.addWriteOff = function (params) {
        return promise.wrap($http.post(writeOffUri, params));
    };
    //回款明细查询
    this.searchWriteOffDetail = function (params) {
        return promise.wrap($http.get(writeOffDetailUri, {params: params}));
    };

    //查询客户类型
    this.searchCustomerType = function (params) {
        return promise.wrap($http.get(customerTypeUri, {params: params}));
    };
    var goodsTypesUri = URI.resources.goodsTypes;//商品规格
    //查询商品
    this.retrieveGoodsTypes = function (params) {
        return promise.wrap($http.get(goodsTypesUri, {params: params}));
    };
    var goodsUri = URI.resources.goods;
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

    //查询当期库存
    this.searchStock =  function (params) {
        return promise.wrap($http.get(stockUri, {params: params}));
    };

    //查询出入库数量
    this.searchStockInOut =  function (params) {
        return promise.wrap($http.get(stockInOutUri, {params: params}));
    };

    //查询现金销售
    this.searchSalesCash = function (params) {
        return promise.wrap($http.get(salesCashUri, {params: params}));
    }

    //增加存银行款信息
    this.addDepositDetail = function (params) {
        return promise.wrap($http.post(depositDetailUri, params));
    };

    //查询存银行款信息
    this.searchDepositDetail =  function (params) {
        return promise.wrap($http.get(depositDetailUri, {params: params}));
    }


    //查询钢瓶规格
    this.retrieveGasCylinderSpecUri = function (params) {
        return promise.wrap($http.get(gasCylinderSpecUri, {params: params}));
    };

    //钢检瓶报表查询
    this.retrieveReportGasCyrDyn = function (params) {
        return promise.wrap($http.get(reportGasCyrDynUri, {params: params}));
    };

    //钢瓶收费标准查询
    this.searchGasCyrChargeSpec  = function (params) {
        return promise.wrap($http.get(gasCyrChargeSpecUri, {params: params}));
    };




}]);

