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
        }
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
                    title: '修改钢瓶信息',
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
            searchBottles();
        };

        init();

    }]);

bottleApp.controller('BottleModalCtrl', ['$scope', 'close', 'BottleService', 'title', 'initVal', function ($scope, close, BottleService, title, initVal) {
    $scope.modalTitle = title;
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
        $scope.vm.bottle = _.clone(initVal)
    };

    init();
}]);