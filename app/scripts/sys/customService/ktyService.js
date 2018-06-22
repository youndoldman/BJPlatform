'use strict';

customServiceApp.service('KtyService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {
    var ktyAuthenticateUri = URI.resources.ktyAuthenticate;
    var ktyReport1Uri = URI.resources.ktyReport1;
    var ktyReport2Uri = URI.resources.ktyReport2;
    var ktyReport3Uri = URI.resources.ktyReport3;
    var ktyReport4Uri = URI.resources.ktyReport4;
    var ktyReport5Uri = URI.resources.ktyReport5;
    var ktyReport6Uri = URI.resources.ktyReport6;
    var ktyCallreportUri = URI.resources.ktyCallreport;

    var ktyCallreportSearchUri = URI.resources.ktyCallreportSearch;

    //云客户认证
    this.authenticate = function (username, password) {
        return promise.wrap($http.post(ktyAuthenticateUri+'?username='+username+'&password='+password));
    };

    //获取坐席-工作组及直拨电话统计报表
    this.retrieveReport1 = function (params,token) {
        return promise.wrap($http.get(ktyReport1Uri,{
            params: params,headers : {'token':token}}));
    }
    //获取坐席-操作状态统计报表
    this.retrieveReport2 = function (params,token) {
        return promise.wrap($http.get(ktyReport2Uri,{
            params: params,headers : {'token':token}}));
    }
    //获取工作组-来话等待时长统计报表
    this.retrieveReport3 = function (params,token) {
        return promise.wrap($http.get(ktyReport3Uri,{
            params: params,headers : {'token':token}}));
    }
    //获取工作组-来话处理时长统计报表
    this.retrieveReport4 = function (params,token) {
        return promise.wrap($http.get(ktyReport4Uri,{
            params: params,headers : {'token':token}}));
    }
    //获取工作组-来话处理时长统计报表
    this.retrieveReport5 = function (params,token) {
        return promise.wrap($http.get(ktyReport5Uri,{
            params: params,headers : {'token':token}}));
    }

    //获取工作组-来话应答/放弃/溢出分析报表
    this.retrieveReport6 = function (params,token) {
        return promise.wrap($http.get(ktyReport6Uri,{
            params: params,headers : {'token':token}}));
    }
    //获得指定时间段呼叫的记录
    this.retrieveCallreport = function (params,token) {
        return promise.wrap($http.get(ktyCallreportUri,{
            params: params,headers : {'token':token}}));
    }

    //获取指定搜素条件的呼叫记录
    this.retrieveCallreportSearch = function (params,token) {
        return promise.wrap($http.get(ktyCallreportSearchUri,{
            params: params,headers : {'token':token}}));
    }
}]);

