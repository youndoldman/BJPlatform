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
    var gasCylinderSpecUri = URI.resources.gasCylinderSpec;//钢瓶规格
    var settlementTypeUri = URI.resources.settlementType;

    var goodsUri = URI.resources.goods;

    var ticketUri = URI.resources.ticket;
    var couponUri = URI.resources.coupon;

    var customerCreditUri = URI.resources.customerCredit;//查询欠款

    var gasCynTrayBindUri = URI.resources.GasCynTrayBind;//托盘绑定
    var gasCynTrayUri = URI.resources.GasCynTray;//托盘查询
    var gasCynTrayUnBindUri = URI.resources.GasCynTrayunBind;//托盘解绑定

    var userCardBindUri = URI.resources.UserCardBind;//用户卡绑定
    var userCardUri = URI.resources.UserCard;//用户卡查询
    var userCardUnBindUri = URI.resources.UserCardunBind;//用户卡解绑定


    var DevelopUserUri = URI.resources.DevelopUser;//客户推荐人接口


    var sysUserUri = URI.resources.users;
    var sysUserFindByUserIdUri = URI.resources.sysUserFindByUserId;

    var adviceUri = URI.resources.Advice;//客户意见

    var gasCynTrayWarningStatusDeleteUri = URI.resources.WarningStatusDelete;//托盘告警解除

    //托盘告警解除
    this.gasCynTrayWarningStatusDelete = function (params) {
        return promise.wrap($http.get(gasCynTrayWarningStatusDeleteUri, {params: params}));
    };

    this.retrieveGoods = function (params) {
        return promise.wrap($http.get(goodsUri, {params: params}));
    };

    //查询钢瓶规格
    this.retrieveGasCylinderSpecUri = function (params) {
        return promise.wrap($http.get(gasCylinderSpecUri, {params: params}));
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
    this.addTicketInfo = function (ticketQuantity,ticketInfo) {
        return promise.wrap($http.post(ticketUri+"?ticketCount="+ticketQuantity, ticketInfo));
    };
    //查询气票
    this.searchTicket = function(params){
        return promise.wrap($http.get(ticketUri, {params: params}));
    };


    //删除气票
    this.deleteTicket = function (ticketDetail) {
        return promise.wrap($http.delete(ticketUri + "/" + ticketDetail.ticketSn));
    };

    //增加优惠券
    this.addCoupon = function(couponQuantity, couponInfo){
        return promise.wrap($http.post(couponUri+"?couponCount="+couponQuantity, couponInfo));
    };

    //查询优惠券
    this.searchCoupon = function(params){
        return promise.wrap($http.get(couponUri, {params: params}));
    }

    //删除优惠券
    this.deleteCoupon = function (couponDetail) {
        return promise.wrap($http.delete(couponUri + "/" + couponDetail.id));
    }

    //查询客户欠款
    this.retrieveCustomerCredit = function (params) {
        return promise.wrap($http.get(customerCreditUri, {params: params}));
    };


    //绑定托盘至客户
    this.bindPallet = function (customerId, palletNumber) {
        return promise.wrap($http.put(gasCynTrayBindUri + "/" + palletNumber+"?userId="+customerId));
    };
    //解除绑定托盘
    this.unBindPallet = function (customerId, palletNumber) {
        return promise.wrap($http.put(gasCynTrayUnBindUri + "/" + palletNumber+"?userId="+customerId));
    };
    //托盘查询
    this.retrievePallets = function (params) {
        return promise.wrap($http.get(gasCynTrayUri, {params: params}));
    };

    //绑定用户卡至客户
    this.bindUserCard = function (customerId, userCardNumber) {
        return promise.wrap($http.put(userCardBindUri + "/" + userCardNumber+"?userId="+customerId));
    };
    //解除绑定用户卡
    this.unBindUserCard = function (customerId, userCardNumber) {
        return promise.wrap($http.put(userCardUnBindUri + "/" + userCardNumber+"?userId="+customerId));
    };
    //用户卡查询
    this.retrieveUserCards = function (params) {
        return promise.wrap($http.get(userCardUri, {params: params}));
    };

    //查询系统客户
    this.retrieveSysUserUri = function (params) {
        return promise.wrap($http.get(sysUserUri, {params: params}));
    };


    //精确查询系统客户
    this.FindSysUserUri = function (params) {
        return promise.wrap($http.get(sysUserFindByUserIdUri, {params: params}));
    };


    //查询客户意见
    this.retrieveAdvice = function (params) {
        return promise.wrap($http.get(adviceUri, {params: params}));
    };

   //创建客户意见
    this.createAdvice = function (advice) {
        return promise.wrap($http.post(adviceUri, advice));
    };

    //删除客户意见
    this.deleteAdvice = function (advice) {
        return promise.wrap($http.delete(adviceUri + "/" + advice.id));
    };


    //绑定推荐人
    this.bindDevelopUser = function (customerId, refereeId) {
        return promise.wrap($http.post(DevelopUserUri +"?customerId=" + customerId+"&refereeId="+refereeId));
    };
    //解除推荐人
    this.unBindDevelopUser = function (customerId, refereeId) {
        return promise.wrap($http.delete(DevelopUserUri +"?customerId=" + customerId+"&refereeId="+refereeId));
    };

    //推荐人查询
    this.retrieveDevelopUser = function (params) {
        return promise.wrap($http.get(DevelopUserUri, {params: params}));
    };


}]);

