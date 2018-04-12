'use strict';

shopManageApp.service('MendSecurityComplaintService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {

    var mendUri = URI.resources.mend;
    var mendTypeUri = URI.resources.mendType;
    var securityUri = URI.resources.security;
    var securityTypeUri = URI.resources.securityType;
    var complaintUri = URI.resources.complaint;
    var complaintTypeUri = URI.resources.complaintType;

    var departmentUri = URI.resources.department;



    //请求报修的类型
    this.retrieveMendTypes = function (params) {
        return promise.wrap($http.get(mendTypeUri, {params: params}));
    };

    //请求报修订单
    this.retrieveMend = function (params) {
        return promise.wrap($http.get(mendUri, {params: params}));
    };

    //创建报修订单
    this.createMend = function (mend) {
        return promise.wrap($http.post(mendUri, mend));
    };

    //修改报修订单
    this.modifyMend = function (mend) {
        return promise.wrap($http.put(mendUri + "/" + mend.mendSn, mend));
    };

    //查询部门信息(下级递归)
    this.retrieveDepartmentLower = function (code) {
        return promise.wrap($http.get(departmentUri + "/Lower"+"?code=" +code));
    };

    //查询部门信息(上级递归)
    this.retrieveDepartmentUpper = function (code) {
        return promise.wrap($http.get(departmentUri + "/Upper"+"?code=" +code));
    };




    //请求安检的类型
    this.retrieveSecurityTypes = function (params) {
        return promise.wrap($http.get(securityTypeUri, {params: params}));
    };

    //请求安检订单
    this.retrieveSecurity = function (params) {
        return promise.wrap($http.get(securityUri, {params: params}));
    };

    //创建安检订单
    this.createSecurity = function (security) {
        return promise.wrap($http.post(securityUri, security));
    };

    //修改安检订单
    this.modifySecurity = function (security) {
        return promise.wrap($http.put(securityUri + "/" + security.securitySn, security));
    };


    //请求投诉的类型
    this.retrieveComplaintTypes = function (params) {
        return promise.wrap($http.get(complaintTypeUri, {params: params}));
    };

    //请求投诉订单
    this.retrieveComplaint = function (params) {
        return promise.wrap($http.get(complaintUri, {params: params}));
    };

    //创建投诉订单
    this.createComplaint = function (complaint) {
        return promise.wrap($http.post(complaintUri, complaint));
    };

    //修改投诉订单
    this.modifyComplaint = function (complaint) {
        return promise.wrap($http.put(complaintUri + "/" + complaint.complaintSn, complaint));
    };


}]);

