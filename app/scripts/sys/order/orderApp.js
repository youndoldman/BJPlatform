'use strict';
var orderApp = angular.module('OrderApp', ['CommonModule', 'angularModalService'])
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('HttpInterceptor');
    }]).run(['$rootScope', 'rootService', 'NavItem', function ($rootScope, rootService, NavItem) {
        rootService.updateActiveNavLv1(NavItem.OrderCenter);
    }]);