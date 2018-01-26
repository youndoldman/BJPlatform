'use strict';

commonModule.factory('HttpInterceptor', ["$q", "$rootScope", '$window', 'URI', function ($q, $rootScope, $window, URI) {
        return {
        request:function(config){
            config = config || $q.when(config);
            return config;
        },
        response: function (response) {
            return $q.when(response)
        },
        responseError: function (response) {
            switch (response.status) {
                case 400:
                    //alert("请求参数错误。");
                    break;
                case 401:
                    //$window.location.href = URI.resources.loginPage;
                    break;
                case 403:
                    //alert("对不起，您暂时没有权限访问该数据。");
                    break;
                case 404:
                    //alert("对不起，访问出错了。");
                    break;
                case 500:
                    //alert("对不起，访问出错了。");
                    break;
                default :
                    break;
                    //$rootScope.$broadcast("ResponseError");

            }
            return $q.reject(response);
        }
    };
}]);
