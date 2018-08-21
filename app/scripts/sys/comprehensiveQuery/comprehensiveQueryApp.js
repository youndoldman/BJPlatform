'use strict';
var comprehensiveQueryApp = angular.module('ComprehensiveQueryApp', ['ui.router', 'CommonModule', 'angularModalService'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {

        // 设置默认显示页面
        $urlRouterProvider.otherwise('/ComprehensiveQuery/ticket');

        $stateProvider
            .state('ComprehensiveQuery', {
                url: '/ComprehensiveQuery',
                abstract: true,
                views: {
                    "": {templateUrl: '../pages/comprehensiveQuery/comprehensiveQueryLink.htm'}
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ComprehensiveQueryCenter.menuItems[0]);
                }
            })
            .state('ComprehensiveQuery.ticket',{
                url:'/ticket',
                views: {
                    "content@ComprehensiveQuery": {
                        templateUrl: '../pages/comprehensiveQuery/ticketList.htm',
                        controller: 'TicketCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ComprehensiveQueryCenter.menuItems[0]);
                }
            })
            .state('ComprehensiveQuery.coupon',{
                url:'/coupon',
                views: {
                    "content@ComprehensiveQuery": {
                        templateUrl: '../pages/comprehensiveQuery/couponList.htm',
                        controller: 'CouponCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ComprehensiveQueryCenter.menuItems[1]);
                }
            })
            .state('ComprehensiveQuery.userCard',{
                url:'/userCard',
                views: {
                    "content@ComprehensiveQuery": {
                        templateUrl: '../pages/comprehensiveQuery/userCardList.htm',
                        controller: 'UserCardCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ComprehensiveQueryCenter.menuItems[2]);
                }
            })
            .state('ComprehensiveQuery.gasCynTray',{
                url:'/gasCynTray',
                views: {
                    "content@ComprehensiveQuery": {
                        templateUrl: '../pages/comprehensiveQuery/gasCynTrayList.htm',
                        controller: 'GasCynTrayCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ComprehensiveQueryCenter.menuItems[3]);
                }
            })
        ;

        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.ComprehensiveQueryCenter);
    }]);
