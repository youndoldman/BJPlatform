'use strict';
var loginApp = angular.module('LoginApp', ['CommonModule', 'angularModalService'])
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('HttpInterceptor');
    }]);

