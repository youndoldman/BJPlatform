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
            .state('ShopManage.orderList',{
                url:'/orderList',
                views: {
                    "content@ShopManage": {
                        templateUrl: '../pages/shopManage/orderList.htm',
                        controller: 'OrderListCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ShopCenter.menuItems[1]);
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
                    rootService.updateActiveNavL2(NavItem.ShopCenter.menuItems[2]);
                }
            })
            .state('ShopManage.Mend',{
                url:'/Mend',
                views: {
                    "content@ShopManage": {
                        templateUrl: '../pages/shopManage/mendList.htm',
                        controller: 'MendCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ShopCenter.menuItems[3]);
                }
            })
            .state('ShopManage.Security',{
                url:'/Security',
                views: {
                    "content@ShopManage": {
                        templateUrl: '../pages/shopManage/securityList.htm',
                        controller: 'SecurityCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ShopCenter.menuItems[4]);
                }
            })
            .state('ShopManage.Complaint',{
                url:'/Complaint',
                views: {
                    "content@ShopManage": {
                        templateUrl: '../pages/shopManage/complaintList.htm',
                        controller: 'ComplaintCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ShopCenter.menuItems[5]);
                }
            })
            .state('ShopManage.MoneyReturn',{
                url:'/MoneyReturn',
                views: {
                    "content@ShopManage": {
                        templateUrl: '../pages/shopManage/moneyReturn.html',
                        controller: 'MoneyReturnCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ShopCenter.menuItems[6]);
                }
            })
            .state('ShopManage.checkBottle',{
                url:'/checkBottle',
                views: {
                    "content@ShopManage": {
                        templateUrl: '../pages/shopManage/checkBottle.html',
                        controller: 'CheckBottleCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ShopCenter.menuItems[7]);
                }
            })
            .state('ShopManage.calculateDelivery',{
                url:'/calculateDelivery',
                views: {
                    "content@ShopManage": {
                        templateUrl: '../pages/shopManage/calculateDelivery.html',
                        controller: 'CalculateDeliveryCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ShopCenter.menuItems[8]);
                }
            });
        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.ShopCenter);
    }]);
