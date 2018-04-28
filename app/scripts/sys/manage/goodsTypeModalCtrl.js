'use strict';
manageApp.controller('GoodsTypeModalCtrl', ['$scope', 'close', 'GoodsService', 'title', 'initVal','Upload','URI','udcModal', function ($scope, close, GoodsService, title, initVal,Upload,URI,udcModal) {
    $scope.modalTitle = title;
    $scope.vm = {
        goodsType: {
        },
        goodsOldCode:null
    };

    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (goodsType) {
        if (goodsType.name != "" &&goodsType.code != "" && title == "新增商品类型") {
            GoodsService.createGoodsType(goodsType).then(function () {
                udcModal.info({"title": "处理结果", "message": "新增商品类型成功 "});
                $scope.close(true);
            }, function (value) {
                udcModal.info({"title": "处理结果", "message": "新增商品类型失败 " + value.message});
            })
        } else if (goodsType.name != "" &&goodsType.code != ""&& title == "修改商品类型") {
            GoodsService.modifyGoodsType(goodsType, $scope.vm.goodsOldCode).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改商品类型成功 "});
                $scope.close(true);
            }, function (value) {
                udcModal.info({"title": "处理结果", "message": "修改商品类型失败 " + value.message});
            })
        }
    };

    var init = function () {
        $scope.vm.goodsType = _.clone(initVal);
        $scope.vm.goodsOldCode = $scope.vm.goodsType.code;
        if(title == "修改商品") {
            $scope.isModify = true;
        }
        else {
            $scope.isModify = false;
        }
    };

    init();
}]);