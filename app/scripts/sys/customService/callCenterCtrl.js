'use strict';

customServiceApp.controller('CallCenterCtrl', ['$scope', '$rootScope', '$filter', '$location', 'Constants',
    'rootService', 'pager', 'udcModal', 'CustomerManageService', 'OrderService','sessionStorage','MendSecurityComplaintService','$document','KtyService','$interval',
    function ($scope, $rootScope, $filter, $location, Constants, rootService, pager, udcModal, CustomerManageService,OrderService,sessionStorage,MendSecurityComplaintService,$document,KtyService,$interval) {

        $scope.currentKTYUser = {};
        $scope.callIn = "呼入";
        $scope.callOut = "呼出";

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

        var gotoPageMissedCallReport = function (pageNo) {
            $scope.pagerMissedCallReport.setCurPageNo(pageNo);
            //$scope.searchMissedCallReport()
            searchMissedCall();
        };
        //托盘报警
        var gotoPageGasCynTrayWarning = function (pageNo) {
            $scope.pagerGasCynTrayWarning.setCurPageNo(pageNo);
            $scope.searchUninterruptCustomers();
        };

        //客户意见
        var gotoPageAdvice = function (pageNo) {
            $scope.pagerAdvice.setCurPageNo(pageNo);
            $scope.searchAdvice();
        };
        $scope.pagerCallReport = pager.init('CustomerListCtrl', gotoPageCallReport);

        $scope.pagerMissedCallReport = pager.init('CustomerListCtrl', gotoPageMissedCallReport);

        $scope.pagerCustomer = pager.init('CustomerListCtrl', gotoPageCustomer);

        $scope.pagerHistory = pager.init('CustomerListCtrl', gotoPageHistory);


        $scope.pagerGasCynTrayWarning = pager.init('CustomerListCtrl', gotoPageGasCynTrayWarning);

        $scope.pagerAdvice = pager.init('CustomerListCtrl', gotoPageAdvice);


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
            //查询未接电话记录
            $('#datetimepickerStart1').datetimepicker({
                format: 'YYYY-MM-DD HH:mm',
                locale: moment.locale('zh-cn'),
                showTodayButton:true,
                toolbarPlacement:'top',
            });
            $('#datetimepickerEnd1').datetimepicker({
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
            //查询电话记录
            $('#datetimepickerStart').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.startTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });

            $('#datetimepickerEnd').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.endTime = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });
            //查询未接电话记录
            $('#datetimepickerStart1').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.startTime1 = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
                        +date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                });

            $('#datetimepickerEnd1').datetimepicker()
                .on('dp.change', function (ev) {
                    var date = ev.date._d;
                    var month = date.getMonth()+1;
                    $scope.q.endTime1 = date.getFullYear()+"-"+month+"-"+date.getDate()+" "
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
            orderTypesConfig: [{key:"OTTNormal",value:"按瓶普通订单"},{key:"OTTTrayWarning",value:"按斤结算订单"}],
        };
        $scope.q = {
            startTime:null,
            endTime:null,
            startTime1:null,
            endTime1:null,
            ani:null,//客户号码
            dnis:null,//客服号码
            ani1:null,//客户号码
            dnis1:null,//客服号码
            userIdUninterupt:null,//不间断供气报警用户名
            userIdUninteruptDealed:null//不间断供气报警处理用户名
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
        $scope.qMissedCallReport = {};
        $scope.qGasCynTrayWarning = {};

        $scope.currentUser = {};
        $scope.vm = {
            callInPhone:"00000000",//呼入电话
            callOutPhone:null,//呼出电话
            currentCustomer:null,
            currentTicketCount:null,//当前气票张数
            CustomerList: [],
            CustomerOrderHistory: [],
            CustomerAutoReportList:[ //不间断供气客户
            ],
            currentCustomerCredit:null,//当前用户欠款
            callReportList:[],
            missedCallReportList:[],
            orderStatusDisplay:["待派送","派送中","待核单","已完成","已作废"],
            missCallCount:0,
            currentAdvice:{userId:null,advice:null},//当前的客户的意见
            adviceHistorylList:[]//当前的客户的意见历史
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
            orderDetail.dealPrice = $scope.temp.selectedGoods.realPrice;
            orderDetail.quantity = $scope.temp.selectedQuantity;
            orderDetail.subtotal = $scope.temp.selectedQuantity * orderDetail.dealPrice;
            // $scope.currentOrder.orderDetailList.push(orderDetail);

            if($scope.currentOrder.orderDetailList.length == 0)
            {
                $scope.currentOrder.orderDetailList.push(orderDetail);
            }
            else
            {
                var findFlag = false;
                for(var i=0; i< $scope.currentOrder.orderDetailList.length; i++) {
                    if(orderDetail.goods.code == $scope.currentOrder.orderDetailList[i].goods.code)
                    {
                        $scope.currentOrder.orderDetailList[i].quantity++;
                        $scope.currentOrder.orderDetailList[i].subtotal = $scope.currentOrder.orderDetailList[i].quantity * orderDetail.dealPrice;
                        findFlag = true;
                        break;
                    }
                }
                if(!findFlag){
                    $scope.currentOrder.orderDetailList.push(orderDetail);
                }
                console.info($scope.currentOrder.orderDetailList);
            }

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
            if($scope.vm.callInPhone==null){
                udcModal.info({"title": "错误提示", "message": "无呼入号码！"});
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
                //订单完成后清除来电号码
                $scope.vm.callInPhone = "00000000";
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "创建订单失败 "+value.message});
            })
        };

        $scope.search = function () {
            $scope.pagerCustomer.setCurPageNo(1);

            searchCustomer();
        };


        $scope.searchWaringCustomer = function (userId) {

            $scope.q.userIdUninteruptDealed = userId;


            $scope.pagerCustomer.setCurPageNo(1);
            $scope.searchParam.customerID = userId;
            $scope.searchParam.customerName = null;
            $scope.searchParam.phone = null;
            $scope.searchParam.address = null;
            $scope.currentOrder.orderTriggerType = "OTTTrayWarning";

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
                $scope.pagerCustomer.setCurPageNo(1);
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
            $scope.vm.callReportList = [];
            $scope.pagerCallReport.setCurPageNo(1);

            searchCallReport();
        }

        var searchCallReport = function () {
            $scope.vm.callReportList = [];

            var query = {};
            //CustomerManageService.retrieveCloudUsers(query).then(function (cloudUsers) {
            //    if(cloudUsers.total !=0 )
            //    {
            //$scope.currentKTYUser = cloudUsers.items[0];
            console.info($scope.currentKTYUser)
            var userName = $scope.currentKTYUser.items[0].userId;
            var password = $scope.currentKTYUser.items[0].password;

            var jsonData = {
                ani:null,
                dnis:null,
            }
            jsonData.ani = $scope.q.ani;
            jsonData.dnis = $scope.q.dnis;

            KtyService.authenticate(userName,password).then(function (response) {
                $scope.q.token = response.data.token;

                var queryParams = {
                    order:"desc",
                    page: $scope.pagerCallReport.getCurPageNo(),
                    per_page: $scope.pagerCallReport.pageSize,
                    sortBy:"createTime",
                    from:$scope.q.startTime,
                    to:$scope.q.endTime,
                }

                queryParams.json = jsonData;
                KtyService.retrieveCallreportSearch(queryParams, $scope.q.token).then(function (response) {
                    $scope.vm.callReportList = response.data;
                    console.log($scope.vm.callReportList);

                    for(var  i =0; i < $scope.vm.callReportList.data.length; i++)
                    {
                        if($scope.vm.callReportList.data[i].direction == "ib")
                        {
                            $scope.vm.callReportList.data[i].directionCN = "呼入";
                        }
                        else{
                            $scope.vm.callReportList.data[i].directionCN = "呼出";
                        }
                    }

                    $scope.pagerCallReport.update($scope.qCallReport, response.data.totalElements, queryParams.page);
                }, function(value) {
                    udcModal.info({"title": "查询失败", "message": value.message});
                });

            }, function(value) {
                udcModal.info({"title": "连接结果", "message": value.message});
            })
            //    }
            //})
        }


        $scope.searchMissedCallReport = function(){
            $scope.vm.missedCallReportList = [];
            $scope.pagerMissedCallReport.setCurPageNo(1);
            searchMissedCall();
        };

        //登录授权，获取token
        var getToken = function(){
            var userName = $scope.currentKTYUser.items[0].userId;
            var password = $scope.currentKTYUser.items[0].password;
            KtyService.authenticate(userName,password).then(function (response) {
                $scope.q.token = response.data.token;
            }, function(value) {
                udcModal.info({"title": "授权失败", "message": value.message});

            })
        };

        //查询未接来电记录
        var searchMissedCall = function(){
            var jsonData = {
                //ani:$scope.q.ani1,
                //dnis:$scope.q.dnis1,
                billingInSec:0,
                direction:"ib"
            };
            var date = new Date();
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            var day = date.getDate();
            var startTime = year + "-" + month + "-" + day + " 00:00:00" ;



            var queryParams = {
                order:"desc",
                page: $scope.pagerMissedCallReport.getCurPageNo(),
                per_page: $scope.pagerMissedCallReport.pageSize,
                sortBy:"createTime",
                from:startTime,
                to:"2028-08-18 00:00:00",
                //from:$scope.q.startTime1,
                //to:$scope.q.endTime1,
            };
            queryParams.json = jsonData;

            KtyService.retrieveCallreportSearch(queryParams, $scope.q.token).then(function (response) {
                if($scope.vm.missCallCount!=response.data.totalElements){
                    $scope.vm.missCallCount=response.data.totalElements;
                    $scope.vm.missedCallReportList = response.data;
                    $scope.pagerMissedCallReport.update($scope.qMissedCallReport, response.data.totalElements, queryParams.page);
                }

            }, function(value) {
                //udcModal.info({"title": "查询失败", "message": value.message});
            });

        }

        var searchOrderHistory = function () {
            //清空表格
            $scope.vm.CustomerOrderHistory = [];
            var queryParams = {
                userId:$scope.vm.currentCustomer.userId,
                pageNo: $scope.pagerHistory.getCurPageNo(),
                pageSize: $scope.pagerHistory.pageSize,
                orderBy: "id desc"
            };
            OrderService.retrieveOrders(queryParams).then(function (orders) {
                $scope.pagerHistory.update($scope.qHistory, orders.total, queryParams.pageNo);
                $scope.vm.CustomerOrderHistory = orders.items;
                //添加订单详情概要
                for(var i=0; i<$scope.vm.CustomerOrderHistory.length;i++){
                    var description ="地址："+$scope.vm.CustomerOrderHistory[i].recvAddr.province+
                        $scope.vm.CustomerOrderHistory[i].recvAddr.city+
                        $scope.vm.CustomerOrderHistory[i].recvAddr.county+
                        $scope.vm.CustomerOrderHistory[i].recvAddr.detail+"\r\n规格：";
                    for(var j=0;j<$scope.vm.CustomerOrderHistory[i].orderDetailList.length;j++){
                        description = description+$scope.vm.CustomerOrderHistory[i].orderDetailList[j].goods.name
                        +" × "+ $scope.vm.CustomerOrderHistory[i].orderDetailList[j].quantity+"\r\n           ";
                    }
                    $scope.vm.CustomerOrderHistory[i].description = description;
                    console.log($scope.vm.CustomerOrderHistory);
                }
            });
        };

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

                console.info($scope.vm.currentCustomer);

                var queryParams1 = {
                    typeCode: $scope.temp.selectedGoodsType.code,
                    status:0,//0 正常上市,
                    province:$scope.vm.currentCustomer.address.province,
                    city:$scope.vm.currentCustomer.address.city,
                    county:$scope.vm.currentCustomer.address.county,
                    cstUserId:$scope.vm.currentCustomer.userId,
                };
                console.info(queryParams1);
                CustomerManageService.retrieveGoods(queryParams1).then(function (goods) {
                    $scope.temp.goodsList = goods.items;
                    console.info(goods.items)
                    $scope.temp.selectedGoods = $scope.temp.goodsList[0];
                    console.info($scope.temp.selectedGoods)
                });
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

        $scope.showMissCallDetail = function (phone) {
            $scope.pagerCustomer.setCurPageNo(1);
            var queryParams = {
                phone:phone,
                pageNo: $scope.pagerCustomer.getCurPageNo(),
                pageSize: $scope.pagerCustomer.pageSize
            };
            searchCustomerByParams(queryParams);
        };

