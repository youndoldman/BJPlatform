'use strict';

customServiceApp.controller('CallCenterCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'CustomerManageService', 'OrderService','sessionStorage','MendSecurityComplaintService','$document','KtyService',
    function ($scope, $rootScope, $filter, $location, Constants, rootService, pager, udcModal, CustomerManageService,OrderService,sessionStorage,MendSecurityComplaintService,$document,KtyService) {

        $scope.currentKTYUser = {};
        var gotoPageCustomer = function (pageNo) {
            $scope.pagerCustomer.setCurPageNo(pageNo);
            searchCustomer();
        };
        var gotoPageHistory = function (pageNo) {
            $scope.pagerHistory.setCurPageNo(pageNo);
            searchOrderHistory();
        };

        var gotoPageCallReport = function (pageNo) {
            $scope.pagerCallReport.setCurPageNo(pageNo);
            searchCallReport();
        };

        $scope.pagerCallReport = pager.init('CustomerListCtrl', gotoPageCallReport);

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
            $('#datetimepickerSecurity').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            $('#datetimepickerComplaint').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                //sideBySide:true,
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            //查询电话记录
            $('#datetimepickerStart').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            $('#datetimepickerEnd').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
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
            $('#datetimepickerRepair').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.currentMend.reserveTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#datetimepickerSecurity').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.currentSecurity.reserveTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#datetimepickerComplaint').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.currentComplaint.reserveTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            $('#datetimepickerStart').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.startTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            //查询电话记录
            $('#datetimepickerEnd').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
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
            selectedMendType:{},//当前选择的报修类型
            securityTypesList:[],//安检类型
            selectedSecurityType:{},//当前选择的安检类型
            complaintTypesList:[],//投诉类型
            selectedComplaintType:{},//当前选择的投诉类型
        };
        $scope.q = {
            startTime:null,
            endTime:null
        };
        $scope.searchParam = {
            customerID:null,
            customerName:null,
            phone:null,
            address:null
        };

        $scope.qCustomer = {};
        $scope.qHistory = {};
        $scope.qCallReport = {};

        $scope.vm = {
            callInPhone:null,//呼入电话
            callOutPhone:null,//呼出电话
            currentCustomer:null,
            CustomerList: [],
            CustomerOrderHistory: [],
            CustomerAutoReportList:[],//不间断供气客户
            currentCustomerCredit:null,//当前用户欠款

            callReportList:[],
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
        //当前安检信息
        $scope.currentSecurity= {
            securityType:{},//安检类型
            customer:{},//安检用户
            recvName:null,//联系人
            recvPhone:null,//联系电话
            recvAddr:{},//安检地址
            detail:null,//安检内容
            reserveTime:null,//预约时间
        };
        //当前投诉信息
        $scope.currentComplaint= {
            complaintType:{},//投诉类型
            customer:{},//投诉用户
            recvName:null,//联系人
            recvPhone:null,//联系电话
            recvAddr:{},//投诉地址
            detail:null,//投诉内容
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
            if($scope.vm.currentCustomer==null){
                udcModal.info({"title": "错误提示", "message": "请选择客户！"});
                return;
            }
            $scope.currentOrder.callInPhone = $scope.vm.callInPhone;
            //$scope.currentOrder.payType = "PTOffline";
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
                    initVal: $scope.vm.callInPhone//增加呼入号码的传入
                }
            }).then(function (result) {
                if (result.value) {
                    var queryParams = {
                        userId:result.param,
                        pageNo: $scope.pagerCustomer.getCurPageNo(),
                        pageSize: $scope.pagerCustomer.pageSize
                    };
                    searchCustomerByParams(queryParams);
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
            }
            udcModal.show({
                templateUrl: "./customService/customerModal.htm",
                controller: "CustomerModalCtrl",
                inputs: {
                    title: '修改客户',
                    initVal: customer
                }
            }).then(function (result) {
                if (result.value) {
                    var queryParams = {
                        userId:result.param,
                        pageNo: $scope.pagerCustomer.getCurPageNo(),
                        pageSize: $scope.pagerCustomer.pageSize
                    };
                    searchCustomerByParams(queryParams);
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

        $scope.searchReport = function(){
            $scope.vm.callReportList = null;
            $scope.pagerCallReport.setCurPageNo(1);
            searchCallReport();
        }

        var searchCallReport = function () {
            $scope.vm.callReportList = [];

            var userName = $scope.currentKTYUser.items[0].userId;
            var password = $scope.currentKTYUser.items[0].password;
            KtyService.authenticate(userName,password).then(function (response) {
                $scope.q.token = response.data.token;
                var queryParams = {
                    order:"sec",
                    page: $scope.pagerCallReport.getCurPageNo(),
                    per_page: $scope.pagerCallReport.pageSize,
                    sortBy:"durationInSec",
                    from:$scope.q.startTime,
                    to:$scope.q.endTime
                }

                KtyService.retrieveCallreport(queryParams, $scope.q.token).then(function (response) {
                    $scope.vm.callReportList = response.data;
                    console.log($scope.vm.callReportList);
                    console.log($scope.vm.callReportList.data.length);
                    $scope.pagerCallReport.update($scope.qCallReport, response.total, queryParams.pageNo);
                }, function(value) {
                    if(value.code == "40007")
                    {
                        $scope.vm.dataList = null;
                        udcModal.info({"title": "查询失败", "message": "用户认证不存在或已过期"});
                    }
                    else if(value.code == "50000")
                    {
                        $scope.vm.dataList = null;
                        udcModal.info({"title": "查询失败", "message": "系统内部错误"});
                    }
                });
            }, function(value) {
                if(value.code == "40001")
                {
                    $scope.vm.dataList = null;
                    udcModal.info({"title": "连接结果", "message": "用户名或密码不正确"});
                }
                else if(value.code == "40002")
                {
                    $scope.vm.dataList = null;
                    udcModal.info({"title": "连接结果", "message": "用户名或密码为空"});
                }
                else if(value.code == "40003")
                {
                    $scope.vm.dataList = null;
                    udcModal.info({"title": "连接结果", "message": "用户权限不正确"});
                }
                else if(value.code == "40004")
                {
                    $scope.vm.dataList = null;
                    udcModal.info({"title": "连接结果", "message": "用户认证不存在或已过期"});
                }
                else if(value.code == "50000")
                {
                    $scope.vm.dataList = null;
                    udcModal.info({"title": "连接结果", "message": "系统内部错误"});
                }
            })

        }

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
            $scope.vm.CustomerList = [];
            $scope.vm.currentCustomer = "";
            var queryParams = {
                userId: $scope.searchParam.customerID,
                userName: $scope.searchParam.customerName,
                phone: $scope.searchParam.phone,
                addrDetail: $scope.searchParam.address,
                pageNo: $scope.pagerCustomer.getCurPageNo(),
                pageSize: $scope.pagerCustomer.pageSize
            }

            //以后修改，这里为了演示方便
            CustomerManageService.retrieveCustomers(queryParams).then(function (customers) {
                $scope.pagerCustomer.update($scope.qCustomer, customers.total, queryParams.pageNo);
                $scope.vm.CustomerList = customers.items;

                if ($scope.vm.CustomerList.length > 0) {
                    $scope.vm.currentCustomer = $scope.vm.CustomerList[0];
                    $scope.showDetail($scope.vm.currentCustomer);
                }
            });
        };

        var searchCustomerByParams = function (Params) {
            //清空表格
            $scope.vm.CustomerList = [];
            $scope.vm.currentCustomer = "";

            //以后修改，这里为了演示方便
            CustomerManageService.retrieveCustomers(Params).then(function (customers) {
                $scope.pagerCustomer.update($scope.qCustomer, customers.total, Params.pageNo);
                $scope.vm.CustomerList = customers.items;

                if ($scope.vm.CustomerList.length > 0) {
                    $scope.vm.currentCustomer = $scope.vm.CustomerList[0];
                    $scope.showDetail($scope.vm.currentCustomer);
                }
            });
        };

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

//将列表中的客户信息显示到详情
        $scope.showDetail = function (customer) {
            $scope.vm.currentCustomer = customer;
            //查询当前用户欠款
            retrieveCustomerCredit();
            //查询当前客户的订气记录
            searchOrderHistory();
            //刷新订单
            refleshOrder();
            //刷新报修订单信息
            refleshMend();
            //刷新安检订单信息
            refleshSecurity();
            //刷新投诉订单信息
            refleshComplaint();
        };

//商品类型改变
        $scope.goodsTypeChange = function () {
            //获取区
            if ($scope.temp.selectedGoodsType==null) {
                return;
            };
            if($scope.vm.currentCustomer == null){
                var queryParams = {
                    typeName: $scope.temp.selectedGoodsType.name,
                    status:0,//0 正常上市,
                };
            }
            else {
                var queryParams = {
                    typeName: $scope.temp.selectedGoodsType.name,
                    status:0,//0 正常上市,
                    province:$scope.vm.currentCustomer.address.province,
                    city:$scope.vm.currentCustomer.address.city,
                    county:$scope.vm.currentCustomer.address.county,
                };
            }
//console.info(queryParams);
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
            $scope.currentKTYUser = sessionStorage.getKTYCurUser();

            $scope.pagerCallReport.pageSize = 10;
            $scope.pagerCallReport.update($scope.qCallReport, 0, 1);

            $scope.pagerCustomer.pageSize=3;
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
            });
            //查询安检类型
            queryParams = {};
            MendSecurityComplaintService.retrieveSecurityTypes(queryParams).then(function (securityTypes) {
                $scope.temp.securityTypesList = securityTypes.items;
                $scope.temp.selectedSecurityType = $scope.temp.securityTypesList[0];
            });
            //查询投诉类型
            queryParams = {};
            MendSecurityComplaintService.retrieveComplaintTypes(queryParams).then(function (complaintTypes) {
                $scope.temp.complaintTypesList = complaintTypes.items;
                $scope.temp.selectedComplaintType = $scope.temp.complaintTypesList[0];
            });
            //不间断供气测试
            //searchCustomerTest();
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
        };


//创建报修单
        $scope.createMend = function () {
            if($scope.vm.currentCustomer==null){
                udcModal.info({"title": "错误提示", "message": "请选择客户！"});
                return;
            }
            //设置客户
            var tempCustomer = {userId:""};
            tempCustomer.userId = $scope.vm.currentCustomer.userId;
            $scope.currentMend.customer = tempCustomer;
            $scope.currentMend.mendType =  $scope.temp.selectedMendType;

            //将订单数据提交到后台
            MendSecurityComplaintService.createMend($scope.currentMend).then(function () {
                udcModal.info({"title": "处理结果", "message": "创建报修单成功 "});
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "创建报修单失败 "+value.message});
            })
        };

