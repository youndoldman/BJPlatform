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
                    "": {templateUrl: '../pages/customService/customServiceLink.htm'}
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[0]);
                }
            })
            .state('CustomService.callCenter',{
                url:'/callCenter',
                views: {
                    "head@CustomService": {
                        templateUrl: '../pages/customService/webphone.htm',
                        controller: 'WebPhoneCtrl',
                        resolve: {}
                    },
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
            .state('CustomService.orderDelivery',{
                url:'/orderDelivery',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/orderDeliveryList.htm',
                        controller: 'OrderDeliveryCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[1]);
                }
            })
            .state('CustomService.orderManage',{
                url:'/orderManage',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/orderList.htm',
                        controller: 'OrderCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[2]);
                }
            })
            .state('CustomService.customerManage',{
                url:'/customerManage',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/customerList.htm',
                        controller: 'CustomerManageCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[3]);
                }
            }).state('CustomService.report1',{
                url:'/report1',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/report1.htm',
                        controller: 'Report1Ctrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[4]);
                }
            })
            .state('CustomService.report2',{
                url:'/report2',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/report2.html',
                        controller: 'Report2Ctrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[5]);
                }
            })
            .state('CustomService.report3',{
            url:'/report3',
            views: {
                "content@CustomService": {
                    templateUrl: '../pages/customService/report3.html',
                    controller: 'Report3Ctrl',
                    resolve: {}
                }
            },
            onEnter: function (rootService, NavItem) {
                rootService.updateActiveNavL2(NavItem.CustomService.menuItems[6]);
            }
        })
            .state('CustomService.report4',{
                url:'/report4',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/report4.html',
                        controller: 'Report4Ctrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[7]);
                }
            })
            .state('CustomService.report5',{
                url:'/report5',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/report5.html',
                        controller: 'Report5Ctrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[8]);
                }
            })
            .state('CustomService.report6',{
                url:'/report6',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/report6.html',
                        controller: 'Report6Ctrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[9]);
                }
            })
            .state('CustomService.storeDailySales',{
                url:'/storeDailySales',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/storeDailySales.html',
                        controller: 'storeDailySalesCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[10]);
                }
            })
            .state('CustomService.checkBottle',{
                url:'/checkBottle',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/checkBottle.html',
                        controller: 'checkBottleCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[11]);
                }
            })
            .state('CustomService.dailyMonthlySales',{
                url:'/dailyMonthlySales',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/dailyMonthlySales.html',
                        controller: 'dailyMonthlySalesCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[12]);
                }
            })
            .state('CustomService.LPGSales',{
                url:'/LPGSales',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/LPGSales.html',
                        controller: 'LPGSalesCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[13]);
                }
            })
            .state('CustomService.LPGSalesBalance',{
                url:'/LPGSalesBalance',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/LPGSalesBalance.html',
                        controller: 'LPGSalesBalanceCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[14]);
                }
            })
            .state('CustomService.LPGSalesCash',{
                url:'/LPGSalesCash',
                views: {
                    "content@CustomService": {
                        templateUrl: '../pages/customService/LPGSalesCash.html',
                        controller: 'LPGSalesCashCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.CustomService.menuItems[15]);
                }
            })
        ;

        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.CustomService);
    }]);
