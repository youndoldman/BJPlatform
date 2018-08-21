'use strict';
comprehensiveQueryApp.controller('TicketModalCtrl', ['$scope', 'close', 'TicketService', 'title', 'initVal','Upload','URI','udcModal', function ($scope, close, TicketService, title, initVal,Upload,URI,udcModal) {
    $scope.modalTitle = title;
    $scope.vm = {
        modifyTicket:null,
        originalTicket:null
    };

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (ticket) {
        TicketService.modifyTicket(ticket).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改气票信息成功 "});
                $scope.close(true);
            }, function (value) {
                udcModal.info({"title": "处理结果", "message": "修改气票信息失败 " + value.message});
            })

    };

    var init = function () {
        $scope.vm.originalTicket = _.clone(initVal);
        if(title == "修改气票") {
            $scope.isModify = true;
        }
        else {
            $scope.isModify = false;
        }
    };

    init();
}]);