//刷新安检订单的数据
        var refleshSecurity = function() {
            if($scope.vm.currentCustomer == null){
                udcModal.info({"title": "错误信息", "message": "请选择客户 "});
                return;
            }
            //填写默认的联系人、联系电话、送气地址
            $scope.currentSecurity.recvAddr = $scope.vm.currentCustomer.address;
            $scope.currentSecurity.recvName = $scope.vm.currentCustomer.name;
            $scope.currentSecurity.recvPhone = $scope.vm.currentCustomer.phone;
        };


//创建安检单
        $scope.createSecurity = function () {
            if($scope.vm.currentCustomer==null){
                udcModal.info({"title": "错误提示", "message": "请选择客户！"});
                return;
            }
            //设置客户
            var tempCustomer = {userId:""};
            tempCustomer.userId = $scope.vm.currentCustomer.userId;
            $scope.currentSecurity.customer = tempCustomer;
            $scope.currentSecurity.securityType =  $scope.temp.selectedSecurityType;

            //将订单数据提交到后台
            MendSecurityComplaintService.createSecurity($scope.currentSecurity).then(function () {
                udcModal.info({"title": "处理结果", "message": "创建安检单成功 "});
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "创建安检单失败 "+value.message});
            })
        };

//刷新投诉订单的数据
        var refleshComplaint = function() {
            if($scope.vm.currentCustomer == null){
                udcModal.info({"title": "错误信息", "message": "请选择客户 "});
                return;
            }
            //填写默认的联系人、联系电话、送气地址
            $scope.currentComplaint.recvAddr = $scope.vm.currentCustomer.address;
            $scope.currentComplaint.recvName = $scope.vm.currentCustomer.name;
            $scope.currentComplaint.recvPhone = $scope.vm.currentCustomer.phone;
        };


