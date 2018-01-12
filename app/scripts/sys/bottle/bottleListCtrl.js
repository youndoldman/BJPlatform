'use strict';

bottleApp.controller('BottleListCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'BottleService', function ($scope, $rootScope, $filter, $location, Constants,

                                                          rootService, pager, udcModal, BottleService) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchBottles();
        };
        $scope.location = function(lon,lan)
        {
            console.log(lon+"------"+lan);
            BottleService.location(lon, lan);
        };
        $scope.pager = pager.init('BottleListCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            bottleName: historyQ.bottleName || ""
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
                templateUrl: "./bottle/bottleModal.htm",
                controller: "BottleModalCtrl",
                inputs: {
                    title: '新增钢瓶',
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
                templateUrl: "./bottle/bottleModal.htm",
                controller: "BottleModalCtrl",
                inputs: {
                    title: '修改钢瓶',
                    initVal: bottle
                }
            }).then(function (result) {
                if (result) {
                    searchBottles();
                }
            })
        };

        $scope.delete = function (user) {
            udcModal.confirm({"title": "删除钢瓶", "message": "是否永久删除钢瓶信息 " + user.name})
                .then(function (result) {
                    if (result) {
                        BottleService.deleteBottle(bottle).then(function () {
                            searchBottles();
                        });
                    }
                })
        };

        var searchBottles = function () {
            var queryParams = {
                name: $scope.q.bottleName,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            BottleService.retrieveBottles(queryParams).then(function (bottles) {
                $scope.pager.update($scope.q, bottles.total, queryParams.pageNo);
                $scope.vm.bottleList = _.map(bottles.items, BottleService.toViewModel);
            });

        };



        var init = function () {
            $scope.vm.bottleList = [{
                id:"001",
                code:"00011",
                specifications:"10kg",
                deviceCode:"00031",
                deviceBattery:"80%",
                birthTime:"2017-01-01",
                lastCheckTime:"2017-01-01",
                nextCheckTime:"2018-01-01",
                deadTime:"2020-01-01",
            }];
            //searchBottles();
        };

        init();

    }]);

bottleApp.controller('BottleModalCtrl', ['$scope', 'close', 'BottleService', 'title', 'initVal', 'udcModal',function ($scope, close, BottleService, title, initVal, udcModal) {
    $scope.modalTitle = title;
    $scope.isModify = false;
    $scope.isBinded = false;//是否绑定定位终端
    $scope.vm = {
        bottle: {}
    };

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (bottle) {
        if (bottle.name != "" && title == "新增钢瓶") {
            BottleService.createBottle(bottle).then(function () {
                $scope.close(true);
            })
        } else if (bottle.name != "" && title == "修改钢瓶") {
            BottleService.modifyBottle(bottle).then(function () {
                $scope.close(true);
            })
        }
    };

    var init = function () {
        $scope.vm.bottle = _.clone(initVal);
        if (title == "修改钢瓶"){
            $scope.isModify = true;
        } else {
            $scope.isModify = false;
        }
        //没有绑定定位终端
        if ($scope.vm.bottle.deviceCode==null){
            $scope.isBinded = false;
        } else{
            $scope.isBinded = true;
        }
    };
    //解除绑定
    $scope.unBind = function () {
        $scope.vm.bottle.deviceCode = null;
        $scope.isBinded = false;
        udcModal.info({"title": "处理结果", "message": "解除绑定成功 "});
    };
    //绑定
    $scope.bind = function () {
        $scope.isBinded = true;
        udcModal.info({"title": "处理结果", "message": "绑定成功 "});
    };

    init();
}]);