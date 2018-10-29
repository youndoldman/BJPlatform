'use strict';
bottleApp.controller('BottleWarningModalCtrl', ['$scope', 'close', 'BottleService', 'title', 'initVal', 'udcModal','$timeout',
    'pager', 'MapService',function ($scope, close, BottleService, title, initVal, udcModal,$timeout, pager,　MapService) {
    $scope.modalTitle = title;
    $scope.isModify = false;
    $scope.vm = {
        warning: {},
        historyList:[],//钢瓶交接记录
        historyListSplitByPage:[],//分页显示的交接记录
        selectedBottlePath:{"name":null, "path":[]}//钢瓶轨迹
    };


    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function (warning) {
        var warningDealed = {id:warning.id, note:warning.note, gasCynWarnStatus:2};
    if (warningDealed.note != null && title == "告警处理") {

            BottleService.modifyGasCylinderWarn(warningDealed).then(function () {
                udcModal.info({"title": "处理结果", "message": "告警处理成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "错误信息", "message": value.message});
            })
        }
    };


    var init = function () {
        $scope.pagerOpsLog.pageSize=5;
        $scope.vm.warning = _.clone(initVal);
        if (title == "告警处理"){
            $scope.isModify = true;
        } else {
            $scope.isModify = false;
        }
        console.log($scope.vm.warning);
        $scope.getBottleTakeOverHistoryByCode($scope.vm.warning.gasCylinder.number,null,null);
        searchOpsLog();

    };

    $scope.pathSimplifierIns = null;
    $scope.PathSimplifier = null;
    //地图初始化
    $scope.map = null;

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
                offset: new AMap.Pixel(0, 20*(i%10)),
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
            });

    };


    //========================

    $scope.location = function(lon,lan)
    {
        BottleService.location(lon, lan);
    };


    var gotoPageOpsLog = function (pageNo) {
        $scope.pagerOpsLog.setCurPageNo(pageNo);
        searchOpsLog();
    };

    $scope.pagerOpsLog = pager.init('BottleListCtrl', gotoPageOpsLog);


    $scope.q = {
        bottleCode: null,
        startTime:"",
        endTime:""
    };


    //钢瓶历史轨迹
    $scope.displayHistory = function (number) {
        $scope.q.selectBottleCode = number;

        //查询变更历史，绘制变更历史
        $scope.getBottleTakeOverHistoryByCode(number, $scope.q.startTime, $scope.q.endTime);
        searchOpsLog();//分页显示交接记录
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
            pageNo: 1,
            pageSize: 10,
            orderBy:"id desc"
        };

        BottleService.retrieveGasCylinderTakeOverHistory(number, queryParams).then(function (historys) {
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
            var takeOverHistoryCount = $scope.vm.historyList.length;
            $scope.vm.selectedBottlePath = {"name":null, "path":[]}//钢瓶轨迹
            $scope.vm.selectedBottlePath.name = "钢瓶号： "+$scope.vm.warning.gasCylinder.number;
            $scope.getBottlePathByCode($scope.vm.warning.gasCylinder.number,$scope.vm.historyList[takeOverHistoryCount-1].createTime,$scope.vm.historyList[0].createTime);
        });
    };
        //查询钢瓶的生命历程
        var searchOpsLog = function () {
            $scope.vm.historyListSplitByPage = [];
            var queryParams = {
                startTime: $scope.q.startTime,
                endTime: $scope.q.endTime,
                pageNo: $scope.pagerOpsLog.getCurPageNo(),
                pageSize: $scope.pagerOpsLog.pageSize,
                orderBy:"id desc"
            };

            BottleService.retrieveGasCylinderTakeOverHistory($scope.vm.warning.gasCylinder.number, queryParams).then(function (historys) {
                $scope.pagerOpsLog.update($scope.q, historys.total, queryParams.pageNo);
                for (var i = 0; i < historys.items.length; i++) {
                    var tempHistory = {
                        srcUser: "",
                        destUser: "",
                        detail: "",
                        createTime: "",
                        longititude: "",
                        latitude: ""
                    };
                    tempHistory.id = historys.items[i].id;
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
    init();

}]);