//将列表中的客户信息显示到详情
        $scope.showDetail = function (customer) {
            $scope.vm.currentCustomer = customer;
            //查询当前用户欠款
            retrieveCustomerCredit();
            //查询当前客户的订气记录
            $scope.pagerHistory.setCurPageNo(1);
            searchOrderHistory();
            //刷新订单
            refleshOrder();
            //刷新报修订单信息
            refleshMend();
            //刷新安检订单信息
            refleshSecurity();
            //刷新投诉订单信息
            refleshComplaint();

            //更新商品
            $scope.goodsTypeChange();

            //查询气票张数
            searchTicketCount();
            //查询客户建议
            $scope.pagerAdvice.setCurPageNo(1);
            $scope.searchAdvice();

            //删除购物车
            $scope.currentOrder.orderDetailList = [];

            //托盘用户,更新订单类型为按斤结算
            if(($scope.vm.currentCustomer.customerType.code=="00003")||($scope.vm.currentCustomer.customerType.code=="00004")){
                $scope.currentOrder.orderTriggerType = "OTTTrayWarning";
            }else{
                $scope.currentOrder.orderTriggerType = "OTTNormal";
            }

        };

//商品类型改变
        $scope.goodsTypeChange = function () {
            //获取区
            if ($scope.temp.selectedGoodsType==null) {
                return;
            };
            if($scope.vm.currentCustomer == null){

                return;
            }
            else {
                var queryParams = {
                    typeCode: $scope.temp.selectedGoodsType.code,
                    status:0,//0 正常上市,
                    province:$scope.vm.currentCustomer.address.province,
                    city:$scope.vm.currentCustomer.address.city,
                    county:$scope.vm.currentCustomer.address.county,
                    cstUserId:$scope.vm.currentCustomer.userId,
                };
                console.info(queryParams);
                CustomerManageService.retrieveGoods(queryParams).then(function (goods) {
                    if(goods.items.length  != 0){
                        $scope.temp.goodsList = goods.items;
                        $scope.temp.selectedGoods = $scope.temp.goodsList[0];
                        //console.info("商品种类触发");
                        //console.info(goods.items);
                    }
                    else {
                        $scope.temp.goodsList = null;
                        $scope.temp.selectedGoods = null;
                        //udcModal.info({"title": "提醒信息", "message": "该地区不售卖此类型的商品"});
                    }
                });
            }

        };

        $scope.searchUninterruptCustomers = function(){
            //清空表格
            var queryParams = {
                userId:$scope.q.userIdUninterupt,
                warningStatus: 1,
                pageNo: $scope.pagerGasCynTrayWarning.getCurPageNo(),
                pageSize: $scope.pagerGasCynTrayWarning.pageSize,
                orderBy:'id desc'
            };
            CustomerManageService.retrievePallets(queryParams).then(function (warnings) {
                $scope.vm.CustomerAutoReportList = warnings.items;
                $scope.pagerGasCynTrayWarning.update($scope.qGasCynTrayWarning, warnings.total, queryParams.page);
            });
        };

        var init = function () {

            $scope.timerGetClouderUser = $interval(function(){
                $scope.currentKTYUser = sessionStorage.getKTYCurUser();
                if($scope.currentKTYUser != null)
                {
                    //获取token
                    getToken();
                    $interval.cancel($scope.timerGetClouderUser);
                }
            }, 1000);

            $scope.pagerCallReport.pageSize = 10;
            $scope.pagerCallReport.update($scope.qCallReport, 0, 1);

            $scope.pagerMissedCallReport.pageSize = 20;
            $scope.pagerMissedCallReport.update($scope.qMissedCallReport, 0, 1);

            $scope.pagerGasCynTrayWarning.pageSize = 10;
            $scope.pagerGasCynTrayWarning.update($scope.qGasCynTrayWarning, 0, 1);

            $scope.pagerCustomer.pageSize=3;
            $scope.pagerHistory.pageSize=2;
            $scope.pagerCustomer.update($scope.qCustomer, 0, 1);
            $scope.pagerHistory.update($scope.qHistory, 0, 1);

            $scope.pagerAdvice.pageSize = 5;
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


            //启动托盘漏气报警的检查线程
            $scope.timerCheckLeak = $interval( function(){
                $scope.searchUninterruptCustomers();//不间断供气报警客户查询
                checkLeak();
            }, 5000);

            //启动未接来电监听线程
            $scope.timerSearchMissedCall = $interval( function(){
                searchMissedCall();
            }, 5000);
            //初始化订单类型为普通订单
            $scope.currentOrder.orderTriggerType = "OTTNormal";

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
            $scope.vm.currentTicketCount = null;
            $scope.vm.currentCustomerCredit= null;
            //订单完成后清除来电号码
            $scope.vm.callInPhone = "00000000";
            $scope.vm.currentCustomer = null;
            $scope.vm.CustomerList = [];
            $scope.vm.CustomerOrderHistory = [];

            $scope.currentOrder = {
                orderDetailList:[],
                orderTriggerType:"OTTNormal"//普通订单
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
        //快捷键
        $document.bind("keypress", function(event) {
            if(event.keyCode==10){//ctrl+enter
                $scope.clearWindow();
                $scope.$apply();
            }
        });

        $document.bind("keypress", function(event) {
            if(event.keyCode==13){//enter
                $scope.search();
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
            $scope.pagerCustomer.setCurPageNo(1);
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
                pageNo: 1,
                orderBy:"id desc"
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

        //弹消息
        var showNotification = function (warning) {
            $.notify("漏气提醒！    客户id:"+warning.user.userId+" ｜ 客户姓名:"
                +warning.user.name+" ｜ 托盘号:"+warning.number+" ｜ "+warning.leakStatus.name, {
                type: 'danger',
                newest_on_top: true,
            });
        };

        //漏气报警检查
        var checkLeak = function () {
            //1级告警
            var queryParams = {
                leakStatus: 1
            };
            CustomerManageService.retrievePallets(queryParams).then(function (warnings) {
                var warningsList = warnings.items;

                for(var i=0; i<warningsList.length; i++){
                    if(warningsList[i].user!=null){
                        showNotification(warningsList[i]);
                    }
                }
            });

            //2级告警
            queryParams = {
                leakStatus: 2
            };
            CustomerManageService.retrievePallets(queryParams).then(function (warnings) {
                var warningsList = warnings.items;

                for(var i=0; i<warningsList.length; i++){
                    if(warningsList[i].user!=null){
                        showNotification(warningsList[i]);
                    }
                }
            });

        };

        //查询剩余气票张数
        var searchTicketCount = function () {
            var queryParams = {
                customerUserId:$scope.vm.currentCustomer.userId,
                useStatus:0,
                pageNo: 1,
                pageSize: 1
            };
            OrderService.retrieveTicket(queryParams).then(function (tickets) {
                $scope.vm.currentTicketCount = tickets.total;
            });
        };

        //催气窗口
        $scope.pressedOrder = function (CustomerOrder) {
            udcModal.show({
                templateUrl: "./customService/pressedOrderHistoryModal.htm",
                controller: "PressedOrderHistoryModalCtrl",
                inputs: {
                    title: '催气提醒',
                    initVal: CustomerOrder
                }
            }).then(function (result) {
                if (result) {
                }
            })
        };

        //查询客户意见
        $scope.searchAdvice = function(){
            //清空表格
            $scope.vm.adviceHistorylList = [];
            var queryParams = {
                userId:$scope.vm.currentCustomer.userId,
                pageNo: $scope.pagerAdvice.getCurPageNo(),
                pageSize:$scope.pagerAdvice.pageSize,
                orderBy:"id desc"
            };
            CustomerManageService.retrieveAdvice(queryParams).then(function (advices) {
                $scope.vm.adviceHistorylList = advices.items;
                $scope.pagerAdvice.update($scope.q, advices.total, queryParams.page);
            });
        };

        //创建客户意见
        $scope.createAdvice = function () {
            if($scope.vm.currentCustomer==null){
                udcModal.info({"title": "错误提示", "message": "请选择客户！"});
                return;
            }
            if($scope.vm.currentAdvice.advice==null){
                udcModal.info({"title": "错误提示", "message": "请填写客户建议！"});
                return;
            }
            //设置客户
            $scope.vm.currentAdvice.userId = $scope.vm.currentCustomer.userId;

            //将订单数据提交到后台
            CustomerManageService.createAdvice($scope.vm.currentAdvice).then(function () {
                udcModal.info({"title": "处理结果", "message": "提交客户意见成功 "});
                $scope.searchAdvice();
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "提交客户意见失败 "+value.message});
            })
        };

        //删除客户意见
        $scope.deleteAdvice = function (advice) {
            //将订单数据提交到后台
            CustomerManageService.deleteAdvice(advice).then(function () {
                udcModal.info({"title": "处理结果", "message": "删除客户意见成功 "});
                $scope.searchAdvice();
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "删除客户意见失败 "+value.message});
            })
        };

        //托盘告警解除
        $scope.GasCynTrayWarningDealed = function (userId) {
            var queryParams = {
                userId:userId,
            };

            CustomerManageService.gasCynTrayWarningStatusDelete(queryParams).then(function () {
                udcModal.info({"title": "处理结果", "message": "告警处理成功 "});
                //不间断供气报警客户查询

                $scope.searchUninterruptCustomers();
            }, function(value) {
                udcModal.info({"title": "处理结果", "message": "告警处理失败 "+value.message});
            })
        };





    }]);
