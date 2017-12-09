'use strict';


commonModule.constant('MISC', {
    'keys': {
        'gaodeKey': 'a44d27e0bf7b64770dad4664e3ba92b1',
    }
});

commonModule.constant('URI', {
    'resources': {
        'loginPage': '../pages/login.htm',
        'mainPage': '../pages/mainCenter.htm',
        'users': '../../../api/sysusers',
        'department': '../../../api/Department',
        'groups': '../../../api/groups',
        'customers': '../../../api/customers',
        'customerSource': '../../../api/CustomerSource',
        'customerLevel': '../../../api/CustomerLevel',
        'customerType': '../../../api/CustomerType',
        'customerCallin':'../../../api/CustomerCallIn',
        'login':'../../../api/sysusers/login',
        'logout':'../../../api/sysusers/logout',
        'orders':'../../../api/Orders',　　　　　//订气订单接口
        'goods': '../../../api/Goods',//商品
        'goodsTypes': '../../../api/GoodsTypes',//商品类型
        'subdistrict':'http://restapi.amap.com/v3/config/district',//高德地图行政区域接口
        'geocode':'http://restapi.amap.com/v3/geocode/geo',//高德地图逆地址解码接口


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
            name: "商城管理",
            target: "_blank",
            href: "./manageCenter.htm#/manage/goods",
            icon: "fa-gears"
        }]
    },
    'OrderCenter': {
        roles:[],
        name: "订单管理",
        href: "orderCenter.htm",
    },
    'CustomService': {
        roles:[],
        name: "客服业务",
        href: "./CustomService.htm",
        menuItems: [{
            index: 0,
            name: "呼叫中心",
            href: "./CustomService.htm#/CustomService/callCenter",
            icon: "fa-group"
        },
            {
            index: 1,
            name: "客户管理",
            target: "_blank",
            href: "./CustomService.htm#/CustomService/customerManage",
            icon: "fa-plane"
        }, {
            index: 2,
            name: "订单管理",
            target: "_blank",
            href: "",
            icon: "fa-gears"
        }, {
            index: 3,
            name: "统计报表",
            target: "_blank",
            href: "",
            icon: "fa-plane"
        }]
    },
    'RoutineCenter': {
        roles:[],
        name: "日常事务",
        href: "./routineCenter.htm",
        menuItems: [{
            index: 0,
            name: "请假流程",
            href: "./routineCenter.htm#/routine/holiday",
            icon: "fa-group"
        }, {
            index: 1,
            name: "报销流程",
            target: "_blank",
            href: "./routineCenter.htm#/routine/expence",
            icon: "fa-gears"
        }, {
            index: 2,
            name: "采购流程",
            target: "_blank",
            href: "",
            icon: "fa-github"
        }, {
            index: 3,
            name: "出差流程",
            target: "_blank",
            href: "",
            icon: "fa-cloud"
        }, {
            index: 4,
            name: "建言献策",
            target: "_blank",
            href: "",
            icon: "fa-reorder"
        }, {
            index: 5,
            name: "我的任务",
            target: "_blank",
            href: "./routineCenter.htm#/routine/mytask",
            icon: "fa-reorder"
        }]
    },
    'FinanceCenter': {
        roles:[],
        name: "财务管理",
        href: "./financeCenter.htm",
        menuItems: [{
            index: 0,
            name: "总账报表",
            href: "./financeCenter.htm#/finance/ledger",
            icon: "fa-group"
        }, {
            index: 1,
            name: "凭证记录",
            target: "_blank",
            href: "./financeCenter.htm#/finance/voucher",
            icon: "fa-gears"
        },
            {
                index: 2,
                name: "工资报表",
                target: "_blank",
                href: "./financeCenter.htm#/finance/wages",
                icon: "fa-plane"
            }]
    },
    'DecisionCenter': {
        roles:[],
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
        roles:[],
        name: "钢瓶管理",
        href: "./gasCenter.htm",
        menuItems: [{
            index: 0,
            name: "钢瓶位置",
            href: "",
            icon: "fa-sitemap"
        }, {
            index: 1,
            name: "门店管理",
            target: "_blank",
            href: "",
            icon: "fa-building"
        }]
    },
    'AppCenter': {
        roles:[],
        name: "增值服务",
        href: "./appCenter.htm",
        menuItems: [{
            index: 0,
            name: "便民服务",
            href: "./appCenter.htm#/apps",
            icon: "fa-phone"
        }]
    },
    'CustomerCenter': {
        roles:[],
        name: "客户中心",
        href: "./customerCenter.htm",
        menuItems: [{
            index: 0,
            name: "我要订气",
            href: "./customerCenter.htm#/customer/create",
            icon: "fa-phone"
        }, {
            index: 1,
            name: "我的订单",
            href: "./customerCenter.htm#/customer/query",
            icon: "fa-sitemap"
        }]
    },
});


