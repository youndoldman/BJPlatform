'use strict';


commonModule.constant('MISC', {
    'keys': {
        'gaodeKey': 'eb79fa54c36eb2021e515ea789bb4bde',
    }
});

commonModule.constant('URI', {
    'resources': {
        'loginPage': '../pages/login.htm',//登录页
        'mainPage': '../pages/mainCenter.htm',//首页
        'users': 'http://47.106.71.160/api/sysusers',//系统用户接口
        'department': 'http://47.106.71.160/api/Department',//部门接口
        'groups': 'http://47.106.71.160/api/groups',//组接口
        'customers': 'http://47.106.71.160/api/customers',//客户接口
        'customerSource': 'http://47.106.71.160/api/CustomerSource',//客户来源查询接口
        //实际使用地址
        //'customerLevel': '../../../api/CustomerLevel',//客户等级查询接口
        //'customerType': '../../../api/CustomerType',//客户类型查询接口
        'customerLevel': 'http://47.106.71.160/api/CustomerLevel',//客户等级查询接口
        'customerType': 'http://47.106.71.160/api/CustomerType',//客户类型查询接口

        'settlementType': 'http://47.106.71.160/api/SettlementType',//结算类型信息查询接口

        'discountStrategies':'http://47.106.71.160/api/DiscountStrategies',//优惠策略接口

        'customerCallin':'http://47.106.71.160/api/CustomerCallIn',//电话关联的客户资料查询接口
        'login':'http://47.106.71.160/api/sysusers/login',//系统用户登录接口
        'logout':'http://47.106.71.160/api/sysusers/logout',//系统用户登出接口
        'orders':'http://47.106.71.160/api/Orders',　　　　　//订气订单接口
        'orderCancel':'../../../api/CancelOrder',　　　　　//订气作废接口

        'taskOrders':'../../../api/TaskOrders',//任务订单接口
        'taskOrdersDeal':'../../../api/TaskOrders/Process',//任务订单办理接口
        //'goods': '../../../api/Goods',//商品
        //'goodsTypes': '../../../api/GoodsTypes',//商品类型
        'goods': 'http://47.106.71.160/api/Goods',//商品
        'goodsTypes': 'http://47.106.71.160/api/GoodsTypes',//商品类型

        'goodsPriceHistory': 'http://47.106.71.160/api/Goods/PriceHistory',//商品价格历史
        'subdistrict':'http://restapi.amap.com/v3/config/district',//高德地图行政区域接口
        'geocode':'http://restapi.amap.com/v3/geocode/geo',//高德地图逆地址解码接口
        'bottle': 'http://47.106.71.160/api/GasCylinder',//钢瓶接口
        'bottleHandOver': '../../../api/GasCylinder/TakeOver',//钢瓶责任交接
        'gpsBind': '../../../api/GasCylinder/Bind',//定位终端绑定接口
        'gpsUnBind': '../../../api/GasCylinder/UnBind',//定位终端解除绑定接口
        'bottleSpecQuery': 'http://47.106.71.160/api/GasCylinderSpec',//钢瓶规格查询
        'cloudUser': 'http://47.106.71.160/api/CloudUser',//云客服用户接口
        'cloudUserBind': 'http://47.106.71.160/api/CloudUser/Bind',//云客服用户绑定接口
        'cloudUserUnBind': 'http://47.106.71.160/api/CloudUser/UnBind',//云客服用户解除绑定接口
        //'adjustPriceSchedules': '../../../api/AdjustPriceSchedules',//调价接口
        'adjustPriceSchedules': 'http://47.106.71.160/api/AdjustPriceSchedules',//调价接口

        'ticket': 'http://47.106.71.160/api/Ticket',//气票信息增加
        'coupon': 'http://47.106.71.160/api/Coupon',//优惠券信息增加

        'salesByBayType': 'http://47.106.71.160/api/Report/Sales/ByPayType',//查询销售日报表(按支付类型查询)
        'salesByCustomerType': 'http://47.106.71.160/api/Report/Sales/ByCustomerType',//查询销售日报表(按支付类型查询)
        'saleContacts':'http://47.106.71.160/api/Report/SaleContacts',//销售往来日报表查询
        'stock':'http://47.106.71.160/api/Report/Stock',//查询库存
        'stockInOut':'http://47.106.71.160/api/Report/StockInOut',//查询出入库数量
        'salesCash':'http://47.106.71.160/api/Report/SaleCash',//销售现金报表

        'mendType': 'http://47.106.71.160/api/MendTypes',//报修类型接口
        'mend': 'http://47.106.71.160/api/Mend',//报修单接口
        'securityType': 'http://47.106.71.160/api/SecurityTypes',//安检类型接口
        'security': 'http://47.106.71.160/api/Security',//安检单接口
        'complaintType': 'http://47.106.71.160/api/ComplaintTypes',//投诉类型接口
        'complaint': 'http://47.106.71.160/api/Complaint',//投诉单接口

        'customerCredit': 'http://47.106.71.160/api/CustomerCredit',//欠款
        'writeOff':'http://47.106.71.160/api/WriteOff',//回款
        'writeOffDetail':'http://47.106.71.160/api/WriteOffDetail',//回款明细信息查询
        'depositDetail':'http://47.106.71.160/api/DepositDetail',//增加存银行款信息记录

        'gasCylinderSpec':'http://47.106.71.160/api/GasCylinderSpec',//查询钢瓶规格
        'addGasCylinderSpec':'http://47.106.71.160/api/GasCyrDynDetail',//钢检瓶动态增加
        'reportGasCyrDyn':'http://47.106.71.160/api/Report/GasCyrDyn',//钢检瓶报表查询
        'gasCyrChargeSpec':'http://47.106.71.160/api/GasCyrChargeSpec',//钢瓶收费标准查询

        'ktyAuthenticate': 'https://cc.ketianyun.com/thirdparty/api/v1/authenticate',//科天云登录接口
        'ktyReport1': 'https://cc.ketianyun.com/thirdparty/api/v1/callsdata/agent/call',//科天云获取坐席-工作组及直拨电话统计报表
        'ktyReport2': 'https://cc.ketianyun.com/thirdparty/api/v1/callsdata/agent/operation',//科天云获取坐席-操作状态统计报表
        'ktyReport3': 'https://cc.ketianyun.com/thirdparty/api/v1/callsdata/workset/incall/waittime',//科天云获取工作组-来话等待时长统计报表
        'ktyReport4': 'https://cc.ketianyun.com/thirdparty/api/v1/callsdata/workset/incall/operatetime',//科天云获取工作组-来话处理时长统计报表
        'ktyReport5': 'https://cc.ketianyun.com/thirdparty/api/v1/callsdata/workset/incall/interval',//科天云获取工作组-来话等待时长分析报表
        'ktyReport6': 'https://cc.ketianyun.com/thirdparty/api/v1/callsdata/workset/incall/analysis',//科天云获取工作组-来话应答/放弃/溢出分析报表

        'ktyCallreport': 'https://cc.ketianyun.com/thirdparty/api/v1/callreports',//科天云获得指定时间段呼叫的记录

    }
});

