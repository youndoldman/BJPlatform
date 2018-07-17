'use strict';

comprehensiveSituationApp.controller('GisWatchCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'GisWatchService', 'MapService', '$timeout',function ($scope, $rootScope, $filter, $location, Constants,
                                                                                            rootService, pager, udcModal, GisWatchService, MapService, $timeout) {


        //function getLocation(){
        //    if (navigator.geolocation){
        //        navigator.geolocation.getCurrentPosition(showPosition,showError);
        //    }else{
        //        alert("浏览器不支持地理定位。");
        //    }
        //}

            var infoWindow = new AMap.InfoWindow();
        $scope.$watch('$viewContentLoaded',function () {
            mapInitial();
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

        $scope.pager = pager.init('GisWatchCtrl', gotoPage);



        $scope.q = {
            userId: null,
            userGroup:null
        };

        $scope.vm = {
            userList: [],
            userGroups:[{name:"配送员",code:"00003"},{name:"调拨员",code:"00007"}]
        };

        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
            searchUsers();
        };


        var searchUsers = function () {
            var queryParams = {
                userId: $scope.q.userId,
                groupCode:$scope.q.userGroup.code,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            };

            GisWatchService.retrieveUsers(queryParams).then(function (users) {
                $scope.pager.update($scope.q, users.total, queryParams.pageNo);
                $scope.vm.userList = users.items;
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





        var init = function () {
            $scope.q.userGroup = $scope.vm.userGroups[0];
            searchUsers();
            markPeisong();
            markDiaobo();

        };

        init();

    }]);
