manageApp.controller('GoodsModalCtrl', ['$scope', 'close', 'GoodsService', 'title', 'initVal','Upload','URI','udcModal', function ($scope, close, GoodsService, title, initVal,Upload,URI,udcModal) {
    $scope.modalTitle = title;
    $scope.vm = {
        goods: {},
        goodsTypesList:[],
    };
    $scope.config = {
        goodsStatusList:[{name:"正常上市",value:0},{name:"暂停销售",value:1},{name:"商品下架",value:2}]
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
            console.log($scope.vm.goods.status);
        }
        else {
            $scope.isModify = false;
            //商品状态初始化
            $scope.vm.goods.status = $scope.config.goodsStatusList[0].value;
            console.log($scope.vm.goods.status);
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