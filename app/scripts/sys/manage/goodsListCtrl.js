'use strict';

manageApp.controller('GoodsListCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'GoodsService','Upload','URI', function ($scope, $rootScope, $filter, $location, Constants,
                                                                 rootService, pager, udcModal, GoodsService,Upload,URI) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchGoods();
        };

        $scope.pager = pager.init('GoodsListCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            goodName: historyQ.goodName || ""
        };

        $scope.vm = {
            goodsList: []
        };
        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchGoods();
        };

        $scope.initPopUp = function () {
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

        $scope.data = {
            file: null
        };

        $scope.modify = function (goods) {
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

        $scope.delete = function (goods) {
            udcModal.confirm({"title": "删除商品", "message": "是否永久删除商品信息 " + goods.goodName})
                .then(function (result) {
                    if (result) {
                        GoodsService.deleteGoods(goods).then(function () {
                            searchGoods();
                        });
                    }
                })
        };

        var searchGoods = function () {
            var queryParams = {
                name: $scope.q.goodName,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            GoodsService.retrieveGoods(queryParams).then(function (goods) {
                $scope.pager.update($scope.q, goods.total, queryParams.pageNo);
                $scope.vm.goodsList = _.map(goods.items, GoodsService.toViewModel);
            });
        };
        var init = function () {
            searchGoods();
        };

        init();

    }]);

manageApp.controller('GoodsModalCtrl', ['$scope', 'close', 'GoodsService', 'title', 'initVal','Upload','URI', function ($scope, close, GoodsService, title, initVal,Upload,URI) {
    $scope.modalTitle = title;
    $scope.vm = {
        goods: {
        }
    };
    $scope.onFileSelect = function($files) {    //$files:是已选文件的名称、大小和类型的数组
        console.log($files);
        for (var i = 0; i < $files.length; i++) {
            $scope.data.file = $files[i];
        }

    };
    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (goods) {
        if (goods.goodName != "" && title == "新增商品") {
            $scope.upload(goods);
            $scope.close(true);
        } else if (goods.name != "" && title == "修改商品") {
            $scope.modify(goods);
            $scope.close(true);
        }
    };

    $scope.data = {
        file: null
    };
    $scope.upload = function (goods) {
        if (!$scope.data.file) {
            console.log("error data.file! ");
            return;
        }
        console.log($scope.data.file);

        var url = URI.resources.goods;  //params是model传的参数，图片上传接口的url

        Upload.upload({
            data: $scope.vm.goods,
            url: url,
            file: $scope.data.file
    }).success(function (goods) {
        }).error(function () {
            logger.log('error');
        });
    };

    $scope.modify = function (goods) {
        console.log($scope.data.file);

        var url = URI.resources.goods;  //params是model传的参数，图片上传接口的url

        Upload.upload({
            data: $scope.vm.goods,
            method: 'PUT',
            url: url,
            file: $scope.data.file
        }).success(function (goods) {
        }).error(function () {
            logger.log('error');
        });
    };

    var init = function () {
        $scope.vm.goods = _.clone(initVal);
        $scope.data.file = $scope.vm.goods.picture;
        if(title == "修改商品") {
            $scope.isModify = true;
        }
        else {
            $scope.isModify = false;
        }
    };

    init();
}]);/**
 * Created by Administrator on 2017/10/10.
 */
