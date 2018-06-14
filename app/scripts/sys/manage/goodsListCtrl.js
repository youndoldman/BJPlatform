'use strict';

manageApp.controller('GoodsListCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'GoodsService','Upload','URI', function ($scope, $rootScope, $filter, $location, Constants,
                                                                 rootService, pager, udcModal, GoodsService,Upload,URI) {

        $(function () {
            $('#datetimepickerStart').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            $('#datetimepickerEnd').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            //优惠计划
            $('#couponPickerStart').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            $('#couponPickerEnd').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
            });

        });
        $(function () {
            $('#datetimepickerStart').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.adjustStartTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#datetimepickerEnd').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.adjustEndTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });

            $('#couponPickerStart').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.couponStartTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#couponPickerEnd').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.couponEndTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
        });


        var gotoPageGoodsType = function (pageNo) {
            $scope.pagerGoodsTypes.setCurPageNo(pageNo);
            searchGoodsTypes();
        };
        var gotoPageGoods = function (pageNo) {
            $scope.pagerGoods.setCurPageNo(pageNo);
            searchGoods();
        };

        var gotoPageGoodsPriceAdjustment = function (pageNo) {
            $scope.pagerGoodsPriceAdjustment.setCurPageNo(pageNo);
            $scope.searchGoodsPriceAdjustment();
        };

        var gotoPageCouponAdjustment = function (pageNo) {
            $scope.pagerCouponAdjustment.setCurPageNo(pageNo);
            $scope.searchCouponAdjustment();
        };

        $scope.pagerGoodsTypes = pager.init('GoodsListCtrl', gotoPageGoodsType);

        $scope.pagerGoods = pager.init('GoodsListCtrl', gotoPageGoods);

        $scope.pagerGoodsPriceAdjustment = pager.init('GoodsListCtrl', gotoPageGoodsPriceAdjustment);

        $scope.pagerCouponAdjustment = pager.init('GoodsListCtrl', gotoPageCouponAdjustment);

        var historyQ1 = $scope.pagerGoodsTypes.getQ();
        var historyQ2 = $scope.pagerGoods.getQ();
        var historyQ3 = $scope.pagerGoodsPriceAdjustment.getQ();
        var historyQ4 = $scope.pagerCouponAdjustment.getQ();

        $scope.q = {
            goodsType:"",
            goodsName:"",
            adjustName:null,     //调价计划名称
            adjustStartTime:null,//调价计划开始时间
            adjustEndTime:null,   //调价计划结束时间

            couponName:null,     //优惠计划名称
            couponStartTime:null,//优惠计划开始时间
            couponEndTime:null,   //优惠计划结束时间
            discountType:null,
            useType:null,
            status:null,

        };

        $scope.config = {
            goodsStatusList:[{name:"正常上市",value:"0"},{name:"暂停销售",value:"1"},{name:"商品下架",value:"2"}]
        };


        $scope.vm = {
            goodsTypesList: [],
            goodsList: [],
            goodsPriceAdjustmentList:[],

            couponAdjustmentList:[],
            discountTypes:["所有类型","直减", "百分比折扣"],
            useTypes:["所有类型","排他型", "叠加型"],
            status:["所有状态","待生效","已生效","作废"],
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
                    searchGoods();
                }
            })
        };


        $scope.initPopUpGoodsPriceAdjustment = function () {
            udcModal.show({
                templateUrl: "./manage/goodsPriceAdjustmentModal.htm",
                controller: "GoodsPriceAdjustmentModalCtrl",
                inputs: {
                    title: '新增调价策略',
                    initVal: {}
                }
            }).then(function (result) {
                if (result) {
                    $scope.searchGoodsPriceAdjustment();
                }
            })
        };

        $scope.initPopUpCouponAdjustment = function () {
            udcModal.show({
                templateUrl: "./manage/couponAdjustmentModal.html",
                controller: "couponAdjustmentModalCtrl",
                inputs: {
                    title: '新增优惠策略',
                    initVal: {}
                }
            }).then(function (result) {
                if (result) {
                    $scope.searchCouponAdjustment();
                }
            })
        };

        //修改调价策略
        $scope.modifyGoodsPriceAdjustment = function (goodsPriceAdjustment) {
            udcModal.show({
                templateUrl: "./manage/goodsPriceAdjustmentModal.htm",
                controller:  "GoodsPriceAdjustmentModalCtrl",
                inputs: {
                    title: '修改调价策略',
                    initVal: goodsPriceAdjustment
                }
            }).then(function (result) {
                if (result) {
                    $scope.searchGoodsPriceAdjustment();
                }
            })
        };

        //删除调价策略
        $scope.deleteGoodsPriceAdjustment = function (goodsPriceAdjustment) {
            udcModal.confirm({"title": "删除调价策略", "message": "是否永久删除调价策略 " + goodsPriceAdjustment.name})
                .then(function (result) {
                    if (result) {
                        GoodsService.deleteAdjustPriceSchedules(goodsPriceAdjustment).then(function () {
                            udcModal.info({"title": "处理结果", "message": "删除调价策略成功 "});
                            $scope.searchGoodsPriceAdjustment();
                        }, function(value) {
                            udcModal.info({"title": "处理结果", "message": "删除调价策略失败 "+value.message});
                        })
                    }
                })
        };

        //修改优惠策略
        $scope.modifyCouponAdjustment = function (couponAdjustment) {
            console.info(couponAdjustment);
            udcModal.show({
                templateUrl: "./manage/couponAdjustmentModal.html",
                controller:  "couponAdjustmentModalCtrl",
                inputs: {
                    title: '修改优惠策略',
                    initVal: couponAdjustment
                }
            }).then(function (result) {
                if (result) {
                    $scope.searchCouponAdjustment();
                }
            })
        };

        //删除优惠策略
        $scope.deleteCouponAdjustment = function (couponAdjustment) {
            udcModal.confirm({"title": "删除优惠策略", "message": "是否永久删除优惠策略 " + couponAdjustment.name})
                .then(function (result) {
                    if (result) {
                        GoodsService.deleteDiscountStrategies(couponAdjustment).then(function () {
                            udcModal.info({"title": "处理结果", "message": "删除优惠策略成功 "});
                            $scope.searchCouponAdjustment();
                        }, function(value) {
                            udcModal.info({"title": "处理结果", "message": "删除优惠策略失败 "+value.message});
                        })
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
                        }, function (value) {
                            udcModal.info({"title": "处理结果", "message": "删除商品信息失败 " + value.message});
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
                        }, function (value) {
                            udcModal.info({"title": "处理结果", "message": "删除商品类型失败 " + value.message});
                        });
                    }
                })
        };



        var searchGoods = function () {
            //console.log($scope.q.goodsName)
            var queryParams = {
                name: $scope.q.goodsName,
                pageNo: $scope.pagerGoods.getCurPageNo(),
                pageSize: $scope.pagerGoods.pageSize
            };

            GoodsService.retrieveGoods(queryParams).then(function (goods) {
                $scope.pagerGoods.update($scope.q, goods.total, queryParams.pageNo);
                $scope.vm.goodsList = _.map(goods.items, GoodsService.toViewModel);
                //console.log($scope.vm.goodsList);
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

        //查询调价记录
        $scope.searchGoodsPriceAdjustment = function () {
            var queryParams = {
                name:$scope.q.adjustName,     //调价计划名称
                startTime:$scope.q.adjustStartTime,//调价计划开始时间
                endTime:$scope.q.adjustEndTime,   //调价计划结束时间

                pageNo: $scope.pagerGoodsPriceAdjustment.getCurPageNo(),
                pageSize: $scope.pagerGoodsPriceAdjustment.pageSize
            };
            GoodsService.retrieveAdjustPriceSchedules(queryParams).then(function (goodsPriceAdjustmentList) {
                $scope.pagerGoodsPriceAdjustment.update($scope.q, goodsPriceAdjustmentList.total, queryParams.pageNo);
                $scope.vm.goodsPriceAdjustmentList = goodsPriceAdjustmentList.items;

            });
        };
//搜索优惠类型
        $scope.discountTypeChange = function(){
            //console.info($scope.q.discountType)
        }
        //查询优惠记录
        $scope.searchCouponAdjustment = function () {
            var queryParams = {
                name:$scope.q.couponName,     //优惠计划名称
                status:$scope.q.status,
                discountType:$scope.q.discountType,
                useType:$scope.q.useType,
                startTime:$scope.q.couponStartTime,//调价优惠开始时间
                endTime:$scope.q.couponEndTime,   //调价优惠结束时间
                pageNo: $scope.pagerCouponAdjustment.getCurPageNo(),
                pageSize: $scope.pagerCouponAdjustment.pageSize
            };
            if($scope.q.discountType == "所有类型")
            {
                queryParams.discountType = null;
            }
            else if($scope.q.discountType == "直减")
            {
                queryParams.discountType = 0;
            }
            else if($scope.q.discountType == "百分比折扣")
            {
                queryParams.discountType = 1;
            }

            if($scope.q.useType == "所有类型")
            {
                queryParams.useType = null;
            }
            else if($scope.q.useType == "排他型")
            {
                queryParams.useType = 0;
            }
            else if($scope.q.useType == "叠加型")
            {
                queryParams.useType = 1;
            }

           if($scope.q.status == "所有状态")
            {
                queryParams.status = null;
            }
            else if($scope.q.status == "待生效")
            {
                queryParams.status = 0;
            }
            else if($scope.q.status == "已生效")
            {
                queryParams.status = 1;
            }
            else if($scope.q.status == "作废")
            {
                queryParams.status = 2;
            }
            //console.info(queryParams);
            GoodsService.retrieveDiscountStrategies(queryParams).then(function (couponAdjustmentList) {
                //console.info(couponAdjustmentList);
                $scope.pagerCouponAdjustment.update($scope.q, couponAdjustmentList.total, queryParams.pageNo);
                $scope.vm.couponAdjustmentList = couponAdjustmentList.items;
                console.info($scope.vm.couponAdjustmentList);
            });
        };


        var init = function () {
            $scope.pagerGoodsTypes.setCurPageNo(0);
            $scope.pagerGoods.setCurPageNo(0);
            $scope.pagerGoodsPriceAdjustment.setCurPageNo(0);
            $scope.pagerCouponAdjustment.setCurPageNo(0);

            $scope.q.discountType = "所有类型";
            $scope.q.useType = "所有类型";
            $scope.q.status = "所有状态";

            $scope.pagerCouponAdjustment.pageSize=5;
            $scope.pagerGoodsTypes.pageSize=5;
            $scope.pagerGoodsPriceAdjustment.pageSize=5;
            $scope.pagerGoods.pageSize=5;
            searchGoodsTypes();
            searchGoods();
            $scope.searchGoodsPriceAdjustment();
            $scope.searchCouponAdjustment();

        };

        init();

    }]);
