'use strict';

bottleApp.controller('BottleMapCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'BottleService', 'MapService', '$timeout',function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal, BottleService, MapService, $timeout) {

       //地图初始化
        $scope.map = null;
        var infoWindow = new AMap.InfoWindow();
        $timeout(function() {
            mapInitial();
            destNationInitial();

        });
        //地图初始化
        var mapInitial = function() {
            var address = "云南丰元大酒店";
            console.log(address);
            MapService.retrieveLocation(address).then(function (value) {
                var location = value.geocodes[0].location;
                var Longitude = parseFloat(location.split(',')[0]);
                var Latitude = parseFloat(location.split(',')[1]);
                $scope.map = new AMap.Map('mapContainer', {
                    center: [Longitude, Latitude],
                    zoom: 15
                });
                AMap.plugin(['AMap.ToolBar','AMap.Scale','AMap.OverView'],
                    function(){
                        $scope.map.addControl(new AMap.ToolBar());

                        $scope.map.addControl(new AMap.Scale());

                        //$scope.map.addControl(new AMap.OverView({isOpen:true}));
                    });
            })



        };
        //目的地址初始化
        var destNationInitial = function() {
            var iconDest = new AMap.Icon({
                image : '../../images/icon/home.ico',//24px*24px
                //icon可缺省，缺省时为默认的蓝色水滴图标，
                size : new AMap.Size(50,50),
                imageSize : new AMap.Size(50,50)
            });
            //var markerDest = new AMap.Marker({
            //    icon : iconDest,//24px*24px
            //    position : [$scope.vm.currentOrder.recvLongitude, $scope.vm.currentOrder.recvLatitude],
            //    offset : new AMap.Pixel(0,0),
            //    map : $scope.map
            //});
            //markerDest.content = $scope.vm.currentOrder.recvAddr.province+$scope.vm.currentOrder.recvAddr.city
            //    +$scope.vm.currentOrder.recvAddr.county+$scope.vm.currentOrder.recvAddr.detail;
            ////给Marker绑定单击事件
            markerDest.on('click', markerDestClick);
        };

        var markerDestClick = function (e) {
            infoWindow.setContent(e.target.content);
            infoWindow.open($scope.map, e.target.getPosition());
        };

        var markerWorkersClick = function (e) {
            //var displayInfo = e.target.content.name+"   "+e.target.content.mobilePhone;
            //infoWindow.setContent(displayInfo);
            //infoWindow.open($scope.map, e.target.getPosition());
            ////将这个配送员信息更新至表单
            //for (var i=0;i<$scope.vm.onLineWorkersList.length;i++) {
            //    if(e.target.content.userId == $scope.vm.onLineWorkersList[i].userId){
            //        $scope.vm.selectedWorker =  _.clone($scope.vm.onLineWorkersList[i]);
            //        break;
            //    }
            //}
            //console.log($scope.vm.selectedWorker);

        };
        //========================
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchBottles();
        };
        $scope.location = function(lon,lan)
        {
            console.log(lon+"------"+lan);
            BottleService.location(lon, lan);
        }
        $scope.pager = pager.init('BottleListCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            bottleName: historyQ.bottleName || ""
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
                    title: '修改钢瓶信息',
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
                name: $scope.q.bottleName,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            BottleService.retrieveBottles(queryParams).then(function (bottles) {
                $scope.pager.update($scope.q, bottles.total, queryParams.pageNo);
                $scope.vm.bottleList = _.map(bottles.items, BottleService.toViewModel);
            });
        };




        var init = function () {
            //searchBottles();
        };

        init();

    }]);

bottleApp.controller('BottleModalCtrl', ['$scope', 'close', 'BottleService', 'title', 'initVal', function ($scope, close, BottleService, title, initVal) {
    $scope.modalTitle = title;
    $scope.vm = {
        bottle: {}
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
            BottleService.modifyBottle(bottle).then(function () {
                $scope.close(true);
            })
        }
    };

    var init = function () {
        $scope.vm.bottle = _.clone(initVal)
    };

    init();
}]);