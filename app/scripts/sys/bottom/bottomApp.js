'use strict';

var bottomApp = angular.module('BottomApp', ['ui.router', 'CommonModule', 'angularModalService', 'angular-baidu-map'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {

        // 设置默认显示页面
        $urlRouterProvider.otherwise('/bottoms/list');

        $stateProvider
            .state('bottoms', {
                url: '/bottoms',
                abstract: true,
                views: {
                    "": {templateUrl: '../pages/bottom/bottomLink.htm'}
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.GasCenter.menuItems[0]);
                }
            })
            .state('bottoms.list',{
                url:'/list',
                views: {
                    "viewer@bottoms": {
                        templateUrl: '../pages/bottom/bottomMap.htm',
                        controller: 'BottomMapCtrl',
                        resolve: {}
                    },
                    "content@bottoms": {
                        templateUrl: '../pages/bottom/bottomList.htm',
                        controller: 'BottomListCtrl',
                        resolve: {}
                    }
                }
            })
            .state('bottoms.detail',{
                url:'/{id: int}',
                views: {
                    "content@bottoms": {
                        templateUrl: '../pages/bottom/bottomDetails.htm',
                        controller: 'BottomDetailsCtrl',
                        resolve: {
                            BottomInfo: function (BottomService, $stateParams) {
                                return BottomService.getBottomInfoById($stateParams.id);
                            }
                        }
                    }

                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.GasCenter.menuItems[0]);
                }
            });

        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.GasCenter);
    }]);
