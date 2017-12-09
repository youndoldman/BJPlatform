'use strict';

manageApp.controller('GoodsListCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'GoodsService','Upload','URI', function ($scope, $rootScope, $filter, $location, Constants,
                                                                 rootService, pager, udcModal, GoodsService,Upload,URI) {
        var gotoPageGoodsType = function (pageNo) {
            $scope.pagerGoodsTypes.setCurPageNo(pageNo);
            searchGoodsTypes();
        };
        var gotoPageGoods = function (pageNo) {
            $scope.pagerGoods.setCurPageNo(pageNo);
            searchGoods();
        };

        $scope.pagerGoodsTypes = pager.init('GoodsListCtrl', gotoPageGoodsType);

        $scope.pagerGoods = pager.init('GoodsListCtrl', gotoPageGoods);

        var historyQ = $scope.pagerGoodsTypes.getQ();

        $scope.q = {
            goodsType:""
        };


        $scope.vm = {
            goodsTypesList: [],
            goodsList: []
        };
        $scope.search = function () {
            $scope.pagerGoods.setCurPageNo(1);
            searchGoods();
        };

        $scope.initPopUpGoods = function () {
            udcModal.show({
                templateUrl: "./manage/goodsModal.html",
                controller: "GoodsModalCtrl",
                inputs: {
                    title: '新增商品',
                    initVal: {}
                }
            }).then(function (result) {
                if (result) {
                    console.log("新增商品");
                    searchGoods();
                }
            })
        };


        $scope.modifyGoods = function (goods) {
            udcModal.show({
                templateUrl: "./manage/goodsModal.html",
                controller:  "GoodsModalCtrl",
                inputs: {
                    title: '修改商品',
                    initVal: goods
                }
            }).then(function (result) {
                if (result) {
                    console.log("修改商品");
                    searchGoods();
                }
            })
        };

        $scope.initPopUpGoodsType = function () {
            udcModal.show({
                templateUrl: "./manage/goodsTypeModal.html",
                controller: "GoodsTypeModalCtrl",
                inputs: {
                    title: '新增商品类型',
                    initVal: {}
                }
            }).then(function (result) {
                if (result) {
                    console.log("新增商品类型");
                    searchGoodsTypes();
                }
            })
        };

        $scope.modifyGoodsType = function (goods) {
            udcModal.show({
                templateUrl: "./manage/goodsTypeModal.html",
                controller:  "GoodsTypeModalCtrl",
                inputs: {
                    title: '修改商品类型',
                    initVal: goods
                }
            }).then(function (result) {
                if (result) {
                    console.log("修改商品类型");
                        searchGoodsTypes();
                }
            })
        };

        $scope.deleteGoods = function (goods) {
            udcModal.confirm({"title": "删除商品", "message": "是否永久删除商品信息 " + goods.name})
                .then(function (result) {
                    if (result) {
                        GoodsService.deleteGoods(goods).then(function () {
                            udcModal.info({"title": "处理结果", "message": "删除商品信息成功 "});
                            searchGoods();
                        });
                    }
                })
        };

        $scope.deleteGoodsType = function (goodsType) {
            udcModal.confirm({"title": "删除商品类型", "message": "是否永久删除商品类型信息 " + goodsType.name})
                .then(function (result) {
                    if (result) {
                        GoodsService.deleteGoodsType(goodsType).then(function () {
                            udcModal.info({"title": "处理结果", "message": "删除商品类型成功 "});
                            searchGoodsTypes();
                        });
                    }
                })
        };



        var searchGoods = function () {
            var queryParams = {
                typeName: $scope.q.goodsType,
                pageNo: $scope.pagerGoods.getCurPageNo(),
                pageSize: $scope.pagerGoods.pageSize
            };

            GoodsService.retrieveGoods(queryParams).then(function (goods) {
                $scope.pagerGoods.update($scope.q, goods.total, queryParams.pageNo);
                $scope.vm.goodsList = _.map(goods.items, GoodsService.toViewModel);
            });
        };

        var searchGoodsTypes = function () {
            var queryParams = {
                pageNo: $scope.pagerGoodsTypes.getCurPageNo(),
                pageSize: $scope.pagerGoodsTypes.pageSize
            };

            GoodsService.retrieveGoodsTypes(queryParams).then(function (goodsTypes) {
                $scope.pagerGoodsTypes.update($scope.q, goodsTypes.total, queryParams.pageNo);
                $scope.vm.goodsTypesList = _.map(goodsTypes.items, GoodsService.toViewModelGoodsTypes);
            });
        };


        var init = function () {
            searchGoodsTypes();
            searchGoods();
        };

        init();

    }]);
