'use strict';

bottleApp.controller('BottleMapCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'BottleService', 'MapService', '$timeout',function ($scope, $rootScope, $filter, $location, Constants,
                                                          rootService, pager, udcModal, BottleService, MapService, $timeout) {

        var drawPath = function(){
            AMapUI.load(['ui/misc/PathSimplifier', 'lib/$'], function(PathSimplifier, $) {

                if (!PathSimplifier.supportCanvas) {
                    alert('当前环境不支持 Canvas！');
                    return;
                }

                //just some colors
                var colors = [
                    "#3366cc", "#dc3912", "#ff9900", "#109618", "#990099", "#0099c6", "#dd4477", "#66aa00",
                    "#b82e2e", "#316395", "#994499", "#22aa99", "#aaaa11", "#6633cc", "#e67300", "#8b0707",
                    "#651067", "#329262", "#5574a6", "#3b3eac"
                ];
                console.log("test-path.json");
                var pathSimplifierIns = new PathSimplifier({
                    zIndex: 100,
                    //autoSetFitView:false,
                    map: $scope.map, //所属的地图实例

                    getPath: function(pathData, pathIndex) {

                        return pathData.path;
                    },
                    getHoverTitle: function(pathData, pathIndex, pointIndex) {

                        if (pointIndex >= 0) {
                            //point
                            return pathData.name + '，点：' + pointIndex + '/' + pathData.path.length;
                        }

                        return pathData.name + '，点数量' + pathData.path.length;
                    },
                    renderOptions: {
                        pathLineStyle: {
                            dirArrowStyle: true
                        },
                        getPathStyle: function(pathItem, zoom) {

                            var color = colors[pathItem.pathIndex % colors.length],
                                lineWidth = Math.round(4 * Math.pow(1.1, zoom - 3));

                            return {
                                pathLineStyle: {
                                    strokeStyle: color,
                                    lineWidth: lineWidth
                                },
                                pathLineSelectedStyle: {
                                    lineWidth: lineWidth + 2
                                },
                                pathNavigatorStyle: {
                                    fillStyle: color
                                }
                            };
                        }
                    }
                });
                window.pathSimplifierIns = pathSimplifierIns;

                $('<div id="loadingTip">加载数据，请稍候...</div>').appendTo(document.body);

                console.log("test-path.json");
                $.getJSON('../images/test-data/test-path.json', function(d) {
                    $('#loadingTip').remove();

                    var flyRoutes = [];

                    //for (var i = 0, len = d.length; i < len; i++) {
                    //
                    //    if (d[i].name.indexOf('乌鲁木齐') >= 0) {
                    //
                    //        d.splice(i, 0, {
                    //            name: '飞行 - ' + d[i].name,
                    //            path: PathSimplifier.getGeodesicPath(
                    //                d[i].path[0], d[i].path[d[i].path.length - 1], 1000)
                    //        });
                    //
                    //        i++;
                    //        len++;
                    //    }
                    //}

                    d.push.apply(d, flyRoutes);

                    pathSimplifierIns.setData(d);

                    //initRoutesContainer(d);

                    function onload() {
                        pathSimplifierIns.renderLater();
                    }

                    function onerror(e) {
                        alert('图片加载失败！');
                    }


                    var navg1 = pathSimplifierIns.createPathNavigator(0, {
                        loop: true,
                        speed: 100000,
                        pathNavigatorStyle: {
                            width: 16,
                            height: 32,
                            //使用图片
                            content: PathSimplifier.Render.Canvas.getImageContent('../images/icon/car.png', onload, onerror),
                            strokeStyle: null,
                            fillStyle: null,
                            //经过路径的样式
                            pathLinePassedStyle: {
                                lineWidth: 6,
                                strokeStyle: 'black',
                                dirArrowStyle: {
                                    stepSpace: 15,
                                    strokeStyle: 'red'
                                }
                            }
                        }
                    });

                    navg1.start();
                });
            });
        };

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

                var style = [{
                    url: 'http://a.amap.com/jsapi_demos/static/images/mass0.png',
                    anchor: new AMap.Pixel(6, 6),
                    size: new AMap.Size(11, 11)
                },{
                    url: 'http://a.amap.com/jsapi_demos/static/images/mass1.png',
                    anchor: new AMap.Pixel(4, 4),
                    size: new AMap.Size(7, 7)
                },{
                    url: '../../images/icon/bottom.ico',
                    anchor: new AMap.Pixel(3, 3),
                    size: new AMap.Size(20, 20)
                }
                ];

                var mass = new AMap.MassMarks(citys, {
                    opacity:0.8,
                    zIndex: 111,
                    cursor:'pointer',
                    style:style
                });
                var marker = new AMap.Marker({content:' ',map:$scope.map})
                mass.on('mouseover',function(e){
                    marker.setPosition(e.data.lnglat);
                    marker.setLabel({content:e.data.name})
                })
                mass.setMap($scope.map);
                var setStyle = function(multiIcon) {
                    if(multiIcon){
                        mass.setStyle(style);
                    }else{
                        mass.setStyle(style[2]);
                    }
                }
                drawPath();
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
            //markerDest.on('click', markerDestClick);
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
