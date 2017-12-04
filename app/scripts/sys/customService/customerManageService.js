'use strict';

customServiceApp.service('CustomerManageService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var customersUri = URI.resources.customers;
    var customerSourceUri = URI.resources.customerSource;
    var customerLevelUri = URI.resources.customerLevel;
    var customerTypeUri = URI.resources.customerType;





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

    this.retrieveDepartment = function () {
        return promise.wrap($http.get(departmentUri));
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


}]);

