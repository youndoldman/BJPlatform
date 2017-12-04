'use strict';
var customServiceApp = angular.module('CustomServiceApp', ['ui.router', 'CommonModule', 'angularModalService'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {

        // 设置默认显示页面
        $urlRouterProvider.otherwise('/CustomService/callCenter');

        $stateProvider
            .state('CustomService', {
                url: '/CustomService',
                abstract: true,
                views: {
                    "": {templateUrl: '../pages/CustomService/customServiceLink.htm'}
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[0]);
                }
            })
            .state('CustomService.callCenter',{
                url:'/callCenter',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/callCenter.htm',
                        controller: 'CallCenterCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[0]);
                }
            })
            .state('CustomService.customerManage',{
                url:'/customerManage',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/CustomService/customerList.htm',
                        controller: 'CustomerManageCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[1]);
                }
            });

        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.CustomService);
    }]);
