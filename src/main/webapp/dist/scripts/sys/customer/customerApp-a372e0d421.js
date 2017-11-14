'use strict';

var customerApp = angular.module('CustomerApp', ['ui.router', 'CommonModule', 'angularModalService'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {

        // 设置默认显示页面
        $urlRouterProvider.otherwise('/customer/create');

        $stateProvider
            .state('customer', {
                url: '/customer',
                abstract: true,
                views: {
                    "": {templateUrl: '../pages/customer/customerLink.htm'}
                }
            })
            .state('customer.create',{
                url:'/create',
                views: {
                    "content@customer": {
                        templateUrl: '../pages/customer/customerCreate.htm',
                        controller: 'customerCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomerCenter.menuItems[0]);
                }
            })
            .state('customer.query',{
                url:'/query',
                views: {
                    "content@customer": {
                        templateUrl: '../pages/customer/customerQuery.htm',
                        controller: 'customerCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomerCenter.menuItems[1]);
                }
            });
        $httpProvider.interceptors.push('HttpInterceptor');
}]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
    rootService.updateActiveNavLv1(NavItem.CustomerCenter);
}]);
