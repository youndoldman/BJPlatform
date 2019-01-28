'use strict';
comprehensiveQueryApp.controller('GasCynTrayModalCtrl', ['$scope', 'close', 'TicketService', 'title', 'initVal','URI','udcModal', function ($scope, close, TicketService, title, initVal,URI,udcModal) {
    $scope.modalTitle = title;
    $scope.vm = {
        gasCynTray:{number:null,calibration:null},
        oldGasCynTray:null,

    };

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (gasCynTray) {
        TicketService.modifyGasCynTray(gasCynTray).then(function () {
                udcModal.info({"title": "处理结果", "message": "托盘标定成功 "});
                $scope.close(true);
            }, function (value) {
                udcModal.info({"title": "处理结果", "message": "托盘标定失败 " + value.message});
            })

    };

    var init = function () {
        $scope.vm.oldGasCynTray = _.clone(initVal);
        $scope.vm.gasCynTray.calibration = $scope.vm.oldGasCynTray.calibration;
        $scope.vm.gasCynTray.number = $scope.vm.oldGasCynTray.number;
        if(title == "托盘标定") {
            $scope.isModify = true;
        } else {
            $scope.isModify = false;
        }
    };

    init();

}]);