'use strict';
var phoneApp = angular.module('PhoneApp', ['ui.router', 'CommonModule', 'angularModalService'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {

        // 设置默认显示页面
        $urlRouterProvider.otherwise('/phone');

        $stateProvider
            .state('phone', {
                url: '/phone',
                views: {
                    "": {templateUrl: '../pages/phone/phoneMain.htm'}
                }
            });
        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.PhoneCenter);
    }]);
