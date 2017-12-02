'use strict';

loginApp.controller('LoginCtrl', ['$scope', '$rootScope', '$http', 'URI', 'promiseWrapper','$window','sessionStorage', function ($scope, $rootScope, $http, URI, promise, $window, sessionStorage) {
    var loginUri = URI.resources.login;
    $scope.userInfo = {
        id: "",
        password:""
    };

    $scope.login = function () {
        var loginParams = {
            id: $scope.userInfo.id,
            password: $scope.userInfo.password
        };
        if(loginParams.id==""||loginParams.password=="")
        {
            return;
        }

        promise.wrap($http.get(loginUri, {params: loginParams})).then(function(value){
            $window.location.href = URI.resources.mainPage;
            sessionStorage.setCurUser(value);
        }, function(value) {
            // failure
            alert("用户名密码错误");
        });
    };

    $scope.myKeyup = function(e){
        var keycode = window.event?e.keyCode:e.which;
        if(keycode==13){
            $scope.login();
        }
    };

    }]);
