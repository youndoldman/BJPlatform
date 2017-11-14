'use strict';

commonModule.directive('njHeader', ['NavItem', 'rootService', function (NavItem, rootService) {
    return {
        restrict: 'AE',
        templateUrl: '../pages/common/njHeader.htm',
        link: function ($scope, $element, $attrs) {

            $scope.navItems = _.values(NavItem);
            $scope.activeNav = rootService.getActiveNav();

        }
    }
}]);

commonModule.directive('njMenu', ['rootService', function (rootService) {
    return {
        restrict: 'AE',
        scope: {},
        templateUrl: '../pages/common/njMenu.htm',
        link: function ($scope, $element, $attrs) {
            $scope.menuItems = [];

            function hasSubMenuItems(item) {
                return item.subMenuItems && item.subMenuItems.length > 0;
            }

            $scope.activeNav = rootService.getActiveNav();

            $scope.getMenuItemClass = function (item, activeNav) {
                if (activeNav.lv2 && item.name == activeNav.lv2.name) {
                    return hasSubMenuItems(item) ? 'active open' : 'active';
                }
                return '';
            };

            $scope.hasSubMenuItems = function (item) {
                return hasSubMenuItems(item);
            };

        }
    }
}]);
