'use strict';

var routineApp = angular.module('RoutineApp', ['ui.router', 'CommonModule', 'angularModalService'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {

        // 设置默认显示页面
        $urlRouterProvider.otherwise('/routine/holiday');

        $stateProvider
            .state('routine', {
                url: '/routine',
                abstract: true,
                views: {
                    "": {templateUrl: '../pages/routine/routineLink.htm'}
                }
            })
            .state('routine.holiday',{
                url:'/holiday',
                views: {
                    "content@routine": {
                        templateUrl: '../pages/routine/holidayForm.htm',
                        controller: '',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.RoutineCenter.menuItems[0]);
                }
            })
            .state('routine.mytask',{
                url:'/mytask',
                views: {
                    "content@routine": {
                        templateUrl: '../pages/routine/mytaskForm.htm',
                        controller: 'mytaskCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.RoutineCenter.menuItems[0]);
                }
            })
            .state('routine.expence',{
                url:'/expence',
                views: {
                    "content@routine": {
                        templateUrl: '../pages/routine/expenceForm.htm',
                        controller: '',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.RoutineCenter.menuItems[1]);
                }
            });
        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.RoutineCenter);
    }]);
