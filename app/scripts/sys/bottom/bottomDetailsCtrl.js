'use strict';

bottomApp.controller('BottomDetailsCtrl', ['$scope', '$rootScope', '$filter', 'Constants',
    'rootService', 'pager', 'udcModal', 'BottomService', 'BottomInfo', function ($scope, $rootScope, $filter, Constants,
                                                          rootService, pager, udcModal, BottomService, BottomInfo) {

        $scope.vm = {
            user: []
        };

        var init = function () {
            console.log("-------BottomInfo : ", BottomInfo);
        };

        init();

    }]);
