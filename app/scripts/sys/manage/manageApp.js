'use strict';

var manageApp = angular.module('ManageApp', ['ui.router', 'CommonModule', 'angularModalService','ngFileUpload'])
    .config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function($httpProvider, $stateProvider, $urlRouterProvider) {

        // 设置默认显示页面
        $urlRouterProvider.otherwise('/manage/users');

        $stateProvider
            .state('manage', {
                url: '/manage',
                abstract: true,
                views: {
                    "": {templateUrl: '../pages/manage/manageLink.htm'}
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ManageCenter.menuItems[0]);
                }
            })
            .state('manage.users',{
                url:'/users',
                views: {
                    "content@manage": {
                        templateUrl: '../pages/manage/userList.htm',
                        controller: 'UserListCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ManageCenter.menuItems[0]);
                }
            })
            .state('manage.department',{
                url:'/department',
                views: {
                    "content@manage": {
                        templateUrl: '../pages/manage/department.htm',
                        controller: 'DepartmentCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ManageCenter.menuItems[1]);
                }
            })
            .state('manage.users.detail',{
                url:'/users/{id: int}',
                views: {
                    "content@manage": {
                        templateUrl: '../pages/manage/userDetails.htm',
                        controller: 'UserDetailsCtrl',
                        resolve: {
                            UserInfo: function (UserService, $stateParams) {
                                return UserService.getUserInfoById($stateParams.id);
                            }
                        }
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ManageCenter.menuItems[0]);
                }
            })
            .state('manage.goods',{
                url:'/goods',
                views: {
                    "content@manage": {
                        templateUrl: '../pages/manage/goodsList.htm',
                        controller: 'GoodsListCtrl',
                        resolve: {}
                    }
                },
                onEnter: function (rootService, NavItem) {
                    rootService.updateActiveNavL2(NavItem.ManageCenter.menuItems[2]);
                }
            })
            .state('manage.cloudUser',{
            url:'/cloudUser',
            views: {
                "content@manage": {
                    templateUrl: '../pages/manage/cloudUserList.htm',
                    controller: 'CloudUserListCtrl',
                    resolve: {}
                }
            },
            onEnter: function (rootService, NavItem) {
                rootService.updateActiveNavL2(NavItem.ManageCenter.menuItems[3]);
            }
        });

        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.ManageCenter);
    }]);

