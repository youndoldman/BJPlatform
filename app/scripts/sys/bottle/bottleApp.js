'use strict';
var bottleApp = angular.module('BottleApp', ['ui.router', 'CommonModule', 'angularModalService'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {


        // 设置默认显示页面
        $urlRouterProvider.otherwise('/bottles/map');
        $stateProvider
            .state('bottles', {
                url: '/bottles',
                abstract: true,
                views: {
                    "": {templateUrl: '../pages/bottle/bottleLink.htm'}
                }
            })
            .state('bottles.map',{
                url:'/map',
                views: {
                    "content@bottles": {
                        templateUrl: '../pages/bottle/bottleMap.htm',
                        controller: 'BottleMapCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.GasCenter.menuItems[0]);
                }
            })

            .state('bottles.list',{
                url:'/list',
                views: {
                    "content@bottles": {
                        templateUrl: '../pages/bottle/bottleList.htm',
                        controller: 'BottleListCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.GasCenter.menuItems[1]);
                }
            })
            .state('bottles.warning',{
                url:'/warning',
                views: {
                    "content@bottles": {
                        templateUrl: '../pages/bottle/bottleWarning.htm',
                        controller: 'BottleWarningCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.GasCenter.menuItems[2]);
                }
            })

            .state('bottles.stock',{
                url:'/stock',
                views: {
                    "content@bottles": {
                        templateUrl: '../pages/bottle/centerStock.htm',
                        controller: 'CenterStockCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.GasCenter.menuItems[3]);
                }
            })
            .state('bottles.filling',{
                url:'/filling',
                views: {
                    "content@bottles": {
                        templateUrl: '../pages/bottle/bottleFilling.htm',
                        controller: 'BottleFillingCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.GasCenter.menuItems[4]);
                }
            });
        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.GasCenter);
    }]);
