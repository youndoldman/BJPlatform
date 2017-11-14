'use strict';

commonModule.factory('udcModal', ['ModalService', '$q', function (ModalService, $q) {

    var show = function (option) {
        var defer = $q.defer();
        var modalOption = {
            templateUrl: option && option.templateUrl || '../pages/common/defaultModal.htm',
            controller: option && option.controller || ['$scope', 'close', function ($scope, close) {

                $scope.modalTitle = option && option.title || "信息";
                $scope.message = option && option.message || "无内容";
                $scope.type = option && option.type || "info";

                $scope.close = function (result) {
                    close(result, 500);
                };
            }],
            inputs: option && option.inputs || angular.noop()
        };

        ModalService.showModal(modalOption).then(function (modal) {
            modal.element.modal();
            modal.close.then(function (result) {
                defer.resolve(result);
            }, function (error) {
                defer.reject(error);
            });
        }, function (error) {
            defer.reject(error);
        });
        return defer.promise;
    };

    var info = function (option) {
        var infoOption = {
            title: "信息",
            type: 'info'
        };
        return show(_.extend(infoOption, option));
    };

    var confirm = function (option) {
        var confirmOption = {
            title: "请您确认",
            type: 'confirm'
        };
        return show(_.extend(confirmOption, option));
    };

    return {
        show: show,
        info: info,
        confirm: confirm
    }

}]);

