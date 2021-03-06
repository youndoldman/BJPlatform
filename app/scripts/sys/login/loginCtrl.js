'use strict';

loginApp.controller('LoginCtrl', ['$scope', '$rootScope', '$http', 'URI', 'promiseWrapper','$window','sessionStorage', function ($scope, $rootScope, $http, URI, promise, $window, sessionStorage) {
    var loginUri = URI.resources.login;
    $scope.userInfo = {
        userId: "",
        password:""
    };


    var getMethod = function (params) {
        return promise.wrap($http.get(loginUri, {params: params}));
    };

    $scope.login = function () {
        var loginParams = {
            userId: $scope.userInfo.userId,
            password: $scope.userInfo.password
        };
        if(loginParams.userId==""||loginParams.password=="")
        {
            return;
        }

        getMethod(loginParams).then(function(value){
            $window.location.href = URI.resources.mainPage;
            sessionStorage.setCurUser(value);
        }, function(value) {
            // failure
            if(value.message != null){
                alert(value.message);
            }

        });
    };

    $scope.myKeyup = function(e){
        var keycode = window.event?e.keyCode:e.which;
        if(keycode==13){
            $scope.login();
        }
    };

    }]);
