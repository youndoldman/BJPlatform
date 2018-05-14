'use strict';

shopManageApp.controller('ShopStockCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'ShopStockService','sessionStorage', function ($scope, $rootScope, $filter, $location, Constants,

                                                                      rootService, pager, udcModal, ShopStockService, sessionStorage) {
        var currentUser = sessionStorage.getCurUser();

        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchBottles();
        };

        $scope.pager = pager.init('ShopStockControl', gotoPage);
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
                templateUrl: "./shopManage/shopStockModal.htm",
                controller: "ShopStockModalCtrl",
                inputs: {
                    title: '门店入库',
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
                templateUrl: "./shopManage/shopStockModal.htm",
                controller: "ShopStockModalCtrl",
                inputs: {
                    title: '门店出库',
                    initVal: bottle
                }
            }).then(function (result) {
                if (result) {
                    searchBottles();
                }
            })
        };


        var searchBottles = function () {
            var queryParams = {
                number: $scope.q.bottleNumber,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
                liableDepartmentCode:currentUser.department.code,
                serviceStatus:2
            };

            ShopStockService.retrieveBottles(queryParams).then(function (bottles) {
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

shopManageApp.controller('ShopStockModalCtrl', ['$scope', 'close', 'ShopStockService', 'title', 'initVal', 'udcModal','sessionStorage',function ($scope, close, ShopStockService, title, initVal, udcModal,sessionStorage) {
    $scope.modalTitle = title;
    $scope.isModify = false;
    $scope.vm = {
        currentUser: {},
        bottle: {},
        handOver:{
            srcUser:null,
            destUser:null,
            nextStatus:null
        },
        reasons:null,
        selectReason:null
    };

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function () {
        var note = "";//详细交接原因
        if($scope.isModify){
            note = $scope.vm.currentUser.department.name+"钢瓶出库";
        }else{
            note = $scope.vm.currentUser.department.name+"钢瓶入库";
        }

        ShopStockService.handOverBottle($scope.vm.handOver.bottleNumber,$scope.vm.handOver.srcUser,
            $scope.vm.handOver.destUser,$scope.vm.selectReason, note).then(function () {
            if($scope.isModify){
                udcModal.info({"title": "处理结果", "message": "门店出库成功 "});
            }else {
                udcModal.info({"title": "处理结果", "message": "门店入库成功 "});
            }
            //$scope.close(true);
        }, function(value) {
            udcModal.info({"title": "错误信息", "message": value.message});
        })

    };



    var init = function () {
        $scope.vm.currentUser = sessionStorage.getCurUser();
        $scope.vm.bottle = _.clone(initVal);
        if ($scope.vm.bottle==null){
            $scope.vm.handOver.bottleNumber = null;
        } else{
            $scope.vm.handOver.bottleNumber = $scope.vm.bottle.number;
        }
        if (title == "门店出库"){
            if($scope.vm.bottle!=null){//有档案出库
                $scope.vm.handOver.srcUser = $scope.vm.bottle.user.userId;
            }else{
                $scope.vm.handOver.srcUser = $scope.vm.currentUser.userId;
            }
            $scope.isModify = true;
            $scope.vm.reasons = [{name:"未知",value:"-1"},{name:"钢瓶调拨",value:"3"},{name:"钢瓶配送",value:"4"}];
            $scope.vm.selectReason = "-1";
        } else {
            $scope.isModify = false;
            $scope.vm.handOver.destUser = $scope.vm.currentUser.userId;
            $scope.vm.reasons = [{name:"门店库存",value:"2"}];
            $scope.vm.selectReason = "2";
        }
    };

    init();

    $scope.findHandoverReason = function () {
        var queryParams = {
            userId:$scope.vm.handOver.destUser//责任人
        };
        ShopStockService.retrieveUsers(queryParams).then(function (usersResult) {
            var usersList = usersResult.items;
            if(usersList.length==1){
                var user = usersResult.items[0];
                if(user.userGroup.code=="00003"){//配送工
                    $scope.vm.selectReason = "4";
                }else if(user.userGroup.code=="00007"){//调拨员
                    $scope.vm.selectReason = "3";
                }else{
                    $scope.vm.selectReason = "-1";
                }
            }
            //$scope.close(true);
        }, function(value) {
            udcModal.info({"title": "计算移交原因失败", "message": value.message});
            $scope.vm.selectReason = {name:"未知",value:"-1"};
        })

    };

}]);