//创建投诉单
        $scope.createComplaint = function () {
            if($scope.vm.currentCustomer==null){
                udcModal.info({"title": "错误提示", "message": "请选择客户！"});
                return;
            }
            //设置客户
            var tempCustomer = {userId:""};
            tempCustomer.userId = $scope.vm.currentCustomer.userId;
            $scope.currentComplaint.customer = tempCustomer;
            $scope.currentComplaint.complaintType =  $scope.temp.selectedComplaintType;

            //将订单数据提交到后台
            MendSecurityComplaintService.createComplaint($scope.currentComplaint).then(function () {
                udcModal.info({"title": "处理结果", "message": "创建投诉单成功 "});
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "创建投诉单失败 "+value.message});
            })
        };

        $scope.config = {
            haveCylinders:[{name:"是",code:true},{name:"否",code:false}],
            customerStatus:[{name:"正常",code:0},{name:"注销",code:1}],

        };

        $scope.clearWindow = function(){
            $scope.vm.currentCustomerCredit= null;
            $scope.vm.callInPhone = null;
            $scope.vm.currentCustomer = null;
            $scope.vm.CustomerList = [];
            $scope.vm.CustomerOrderHistory = [];
            $scope.currentOrder = {
                orderDetailList:[]
            };
            $scope.vm.callReportList=[];
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
            //当前安检信息
            $scope.currentSecurity= {
                securityType:{},//安检类型
                customer:{},//安检用户
                recvName:null,//联系人
                recvPhone:null,//联系电话
                recvAddr:{},//安检地址
                detail:null,//安检内容
                reserveTime:null,//预约时间
            };
            //当前投诉信息
            $scope.currentComplaint= {
                complaintType:{},//投诉类型
                customer:{},//投诉用户
                recvName:null,//联系人
                recvPhone:null,//联系电话
                recvAddr:{},//投诉地址
                detail:null,//投诉内容
                reserveTime:null,//预约时间
            };

            //搜索条件
            $scope.searchParam = {
                customerID:null,
                customerName:null,
                phone:null,
                address:null
            };
            $scope.pagerCustomer.update($scope.qCustomer, 0, 1);
            $scope.pagerHistory.update($scope.qHistory, 0, 1);
        };
        $document.bind("keypress", function(event) {

            if(event.keyCode==10){//ctrl+enter
                $scope.clearWindow();
                $scope.$apply();
            }
        });

