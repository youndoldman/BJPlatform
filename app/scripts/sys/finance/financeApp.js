'use strict';

var financeApp = angular.module('FinanceApp', ['ui.router', 'CommonModule', 'angularModalService'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {

        // 设置默认显示页面
        $urlRouterProvider.otherwise('/finance/ledger');

        $stateProvider
            .state('finance', {
                url: '/finance',
                abstract: true,
                views: {
                    "": {templateUrl: '../pages/finance/financeLink.htm'}
                }
            })
            .state('finance.ledger',{
                url:'/ledger',
                views: {
                    "content@finance": {
                        templateUrl: '../pages/finance/ledgerList.htm',
                        controller: '',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[0]);
                }
            })
            .state('finance.voucher',{
                url:'/voucher',
                views: {
                    "content@finance": {
                        templateUrl: '../pages/finance/voucherList.htm',
                        controller: 'VoucherListCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[1]);
                }
            })
            .state('finance.wages',{
                url:'/wages',
                views: {
                    "content@finance": {
                        templateUrl: '../pages/finance/wagesList.htm',
                        controller: '',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.FinanceCenter.menuItems[2]);
                }
            });

        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.FinanceCenter);
    }]);
