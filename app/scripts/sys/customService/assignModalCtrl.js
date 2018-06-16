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
        var displayInfo = e.target.content.name+"   "+e.target.content.mobilePhone;
        infoWindow.setContent(displayInfo);
        infoWindow.open($scope.map, e.target.getPosition());
        //将这个配送员信息更新至表单
        for (var i=0;i<$scope.vm.onLineWorkersList.length;i++) {
            if(e.target.content.userId == $scope.vm.onLineWorkersList[i].userId){
                $scope.vm.selectedWorker =  _.clone($scope.vm.onLineWorkersList[i]);
                break;
            }
        }
        console.log($scope.vm.selectedWorker);

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
        var dealparams = {
            businessKey: $scope.vm.currentOrder.orderSn,
            candiUser   : $scope.vm.selectedWorker.userId,
            orderStatus: 1
        };
        OrderService.dealDistribution(dealparams,$scope.vm.currentOrder.taskId).then(function () {
            udcModal.info({"title": "处理结果", "message": "人工指派配送工成功，工号："+$scope.vm.selectedWorker.userId});
            $scope.close(true);
        }, function(value) {
            udcModal.info({"title": "处理结果", "message": "人工指派配送工失败 "+value.message});
            $scope.close(false);
        })

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
            groupCode: '00003'//配送工组代码
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
        var iconWorker = new AMap.Icon({
            image : '../images/icon/worker.ico',//24px*24px
            //icon可缺省，缺省时为默认的蓝色水滴图标，
            size : new AMap.Size(50,50),
            imageSize : new AMap.Size(50,50)
        });
        for (var i=0;i<$scope.vm.onLineWorkersList.length;i++){

            console.log($scope.vm.onLineWorkersList[i].userPosition.longitude);
            console.log($scope.vm.onLineWorkersList[i].userPosition.latitude);
            var markerDest = new AMap.Marker({
                icon : iconWorker,//24px*24px
                position : [$scope.vm.onLineWorkersList[i].userPosition.longitude, $scope.vm.onLineWorkersList[i].userPosition.latitude],
                offset : new AMap.Pixel(0,0),
                map : $scope.map
            });


            markerDest.content = {userId:$scope.vm.onLineWorkersList[i].userId,
                name:$scope.vm.onLineWorkersList[i].name, mobilePhone:$scope.vm.onLineWorkersList[i].mobilePhone};

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
        console.log("refleshWorkerInfo");
        $scope.vm.selectedWorker=$scope.vm.selectedWorker;
    };
    init();
}]);