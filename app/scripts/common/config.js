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
        'users': '../../../api/sysusers',//系统用户接口
        'department': '../../../api/Department',//部门接口
        'groups': '../../../api/groups',//组接口
        'customers': '../../../api/customers',//客户接口
        'customerSource': '../../../api/CustomerSource',//客户来源查询接口
        //实际使用地址
        //'customerLevel': '../../../api/CustomerLevel',//客户等级查询接口
        //'customerType': '../../../api/CustomerType',//客户类型查询接口
        'customerLevel': '../../../api/CustomerLevel',//客户等级查询接口
        'customerType': '../../../api/CustomerType',//客户类型查询接口

        'settlementType': '../../../api/SettlementType',//结算类型信息查询接口

        'discountStrategies':'../../../api/DiscountStrategies',//优惠策略接口

        'customerCallin':'../../../api/CustomerCallIn',//电话关联的客户资料查询接口
        'login':'../../../api/sysusers/login',//系统用户登录接口
        'logout':'../../../api/sysusers/logout',//系统用户登出接口
        'orders':'../../../api/Orders',　　　　　//订气订单接口
        'orderCancel':'../../../api/CancelOrder',　　　　　//订气作废接口

        'taskOrders':'../../../api/TaskOrders',//任务订单接口
        'taskOrdersDeal':'../../../api/TaskOrders/Process',//任务订单办理接口
        'taskOrdersTransfer':'../../../api/TaskOrders/Modify',//任务订单转派接口
        //'goods': '../../../api/Goods',//商品
        //'goodsTypes': '../../../api/GoodsTypes',//商品类型
        'goods': '../../../api/Goods',//商品
        'goodsTypes': '../../../api/GoodsTypes',//商品类型

        'goodsPriceHistory': '../../../api/Goods/PriceHistory',//商品价格历史
        'subdistrict':'https://restapi.amap.com/v3/config/district',//高德地图行政区域接口
        'geocode':'https://restapi.amap.com/v3/geocode/geo',//高德地图逆地址解码接口
        'bottle': '../../../api/GasCylinder',//钢瓶接口
        'bottleHandOver': '../../../api/GasCylinder/TakeOver',//钢瓶责任交接
        'bottleByRange': '../../../api/GasCylinder/GetByRange',//范围获取钢瓶
        'bottleLocations': '../../../api/GasCylinder/GetLocations',//范围获取钢瓶
        'gpsBind': '../../../api/GasCylinder/Bind',//定位终端绑定接口
        'gpsUnBind': '../../../api/GasCylinder/UnBind',//定位终端解除绑定接口
        'bottleSpecQuery': '../../../api/GasCylinderSpec',//钢瓶规格查询
        'cloudUser': '../../../api/CloudUser',//云客服用户接口
        'cloudUserBind': '../../../api/CloudUser/Bind',//云客服用户绑定接口
        'cloudUserUnBind': '../../../api/CloudUser/UnBind',//云客服用户解除绑定接口
        //'adjustPriceSchedules': '../../../api/AdjustPriceSchedules',//调价接口
        'adjustPriceSchedules': '../../../api/AdjustPriceSchedules',//调价接口

        'ticket': '../../../api/Ticket',//气票信息增加
        'coupon': '../../../api/Coupon',//优惠券信息增加

        'salesByBayType': '../../../api/Report/Sales/ByPayType',//查询销售日报表(按支付类型查询)
        'salesByCustomerType': '../../../api/Report/Sales/ByCustomerType',//查询销售日报表(按支付类型查询)
        'saleContacts':'../../../api/Report/SaleContacts',//销售往来日报表查询
        'stock':'../../../api/Report/Stock',//查询库存
        'stockInOut':'../../../api/Report/StockInOut',//查询出入库数量
        'salesCash':'../../../api/Report/SaleCash',//销售现金报表


        'mendType': '../../../api/MendTypes',//报修类型接口
        'mend': '../../../api/Mend',//报修单接口
        'securityType': '../../../api/SecurityTypes',//安检类型接口
        'security': '../../../api/Security',//安检单接口
        'complaintType': '../../../api/ComplaintTypes',//投诉类型接口
        'complaint': '../../../api/Complaint',//投诉单接口

        'customerCredit': '../../../api/CustomerCredit',//欠款
        'writeOff':'../../../api/WriteOff',//回款
        'writeOffDetail':'../../../api/WriteOffDetail',//回款明细信息查询
        'depositDetail':'../../../api/DepositDetail',//增加存银行款信息记录

        'gasCylinderSpec':'../../../api/GasCylinderSpec',//查询钢瓶规格
        'addGasCylinderSpec':'../../../api/GasCyrDynDetail',//钢检瓶动态增加
        'reportGasCyrDyn':'../../../api/Report/GasCyrDyn',//钢检瓶报表查询
        'gasCyrChargeSpec':'../../../api/GasCyrChargeSpec',//钢瓶收费标准查询

        'gasCylinderPosition':'../../../api/GasCylinderPosition',//钢瓶轨迹查询
        'gasCylinderTakeOverHistory':'../../../api/GasCylinder/TakeOver/History',//钢瓶交接记录查询

        'GasCylinderWarn':'../../../api/GasCylinderWarn',//告警查询

        'GasCynTrayBind':'../../../api/GasCynTray/Bind',//托盘绑定
        'GasCynTray':'../../../api/GasCynTray',//托盘查询
        'GasCynTrayunBind':'../../../api/GasCynTray/unBind',//托盘解绑定
        'GasCynTrayHistory':'../../../api/GasCynTray/History',//托盘历史数据查询

        'GasCynFactory':'../../../api/GasCynFactory',//6.5.	钢瓶厂家接口
        'sysUserPosition':'../../../api/sysusers/position',//用户位置
        'sysUserKeepAlive':'../../../api/sysusers/KeepAlive',//用户心跳
        'UserCard':'../../../api/UserCard',//用户卡查询
        'UserCardBind':'../../../api/UserCard/Bind',//用户卡绑定
        'UserCardunBind':'../../../api/UserCard/unBind',//用户卡解绑定
        'GasFillingMerge':'../../../api/GasFillingMerge',//8.3.	充装数据查询

        'sysUserFindByUserId':'../../../api/sysusers/FindByUserId',//系统用户精确查询
        'DiscountStrategiesCancel':'../../../api/DiscountStrategies/Cancel',//10.8.	作废优惠方案
        'SysUserPhoto': '../../../api/sysusers/photo',//用户照片
        'OrderUrgency': '../../../api/OrderUrgency',//催单信息
        'Advice': '../../../api/Advice',//客户意见
        'ElectDeposit': '../../../api/ElectDeposit',//电子押金单





        'SaleByWeight':'../../../api/Report/SaleByWeight/ByPayType',//按斤销售方式
        'sendMs':'../../../api/Untils/SendBatchSms',//短信推送

        'WarningStatusDelete':'../../../api/GasCynTray/WarningStatusDelete',//托盘告警解除




        'ktyAuthenticate': 'https://cc.ketianyun.com/thirdparty/api/v1/authenticate',//科天云登录接口
        'ktyReport1': 'https://cc.ketianyun.com/thirdparty/api/v1/callsdata/agent/call',//科天云获取坐席-工作组及直拨电话统计报表
        'ktyReport2': 'https://cc.ketianyun.com/thirdparty/api/v1/callsdata/agent/operation',//科天云获取坐席-操作状态统计报表
        'ktyReport3': 'https://cc.ketianyun.com/thirdparty/api/v1/callsdata/workset/incall/waittime',//科天云获取工作组-来话等待时长统计报表
        'ktyReport4': 'https://cc.ketianyun.com/thirdparty/api/v1/callsdata/workset/incall/operatetime',//科天云获取工作组-来话处理时长统计报表
        'ktyReport5': 'https://cc.ketianyun.com/thirdparty/api/v1/callsdata/workset/incall/interval',//科天云获取工作组-来话等待时长分析报表
        'ktyReport6': 'https://cc.ketianyun.com/thirdparty/api/v1/callsdata/workset/incall/analysis',//科天云获取工作组-来话应答/放弃/溢出分析报表

        'ktyCallreport': 'https://cc.ketianyun.com/thirdparty/api/v1/callreports',//科天云获得指定时间段呼叫的记录
        'ktyCallreportSearch': 'https://cc.ketianyun.com/thirdparty/api/v1/callreports/search',//科天云获得获取指定搜索条件的呼叫记录



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
            icon: "fa-bars"
        }, {
            index: 2,
            name: "商品管理",
            target: "_blank",
            href: "./manageCenter.htm#/manage/goods",
            icon: "fa-gears"
        }, {
            index: 3,
            name: "云客服账号",
            target: "_blank",
            href: "./manageCenter.htm#/manage/cloudUser",
            icon: "fa-github"
        }, {
            index: 4,
            name: "钢瓶厂家",
            target: "_blank",
            href: "./manageCenter.htm#/manage/factoryManage",
            icon: "fa-gears"
        }, {
            index: 5,
            name: "短信推送",
            target: "_blank",
            href: "./manageCenter.htm#/manage/sendMs",
            icon: "fa-gears"
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
            index: 1,
            name: "电子押金",
            href: "./shopCenter.htm#/ShopManage/electDepositCheck",
            icon: "fa-group"
        }, {
            index: 2,
            name: "订单管理",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/orderList",
            icon: "fa-gears"
        }, {
            index: 3,
            name: "库存管理",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/stockControl",
            icon: "fa-gears"
        },{
            index: 4,
            name: "报修处理",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/Mend",
            icon: "fa-gears"
        }, {
            index: 5,
            name: "安检处理",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/Security",
            icon: "fa-gears"
        }, {
            index: 6,
            name: "投诉处理",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/Complaint",
            icon: "fa-gears"
        }, {
            index: 7,
            name: "回款操作",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/MoneyReturn",
            icon: "fa-edit"
        }, {
            index: 8,
            name: "钢检瓶操作",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/checkBottle",
            icon: "fa-edit"
        },{
            index: 9,
            name: "配送统计",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/calculateDelivery",
            icon: "fa-table"
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
            icon: "fa-search"
        }, {
            index: 3,
            name: "客户管理",
            target: "_blank",
            href: "./customService.htm#/CustomService/customerManage",
            icon: "fa-gears"
        }, {
            index: 4,
            name: "报修处理",
            target: "_blank",
            href: "./customService.htm#/CustomService/Mend",
            icon: "fa-gears"
        }, {
            index: 5,
            name: "安检处理",
            target: "_blank",
            href: "./customService.htm#/CustomService/Security",
            icon: "fa-gears"
        }, {
            index: 6,
            name: "投诉处理",
            target: "_blank",
            href: "./customService.htm#/CustomService/Complaint",
            icon: "fa-gears"
        }, {
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
        roles:[1,8],
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
            name: "综合监控",
            href: "./decisionCenter.htm#/decision/statistic",
            icon: "fa-edit"
        }, {
            index: 1,
            name: "成本分析",
            href: "./decisionCenter.htm#/decision/cost",
            icon: "fa-group"
        }, {
            index: 2,
            name: "用气统计",
            href: "./decisionCenter.htm#/decision/gasUsage",
            icon: "fa-gears"
        }
            //{
        //    index: 1,
        //    name: "销售分析",
        //    target: "_blank",
        //    href: "./decisionCenter.htm#/decision/sales",
        //    icon: "fa-plane"
        //}, {
        //    index: 2,
        //    name: "市场分析",
        //    target: "_blank",
        //    href: "./decisionCenter.htm#/decision/market",
        //    icon: "fa-magnet"
        //}
        ]
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
        }, {
            index: 2,
            name: "流转异常",
            href: "./gasCenter.htm#/bottles/warning",
            icon: "fa-exclamation-triangle"
        },{
            index: 3,
            name: "库存管理",
            href: "./gasCenter.htm#/bottles/stock",
            icon: "fa-magnet"
        },{
            index: 4,
            name: "罐装记录",
            href: "./gasCenter.htm#/bottles/filling",
            icon: "fa-gears"
        }]
    },
    'ComprehensiveQueryCenter': {
        roles:[1,2],
        name: "综合查询",
        href: "./comprehensiveQueryCenter.htm",
        menuItems: [{
            index: 0,
            name: "气票查询",
            target: "_blank",
            href: "./comprehensiveQueryCenter.htm#/ComprehensiveQuery/ticket",
            icon: "fa-table"
        }, {
            index: 1,
            name: "优惠券查询",
            target: "_blank",
            href: "./comprehensiveQueryCenter.htm#/ComprehensiveQuery/coupon",
            icon: "fa-table"
        }, {
            index: 2,
            name: "用户卡查询",
            target: "_blank",
            href: "./comprehensiveQueryCenter.htm#/ComprehensiveQuery/userCard",
            icon: "fa-table"
        }, {
            index: 3,
            name: "托盘查询",
            target: "_blank",
            href: "./comprehensiveQueryCenter.htm#/ComprehensiveQuery/gasCynTray",
            icon: "fa-table"
        }]
    },
    'ComprehensiveSituationCenter': {
        roles:[],
        name: "综合态势",
        href: "./comprehensiveSituation.htm",
        menuItems: [{
            index: 0,
            name: "员工监控",
            href: "./comprehensiveSituation.htm#/comprehensiveSituation/map",
            icon: "fa-phone"
        }]
    }
});


