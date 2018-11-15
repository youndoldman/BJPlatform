'use strict';
customServiceApp.controller('PressedOrderHistoryModalCtrl', ['$scope', 'close', 'OrderService', 'title', 'initVal', 'udcModal','$timeout',
    'pager','$interval',function ($scope, close, OrderService, title, initVal, udcModal,$timeout, pager,$interval) {
        $scope.modalTitle = title;
        $scope.isModify = false;
        $scope.vm = {
            pressedHistory: {orderSn:null,note:null},
            pressedHistoryList:[],//钢瓶交接记录
            CustomerOrder:null,
            selectedWorker:null
        };

        $scope.close = function (result) {
            close(result, 500);
        };

        $scope.submit = function (pressedHistory) {
            OrderService.createOrderUrgency(pressedHistory).then(function () {
                udcModal.info({"title": "处理结果", "message": "催气成功 "});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "错误信息", "message": value.message});
            })

        };


        var init = function () {
            $scope.pagerOpsLog.pageSize=5;
            $scope.vm.CustomerOrder = _.clone(initVal);
            $scope.isModify = false;
            searchOpsLog();
            $scope.vm.pressedHistory.orderSn = $scope.vm.CustomerOrder.orderSn;

            //刷新送气工位置
            //规避不同作用域的BUG
            $scope.timer = $interval( function(){
                refleshWorkerInfo()
            }, 15000);

        };

        var gotoPageOpsLog = function (pageNo) {
            $scope.pagerOpsLog.setCurPageNo(pageNo);
            searchOpsLog();
        };

        $scope.pagerOpsLog = pager.init('PressedOrderHistoryModalCtrl', gotoPageOpsLog);


        $scope.q = {

        };

        //查询催气记录
        var searchOpsLog = function () {
            $scope.vm.pressedHistoryList = [];
            var queryParams = {
                orderSn: $scope.vm.CustomerOrder.orderSn,
                pageNo: $scope.pagerOpsLog.getCurPageNo(),
                pageSize: $scope.pagerOpsLog.pageSize,
                orderBy:"id desc"
            };

            OrderService.retrieveOrderUrgency(queryParams).then(function (historys) {
                $scope.pagerOpsLog.update($scope.q, historys.total, queryParams.pageNo);
                $scope.vm.pressedHistoryList = historys.items;
            });
        };

        //地图
        var infoWindow = new AMap.InfoWindow();

        $timeout(function(){
            mapInitial();
            destNationInitial();
            //查找在线的配送工
            searchWorkers();
        },500);
        //地图初始化
        var mapInitial = function() {
            $scope.map = new AMap.Map('mapContainer', {
                resizeEnable: true,
                center: [$scope.vm.CustomerOrder.recvLongitude, $scope.vm.CustomerOrder.recvLatitude],
                zoom: 15
            });
            AMap.plugin(['AMap.ToolBar','AMap.Scale','AMap.OverView'],
                function(){
                    $scope.map.addControl(new AMap.ToolBar());

                    $scope.map.addControl(new AMap.Scale());

                    //$scope.map.addControl(new AMap.OverView({isOpen:true}));
                });
        };
        //目的地址初始化
        var destNationInitial = function() {
            var iconDest = new AMap.Icon({
                image : '../images/icon/home.ico',//24px*24px
                //icon可缺省，缺省时为默认的蓝色水滴图标，
                size : new AMap.Size(50,50),
                imageSize : new AMap.Size(50,50)
            });
            var markerDest = new AMap.Marker({
                icon : iconDest,//24px*24px
                position : [$scope.vm.CustomerOrder.recvLongitude, $scope.vm.CustomerOrder.recvLatitude],
                offset : new AMap.Pixel(0,0),
                map : $scope.map
            });
            markerDest.content = $scope.vm.CustomerOrder.recvAddr.province+$scope.vm.CustomerOrder.recvAddr.city
                +$scope.vm.CustomerOrder.recvAddr.county+$scope.vm.CustomerOrder.recvAddr.detail;
            //给Marker绑定单击事件
            markerDest.on('click', markerDestClick);
        };

        var markerDestClick = function (e) {
            infoWindow.setContent(e.target.content);
            infoWindow.open($scope.map, e.target.getPosition());
        };

        var markerWorkersClick = function (e) {

            var userId = e.target.content.userId
            var queryParams = {
                pageNo: 1,
                pageSize: 100,
                liableUserId: userId,
                loadStatus:"LSHeavy"
            };
            OrderService.retrieveBottles(queryParams).then(function (bottles) {
                var bottleCount_5 = 0;
                var bottleCount_15 = 0;
                var bottleCount_50 = 0;
                for(var i=0;i<bottles.items.length; i++ ){
                    var bottle = bottles.items[i];
                    if(bottle.spec.code=="0001"){
                        bottleCount_5++;
                    }else if(bottle.spec.code=="0002") {
                        bottleCount_15++;
                    }else if(bottle.spec.code=="0003") {
                        bottleCount_50++;
                    }
                }
                var info=[];
                info.push("<div><div><img style=\"flow:left;width: 25px;height: 25px\" src=\"../images/icon/bottle.ico\"/>"+"<b style='font-size: 21px'>"+"直销员  |  "+$scope.vm.selectedWorker.userId+"</b></div>");
                info.push("<div style=\"padding:0px 0px 0px 4px;\"><b>"+"姓名:   "+$scope.vm.selectedWorker.name+"</b>");
                info.push("<b>5公斤(重瓶) :   "+bottleCount_5+"</b>");
                info.push("<b>15公斤(重瓶):   "+bottleCount_15+"</b>");
                info.push("<b>50公斤(重瓶):   "+bottleCount_50+"</b>");
                info.push("</div></div>");
                infoWindow.setContent(info.join("<br/>"));
                infoWindow.open($scope.map, e.target.getPosition());
            });

        };

        //地图上标绘配送工
        var markOnlineWorkers = function(){

            var iconWorker = new AMap.Icon({
                image : '../images/icon/worker.ico',//24px*24px
                //icon可缺省，缺省时为默认的蓝色水滴图标，
                size : new AMap.Size(50,50),
                imageSize : new AMap.Size(50,50)
            });
            for (var i=0;i<$scope.vm.onLineWorkersList.length;i++){
                if($scope.vm.onLineWorkersList[i].userPosition==null){
                    continue;
                }
                var markerDest = new AMap.Marker({
                    icon : iconWorker,//24px*24px
                    position : [$scope.vm.onLineWorkersList[i].userPosition.longitude, $scope.vm.onLineWorkersList[i].userPosition.latitude],
                    offset : new AMap.Pixel(0,0),
                    label:{content:$scope.vm.onLineWorkersList[i].name, offset:new AMap.Pixel(0,40)}

                });
                markerDest.setMap($scope.map);
                // 设置鼠标划过点标记显示的文字提示
                markerDest.content = {userId:$scope.vm.onLineWorkersList[i].userId,
                    name:$scope.vm.onLineWorkersList[i].name, mobilePhone:$scope.vm.onLineWorkersList[i].mobilePhone};
                //给Marker绑定单击事件
                markerDest.on('click', markerWorkersClick);
            }
        };
        //查找在线的配送工
        var searchWorkers = function () {
            if($scope.vm.CustomerOrder.dispatcher==null){
                return;
            }
            var queryParams = {
                userId:$scope.vm.CustomerOrder.dispatcher.userId
            };
            OrderService.retrieveDistributionworkers(queryParams).then(function (workers) {
                $scope.vm.onLineWorkersList = workers.items;
                $scope.vm.selectedWorker = $scope.vm.onLineWorkersList[0];
                //在地图上标绘
                markOnlineWorkers();
            });

        };

        var refleshWorkerInfo = function () {
            $scope.map.clearMap();
            destNationInitial();
            searchWorkers();
        };
        init();

    }]);