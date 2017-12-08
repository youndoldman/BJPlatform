'use strict';
manageApp.controller('GoodsTypeModalCtrl', ['$scope', 'close', 'GoodsService', 'title', 'initVal','Upload','URI','udcModal', function ($scope, close, GoodsService, title, initVal,Upload,URI,udcModal) {
    $scope.modalTitle = title;
    $scope.vm = {
        goodsType: {
        }
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
            })
        } else if (goodsType.name != "" &&goodsType.code != ""&& title == "修改商品类型") {
            GoodsService.modifyGoodsType(goodsType).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改商品类型成功 "});
                $scope.close(true);
            })
        }
    };




    var init = function () {
        $scope.vm.goodsType = _.clone(initVal);
        if(title == "修改商品") {
            $scope.isModify = true;
        }
        else {
            $scope.isModify = false;
        }
    };

    init();
}]);