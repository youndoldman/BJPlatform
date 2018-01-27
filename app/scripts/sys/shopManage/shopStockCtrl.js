'use strict';

shopManageApp.controller('ShopStockCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'ShopStockService','sessionStorage', function ($scope, $rootScope, $filter, $location, Constants,

                                                                      rootService, pager, udcModal, ShopStockService, sessionStorage) {
        var currentUser = sessionStorage.getCurUser();

        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchBottles();
        };

        $scope.pager = pager.init('ShopStockControl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            bottleNumber: historyQ.bottleNumber || ""
        };

        $scope.vm = {
            bottleList: []
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchBottles();
        };

        $scope.initPopUp = function () {
            udcModal.show({
                templateUrl: "./shopManage/shopStockModal.htm",
                controller: "ShopStockModalCtrl",
                inputs: {
                    title: '门店入库',
                    initVal: {}
                }
            }).then(function (result) {
                if (result) {
                    searchBottles();
                }
            })
        };

        $scope.viewDetails = function (bottle) {
            $location.path('/bottle/' + bottle.id);
        };

        $scope.modify = function (bottle) {
            udcModal.show({
                templateUrl: "./shopManage/shopStockModal.htm",
                controller: "ShopStockModalCtrl",
                inputs: {
                    title: '门店出库',
                    initVal: bottle
                }
            }).then(function (result) {
                if (result) {
                    searchBottles();
                }
            })
        };


        var searchBottles = function () {
            var queryParams = {
                number: $scope.q.bottleNumber,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
                liableDepartmentCode:currentUser.department.code,
                serviceStatus:2
            };

            ShopStockService.retrieveBottles(queryParams).then(function (bottles) {
                $scope.pager.update($scope.q, bottles.total, queryParams.pageNo);
                $scope.vm.bottleList = bottles.items;
            });

        };


        var init = function () {
            //查询钢瓶
            searchBottles();
        };

        init();

    }]);

shopManageApp.controller('ShopStockModalCtrl', ['$scope', 'close', 'ShopStockService', 'title', 'initVal', 'udcModal','sessionStorage',function ($scope, close, ShopStockService, title, initVal, udcModal,sessionStorage) {
    $scope.modalTitle = title;
    $scope.isModify = false;
    $scope.vm = {
        bottle: {},
        handOver:{
            srcUser:null,
            destUser:null,
            nextStatus:null
        },
        reasons:null,
        selectReason:null
    };

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function () {
        ShopStockService.handOverBottle($scope.vm.handOver.bottleNumber,$scope.vm.handOver.srcUser,
            $scope.vm.handOver.destUser,$scope.vm.selectReason.value).then(function () {
            if($scope.isModify){
                udcModal.info({"title": "处理结果", "message": "门店出库成功 "});
            }else {
                udcModal.info({"title": "处理结果", "message": "门店入库成功 "});
            }
            //$scope.close(true);
        }, function(value) {
            udcModal.info({"title": "错误信息", "message": value.message});
        })

    };

    var init = function () {
        var currentUser = sessionStorage.getCurUser();
        $scope.vm.bottle = _.clone(initVal);
        if ($scope.vm.bottle==null){
            $scope.vm.handOver.bottleNumber = null;
        } else{
            $scope.vm.handOver.bottleNumber = $scope.vm.bottle.number;
        }
        if (title == "门店出库"){
            $scope.isModify = true;
            $scope.vm.handOver.srcUser = $scope.vm.bottle.user.userId;
            $scope.vm.reasons = [{name:"钢瓶调拨",value:"3"},{name:"钢瓶配送",value:"4"}];
            $scope.vm.selectReason = $scope.vm.reasons[0];
        } else {
            $scope.isModify = false;
            $scope.vm.handOver.destUser = currentUser.userId;
            $scope.vm.reasons = [{name:"门店库存",value:"2"}];
            $scope.vm.selectReason = $scope.vm.reasons[0];
        }
    };

    init();
}]);