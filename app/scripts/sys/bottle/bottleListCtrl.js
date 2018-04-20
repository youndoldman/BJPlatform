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
            number: null,
            liableUserId: null,
            liableDepartmentCode: null
        };

        $scope.vm = {
            bottleList: []
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchBottles();
        };

        $scope.initDepartmentSelect = function () {
            udcModal.show({
                templateUrl: "./bottle/departmentSelectModal.htm",
                controller: "DepartmentSelectModalCtrl",
                inputs: {
                    title: '百江燃气组织架构',
                    initVal: {}
                }
            }).then(function (result) {
                if (result!=null) {
                    $scope.q.liableDepartmentCode = result.code;
                    //console.info($scope.q.liableDepartmentCode);
                    searchBottles();
                }
            })
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

        $scope.delete = function (bottle) {
            udcModal.confirm({"title": "删除钢瓶", "message": "是否永久删除钢瓶信息 " + bottle.number})
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
                number: $scope.q.number,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
                liableUserId: $scope.q.liableUserId,
                liableDepartmentCode: $scope.q.liableDepartmentCode
            };
            BottleService.retrieveBottles(queryParams).then(function (bottles) {
                $scope.pager.update($scope.q, bottles.total, queryParams.pageNo);
                $scope.vm.bottleList = bottles.items;
                console.log($scope.vm.bottleList);
            });


        };



        var init = function () {
            //查询钢瓶
            searchBottles();
        };

        init();

    }]);

bottleApp.controller('BottleModalCtrl', ['$scope', 'close', 'BottleService', 'title', 'initVal', 'udcModal',function ($scope, close, BottleService, title, initVal, udcModal) {
    //时间空间
    $scope.$watch('$viewContentLoaded',function () {
        $(function () {
            $('#datetimepickerProductionDate').datetimepicker({
                format: 'YYYY-MM-DD',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',

            });
            $('#datetimepickerVerifyDate').datetimepicker({
                format: 'YYYY-MM-DD',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',

            });
            $('#datetimepickerNextVerifyDate').datetimepicker({
                format: 'YYYY-MM-DD',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',

            });
            $('#datetimepickerScrapDate').datetimepicker({
                format: 'YYYY-MM-DD',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',

            });
        });
        $(function () {
            $('#datetimepickerProductionDate').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.vm.bottle.productionDate = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#datetimepickerVerifyDate').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.vm.bottle.verifyDate = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#datetimepickerNextVerifyDate').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.vm.bottle.nextVerifyDate = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#datetimepickerScrapDate').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.vm.bottle.scrapDate = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
        });
    });




    $scope.modalTitle = title;
    $scope.isModify = false;
    $scope.isBinded = false;//是否绑定定位终端
    $scope.vm = {
        bottle: {},
        config:{
            lifeStatus:[{value:0,name:"未启用"},
                {value:1,name:"已启用"},
                {value:2,name:"已停用"},
                {value:3,name:"已作废"}],
            serviceStatus:[{value:0,name:"待使用"},
                {value:1,name:"气站库存"},
                {value:2,name:"门店库存"},
                {value:3,name:"在途运输"},
                {value:4,name:"在途派送"},
                {value:5,name:"客户使用"},
                {value:6,name:"空瓶回收"}],
            specs:[]
        }
    };

    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (bottle) {
        if (bottle.name != "" && title == "新增钢瓶") {
            BottleService.createBottle(bottle).then(function () {
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "错误信息", "message": value.message});
            })
        } else if (bottle.name != "" && title == "修改钢瓶") {
            //去除定位终端字段，才能提交
            bottle.locationDevice = null;
            BottleService.modifyBottle(bottle).then(function () {
                udcModal.info({"title": "处理结果", "message": "修改钢瓶信息成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "错误信息", "message": value.message});
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

            }
        })
        if($scope.isModify) {
            //钢瓶状态格式化
            $scope.vm.bottle.lifeStatus = $scope.vm.bottle.lifeStatus.index;
            $scope.vm.bottle.serviceStatus = $scope.vm.bottle.serviceStatus.index;
        } else{
            $scope.vm.bottle.lifeStatus = $scope.vm.config.lifeStatus[1].value;
            $scope.vm.bottle.serviceStatus = $scope.vm.config.serviceStatus[0].value;
        }

    };
    //解除绑定
    $scope.unBind = function () {
        BottleService.unBindBottle($scope.vm.bottle, $scope.vm.bottle.locationDevice.number).then(function () {
            $scope.vm.bottle.locationDevice = null;
            $scope.isBinded = false;
            udcModal.info({"title": "处理结果", "message": "解除绑定成功 "});
        }, function(value) {
            udcModal.info({"title": "错误信息", "message": "解除绑定失败"});
        })
    };
    //绑定
    $scope.bind = function () {
        BottleService.bindBottle($scope.vm.bottle, $scope.vm.bottle.locationDevice.number).then(function () {
            $scope.isBinded = true;
            udcModal.info({"title": "处理结果", "message": "绑定成功 "});
        }, function(value) {
            udcModal.info({"title": "错误信息", "message": "绑定失败"});
        })
    };

    init();
}]);