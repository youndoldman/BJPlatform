'use strict';

customServiceApp.service('MendSecurityComplaintService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {

    var mendUri = URI.resources.mend;
    var mendTypeUri = URI.resources.mendType;
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


}]);

