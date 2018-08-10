'use strict';

comprehensiveSituationApp.controller('GisWatchCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'GisWatchService', 'MapService', '$timeout','$interval',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                                          rootService, pager, udcModal, GisWatchService, MapService, $timeout,$interval) {


        //function getLocation(){
        //    if (navigator.geolocation){
        //        navigator.geolocation.getCurrentPosition(showPosition,showError);
        //    }else{
        //        alert("浏览器不支持地理定位。");
        //    }
        //}
        $scope.pathSimplifierIns = null;
        $scope.PathSimplifier = null;
        $scope.mapUserPosition = new Map();

        var infoWindow = new AMap.InfoWindow();
        $scope.$watch('$viewContentLoaded',function () {
            mapInitial();
            pathSimplifierInitial(); //轨迹绘制引擎初始化
            init();//地图加载成功再执行Init
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
        //标记麻点
        var markPeisong = function () {
            var queryParams = {
                groupCode:"00003",
                aliveStatus:1
            };

            GisWatchService.retrieveUsers(queryParams).then(function (users) {
                var usersList = users.items;
                var data = [];
                for(var i=0;i<usersList.length;i++){
                    if(usersList[i].userPosition==null){
                        continue;
                    }
                    var temp = {
                        lnglat: [usersList[i].userPosition.longitude, usersList[i].userPosition.latitude], //点标记位置
                        name: usersList[i].name,
                        id:i
                    };
                    data.push(temp);
                }
                console.log(data);

                var style = [{
                    url: '../images/icon/worker.ico',
                    anchor: new AMap.Pixel(6, 6),
                    size: new AMap.Size(40, 40)
                }];

                var mass = new AMap.MassMarks(data, {
                    opacity:0.8,
                    zIndex: 111,
                    cursor:'pointer',
                    style:style
                });

                //每一个麻点打label
                for(var i=0;i<data.length;i++){
                    var marker = new AMap.Marker({content:' ',map:$scope.map});
                    marker.setPosition(data[i].lnglat);
                    marker.setLabel({content:data[i].name});
                    mass.setMap($scope.map);
                }

            });
        };
        var markDiaobo = function () {
            var queryParams = {
                groupCode:"00007",
                aliveStatus:1
            };

            GisWatchService.retrieveUsers(queryParams).then(function (users) {
                var usersList = users.items;
                var data = [];
                for(var i=0;i<usersList.length;i++){
                    if(usersList[i].userPosition==null){
                        continue;
                    }
                    var temp = {
                        lnglat: [usersList[i].userPosition.longitude, usersList[i].userPosition.latitude], //点标记位置
                        name: usersList[i].name,
                        id:i
                    };
                    data.push(temp);
                }
                console.log(data);

                var style = [{
                    url: '../images/icon/delivery.ico',
                    anchor: new AMap.Pixel(6, 6),
                    size: new AMap.Size(40, 40)
                }];

                var mass = new AMap.MassMarks(data, {
                    opacity:0.8,
                    zIndex: 111,
                    cursor:'pointer',
                    style:style
                });

                //每一个麻点打label
                for(var i=0;i<data.length;i++){
                    var marker = new AMap.Marker({content:' ',map:$scope.map});
                    marker.setPosition(data[i].lnglat);
                    marker.setLabel({content:data[i].name});
                    mass.setMap($scope.map);
                }

            });
        };


        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchUsers();
        };

        var gotoPageGasTray = function (pageNo) {
            $scope.pagerGasTray.setCurPageNo(pageNo);
            searchGasTray();
        };

        $scope.pager = pager.init('GisWatchCtrl', gotoPage);
        $scope.pagerGasTray = pager.init('GisWatchCtrl', gotoPageGasTray);



        $scope.q = {
            userId: null,
            userGroup:null,
            gasTrayId:null,
            gasTrayLeakStatus:null
        };

        $scope.vm = {
            userList: [],
            gasTrayList: [],
            userGroups:[{name:"配送员",code:"00003"},{name:"调拨员",code:"00007"}],
            gasTrayLeakStatus:[{name:"漏气报警",code:"1"},{name:"正常运行",code:"0"}]
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchUsers();
        };

        $scope.searchGasTrayAction = function () {
            $scope.pagerGasTray.setCurPageNo(1);
            searchGasTray();
        };


        var searchUsers = function () {
            var queryParams = {
                userId: $scope.q.userId,
                groupCode:$scope.q.userGroup.code,
                orderBy:"alive_status desc",
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            GisWatchService.retrieveUsers(queryParams).then(function (users) {
                $scope.pager.update($scope.q, users.total, queryParams.pageNo);
                $scope.vm.userList = users.items;
            });
        };

        var searchGasTray = function () {
            var queryParams = {
                number: $scope.q.gasTrayId,
                leakStatus:$scope.q.gasTrayLeakStatus.code,
                pageNo: $scope.pagerGasTray.getCurPageNo(),
                pageSize: $scope.pagerGasTray.pageSize
            };

            GisWatchService.retrieveGasTray(queryParams).then(function (gasTrays) {
                $scope.pagerGasTray.update($scope.q, gasTrays.total, queryParams.pageNo);
                $scope.vm.gasTrayList = gasTrays.items;
            });
        };





        //员工定位
        $scope.location = function (longitude,latitude,aliveStatus) {
            if(aliveStatus.index==0){
                udcModal.info({"title": "提示", "message": "该员工不在线 "});
            }else{
                $scope.map.setCenter([longitude, latitude])
            }

        };

        //托盘标注
        var markOneGasTray = function (imagePath, longitude, latitude, gasTray) {
            var iconGasTray = new AMap.Icon({
                image : imagePath,//24px*24px
                //icon可缺省，缺省时为默认的蓝色水滴图标，
                size : new AMap.Size(50,50),
                imageSize : new AMap.Size(50,50)
            });
            var markerGasTray = new AMap.Marker({
                icon : iconGasTray,//24px*24px
                position : [longitude, latitude],
                offset : new AMap.Pixel(0,0),
                map : $scope.map
            });
            markerGasTray.setAnimation("AMAP_ANIMATION_DROP");
            markerGasTray.content = gasTray;
            //给Marker绑定单击事件
            markerGasTray.on('click', gasTrayInfoMark);
        };

        //托盘定位
        $scope.locationGasTray = function (longitude,latitude,gasTray) {
            //$scope.map.clearMap( );
            //var imagePath = "";
            //if(gasTray.leakStatus.index==0){//工作正常
            //    imagePath = '../images/icon/gasTray.png';
            //}else {
            //    imagePath = '../images/icon/gasTrayLeak.png';
            //}
            //markOneGasTray(imagePath,longitude,latitude,gasTray);
            $scope.map.setCenter([longitude, latitude]);

        };





        var init = function () {
            $scope.pager.pageSize=5;
            $scope.pager.setCurPageNo(1);
            $scope.pagerGasTray.pageSize=5;
            $scope.pagerGasTray.setCurPageNo(1);
            $scope.q.userGroup = $scope.vm.userGroups[0];
            $scope.q.gasTrayLeakStatus = $scope.vm.gasTrayLeakStatus[0];
            searchUsers();
            searchGasTray();
            reflesh();//标注配送工
            markAllLeakGasTray();//标注漏气托盘

            //启动绘制轨迹的定时器
            $scope.timer = $interval( function(){
                searchUsers();
                searchGasTray();
                reflesh();//标注配送工
                markAllLeakGasTray();//标注漏气托盘
            }, 5000);

            //markPeisong();
            //markDiaobo();
        };



        var reflesh = function(){
            //请求所有在线配送工的经纬度
            var queryParams = {
                //groupCode:"00003",
                aliveStatus:1
            };

            GisWatchService.retrieveUsers(queryParams).then(function (users) {
                var usersList = users.items;
                var paths = [];
                for (var i = 0; i < usersList.length; i++) {
                    if (usersList[i].userPosition == null) {
                        continue;
                    }
                    //配送工或者调拨车
                    if(usersList[i].userGroup.code=="00003"||usersList[i].userGroup.code=="00007"){
                        if ($scope.mapUserPosition.has(usersList[i].userId)) {
                            var lastPosition = $scope.mapUserPosition.get(usersList[i].userId);
                            var tempPath = {"name":usersList[i].userGroup.name+"|"+usersList[i].userId+"|"+usersList[i].name
                            +"|"+usersList[i].mobilePhone+"|"+usersList[i].department.name, "path":[]}//钢瓶轨迹
                            tempPath.path = [
                                [lastPosition.longitude, lastPosition.latitude],
                                [usersList[i].userPosition.longitude, usersList[i].userPosition.latitude]
                            ];
                            paths.push(tempPath);
                            $scope.mapUserPosition.delete(usersList[i].userId);
                        }
                        $scope.mapUserPosition.set(usersList[i].userId, usersList[i].userPosition);
                    }

                }
                if(paths.length>0){
                    drawPath(paths);
                }

            });


        };

        //标绘所有的报警托盘--有问题，先注释掉
        var markAllLeakGasTray = function(){

            //请求所有在线配送工的经纬度
            var queryParams = {
                leakStatus:1,
            };

            GisWatchService.retrieveGasTray(queryParams).then(function (gasTrays) {
                $scope.map.clearMap();
                var gasTraysList = gasTrays.items;
                var imagePath = '../images/icon/gasTrayLeak.png';
                for (var i = 0; i < gasTraysList.length; i++) {
                    markOneGasTray(imagePath,gasTraysList[i].longitude,gasTraysList[i].latitude,gasTraysList[i]);
                }

            });


        };
        //绘制轨迹
        var drawPath = function(paths){
            //$scope.pathSimplifierIns.clearPathNavigators();
            $('#loadingTip').remove();
            $scope.pathSimplifierIns.setData(paths);
            function onload() {
                $scope.pathSimplifierIns.renderLater();
            }
            function onerror(e) {
                alert('图片加载失败！');
            }
            for(var i=0; i<paths.length; i++){
                var userType = paths[i].name.split('|')[0];
                var imageName = "";
                if(userType=="配送员"){
                    imageName = "../images/icon/car.png";
                }else{
                    imageName = "../images/icon/plane.png";
                }
                var navg1 = $scope.pathSimplifierIns.createPathNavigator(i, {
                    loop: false,
                    speed: 100,
                    pathNavigatorStyle: {
                        width: 20,
                        height: 32,
                        //使用图片
                        content: $scope.PathSimplifier.Render.Canvas.getImageContent(imageName, onload, onerror),
                        strokeStyle: null,
                        fillStyle: null,
                    }
                });
                navg1.start();
            }
        };




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
                    autoSetFitView:false,
                    map: $scope.map, //所属的地图实例
                    getPath: function(pathData, pathIndex) {

                        return pathData.path;
                    },
                    getHoverTitle: function(pathData, pathIndex, pointIndex) {
                        userInfoMark(pathData.name,pathData.path[1]);

                        if (pointIndex >= 0) {
                            //point
                            return pathData.name;
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

                window.pathSimplifierIns = $scope.pathSimplifierIns;
            });
        };






        //创建标注
        var  userInfoMark　= function(pathName,location) {
            var userInfo = pathName.split('|');
            var groupName = userInfo[0];
            var userId = userInfo[1];
            var userName = userInfo[2];
            var phone = userInfo[3];
            var departmentName = userInfo[4];

            //查询钢瓶信息
            var queryParams = {
                liableUserId: userId,
                pageNo: 1,
                pageSize: 1
            };

            GisWatchService.retrieveBottles(queryParams).then(function (bottles) {
                var bottlesCount = bottles.total;
                var info=[];
                info.push("<div><div><img style=\"flow:left;width: 25px;height: 25px\" src=\"../images/icon/delivery.ico\"/>"+"<b style='font-size: 21px'>"+groupName+"  |  "+userId+"</b></div>");
                info.push("<div style=\"padding:0px 0px 0px 4px;\"><b>"+"姓名:   "+userName+"</b>");
                info.push("<b>电话:   "+phone+"</b>")
                info.push("<b>部门:   "+departmentName+"</b>")
                info.push("<b>钢瓶数量:   "+bottlesCount+"  瓶</b>")
                info.push("</div></div>");
                var infoWindow= new AMap.InfoWindow({
                    content: info.join("<br/>")
                });
                infoWindow.open($scope.map, location);
            });
        };

        //创建托盘标注
        var  gasTrayInfoMark　= function(e) {
            var gasTray = e.target.content;
            //查询用户信息
            var queryParams = {
                userId: gasTray.user.userId,
            };

            GisWatchService.retrieveCustomers(queryParams).then(function (customers) {
                var customersList = customers.items;
                if(customersList.length==0){
                    udcModal.info({"title": "错误信息", "message": "用户信息查询失败"});
                    return;
                }
                var customer = customersList[0];
                var info=[];
                info.push("<div><img style=\"flow:left;width: 40px;height: 40px\" src=\"../images/icon/workerImage.png\"/>"+"<div style='font-size: 21px'>"+"客户"+"  |  "+customer.userId+"</div>");
                info.push("<div style=\"padding:0px 0px 0px 4px;\"><b>"+"姓名:   "+customer.name+"</b>");
                info.push("<b>电话:   "+customer.phone+"</b>")
                info.push("<b>地址:   "+customer.address.province+customer.address.city+
                    customer.address.county+customer.address.detail+"</b>")
                info.push("<b>钢瓶重量:   "+gasTray.weight+"  公斤</b>")
                info.push("</div></div>");
                var infoWindow= new AMap.InfoWindow({
                    content: info.join("<br/>")
                });
                infoWindow.open($scope.map, e.target.getPosition());
            });
        };


        //init();

    }]);
