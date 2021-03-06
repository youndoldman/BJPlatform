'use strict';

var commonModule = angular.module('CommonModule', []);


commonModule.service('rootService', [function () {

    var self = this;
    self.activeNav = {};

    self.updateActiveNavLv1 = function (navHeaderItem) {
        self.activeNav.lv1 = navHeaderItem;
    };

    self.updateActiveNavL2 = function (menuItem) {
        self.activeNav.lv2 = menuItem;
    };

    self.updateActiveNavL3 = function (subMenuItem) {
        self.activeNav.lv3 = subMenuItem;
    };

    self.getActiveNav = function () {
        return self.activeNav;
    };

}]);

commonModule.service('sessionStorage', ['$window', function ($window) {

    var KEY = "session_createModel",
        self = this;

    var getDataFromSessionStorage = function () {
        return angular.fromJson($window.sessionStorage.getItem(KEY)) || {};
    };

    var saveDataFromSessionStorage = function (data) {
        $window.sessionStorage.setItem(KEY, angular.toJson(data));
    };

    self.put = function (id, text) {
        var data = getDataFromSessionStorage();
        data[id] = text;
        saveDataFromSessionStorage(data);
    };

    self.get = function (id) {
        var data = getDataFromSessionStorage();
        if (data[id]) {
            return data[id];
        } else {
            return null;
        }
    };

    self.remove = function (id) {
        var data = getDataFromSessionStorage();
        if (data.hasOwnProperty(id)) {
            delete  data[id];
            saveDataFromSessionStorage(data);
        }
    };

    self.getKTYCurUser = function () {
        return angular.fromJson($window.sessionStorage.getItem("KTYcurUser"));
    }
    self.setKTYCurUser = function (data) {
        $window.sessionStorage.setItem("KTYcurUser", angular.toJson(data));
    }

    self.getCurUser = function () {
        return angular.fromJson($window.sessionStorage.getItem("curUser"));
    }
    self.setCurUser = function (data) {
        $window.sessionStorage.setItem("curUser", angular.toJson(data));
    }
    self.clearCurUser = function () {
        $window.sessionStorage.setItem("curUser", null);
    }
}]);

commonModule.service('localStorage', ['$window', function ($window) {

    //todo replace with log in user id as local storage key
    var KEY = "loginUserId" + "_createModel",
        self = this;

    var getDataFromLocalStorage = function () {
        return angular.fromJson($window.localStorage.getItem(KEY)) || {};
    };

    var saveDataFromLocalStorage = function (data) {
        $window.localStorage.setItem(KEY, angular.toJson(data));
    };

    self.put = function (id, text) {
        var data = getDataFromLocalStorage();
        data[id] = text;
        saveDataFromLocalStorage(data);
    };

    self.get = function (id) {
        var data = getDataFromLocalStorage();
        if (data[id]) {
            return data[id];
        } else {
            return null;
        }
    };

    self.remove = function (id) {
        var data = getDataFromLocalStorage();
        if (data.hasOwnProperty(id)) {
            delete  data[id];
            saveDataFromLocalStorage(data);
        }
    };
}]);

commonModule.service('promiseWrapper', ['$q', '$rootScope', function ($q, $rootScope) {
    var self = this;

    self.wrap = function (request) {
        var defer = $q.defer();
        request.success(function (data) {
            defer.resolve(data)
        }).error(function (data) {
            defer.reject(data)
        });
        return defer.promise;
    };

    self.mockWrap = function (data) {
        var defer = $q.defer();
        defer.resolve(data);
        return defer.promise;
    };

    self.wrapWithRootScopeLoading = function (request) {
        var defer = $q.defer();
        $rootScope.rootScopeLoading = true;
        request.success(function (data) {
            $rootScope.rootScopeLoading = false;
            defer.resolve(data)
        }).error(function (data) {
            $rootScope.rootScopeLoading = false;
            defer.reject(data)
        });
        return defer.promise;
    }

}]);


commonModule.controller('CommonModuleCtrl', ['$rootScope','$scope', '$interval','$timeout','sessionStorage', 'URI', 'promiseWrapper','$window','$http','rootService',function ($rootScope,$scope,$interval,$timeout,sessionStorage,URI,promise,$window,$http,rootService) {
    $scope.currentTime = "";
    $scope.currentUser = {};
    $scope.currentUserPhoto = "";



    function getDate(){
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        var hh = date.getHours();
        var mm = date.getMinutes();
        var ss = date.getSeconds();
        $scope.currentTime = year + "-" + month + "-" + day + " " + hh + ":" + mm + ":" + ss;
    }
    var init = function () {
        $scope.currentUser = sessionStorage.getCurUser();

        if($scope.currentUser == null){
            $window.location.href = URI.resources.loginPage;
        }else{
            $scope.currentUserPhoto = URI.resources.SysUserPhoto+"/"+$scope.currentUser.userId;
            console.log($scope.currentUserPhoto);
            $scope.timer = $interval( function(){
                updateHeartBit();//心跳
            }, 20000);
        }


    };
    $scope.inRoles = function(roles)
    {
        var curUser = sessionStorage.getCurUser();
        if(curUser==null){
            return false;;
        }
        var i = roles.length;
        if(i == 0)
        {
            return true;
        }
        while (i--) {
            if (roles[i] == curUser.userGroup.id) {
                return true;
            }
        }
        return false;
    };
    $scope.logout = function ()
    {
        var curUser = sessionStorage.getCurUser();
        var logoutUri = URI.resources.logout;
        promise.wrap($http.get(logoutUri+"/"+curUser.userId)).then(function(value){
            sessionStorage.clearCurUser();
            $window.location.href = URI.resources.loginPage;
        }, function(value) {
            alert("连接超时");
        });
    };
    var sysUserKeepAliveUri = URI.resources.sysUserKeepAlive;
    var updateHeartBit = function () {
        var user = sessionStorage.getCurUser();
        promise.wrap($http.get(sysUserKeepAliveUri+"/"+user.userId));

    };

    //第一个菜单跳转
    $scope.swtchFirstHref = function (){
        var activeNav = rootService.getActiveNav();
        for(var i = 0; i< activeNav.lv1.menuItems.length; i++)
        {
            var item = activeNav.lv1.menuItems[i];
            if($scope.inRoles(item.roles)){
                $window.location.href = item.href;
                break;
            }
        }

    };

    init();
}]);