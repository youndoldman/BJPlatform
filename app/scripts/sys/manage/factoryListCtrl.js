'use strict';

manageApp.controller('FactoryListCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'GoodsService', '$timeout', function ($scope, $rootScope, $filter, $location, Constants,
                                                                 rootService, pager, udcModal, GoodsService,$timeout) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchFactorys();
        };

        $scope.pager = pager.init('FactoryListCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            factoryName: null,

        };

        $scope.vm = {
            factoryList: [],
        };
        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchFactorys();
        };

        $scope.initPopUp = function () {
            udcModal.show({
                templateUrl: "./manage/factoryModal.htm",
                controller: "FactoryModalCtrl",
                inputs: {
                    title: '新增厂家',
                    initVal: {}
                }
            }).then(function (result) {
                if (result) {
                    searchFactorys();
                }
            })
        };


        $scope.modify = function (factory) {
            udcModal.show({
                templateUrl: "./manage/factoryModal.htm",
                controller: "FactoryModalCtrl",
                inputs: {
                    title: '修改厂家',
                    initVal: factory
                }
            }).then(function (result) {
                if (result) {
                    searchFactorys();
                }
            })
        };

        $scope.delete = function (factory) {
            udcModal.confirm({"title": "删除厂家", "message": "是否永久删除厂家信息 " + user.name})
                .then(function (result) {
                    if (result) {
                        GoodsService.deleteFactory(factory).then(function () {
                            udcModal.info({"title": "处理结果", "message": "删除厂家成功 "});
                            searchFactorys();
                        }, function (value) {
                            udcModal.info({"title": "处理结果", "message": "修改厂家失败 " + value.message});
                        })
                    }
                })
        };

        var searchFactorys = function () {
            var queryParams = {
                name: $scope.q.factoryName,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            GoodsService.retrieveFactorys(queryParams).then(function (factorys) {
                $scope.pager.update($scope.q, factorys.total, queryParams.pageNo);
                $scope.vm.factoryList = factorys.items;
            });
        };



        var init = function () {
            searchFactorys();

        };


        init();

    }]);

manageApp.controller('FactoryModalCtrl', ['$scope', 'close', 'GoodsService', 'title', 'initVal','$document','$interval', 'udcModal','$timeout',function ($scope, close, GoodsService, title, initVal,$document,$interval,udcModal,$timeout) {

    $scope.modalTitle = title;
    $scope.vm = {
        factory: {}

    };
    $scope.isModify = false;


    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (factory) {
        if (factory.name != "" && title == "新增厂家") {
            GoodsService.createFactory(factory).then(function () {
                udcModal.info({"title": "处理结果", "message": "创建厂家成功！"});
                $scope.close(true);
            }, function (value) {
                udcModal.info({"title": "处理结果", "message": "创建厂家失败 " + value.message});
            })
        } else if (factory.name != "" && title == "修改厂家") {
            GoodsService.modifyFactory(factory).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改厂家成功！"});
                $scope.close(true);
            }, function (value) {
                udcModal.info({"title": "处理结果", "message": "修改厂家失败 " + value.message});
            })
        }
    };

    var init = function () {
        $scope.vm.factory = _.clone(initVal);
        if (title == "修改厂家") {
            $scope.isModify = true;
        }


    };

    init();
}]);
