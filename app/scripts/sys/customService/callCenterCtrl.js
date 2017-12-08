'use strict';

customServiceApp.controller('CallCenterCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'CustomerManageService', 'CallCenterService',function ($scope, $rootScope, $filter, $location, Constants,
                                                                           rootService, pager, udcModal, CustomerManageService,CallCenterService) {
        var gotoPage = function (pageNo) {
            $scope.pager.setCurPageNo(pageNo);
            searchCustomer();
        };
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

        };

        $scope.searchParam = {
            phone:null,
            address:null
        };

        $scope.pager = pager.init('CustomerListCtrl', gotoPage);
        var historyQ = $scope.pager.getQ();

        $scope.q = {
            CustomerName: historyQ.CustomerName || "",
            CustomerPhone: historyQ.CustomerPhone || "",
            CustomerAddress: historyQ.CustomerAddress || ""
        };

        $scope.vm = {
            callInPhone:"13913015340",
            currentCustomer:null,
            CustomerList: [],
            CustomerOrderHistory: [],
        };


        //当前订单信息
        $scope.currentOrder= {
            orderDetailList:[],
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

            //查询经纬度
            var address = $scope.vm.currentCustomer.address.province+$scope.vm.currentCustomer.address.city
                +$scope.vm.currentCustomer.address.county+$scope.vm.currentCustomer.address.detail;

            CustomerManageService.retrieveSubdistrict(address).then(function (value) {
                var location = value.geocodes[0].location;
                $scope.currentOrder.recvLongitude = location.split(',')[0];
                $scope.currentOrder.recvLatitude = location.split(',')[1];
            })

            //将订单数据提交到后台
            CallCenterService.createOrder($scope.currentOrder).then(function () {
                udcModal.info({"title": "处理结果", "message": "创建订单成功 "});
                $scope.close(true);
            })
        };


        $scope.search = function () {
            $scope.pager.setCurPageNo(1);
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
                    title: '修改客户',
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

        var searchCustomer = function () {
            //清空表格
            $scope.vm.customerList = [];
            $scope.vm.currentCustomer = "";
            var queryParams = {
                phone:$scope.searchParam.phone,
                detail:$scope.searchParam.address,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            }

            console.log(queryParams);

            CustomerManageService.retrieveCustomerCallin(queryParams).then(function (customersCallIn) {
                $scope.pager.update($scope.q, customersCallIn.total, queryParams.pageNo);
                for(var i=0;i<customersCallIn.items.length;i++)
                {
                    var customer = customersCallIn.items[i].customer;
                    if(customer != null) {
                        $scope.vm.customerList.push(customer);
                    }
                }
                if($scope.vm.customerList.length>0){
                    $scope.vm.currentCustomer = $scope.vm.customerList[0];
                    //刷新订单
                    refleshOrder();
                }
            });
        };
        //将列表中的客户信息显示到详情
        $scope.showDetail = function (customer) {
            $scope.vm.currentCustomer = customer;
            //查询当前客户的订气记录
            //清空表格
            $scope.vm.CustomerOrderHistory = [];
            var queryParams = {
                userId:$scope.vm.currentCustomer.userId,
                pageNo: $scope.pager.getCurPageNo(),
                pageSize: $scope.pager.pageSize
            }


            CustomerManageService.retrieveCustomerCallin(queryParams).then(function (customersCallIn) {
                $scope.pager.update($scope.q, customersCallIn.total, queryParams.pageNo);
                $scope.vm.CustomerOrderHistory = customersCallIn.items;
            });

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
                goodsType: $scope.temp.selectedGoodsType,
            };
            CustomerManageService.retrieveGoods(queryParams).then(function (goods) {
                $scope.temp.goodsList = goods.items;
                $scope.temp.selectedGoods = $scope.temp.goodsList[0];
            });

        }

        var init = function () {
            //呼叫中心初始化
            callCenterLogin();
            $scope.pager.update($scope.q, 0, 1);
            //查询商品类型
            var queryParams = {
            };
            CustomerManageService.retrieveGoodsTypes(queryParams).then(function (goodsTypes) {
                $scope.temp.goodsTypesList = goodsTypes.items;
                $scope.temp.selectedGoodsType = $scope.temp.goodsTypesList[0];
            });
            $scope.goodsTypeChange();
        };

        init();
    }]);
