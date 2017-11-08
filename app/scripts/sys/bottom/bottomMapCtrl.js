'use strict';

bottomApp.controller('BottomMapCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'BottomService', function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal, BottomService) {


        $scope.mapReady = function (map) {
            BottomService.mapReady(map);
        }

    }]);
