'use strict';

customServiceApp.service('KtyService', ['$http', 'URI', 'promiseWrapper','MISC', function ($http, URI, promise,MISC) {
    var ktyAuthenticateUri = URI.resources.ktyAuthenticate;
    var ktyReport1Uri = URI.resources.ktyReport1;





    //云客户认证
    this.authenticate = function (username, password) {
        return promise.wrap($http.post(ktyAuthenticateUri+'?username='+username+'&password='+password));
    };



    //获取坐席-工作组及直拨电话统计报表
    this.retrieveReport1 = function (params,token) {
        return promise.wrap($http.get(ktyReport1Uri,{
            params: params,headers : {'token':token}}));

    }
}]);