commonModule.constant('NavItem', {
    // NavLv1: header, NavLv2: menuItem, NavLv3: subMenuItem
    // note: set menuItem 'href' prop to be empty when it has subMenuItems
    'Home': {
        roles:[],
        name: "首页",
        href: "./mainCenter.htm",
        menuItems: [{
            index: 0,
            name: "测试-1",
            href: "./mainCenter.htm#/main/test1",
            icon: "fa-star",
        }, {
            index: 1,
            name: "测试-2",
            href: "./mainCenter.htm#/main/test2",
            icon: "fa-twitter-square"
        },{
            index: 2,
            name: "测试-3",
            href: "./mainCenter.htm#/main/test3",
            icon: "fa-key"
        }, {
            index: 3,
            name: "测试-4",
            href: "./mainCenter.htm#/main/test4",
            icon: "fa-gears"
        }, {
            index: 4,
            name: "测试-5",
            href: "./mainCenter.htm#/main/test5",
            icon: "fa-upload"
        }, {
            index: 5,
            name: "测试-6",
            href: "./mainCenter.htm#/main/test6",
            icon: "fa-phone"
        }, {
            index: 6,
            name: "测试-7",
            href: "./mainCenter.htm#/main/test7",
            icon: "fa-tasks"
        }
        ]
    },

    'ManageCenter': {
        roles:[1],
        name: "系统管理",
        href: "./manageCenter.htm",
        menuItems: [{
            index: 0,
            name: "用户管理",
            href: "./manageCenter.htm#/manage/users",
            icon: "fa-group"
        }, {
            index: 1,
            name: "组织架构",
            target: "_blank",
            href: "./manageCenter.htm#/manage/department",
            icon: "fa-gears"
        }, {
            index: 2,
            name: "商品管理",
            target: "_blank",
            href: "./manageCenter.htm#/manage/goods",
            icon: "fa-plane"
        }, {
            index: 3,
            name: "云客服账号",
            target: "_blank",
            href: "./manageCenter.htm#/manage/cloudUser",
            icon: "fa-github"
        }]
    },
    'ShopCenter': {
        roles:[1,5],
        name: "门店管理",
        href: "./shopCenter.htm",
        menuItems: [{
            index: 0,
            name: "订单核实",
            href: "./shopCenter.htm#/ShopManage/orderCheck",
            icon: "fa-group"
        }, {
            index: 2,
            name: "订单管理",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/orderList",
            icon: "fa-plane"
        }, {
            index: 1,
            name: "库存管理",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/stockControl",
            icon: "fa-plane"
        },{
            index: 3,
            name: "报修处理",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/Mend",
            icon: "fa-plane"
        }, {
            index: 4,
            name: "安检处理",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/Security",
            icon: "fa-plane"
        }, {
            index: 5,
            name: "投诉处理",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/Complaint",
            icon: "fa-plane"
        }, {
            index: 6,
            name: "回款操作",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/MoneyReturn",
            icon: "fa-edit"
        }, {
            index: 7,
            name: "钢检瓶操作",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/checkBottle",
            icon: "fa-edit"
        }]
    },
    'CustomService': {
        roles:[1,2],
        name: "客服业务",
        href: "./customService.htm",
        menuItems: [{
            index: 0,
            name: "呼叫中心",
            href: "./customService.htm#/CustomService/callCenter",
            icon: "fa-group"
        },  {
            index: 1,
            name: "订单配送",
            target: "_blank",
            href: "./customService.htm#/CustomService/orderDelivery",
            icon: "fa-gears"
        }, {
            index: 2,
            name: "订单查询",
            target: "_blank",
            href: "./customService.htm#/CustomService/orderManage",
            icon: "fa-gears"
        }, {
            index: 3,
            name: "客户管理",
            target: "_blank",
            href: "./customService.htm#/CustomService/customerManage",
            icon: "fa-plane"
        }, {
            index: 4,
            name: "报修处理",
            target: "_blank",
            href: "./customService.htm#/CustomService/mendList",
            icon: "fa-table"
        }, {
            index: 5,
            name: "安检处理",
            target: "_blank",
            href: "./customService.htm#/CustomService/securityList",
            icon: "fa-table"
        }, {
            index: 6,
            name: "投诉处理",
            target: "_blank",
            href: "./customService.htm#/CustomService/complaintList",
            icon: "fa-table"
        },{
            index: 7,
            name: "电话统计",
            target: "_blank",
            href: "./customService.htm#/CustomService/report1",
            icon: "fa-table"
        }, {
            index: 8,
            name: "操作状态统计",
            target: "_blank",
            href: "./customService.htm#/CustomService/report2",
            icon: "fa-table"
        }, {
            index: 9,
            name: "等待时长统计",
            target: "_blank",
            href: "./customService.htm#/CustomService/report3",
            icon: "fa-table"
        }, {
            index: 10,
            name: "处理时长统计",
            target: "_blank",
            href: "./customService.htm#/CustomService/report4",
            icon: "fa-table"
        }, {
            index: 11,
            name: "等待时长分析",
            target: "_blank",
            href: "./customService.htm#/CustomService/report5",
            icon: "fa-table"
        }, {
            index: 12,
            name: "来话分析",
            target: "_blank",
            href: "./customService.htm#/CustomService/report6",
            icon: "fa-table"
        }
        ]
    },

    'FinanceCenter': {
        roles:[1],
        name: "财务管理",
        href: "./financeCenter.htm",
        menuItems: [
            {
                index: 0,
                name: "门店销售",
                target: "_blank",
                href: "./financeCenter.htm#/finance/storeDailySales",
                icon: "fa-table"
            }, {
                index: 1,
                name: "钢检瓶情况",
                target: "_blank",
                href: "./financeCenter.htm#/finance/checkBottle",
                icon: "fa-table"
            }, {
                index: 2,
                name: "销售统计表",
                target: "_blank",
                href: "./financeCenter.htm#/finance/dailyMonthlySales",
                icon: "fa-table"
            }, {
                index: 3,
                name: "库存统计表",
                target: "_blank",
                href: "./financeCenter.htm#/finance/checkStock",
                icon: "fa-table"
            }, {
            //    index: 4,
            //    name: "LPG销售往来",
            //    target: "_blank",
            //    href: "./financeCenter.htm#/finance/LPGSalesBalance",
            //    icon: "fa-table"
            //}, {
            //    index: 5,
            //    name: "LPG销售现金",
            //    target: "_blank",
            //    href: "./financeCenter.htm#/finance/LPGSalesCash",
            //    icon: "fa-table"
            //},{
                index: 4,
                name: "银行款信息",
                target: "_blank",
                href: "./financeCenter.htm#/finance/depositOperation",
                icon: "fa-edit"
            }, {
                index: 5,
                name: "回款操作",
                target: "_blank",
                href: "./financeCenter.htm#/finance/writeOff",
                icon: "fa-edit"
            }
        //    {
        //    index: 0,
        //    name: "总账报表",
        //    href: "./financeCenter.htm#/finance/ledger",
        //    icon: "fa-group"
        //}, {
        //    index: 1,
        //    name: "凭证记录",
        //    target: "_blank",
        //    href: "./financeCenter.htm#/finance/voucher",
        //    icon: "fa-gears"
        //},
        //    {
        //        index: 2,
        //        name: "工资报表",
        //        target: "_blank",
        //        href: "./financeCenter.htm#/finance/wages",
        //        icon: "fa-plane"
        //    }
        ]
    },
    'DecisionCenter': {
        roles:[1],
        name: "决策分析",
        href: "./decisionCenter.htm",
        menuItems: [{
            index: 0,
            name: "成本分析",
            href: "./decisionCenter.htm#/decision/cost",
            icon: "fa-group"
        }, {
            index: 1,
            name: "销售分析",
            target: "_blank",
            href: "./decisionCenter.htm#/decision/sales",
            icon: "fa-plane"
        }, {
            index: 2,
            name: "市场分析",
            target: "_blank",
            href: "./decisionCenter.htm#/decision/market",
            icon: "fa-magnet"
        }]
    },

    'GasCenter': {
        roles:[1,6],
        name: "钢瓶管理",
        href: "./gasCenter.htm",
        menuItems: [{
            index: 0,
            name: "钢瓶位置",
            href: "./gasCenter.htm#/bottles/map",
            icon: "fa-sitemap"
        }, {
            index: 1,
            name: "钢瓶档案",
            href: "./gasCenter.htm#/bottles/list",
            icon: "fa-building"
        },{
            index: 1,
            name: "库存管理",
            href: "./gasCenter.htm#/bottles/stock",
            icon: "fa-magnet"
        }]
    },
    //'AppCenter': {
    //    roles:[],
    //    name: "增值服务",
    //    href: "./appCenter.htm",
    //    menuItems: [{
    //        index: 0,
    //        name: "便民服务",
    //        href: "./appCenter.htm#/apps",
    //        icon: "fa-phone"
    //    }]
    //},
    //'CustomerCenter': {
    //    roles:[],
    //    name: "客户中心",
    //    href: "./customerCenter.htm",
    //    menuItems: [{
    //        index: 0,
    //        name: "我要订气",
    //        href: "./customerCenter.htm#/customer/create",
    //        icon: "fa-phone"
    //    }, {
    //        index: 1,
    //        name: "我的订单",
    //        href: "./customerCenter.htm#/customer/query",
    //        icon: "fa-sitemap"
    //    }]
    //},
});


