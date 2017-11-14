'use strict';
var mainApp = angular.module('MainApp', ['CommonModule', 'angularModalService'])
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.Home);
    }]);