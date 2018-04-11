'use strict';

customServiceApp.controller('CallCenterCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'CustomerManageService', 'OrderService','sessionStorage','MendSecurityComplaintService',function ($scope, $rootScope, $filter, $location, Constants,
                                                                           rootService, pager, udcModal, CustomerManageService,OrderService,sessionStorage,MendSecurityComplaintService) {


        var gotoPageCustomer = function (pageNo) {
            $scope.pagerCustomer.setCurPageNo(pageNo);
            searchCustomer();
        };
        var gotoPageHistory = function (pageNo) {
            $scope.pagerHistory.setCurPageNo(pageNo);
            searchOrderHistory();
        };
        $scope.pagerCustomer = pager.init('CustomerListCtrl', gotoPageCustomer);

        $scope.pagerHistory = pager.init('CustomerListCtrl', gotoPageHistory);



        $(function () {

            $('#datetimepickerOrder').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',

            });
            $('#datetimepickerRepair').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',

            });
        });
        $(function () {
            $('#datetimepickerOrder').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.currentOrder.reserveTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
        });
        $scope.temp = {
            selectedGoodsType:{},
            selectedGoods:{},
            selectedQuantity:1,
            goodsTypesList:[],
            goodsList:[],
            mendTypesList:[],//报修类型
            selectedMendType:{}//当前选择的报修类型
        };

        $scope.searchParam = {
            phone:null,
            address:null
        };




        $scope.qCustomer = {

        };
        $scope.qHistory = {

        };


        $scope.vm = {
            callInPhone:"13913015340",
            currentCustomer:null,
            CustomerList: [],
            CustomerOrderHistory: [],
            CustomerAutoReportList:[ //不间断供气客户
            ]
        };


        //当前订单信息
        $scope.currentOrder= {
            orderDetailList:[],
        };

        //当前报修信息
        $scope.currentMend= {
            mendType:{},//报修类型
            customer:{},//报修用户
            recvName:null,//联系人
            recvPhone:null,//联系电话
            recvAddr:{},//报修地址
            detail:null,//报修内容
            reserveTime:null,//预约时间
        };


        //添加购物车
        $scope.addToCart = function () {
            if($scope.vm.currentCustomer == null){
                udcModal.info({"title": "错误信息", "message": "请选择客户 "});
                return;
            }
            var orderDetail = {};
            orderDetail.goods = $scope.temp.selectedGoods;
            orderDetail.originalPrice = $scope.temp.selectedGoods.price;
            orderDetail.dealPrice = $scope.temp.selectedGoods.price;
            orderDetail.quantity = $scope.temp.selectedQuantity;
            orderDetail.subtotal = $scope.temp.selectedQuantity * orderDetail.dealPrice;
            $scope.currentOrder.orderDetailList.push(orderDetail);
            //刷新订单
            refleshOrder();
        };

        //删除购物车
        $scope.deleteGoodsFromCart = function (orderDetail) {
            for(var i=0; i<$scope.currentOrder.orderDetailList.length; i++){
                if ($scope.currentOrder.orderDetailList[i] == orderDetail) {
                    $scope.currentOrder.orderDetailList.splice(i, 1);
                    break;
                }
            }
            //刷新订单
            refleshOrder();
        };


        //刷新当前订单的数据
        var refleshOrder = function() {
            if($scope.vm.currentCustomer == null){
                udcModal.info({"title": "错误信息", "message": "请选择客户 "});
                return;
            }
            //计算订单总额
            var amount = 0;
            for (var i = 0; i < $scope.currentOrder.orderDetailList.length; i++) {
                amount += $scope.currentOrder.orderDetailList[i].subtotal;
            }
            $scope.currentOrder.orderAmount = amount;
            //填写默认的联系人、联系电话、送气地址
            $scope.currentOrder.recvAddr = $scope.vm.currentCustomer.address;
            $scope.currentOrder.recvName = $scope.vm.currentCustomer.name;
            $scope.currentOrder.recvPhone = $scope.vm.currentCustomer.phone;

            //查询经纬度
            var address = $scope.vm.currentCustomer.address.province+$scope.vm.currentCustomer.address.city
                +$scope.vm.currentCustomer.address.county+$scope.vm.currentCustomer.address.detail;

            CustomerManageService.retrieveLocation(address).then(function (value) {
                var location = value.geocodes[0].location;
                $scope.currentOrder.recvLongitude = parseFloat(location.split(',')[0]);
                $scope.currentOrder.recvLatitude = parseFloat(location.split(',')[1]);
            })
        };

        //创建订单
        $scope.createOrder = function () {
            $scope.currentOrder.callInPhone = $scope.vm.callInPhone;
            $scope.currentOrder.payType = "PTOffline";
            $scope.currentOrder.accessType = "ATCustomService";

            //设置客户
            var tempCustomer = {userId:""};
            tempCustomer.userId = $scope.vm.currentCustomer.userId;
            $scope.currentOrder.customer = tempCustomer;



            //将订单数据提交到后台
            OrderService.createOrder($scope.currentOrder).then(function () {
                udcModal.info({"title": "处理结果", "message": "创建订单成功 "});
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "创建订单失败 "+value.message});
            })
        };

        $scope.search = function () {
            $scope.pagerCustomer.setCurPageNo(1);
            searchCustomer();
        };

        $scope.initPopUp = function () {
            udcModal.show({
                templateUrl: "./customService/customerModal.htm",
                controller: "CustomerModalCtrl",
                inputs: {
                    title: '新增客户',
                    initVal: {}
                }
            }).then(function (result) {
                if (result) {
                    searchCustomer();
                }
            })
        };

        $scope.viewDetails = function (customer) {
            $location.path('/customer/' + customer.id);
        };

        $scope.modify = function (customer) {
            if (customer==null) {
                udcModal.info({"title": "错误信息", "message": "请选择客户 "});
                return;
            };
            udcModal.show({
                templateUrl: "./customService/customerModal.htm",
                controller: "CustomerModalCtrl",
                inputs: {
                    titl e: '修改客户',
                    initVal: customer
                }
            }).then(function (result) {
                if (result) {
                    searchCustomer();
                }
            })
        };

        $scope.delete = function (customer) {
            udcModal.confirm({"title": "删除客户", "message": "是否永久删除客户信息 " + customer.name})
                .then(function (result) {
                    if (result) {
                        CustomerManageService.deleteCustomer(customer).then(function () {
                            searchCustomer();
                        });
                    }
                })
        };

        var searchOrderHistory = function () {
            //清空表格
            $scope.vm.CustomerOrderHistory = [];
            var queryParams = {
                userId:$scope.vm.currentCustomer.userId,
                pageNo: $scope.pagerHistory.getCurPageNo(),
                pageSize: $scope.pagerHistory.pageSize
            }


            OrderService.retrieveOrders(queryParams).then(function (orders) {
                $scope.pagerHistory.update($scope.qHistory, orders.total, queryParams.pageNo);
                $scope.vm.CustomerOrderHistory = orders.items;
            });
        }

        var searchCustomer = function () {
            //清空表格
            $scope.vm.customerList = [];
            $scope.vm.currentCustomer = "";
            var queryParams = {
                phone:$scope.searchParam.phone,
                addrDetail:$scope.searchParam.address,
                pageNo: $scope.pagerCustomer.getCurPageNo(),
                pageSize: $scope.pagerCustomer.pageSize
            }


            //以后修改，这里为了演示方便
            CustomerManageService.retrieveCustomers(queryParams).then(function (customers) {
                $scope.pagerCustomer.update($scope.qCustomer, customers.total, queryParams.pageNo);
                $scope.vm.customerList=customers.items;

                if($scope.vm.customerList.length>0){
                    $scope.vm.currentCustomer = $scope.vm.customerList[0];
                    //刷新订单
                    refleshOrder();
                }
            });

            //CustomerManageService.retrieveCustomerCallin(queryParams).then(function (customersCallIn) {
            //    $scope.pager.update($scope.q, customersCallIn.total, queryParams.pageNo);
            //    for(var i=0;i<customersCallIn.items.length;i++)
            //    {
            //        var customer = customersCallIn.items[i].customer;
            //        if(customer != null) {
            //            $scope.vm.customerList.push(customer);
            //        }
            //    }
            //    if($scope.vm.customerList.length>0){
            //        $scope.vm.currentCustomer = $scope.vm.customerList[0];
            //        //刷新订单
            //        refleshOrder();
            //    }
            //});
        };
        //将列表中的客户信息显示到详情
        $scope.showDetail = function (customer) {
            $scope.vm.currentCustomer = customer;
            //查询当前客户的订气记录
            searchOrderHistory();

            //刷新订单
            refleshOrder();
        };

        //商品类型改变
        $scope.goodsTypeChange = function () {
            //获取区
            if ($scope.temp.selectedGoodsType==null) {
                return;
            };
            var queryParams = {
                typeName: $scope.temp.selectedGoodsType.name,
            };
            CustomerManageService.retrieveGoods(queryParams).then(function (goods) {
                $scope.temp.goodsList = goods.items;
                $scope.temp.selectedGoods = $scope.temp.goodsList[0];
            });
        };
        var searchCustomerTest = function () {
            //清空表格
            var queryParams = {
                pageNo: 1,
                pageSize: 25
            };
            //以后修改，这里为了演示方便
            CustomerManageService.retrieveCustomers(queryParams).then(function (customers) {
                $scope.vm.CustomerAutoReportList = customers.items;
            });
        };

        var init = function () {
            $scope.pagerCustomer.pageSize=2;
            $scope.pagerHistory.pageSize=2;
            $scope.pagerCustomer.update($scope.qCustomer, 0, 1);
            $scope.pagerHistory.update($scope.qHistory, 0, 1);
            //查询商品类型
            var queryParams = {
            };
            CustomerManageService.retrieveGoodsTypes(queryParams).then(function (goodsTypes) {
                $scope.temp.goodsTypesList = goodsTypes.items;
                $scope.temp.selectedGoodsType = $scope.temp.goodsTypesList[0];
                $scope.goodsTypeChange();
            });
            //查询报修类型
            queryParams = {};
            MendSecurityComplaintService.retrieveMendTypes(queryParams).then(function (mendTypes) {
                $scope.temp.mendTypesList = mendTypes.items;
                $scope.temp.selectedMendType = $scope.temp.mendTypesList[0];
                $scope.goodsTypeChange();
            });

            //不间断供气测试
            searchCustomerTest();
        };

        init();

        //刷新维修订单的数据
        var refleshMend = function() {
            if($scope.vm.currentCustomer == null){
                udcModal.info({"title": "错误信息", "message": "请选择客户 "});
                return;
            }

            //填写默认的联系人、联系电话、送气地址
            $scope.currentMend.recvAddr = $scope.vm.currentCustomer.address;
            $scope.currentMend.recvName = $scope.vm.currentCustomer.name;
            $scope.currentMend.recvPhone = $scope.vm.currentCustomer.phone;
        }

    }]);
