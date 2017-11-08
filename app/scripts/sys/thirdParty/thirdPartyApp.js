'use strict';

var thirdPartyApp = angular.module('ThirdPartyApp', ['ui.router', 'CommonModule', 'angularModalService'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {

        // 设置默认显示页面
        $urlRouterProvider.otherwise('/apps');
        $stateProvider
            .state('apps', {
                url: '/apps',
                views: {
                    "": {
                        templateUrl: '../pages/thirdParty/apps.htm',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.AppCenter.menuItems[0]);
                }
            });
        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.AppCenter);
    }]);


