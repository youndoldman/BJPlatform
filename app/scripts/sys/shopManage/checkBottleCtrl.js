/**
 * Created by Administrator on 2018/5/2.
 */

'use strict';

shopManageApp.controller('CheckBottleCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'ShopStockService','sessionStorage', function ($scope, $rootScope, $filter, $location, Constants,

                                                                                       rootService, pager, udcModal, ShopStockService, sessionStorage) {

        $scope.currentUser = {};

        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            $scope.searchCredit();
        };

        $scope.pager = pager.init('MoneyReturnCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            amount:null,
            gasCyrSpecCode:null,
            gasCyrDynOperType:null,
            note:null,
        };


        $scope.data = {
            gasCyrSpecCode1:["所有类型"],
            gasCyrDynOperType:["领用","送检","收取钢检瓶","退维修瓶","退报废瓶","押瓶","退押金瓶"],
            gasCyrDynOperType1:["所有类型","领用","送检","收取钢检瓶","退维修瓶","退报废瓶","押瓶","退押金瓶"],

            bottleList:[],

            gasCylinderSpecList:[],
            selectedGoodsType:{},

            gasCyrSpecCode:null,
            operType:null,
        }

        $scope.gasCylinderSpecChange = function () {
            //console.info($scope.data.selectedGoodsType);
            $scope.q.gasCyrSpecCode = $scope.data.selectedGoodsType.code;
            console.info($scope.q.gasCyrSpecCode);
        }

        $scope.gasCyrDynOperType = null;

        $scope.gasCyrDynOperTypeChange = function () {

            if($scope.gasCyrDynOperType == "领用")
            {
                $scope.q.gasCyrDynOperType = 0;
            }
            else if($scope.gasCyrDynOperType == "送检")
            {
                $scope.q.gasCyrDynOperType = 1;
            }
            else if($scope.gasCyrDynOperType == "收取钢检瓶")
            {
                $scope.q.gasCyrDynOperType = 2;
            }
            else if($scope.gasCyrDynOperType == "退维修瓶")
            {
                $scope.q.gasCyrDynOperType = 3;
            }
            else if($scope.gasCyrDynOperType == "退报废瓶")
            {
                $scope.q.gasCyrDynOperType = 4;
            }
            else if($scope.gasCyrDynOperType == "押瓶")
            {
                $scope.q.gasCyrDynOperType = 5;
            }
            else if($scope.gasCyrDynOperType == "退押金瓶")
            {
                $scope.q.gasCyrDynOperType = 6;
            }
            console.info($scope.q.gasCyrDynOperType);
}

        $scope.saveBottle = function(returnMoney){
            var queryParams = {
                operUserId:$scope.currentUser.userId,
                gasCyrSpecCode:$scope.q.gasCyrSpecCode,
                gasCyrDynOperType:$scope.q.gasCyrDynOperType,
                amount:$scope.q.amount,
                note:$scope.q.note,
            };

            console.info(queryParams);
            ShopStockService.addGasCylinderSpecUri(queryParams).then(function () {
                udcModal.info({"title": "处理结果", "message": "钢检瓶录入成功 "});

            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "钢检瓶录入失败 "+value.message});
            })

        }

        $scope.searchBottle = function () {
            console.info("click button");
            var queryParams = {
                departmentCode: $scope.currentUser.department.code,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize,
            };
            if($scope.data.operType == "所有类型")
            {
                queryParams.operType = null;
            }
            else if($scope.data.operType == "领用")
            {
                queryParams.operType = 0;
            }
            else if($scope.data.operType == "送检")
            {
                queryParams.operType = 1;
            }
            else if($scope.data.operType == "收取钢检瓶")
            {
                queryParams.operType = 2;
            }
            else if($scope.data.operType == "退维修瓶")
            {
                queryParams.operType = 3;
            }
            else if($scope.data.operType == "退报废瓶")
            {
                queryParams.operType = 4;
            }
            else if($scope.data.operType == "押瓶")
            {
                queryParams.operType = 5;
            }
            else if($scope.data.operType == "退押金瓶")
            {
                queryParams.operType = 6;
            }

            if($scope.data.gasCyrSpecCode == "所有类型")
            {
                queryParams.gasCyrSpecCode = null;
            }
            else
            {
                for(var i = 0; i <　$scope.data.gasCylinderSpecList.length; i ++){
                    if($scope.data.gasCyrSpecCode == $scope.data.gasCylinderSpecList[i].name)
                    {
                        queryParams.gasCyrSpecCode = $scope.data.gasCylinderSpecList[i].code;
                    }
                }
            }

            console.info("钢检瓶录入查询");
            console.info(queryParams);
            ShopStockService.searchGasCylinder(queryParams).then(function (detail) {
                $scope.pager.update($scope.q, detail.total, queryParams.pageNo);
                $scope.data.bottleList = detail.items;
                console.info($scope.data.bottleList );
            });
        };

        var init = function () {
            $scope.currentUser = sessionStorage.getCurUser();
            console.info($scope.currentUser);

            $scope.gasCyrDynOperType = "领用";
            $scope.q.gasCyrDynOperType = 0;

            $scope.data.gasCyrSpecCode = "所有类型";
            $scope.data.operType = "所有类型";

            $scope.searchBottle();

            //查询钢瓶类型规格
            var queryParams = {};
            ShopStockService.retrieveGasCylinderSpecUri(queryParams).then(function (GasCylinderSpec) {
                $scope.data.gasCylinderSpecList = GasCylinderSpec.items;
                //$scope.data.selectedGoodsType = $scope.data.goodsTypesList[0];
                console.info($scope.data.gasCylinderSpecList);
                $scope.data.selectedGoodsType = $scope.data.gasCylinderSpecList[0];
                $scope.q.gasCyrSpecCode = $scope.data.selectedGoodsType.code;

                for(var i = 0; i <　$scope.data.gasCylinderSpecList.length; i ++){
                    $scope.data.gasCyrSpecCode1[i+1] = $scope.data.gasCylinderSpecList[i].name;
                }
            });


            //console.info($scope.data.gasCyrSpecCode1)
        };

        init();

    }]);