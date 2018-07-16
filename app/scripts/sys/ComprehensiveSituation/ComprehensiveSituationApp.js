'use strict';
var comprehensiveSituationApp = angular.module('ComprehensiveSituationApp', ['ui.router', 'CommonModule', 'angularModalService'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {

        // 设置默认显示页面
        $urlRouterProvider.otherwise('/comprehensiveSituation/map');
        $stateProvider
            .state('comprehensiveSituation', {
                url: '/comprehensiveSituation',
                abstract: true,
                views: {
                    "": {templateUrl: '../pages/ComprehensiveSituation/comprehensiveSituationLink.htm'}
                }
            })
            .state('comprehensiveSituation.map',{
                url:'/map',
                views: {
                    "content@comprehensiveSituation": {
                        templateUrl: '../pages/ComprehensiveSituation/gisWatch.htm',
                        controller: 'GisWatchCtrl',
                        resolve: {}
                    }
                }
            });
        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.ComprehensiveSituationCenter);
    }]);
