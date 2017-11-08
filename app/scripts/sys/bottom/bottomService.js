'use strict';

bottomApp.service('BottomService', ['$http', 'URI', 'promiseWrapper', function ($http, URI, promise) {

    var bottomsUri = URI.resources.bottoms;
    var bottomMap;

    this.toViewModel = function (bottomFromApi) {
        return {
            id: bottomFromApi.id,
            name: bottomFromApi.name,
            facture: bottomFromApi.facture,
            location: bottomFromApi.location,
            createTime: bottomFromApi.createTime,
            updateTime: bottomFromApi.updateTime
        }
    };

    this.retrieveBottoms = function (params) {
        return promise.wrap($http.get(bottomsUri, {params: params}));
    };

    this.getBottomInfoById = function (bottomId) {
        return promise.wrap($http.get(bottomsUri + '/' + bottomId));
    };

    this.createBottom = function (bottom) {
        return promise.wrap($http.post(bottomsUri, bottom));
    };

    this.modifyBottom = function (bottom) {
        return promise.wrap($http.put(bottomsUri + "/" + bottom.id, bottom));
    };

    this.deleteBottom = function (bottom) {
        return promise.wrap($http.delete(bottomsUri + "/" + bottom.id));
    };

    this.location = function(lon, lan)
    {
        var point = new BMap.Point(lon, lan);
        bottomMap.centerAndZoom(point, 15);
    }
    this.mapReady = function (map) {
        bottomMap = map;
        map.enableScrollWheelZoom();
        map.addControl(new BMap.NavigationControl());
        map.addControl(new BMap.ScaleControl());
        map.addControl(new BMap.OverviewMapControl());
        map.addControl(new BMap.MapTypeControl());


        var point = new BMap.Point(102.73, 25.04);
        map.centerAndZoom(point, 15);
        var marker = new BMap.Marker(point);
        map.addOverlay(marker);

        //
        var sContent =
            "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>天安门</h4>" +
            "<img style='float:right;margin:4px' id='imgDemo' src='../img/tianAnMen.jpg' width='139' height='104' title='天安门'/>" +
            "<p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'></p>" +
            "</div>";
        var infoWindow = new BMap.InfoWindow(sContent);  // 创建信息窗口对象
        //创建小狐狸
        var bottoms = [
            {"ID":"1", "longitude": "102.730", "latitude":"25.040"},
            {"ID":"2", "longitude": "102.735", "latitude":"25.045"},
            {"ID":"3", "longitude": "102.731", "latitude":"25.047"},
            {"ID":"4", "longitude": "102.740", "latitude":"25.045"},
            {"ID":"5", "longitude": "102.741", "latitude":"25.047"},
            {"ID":"6", "longitude": "102.745", "latitude":"25.045"},
            {"ID":"7", "longitude": "102.741", "latitude":"25.047"},
            {"ID":"8", "longitude": "102.730", "latitude":"26.040"},
            {"ID":"9", "longitude": "102.765", "latitude":"25.055"}
        ];
        var cars = [
            {"ID":"1", "longitude": "102.720", "latitude":"25.040"},
            {"ID":"2", "longitude": "102.725", "latitude":"25.045"},
            {"ID":"3", "longitude": "102.721", "latitude":"25.047"}
        ];
        for (var i=0;i<bottoms.length;i++)
        {
            var pt = new BMap.Point(bottoms[i].longitude, bottoms[i].latitude);
            var myIcon = new BMap.Icon("../../images/icon/bottom.ico", new BMap.Size(50,50));
            var marker2 = new BMap.Marker(pt,{icon:myIcon});  // 创建标注
            map.addOverlay(marker2);              // 将标注添加到地图中
            marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画

            marker2.addEventListener("click", function(e){
                var pointTemp = new BMap.Point(e.point.lng,e.point.lat);
                var opts = {
                    width : 150,     // 信息窗口宽度
                    height: 50,     // 信息窗口高度
                    title : "煤气罐定位" , // 信息窗口标题
                    enableMessage:true,//设置允许信息窗发送短息
                    message:"亲耐滴，晚上一起吃个饭吧？戳下面的链接看下地址喔~"
                }
                var infoWindow = new BMap.InfoWindow(e.point.lng+","+e.point.lat+"</br>规格：5Kg", opts);  // 创建信息窗口对象

                map.openInfoWindow(infoWindow,pointTemp); //开启信息窗口

            });
        }

        for (var i=0;i<cars.length;i++)
        {
            var pt = new BMap.Point(cars[i].longitude, cars[i].latitude);
            var myIcon = new BMap.Icon("../../images/icon/delivery.ico", new BMap.Size(50,50));
            var marker2 = new BMap.Marker(pt,{icon:myIcon});  // 创建标注
            map.addOverlay(marker2);              // 将标注添加到地图中
        }


        var myP1 = new BMap.Point(102.721,25.047);    //起点
        var myP2 = new BMap.Point(102.740,25.044);      //终点
        var myIcon = new BMap.Icon("../../images/icon/delivery.ico", new BMap.Size(50, 50), {    //小车图片
            //offset: new BMap.Size(0, -5),    //相当于CSS精灵
            imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
        });
        var driving2 = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true}});    //驾车实例
        driving2.search(myP1, myP2);    //显示一条公交线路

        window.run = function (){
            var driving = new BMap.DrivingRoute(map);    //驾车实例
            driving.search(myP1, myP2);
            driving.setSearchCompleteCallback(function(){
                var pts = driving.getResults().getPlan(0).getRoute(0).getPath();    //通过驾车实例，获得一系列点的数组
                var paths = pts.length;    //获得有几个点

                var carMk = new BMap.Marker(pts[0],{icon:myIcon});
                map.addOverlay(carMk);
                i=0;
                function resetMkPoint(i){
                    carMk.setPosition(pts[i]);
                    if(i < paths){
                        setTimeout(function(){
                            i++;
                            resetMkPoint(i);
                        },100);
                    }
                }
                setTimeout(function(){
                    resetMkPoint(1);
                },10)

            });
        }

        setTimeout(function(){
            run();
        },1500);

    };

}]);

