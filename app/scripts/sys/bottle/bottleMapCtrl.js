'use strict';

bottleApp.controller('BottleMapCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'BottleService', 'MapService', '$timeout',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                            rootService, pager, udcModal, BottleService, MapService, $timeout) {

        $scope.pathSimplifierIns = null;
        $scope.PathSimplifier = null;
        //地图初始化
        $scope.map = null;
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
        });
        $(function () {
            function p(s) {
                return s < 10 ? '0' + s: s;
            }

            $('#datetimepickerStart').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.startTime = date.getFullYear()+"-"+p(month)+"-"+p(date.getDate())+" "
                        +p(date.getHours())+":"+p(date.getMinutes())+":"+p(date.getSeconds());
                });
            $('#datetimepickerEnd').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.endTime = date.getFullYear()+"-"+p(month)+"-"+p(date.getDate())+" "
                        +p(date.getHours())+":"+p(date.getMinutes())+":"+p(date.getSeconds());
                });
        });

        var pathSimplifierInitial = function(){
            AMapUI.load(['ui/misc/PathSimplifier', 'lib/$'], function(PathSimplifier, $) {

                if (!PathSimplifier.supportCanvas) {
                    alert('当前环境不支持 Canvas！');
                    return;
                }
                $scope.PathSimplifier = PathSimplifier;
                //just some colors
                var colors = [
                    "#3366cc", "#dc3912", "#ff9900", "#109618", "#990099", "#0099c6", "#dd4477", "#66aa00",
                    "#b82e2e", "#316395", "#994499", "#22aa99", "#aaaa11", "#6633cc", "#e67300", "#8b0707",
                    "#651067", "#329262", "#5574a6", "#3b3eac"
                ];
                $scope.pathSimplifierIns = new PathSimplifier({
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
                                lineWidth = Math.round(2 * Math.pow(1.1, zoom - 3));

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

                console.log($scope.pathSimplifierIns);
                window.pathSimplifierIns = $scope.pathSimplifierIns;

                //$('<div id="loadingTip">加载数据，请稍候...</div>').appendTo(document.body);
            });
        };
        //标注钢瓶的历史操作记录
        var markHistory = function(){
            $scope.map.clearMap();
            console.log($scope.vm.historyList);

            for(var i = 0, marker; i < $scope.vm.historyList.length; i++){
                var contents = $scope.vm.historyList[i].srcUser+"->"+
                    $scope.vm.historyList[i].destUser+"--"+$scope.vm.historyList[i].detail+"--"+
                    $scope.vm.historyList[i].createTime;

                marker=new AMap.Marker({
                    position:[$scope.vm.historyList[i].longititude, $scope.vm.historyList[i].latitude],
                    map:$scope.map
                });

                marker.setLabel({
                    offset: new AMap.Pixel(0, 30*(i%10)),
                    content: contents
                });
            }
            $scope.map.setFitView();

        };
        //绘制轨迹
        var drawPath = function(pathDataFileName){
            $scope.pathSimplifierIns.clearPathNavigators();



            var paths = [];
            paths.push($scope.vm.selectedBottlePath);
                $('#loadingTip').remove();
                $scope.pathSimplifierIns.setData(paths);
                function onload() {
                    $scope.pathSimplifierIns.renderLater();
                }
                function onerror(e) {
                    alert('图片加载失败！');
                }
                var navg1 = $scope.pathSimplifierIns.createPathNavigator(0, {
                    loop: true,
                    speed: 1000,
                    pathNavigatorStyle: {
                        width: 16,
                        height: 32,
                        //使用图片
                        content: $scope.PathSimplifier.Render.Canvas.getImageContent('../images/icon/car.png', onload, onerror),
                        strokeStyle: null,
                        fillStyle: null,
                        //经过路径的样式
                        //pathLinePassedStyle: {
                        //    lineWidth: 6,
                        //    strokeStyle: 'black',
                        //    dirArrowStyle: {
                        //        stepSpace: 15,
                        //        strokeStyle: 'red'
                        //    }
                        //}
                    }
                });
                navg1.start();


        };


        var infoWindow = new AMap.InfoWindow();
        $scope.$watch('$viewContentLoaded',function () {
            mapInitial();
            pathSimplifierInitial(); //轨迹绘制引擎初始化
        });
        //地图初始化
        var mapInitial = function() {
            $scope.map = new AMap.Map('mapContainer', {
                center: [102.7278090000, 25.1333240000],
                zoom: 15
            });
            AMap.plugin(['AMap.ToolBar','AMap.Scale','AMap.OverView'],
                function(){
                    $scope.map.addControl(new AMap.ToolBar());

                    $scope.map.addControl(new AMap.Scale());

                    //$scope.map.addControl(new AMap.OverView({isOpen:true}));
                });

            //var style = [{
            //    url: 'http://a.amap.com/jsapi_demos/static/images/mass0.png',
            //    anchor: new AMap.Pixel(6, 6),
            //    size: new AMap.Size(11, 11)
            //},{
            //    url: 'http://a.amap.com/jsapi_demos/static/images/mass1.png',
            //    anchor: new AMap.Pixel(4, 4),
            //    size: new AMap.Size(7, 7)
            //},{
            //    url: '../../images/icon/bottom.ico',
            //    anchor: new AMap.Pixel(3, 3),
            //    size: new AMap.Size(20, 20)
            //}
            //];

            //var mass = new AMap.MassMarks(citys, {
            //    opacity:0.8,
            //    zIndex: 111,
            //    cursor:'pointer',
            //    style:style
            //});
            //var marker = new AMap.Marker({content:' ',map:$scope.map})
            //mass.on('mouseover',function(e){
            //    marker.setPosition(e.data.lnglat);
            //    marker.setLabel({content:e.data.name})
            //})
            //mass.setMap($scope.map);
            //var setStyle = function(multiIcon) {
            //    if(multiIcon){
            //        mass.setStyle(style);
            //    }else{
            //        mass.setStyle(style[2]);
            //    }
            //}


        };
        //关键地标标注初始化
        var importantMarksInitial = function() {
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

        $scope.location = function(lon,lan)
        {
            BottleService.location(lon, lan);
        };
        var gotoPageBottles = function (pageNo) {
            $scope.pagerBottle.setCurPageNo(pageNo);
            searchBottles();
        };

        $scope.pagerBottle = pager.init('BottleListCtrl', gotoPageBottles);

        var gotoPageOpsLog = function (pageNo) {
            $scope.pagerOpsLog.setCurPageNo(pageNo);
            searchOpsLog();
        };

        $scope.pagerOpsLog = pager.init('BottleListCtrl', gotoPageOpsLog);


        $scope.q = {
            bottleCode: null,
            selectBottleCode: null,
            startTime:"",
            endTime:"",
        };

        $scope.vm = {
            bottleList: [],
            historyList:[],//钢瓶交接记录
            historyListSplitByPage:[],//分页显示的交接记录
            selectedBottlePath:{"name":null, "path":[]}//钢瓶轨迹
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
                number: $scope.q.bottleCode,
                pageNo: $scope.pagerBottle.getCurPageNo(),
                pageSize: $scope.pagerBottle.pageSize
            };

            BottleService.retrieveBottles(queryParams).then(function (bottles) {
                $scope.pagerBottle.update($scope.q, bottles.total, queryParams.pageNo);
                $scope.vm.bottleList = bottles.items;
            });
        };

        //查询钢瓶的生命历程
        var searchOpsLog = function () {
            $scope.vm.historyListSplitByPage = [];
            var queryParams = {
                startTime: $scope.q.startTime,
                endTime: $scope.q.endTime,
                pageNo: $scope.pagerOpsLog.getCurPageNo(),
                pageSize: $scope.pagerOpsLog.pageSize
            };

            BottleService.retrieveGasCylinderTakeOverHistory($scope.q.selectBottleCode, queryParams).then(function (historys) {
                for (var i = 0; i < historys.items.length; i++) {
                    var tempHistory = {
                        srcUser: "",
                        destUser: "",
                        detail: "",
                        createTime: "",
                        longititude: "",
                        latitude: ""
                    };
                    tempHistory.srcUser = historys.items[i].srcUser.userId + "(" + historys.items[i].srcUser.name + ")";
                    tempHistory.destUser = historys.items[i].targetUser.userId + "(" + historys.items[i].targetUser.name + ")";
                    tempHistory.detail = historys.items[i].note;
                    tempHistory.createTime = historys.items[i].optime;

                    tempHistory.longititude = historys.items[i].longitude;
                    tempHistory.latitude = historys.items[i].latitude;
                    $scope.vm.historyListSplitByPage.push(tempHistory);
                }
            });
        };

        //钢瓶定位
        $scope.location = function (longitude,latitude) {

            $scope.map.clearMap( );
            $scope.pathSimplifierIns.setData([]);
            $scope.pathSimplifierIns.clearPathNavigators();
            var iconBottle = new AMap.Icon({
                image : '../images/icon/bottle.ico',//24px*24px
                //icon可缺省，缺省时为默认的蓝色水滴图标，
                size : new AMap.Size(50,50),
                imageSize : new AMap.Size(50,50)
            });
            var markerDest = new AMap.Marker({
                icon : iconBottle,//24px*24px
                position : [longitude, latitude],
                offset : new AMap.Pixel(0,0),
                map : $scope.map
            });
            markerDest.setAnimation("AMAP_ANIMATION_BOUNCE");
            $scope.map.setCenter([longitude, latitude])
        };

        //钢瓶历史轨迹
        $scope.displayHistory = function (number) {
            $scope.q.selectBottleCode = number;
            //绘制轨迹

            getPath(number);
            //查询变更历史，绘制变更历史
            $scope.getBottleTakeOverHistoryByCode(number, $scope.q.startTime, $scope.q.endTime);
            searchOpsLog();//分页显示交接记录
        };




        var init = function () {
            $scope.pagerBottle.pageSize=5;
            $scope.pagerOpsLog.pageSize=5;
            //初始化时间段
            var curDate = new Date();
            curDate.toDateString;
            var month = curDate.getMonth()+1;
            $scope.q.endTime = curDate.getFullYear()+"-"+month+"-"+curDate.getDate()+" "
                +curDate.getHours()+":"+curDate.getMinutes()+":"+curDate.getSeconds();
            $scope.q.startTime = curDate.getFullYear()+"-"+month+"-"+curDate.getDate()+" "
                +"00"+":"+"00"+":"+"00";
            searchBottles();

        };

        var getPath = function (bottleCode) {
            $scope.vm.selectedBottlePath = {"name":null, "path":[]}//钢瓶轨迹
            $scope.vm.selectedBottlePath.name = "钢瓶号： "+bottleCode;
            $scope.getBottlePathByCode(bottleCode, $scope.q.startTime, $scope.q.endTime);

        };



        //查询钢瓶轨迹
        $scope.getBottlePathByCode = function (number, startTime, endTime) {
            var queryParams = {
                number: number,
                startTime: startTime,
                endTime: endTime
            };
            console.log("getBottlePathByCode:  "+number+"   "+startTime+"   "+endTime);
            BottleService.retrieveGasCylinderPosition(queryParams).then(function (paths) {
                if(paths.total==0){
                    udcModal.info({"title": "提示信息", "message": "无轨迹数据"});
                }

                $scope.vm.selectedBottlePath.total+=paths.total;

                for(var i=0; i<paths.items.length; i++){
                    var location = paths.items[i].location.split(","); //字符分割
                    $scope.vm.selectedBottlePath.path.push(location);
                }
                if(paths.total == 5000){
                    $scope.getBottlePathByCode(paths.items[paths.items.length-1].code, paths.items[paths.items.length-1].createTime, $scope.q.endTime);
                }else{
                    console.log($scope.vm.selectedBottlePath);
                    //数据获取完毕，可以绘制轨迹
                    drawPath();
                }

            });
        };


        //查询钢瓶交接记录
        $scope.getBottleTakeOverHistoryByCode = function (number, startTime, endTime) {
            $scope.vm.historyList = [];
            var queryParams = {
                startTime: startTime,
                endTime: endTime,
            };

            BottleService.retrieveGasCylinderTakeOverHistory(number, queryParams).then(function (historys) {
                if(historys.total==0){
                    udcModal.info({"title": "提示信息", "message": "无交接记录"});
                }
                for(var i=0; i<historys.items.length; i++){
                    var tempHistory = {srcUser:"", destUser:"", detail:"", createTime:"", longititude:"", latitude:""};
                    tempHistory.srcUser = historys.items[i].srcUser.userId+"("+historys.items[i].srcUser.name+")";
                    tempHistory.destUser = historys.items[i].targetUser.userId+"("+historys.items[i].targetUser.name+")";
                    tempHistory.detail = historys.items[i].note;
                    tempHistory.createTime = historys.items[i].optime;

                    tempHistory.longititude = historys.items[i].longitude;
                    tempHistory.latitude = historys.items[i].latitude;
                    $scope.vm.historyList.push(tempHistory);
                }
                //显示记录
                markHistory();
            });
        };
        init();

    }]);
