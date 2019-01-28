'use strict';

customServiceApp.controller('AssignModalCtrl', ['$scope', 'close', 'OrderService', 'CustomerManageService', 'title', 'initVal','udcModal','$timeout','$interval',function ($scope, close, OrderService, CustomerManageService, title, initVal, udcModal,$timeout,$interval) {

    $scope.vm = {
        currentOrder: {},
        onLineWorkersList:[],
        selectedWorker:{}
    };

    $scope.modalTitle = title;
    $scope.map = null;
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
            center: [$scope.vm.currentOrder.recvLongitude, $scope.vm.currentOrder.recvLatitude],
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
            position : [$scope.vm.currentOrder.recvLongitude, $scope.vm.currentOrder.recvLatitude],
            offset : new AMap.Pixel(0,0),
            map : $scope.map
        });
        markerDest.content = $scope.vm.currentOrder.recvAddr.province+$scope.vm.currentOrder.recvAddr.city
            +$scope.vm.currentOrder.recvAddr.county+$scope.vm.currentOrder.recvAddr.detail;
        //给Marker绑定单击事件
        markerDest.on('click', markerDestClick);
    };

    var markerDestClick = function (e) {
        infoWindow.setContent(e.target.content);
        infoWindow.open($scope.map, e.target.getPosition());
    };

    var markerWorkersClick = function (e) {
        for (var i=0;i<$scope.vm.onLineWorkersList.length;i++) {
            if(e.target.content.userId == $scope.vm.onLineWorkersList[i].userId){
                $scope.vm.selectedWorker =  _.clone($scope.vm.onLineWorkersList[i]);
                break;
            }
        }
        var queryParams = {
            pageNo: 1,
            pageSize: 100,
            liableUserId: $scope.vm.selectedWorker.userId,
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
            info.push("<div><div><img style=\"flow:left;width: 40px;height: 40px\" src=\"../../api/sysusers/photo/"+$scope.vm.selectedWorker.userId+"\"/>"+"<b style='font-size: 21px'>"+"直销员  |  "+$scope.vm.selectedWorker.userId+"</b></div>");
            info.push("<div style=\"padding:0px 0px 0px 4px;\"><b>"+"姓名:   "+$scope.vm.selectedWorker.name+"</b>");
            info.push("<b>5公斤(重瓶) :   "+bottleCount_5+"</b>");
            info.push("<b>15公斤(重瓶):   "+bottleCount_15+"</b>");
            info.push("<b>50公斤(重瓶):   "+bottleCount_50+"</b>");
            info.push("</div></div>");
            infoWindow.setContent(info.join("<br/>"));
            infoWindow.open($scope.map, e.target.getPosition());
        });

    };



    $scope.close = function (result) {
        close(result, 500);
    };

    $scope.submit = function () {
        //强行指派配送工
        if($scope.vm.selectedWorker.userId==null){
            udcModal.info({"title": "错误信息", "message": "请在地图中选择将要派单的配送工！ "});
            return;
        }
        if($scope.modalTitle=="强派订单"){
            var dealparams = {
                businessKey: $scope.vm.currentOrder.orderSn,
                candiUser   : $scope.vm.selectedWorker.userId,
                orderStatus: 1,
                forceDispatch:true
            };
            OrderService.dealDistribution(dealparams,$scope.vm.currentOrder.taskId).then(function () {
                udcModal.info({"title": "处理结果", "message": "人工指派配送工成功，工号："+$scope.vm.selectedWorker.userId});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "人工指派配送工失败 "+value.message});
                $scope.close(false);
            })
        }else{
            var dealparams = {
                businessKey: $scope.vm.currentOrder.orderSn,
                candiUser   : $scope.vm.selectedWorker.userId
            };
            OrderService.transferDistribution(dealparams,$scope.vm.currentOrder.taskId).then(function () {
                udcModal.info({"title": "处理结果", "message": "人工转派配送工成功，工号："+$scope.vm.selectedWorker.userId});
                $scope.close(true);
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "人工转派配送工失败 "+value.message});
                $scope.close(false);
            })
        }

    };

    var getDestLonLat = function () {
        //查询经纬度
        var address = $scope.vm.currentCustomer.address.province+$scope.vm.currentCustomer.address.city
            +$scope.vm.currentCustomer.address.county+$scope.vm.currentCustomer.address.detail;

        CustomerManageService.retrieveLocation(address).then(function (value) {
            var location = value.geocodes[0].location;
            $scope.currentOrder.recvLongitude = parseFloat(location.split(',')[0]);
            $scope.currentOrder.recvLatitude = parseFloat(location.split(',')[1]);
        })

    };



    //查找在线的配送工
    var searchWorkers = function () {
        var queryParams = {
            groupCode: '00003',//配送工组代码
            aliveStatus: 1//在线
        };
        OrderService.retrieveDistributionworkers(queryParams).then(function (workers) {
            $scope.vm.onLineWorkersList = workers.items;
            console.log($scope.vm.onLineWorkersList);
            //在地图上标绘
            markOnlineWorkers();
        });
    };
    //地图上标绘配送工
    var markOnlineWorkers = function(){

        for (var i=0;i<$scope.vm.onLineWorkersList.length;i++){
            if($scope.vm.onLineWorkersList[i].userPosition==null){
                continue;
            }
            var imageName = "../../api/sysusers/photo/"+$scope.vm.onLineWorkersList[i].userId;
            var iconWorker = new AMap.Icon({
                image : imageName,//24px*24px
                //icon可缺省，缺省时为默认的蓝色水滴图标，
                size : new AMap.Size(50,50),
                imageSize : new AMap.Size(50,50)
            });

            var markerDest = new AMap.Marker({
                icon : iconWorker,//24px*24px
                position : [$scope.vm.onLineWorkersList[i].userPosition.longitude, $scope.vm.onLineWorkersList[i].userPosition.latitude],
                offset : new AMap.Pixel(0,0),
                label:{content:$scope.vm.onLineWorkersList[i].name, offset:new AMap.Pixel(0,40)}

            });
            markerDest.setMap($scope.map);
            // 设置鼠标划过点标记显示的文字提示
            //markerDest.setTitle('我是marker的title');

            // 设置label标签
            // label默认蓝框白底左上角显示，样式className为：amap-marker-label
            //markerDest.setLabel({
            //    //修改label相对于maker的位置
            //    offset: new AMap.Pixel(20, 20),
            //    content:"111"
            //});


            markerDest.content = {userId:$scope.vm.onLineWorkersList[i].userId,
                name:$scope.vm.onLineWorkersList[i].name, mobilePhone:$scope.vm.onLineWorkersList[i].mobilePhone};




            //var displayInfo = markerDest.content.name+"   "+markerDest.content.mobilePhone;
            //infoWindow.setContent(displayInfo);
            //infoWindow.open($scope.map, markerDest.getPosition());

            //给Marker绑定单击事件
            markerDest.on('click', markerWorkersClick);
        }
    }

    var init = function () {
        $scope.vm.currentOrder = _.clone(initVal);

        //规避不同作用域的BUG
        $scope.timer = $interval( function(){
            refleshWorkerInfo()
        }, 200);

        //mapInit();
    };

    var refleshWorkerInfo = function () {
        //console.log("refleshWorkerInfo");
        $scope.vm.selectedWorker=$scope.vm.selectedWorker;
    };


    init();
}]);