'use strict';

commonModule.constant('URI', {
    'resources': {
        'loginPage': '../pages/login.htm',
        'mainPage': '../pages/mainCenter.htm',
        'users': '../../../api/users',
        'bottoms': '../../../api/bottoms',
        'login':'../../../api/login',
        'logout':'../../../api/logout',
        'orders':'../../../api/orders'

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

    'UserCenter': {
        roles:["管理员"],
        name: "客户管理",
        href: "./userCenter.htm",
        menuItems: [{
            index: 0,
            name: "用户列表",
            href: "./userCenter.htm",
            icon: "fa-group"
        }, {
            index: 1,
            name: "权限管理",
            target: "_blank",
            href: "",
            icon: "fa-gears"
        }]
    },
    'OrderCenter': {
        roles:["管理员"],
        name: "订单管理",
        href: "orderCenter.htm",
    },
    'PhoneCenter': {
        roles:["管理员","客服"],
        name: "呼叫中心",
        href: "./phoneCenter.htm",
        menuItems: []
    },
    'RoutineCenter': {
        roles:["客服","管理员","老板","财务","客户","配送"],
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
        roles:["管理员", "财务"],
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
        roles:["管理员", "老板"],
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
        roles:["管理员"],
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
        roles:["管理员"],
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
        roles:["客户","管理员"],
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
    'HelpCenter': {
        roles:["客户"],
        name: "帮助中心",
        href: "",
        target: "_blank"
    }
});


