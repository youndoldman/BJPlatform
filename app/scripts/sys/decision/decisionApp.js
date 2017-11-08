'use strict';

var decisionApp = angular.module('DecisionApp', ['ui.router', 'CommonModule', 'angularModalService'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {

        // 设置默认显示页面
        $urlRouterProvider.otherwise('/decision/cost');
        $stateProvider
            .state('decision', {
                url: '/decision',
                abstract: true,
                views: {
                    "": {
                        templateUrl: '../pages/decision/decisionlink.htm',
                        resolve: {}
                    }
                },
            })
            .state('decision.cost', {
                url: '/cost',
                views: {
                    "content@decision": {
                        templateUrl: '../pages/decision/cost.htm',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.DecisionCenter.menuItems[0]);
                }
            })
            .state('decision.sales', {
                url: '/sales',
                views: {
                    "content@decision": {
                        templateUrl: '../pages/decision/sales.htm',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.DecisionCenter.menuItems[1]);
                }
            })
            .state('decision.market', {
                url: '/market',
                views: {
                    "content@decision": {
                        templateUrl: '../pages/decision/market.htm',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.DecisionCenter.menuItems[2]);
                }
            });
        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.DecisionCenter);
    }]);


