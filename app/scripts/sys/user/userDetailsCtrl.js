'use strict';

userApp.controller('UserDetailsCtrl', ['$scope', '$rootScope', '$filter', 'Constants',
    'rootService', 'pager', 'udcModal', 'UserService', 'UserInfo', function ($scope, $rootScope, $filter, Constants,
                                                          rootService, pager, udcModal, UserService, UserInfo) {

        $scope.vm = {
            user: []
        };

        var init = function () {
            console.log("-------UserInfo : ", UserInfo);
        };

        init();

    }]);
