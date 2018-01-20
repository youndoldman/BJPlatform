'use strict';
var shopManageApp = angular.module('ShopManageApp', ['ui.router', 'CommonModule', 'angularModalService'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {

        // 设置默认显示页面
        $urlRouterProvider.otherwise('/ShopManage/orderCheck');
        $stateProvider
            .state('ShopManage', {
                url: '/ShopManage',
                abstract: true,
                views: {
                    "": {templateUrl: '../pages/shopManage/shopManageLink.htm'}
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ShopCenter.menuItems[0]);
                }
            })
            .state('ShopManage.orderCheck',{
                url:'/orderCheck',
                views: {
                    "content@ShopManage": {
                        templateUrl: '../pages/shopManage/orderCheckList.htm',
                        controller: 'OrderCheckCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ShopCenter.menuItems[0]);
                }
            })
            .state('ShopManage.stockControl',{
                url:'/stockControl',
                views: {
                    "content@ShopManage": {
                        templateUrl: '../pages/shopManage/shopStock.htm',
                        controller: 'ShopStockCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ShopCenter.menuItems[1]);
                }
            });
        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.ShopCenter);
    }]);
