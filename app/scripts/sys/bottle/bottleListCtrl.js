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
            bottleNumber: historyQ.bottleNumber || ""
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
                number: $scope.q.bottleNumber,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            BottleService.retrieveBottles(queryParams).then(function (bottles) {
                $scope.pager.update($scope.q, bottles.total, queryParams.pageNo);
                $scope.vm.bottleList = bottles.items;
            });

        };



        var init = function () {
            //查询钢瓶
            searchBottles();
        };

        init();

    }]);

bottleApp.controller('BottleModalCtrl', ['$scope', 'close', 'BottleService', 'title', 'initVal', 'udcModal',function ($scope, close, BottleService, title, initVal, udcModal) {
    $scope.modalTitle = title;
    $scope.isModify = false;
    $scope.isBinded = false;//是否绑定定位终端
    $scope.vm = {
        bottle: {},
        config:{
            status:[{value:0,name:"未启用"},
                {value:1,name:"已启用"},
                {value:2,name:"已停用"},
                {value:3,name:"已作废"}],
            specs:[],
        }
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
            //去除定位终端字段，才能提交
            bottle.locationDevice = null;
            BottleService.modifyBottle(bottle).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改钢瓶信息成功 "});
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
        if ($scope.vm.bottle.locationDevice==null){
            $scope.isBinded = false;
        } else{
            $scope.isBinded = true;
        }
        //获取钢瓶规格列表
        BottleService.retrieveBottleSpecs().then(function (specs) {
            $scope.vm.config.specs = specs.items;
            if($scope.isModify){
                //规格
                for(var i=0; i<$scope.vm.config.specs.length; i++){
                    if($scope.vm.bottle.spec.code == $scope.vm.config.specs[i].code){
                        $scope.vm.bottle.spec = $scope.vm.config.specs[i];
                        break;
                    }
                }
            }else {
                $scope.vm.bottle.spec = $scope.vm.config.specs[0];
                $scope.vm.bottle.status = $scope.vm.config.status[0].value;
            }
        })
        if($scope.isModify) {
            //钢瓶状态格式化
            $scope.vm.bottle.status = $scope.vm.bottle.status.index;
        }

    };
    //解除绑定
    $scope.unBind = function () {
        BottleService.unBindBottle($scope.vm.bottle, $scope.vm.bottle.locationDevice.number).then(function () {
            $scope.vm.bottle.locationDevice = null;
            $scope.isBinded = false;
            udcModal.info({"title": "处理结果", "message": "解除绑定成功 "});
        })
    };
    //绑定
    $scope.bind = function () {
        BottleService.bindBottle($scope.vm.bottle, $scope.vm.bottle.locationDevice.number).then(function () {
            $scope.isBinded = true;
            udcModal.info({"title": "处理结果", "message": "绑定成功 "});
        })
    };

    init();
}]);