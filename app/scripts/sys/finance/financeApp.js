'use strict';

var financeApp = angular.module('FinanceApp', ['ui.router', 'CommonModule', 'angularModalService'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {

        // 设置默认显示页面
        $urlRouterProvider.otherwise('/finance/storeDailySales');

        $stateProvider
            .state('finance', {
                url: '/finance',
                abstract: true,
                views: {
                    "": {templateUrl: '../pages/finance/financeLink.htm'}
                }
            })
            .state('finance.storeDailySales',{
                url:'/storeDailySales',
                views: {
                    "content@finance": {
                        templateUrl: '../pages/finance/storeDailySales.html',
                        controller: 'storeDailySalesCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[0]);
                }
            })
            .state('finance.checkBottle',{
                url:'/checkBottle',
                views: {
                    "content@finance": {
                        templateUrl: '../pages/finance/checkBottle.html',
                        controller: 'checkBottleCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[1]);
                }
            })
            .state('finance.dailyMonthlySales',{
                url:'/dailyMonthlySales',
                views: {
                    "content@finance": {
                        templateUrl: '../pages/finance/dailyMonthlySales.html',
                        controller: 'dailyMonthlySalesCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[2]);
                }
            })
            .state('finance.LPGSales',{
                url:'/LPGSales',
                views: {
                    "content@finance": {
                        templateUrl: '../pages/finance/LPGSales.html',
                        controller: 'LPGSalesCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[3]);
                }
            })
            .state('finance.LPGSalesBalance',{
                url:'/LPGSalesBalance',
                views: {
                    "content@finance": {
                        templateUrl: '../pages/finance/LPGSalesBalance.html',
                        controller: 'LPGSalesBalanceCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[4]);
                }
            })
            .state('finance.LPGSalesCash',{
                url:'/LPGSalesCash',
                views: {
                    "content@finance": {
                        templateUrl: '../pages/finance/LPGSalesCash.html',
                        controller: 'LPGSalesCashCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[5]);
                }
            })
            .state('finance.depositOperation',{
                url:'/depositOperation',
                views: {
                    "content@finance": {
                        templateUrl: '../pages/finance/depositOperation.html',
                        controller: 'DepositOperationCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[6]);
                }
            })
            .state('finance.writeOff',{
                url:'/writeOff',
                views: {
                    "content@finance": {
                        templateUrl: '../pages/finance/writeOff.html',
                        controller: 'WriteOffCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[7]);
                }
            })
            //.state('finance.ledger',{
            //    url:'/ledger',
            //    views: {
            //        "content@finance": {
            //            templateUrl: '../pages/finance/ledgerList.htm',
            //            controller: '',
            //            resolve: {}
            //        }
            //    },
            //    onEnter: function (rootService, NavItem) {
            //        rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[0]);
            //    }
            //})
            //.state('finance.voucher',{
            //    url:'/voucher',
            //    views: {
            //        "content@finance": {
            //            templateUrl: '../pages/finance/voucherList.htm',
            //            controller: 'VoucherListCtrl',
            //            resolve: {}
            //        }
            //    },
            //    onEnter: function (rootService, NavItem) {
            //        rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[1]);
            //    }
            //})
            //.state('finance.wages',{
            //    url:'/wages',
            //    views: {
            //        "content@finance": {
            //            templateUrl: '../pages/finance/wagesList.htm',
            //            controller: '',
            //            resolve: {}
            //        }
            //    },
            //    onEnter: function (rootService, NavItem) {
            //        rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[2]);
            //    }
            //})
        ;

        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.FinanceCenter);
    }]);