//查询当前用户欠款

        var retrieveCustomerCredit = function(){
            var queryParams = {
                userId:$scope.vm.currentCustomer.userId
            };
            CustomerManageService.retrieveCustomerCredit(queryParams).then(function (credit) {
                var CreditList = credit.items;
                if(CreditList.length==0){
                    $scope.vm.currentCustomerCredit = 0+"元";
                }else{
                    $scope.vm.currentCustomerCredit = credit.items[0].amount+"元";
                }

            }, function(value) {
                udcModal.info({"title": "错误信息", "message": "查询当前用户欠款信息失败 "+value.message});
            })
        };


//监听来电事件
        $scope.$on("Event_Callin", function(e, m) {
            $scope.clearWindow();
            $scope.vm.callInPhone = m;
            //查询相关联的客户
            searchCallInCustomer($scope.vm.callInPhone);
        });


//监听呼出号码变化的事件
        $scope.$on("Event_CallOutPhoneChanged", function(e, m) {
            $scope.vm.callOutPhone = m;
            //查询相关联的客户
            if((m=="")||(m==null)){

            }else{
                //searchCallInCustomer($scope.vm.callOutPhone);//以后有中继线路再放开
            }

        });


//查询呼入电话的曾经订气的客户
        var searchCallInCustomer = function (callInPhone) {
            //清空表格
            $scope.vm.CustomerList = [];
            $scope.vm.currentCustomer = "";
            var queryParams = {
                phone:callInPhone,
                pageSize: $scope.pagerCustomer.pageSize,
                pageNo: 1
            };

            CustomerManageService.retrieveCustomerCallin(queryParams).then(function (customers) {
                $scope.pagerCustomer.update($scope.qCustomer, customers.total, 1);
                $scope.vm.CustomerList = [];
                $scope.vm.currentCustomer = null;
                for(var i=0; i<customers.items.length; i++){
                    var tempCustomer = customers.items[i].customer;
                    $scope.vm.CustomerList.push(tempCustomer);
                }

                if($scope.vm.CustomerList.length>0){
                    $scope.vm.currentCustomer = $scope.vm.CustomerList[0];
                    $scope.showDetail($scope.vm.currentCustomer);
                }else{
                    //如果没有订气记录,按照来电号码搜寻客户资料
                    var queryParams = {
                        phone:$scope.vm.callInPhone,
                        pageNo: $scope.pagerCustomer.getCurPageNo(),
                        pageSize: $scope.pagerCustomer.pageSize
                    };
                    searchCustomerByParams(queryParams);
                }
            });

        };
    }]);
