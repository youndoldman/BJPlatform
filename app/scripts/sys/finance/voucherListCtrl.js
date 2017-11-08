'use strict';

financeApp.controller('VoucherListCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal) {

        $scope.initPopUp = function () {
            udcModal.show({
                templateUrl: "./finance/voucherModal.htm",
                controller: "",
                inputs: {
                    title: '凭证录入',
                    initVal: {}
                }
            }).then(function (result) {
                if (result) {
                }
            })
        };


    }]);
