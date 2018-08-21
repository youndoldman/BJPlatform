'use strict';
comprehensiveQueryApp.controller('TicketModalCtrl', ['$scope', 'close', 'TicketService', 'title', 'initVal','URI','udcModal', function ($scope, close, TicketService, title, initVal,URI,udcModal) {
    $scope.modalTitle = title;
    $scope.vm = {
        modifyTicket:{ticketSn:null},
        originalTicket:null,
        name:null
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
        $scope.vm.modifyTicket.ticketSn = $scope.vm.originalTicket.ticketSn;
        if(title == "修改气票") {
            $scope.isModify = true;
        } else {
            $scope.isModify = false;
        }
    };

    init();
    //查询员工姓名
    $scope.getSalesManName = function () {
        var queryParams = {
            userId: $scope.vm.modifyTicket.salemanId,
        };
        TicketService.FindSysUserUri(queryParams).then(function (sysUsers) {
            $scope.vm.name = sysUsers.name;
        }, function(value) {
            $scope.vm.name = null;
        })
    };
}]);