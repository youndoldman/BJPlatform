manageApp.controller('GoodsModalCtrl', ['$scope', 'close', 'GoodsService', 'title', 'initVal','Upload','URI','udcModal', function ($scope, close, GoodsService, title, initVal,Upload,URI,udcModal) {
    $scope.modalTitle = title;
    $scope.vm = {
        goods: {},
        goodsTypesList:[],
    };
    $scope.isModify = false;

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (goods) {
        if (title == "新增商品") {
            GoodsService.createGoods(goods).then(function () {
                udcModal.info({"title": "处理结果", "message": "新增商品成功 "});
                $scope.close(true);
            })
        } else if (title == "修改商品") {
            GoodsService.modifyGoods(goods).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改商品成功 "});
                $scope.close(true);
            })
        }
    };




    var init = function () {
        $scope.vm.goods = _.clone(initVal);
        if(title == "修改商品") {
            $scope.isModify = true;
        }
        else {
            $scope.isModify = false;
        }
        //查询商品类型
        var queryParams = {
        };
        GoodsService.retrieveGoodsTypes(queryParams).then(function (goodsTypes) {
            $scope.vm.goodsTypesList = _.map(goodsTypes.items, GoodsService.toViewModelGoodsTypes);
            $scope.vm.goods.goodsType = $scope.vm.goodsTypesList[0];
        });
    };

    init();
}]);