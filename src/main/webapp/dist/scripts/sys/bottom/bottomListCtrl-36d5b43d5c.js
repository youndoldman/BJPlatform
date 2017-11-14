'use strict';

bottomApp.controller('BottomListCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'BottomService', function ($scope, $rootScope, $filter, $location, Constants,

                                                          rootService, pager, udcModal, BottomService) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchBottoms();
        };
        $scope.location = function(lon,lan)
        {
            console.log(lon+"------"+lan);
            BottomService.location(lon, lan);
        }
        $scope.pager = pager.init('BottomListCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            bottomName: historyQ.bottomName || ""
        };

        $scope.vm = {
            bottomList: []
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchBottoms();
        };

        $scope.initPopUp = function () {
            udcModal.show({
                templateUrl: "./bottom/bottomModal.htm",
                controller: "BottomModalCtrl",
                inputs: {
                    title: '新增钢瓶',
                    initVal: {}
                }
            }).then(function (result) {
                if (result) {
                    searchBottoms();
                }
            })
        };

        $scope.viewDetails = function (bottom) {
            $location.path('/bottom/' + bottom.id);
        };

        $scope.modify = function (bottom) {
            udcModal.show({
                templateUrl: "./bottom/bottomModal.htm",
                controller: "BottomModalCtrl",
                inputs: {
                    title: '修改钢瓶信息',
                    initVal: bottom
                }
            }).then(function (result) {
                if (result) {
                    searchBottoms();
                }
            })
        };

        $scope.delete = function (user) {
            udcModal.confirm({"title": "删除钢瓶", "message": "是否永久删除钢瓶信息 " + user.name})
                .then(function (result) {
                    if (result) {
                        BottomService.deleteBottom(bottom).then(function () {
                            searchBottoms();
                        });
                    }
                })
        };

        var searchBottoms = function () {
            var queryParams = {
                name: $scope.q.bottomName,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            BottomService.retrieveBottoms(queryParams).then(function (bottoms) {
                $scope.pager.update($scope.q, bottoms.total, queryParams.pageNo);
                $scope.vm.bottomList = _.map(bottoms.items, BottomService.toViewModel);
            });
        };



        var init = function () {
            searchBottoms();
        };

        init();

    }]);

bottomApp.controller('BottomModalCtrl', ['$scope', 'close', 'BottomService', 'title', 'initVal', function ($scope, close, BottomService, title, initVal) {
    $scope.modalTitle = title;
    $scope.vm = {
        bottom: {}
    };

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (bottom) {
        if (bottom.name != "" && title == "新增钢瓶") {
            BottomService.createBottom(bottom).then(function () {
                $scope.close(true);
            })
        } else if (bottom.name != "" && title == "修改钢瓶") {
            BottomService.modifyBottom(bottom).then(function () {
                $scope.close(true);
            })
        }
    };

    var init = function () {
        $scope.vm.bottom = _.clone(initVal)
    };

    init();
}]);