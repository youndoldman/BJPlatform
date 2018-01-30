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
        'customerLevel': '../../../api/CustomerLevel',//客户等级查询接口
        'customerType': '../../../api/CustomerType',//客户类型查询接口
        'customerCallin':'../../../api/CustomerCallIn',//电话关联的客户资料查询接口
        'login':'../../../api/sysusers/login',//系统用户登录接口
        'logout':'../../../api/sysusers/logout',//系统用户登出接口
        'orders':'../../../api/Orders',　　　　　//订气订单接口
        'orderCancel':'../../../api/CancelOrder',　　　　　//订气作废接口

        'taskOrders':'../../../api/TaskOrders',//任务订单接口
        'taskOrdersDeal':'../../../api/TaskOrders/Process',//任务订单办理接口
        'goods': '../../../api/Goods',//商品
        'goodsTypes': '../../../api/GoodsTypes',//商品类型
        'subdistrict':'http://restapi.amap.com/v3/config/district',//高德地图行政区域接口
        'geocode':'http://restapi.amap.com/v3/geocode/geo',//高德地图逆地址解码接口
        'bottle': '../../../api/GasCylinder',//钢瓶接口
        'bottleHandOver': '../../../api/GasCylinder/TakeOver',//钢瓶责任交接
        'gpsBind': '../../../api/GasCylinder/Bind',//定位终端绑定接口
        'gpsUnBind': '../../../api/GasCylinder/UnBind',//定位终端解除绑定接口
        'bottleSpecQuery': '../../../api/GasCylinderSpec',//钢瓶规格查询
        'cloudUser': '../../../api/CloudUser',//云客服用户接口
        'cloudUserBind': '../../../api/CloudUser/Bind',//云客服用户绑定接口
        'cloudUserUnBind': '../../../api/CloudUser/UnBind'//云客服用户解除绑定接口








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
            name: "商城管理",
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
            index: 1,
            name: "库存管理",
            target: "_blank",
            href: "./shopCenter.htm#/ShopManage/stockControl",
            icon: "fa-plane"
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
        }, {
            index: 1,
            name: "订单管理",
            target: "_blank",
            href: "./customService.htm#/CustomService/orderManage",
            icon: "fa-gears"
        }, {
            index: 2,
            name: "客户管理",
            target: "_blank",
            href: "./customService.htm#/CustomService/customerManage",
            icon: "fa-plane"
        //}, {
        //    index: 3,
        //    name: "统计报表",
        //    target: "_blank",
        //    href: "",
        //    icon: "fa-plane"
        }]
    },
    //'RoutineCenter': {
    //    roles:[1],
    //    name: "日常事务",
    //    href: "./routineCenter.htm",
    //    menuItems: [{
    //        index: 0,
    //        name: "请假流程",
    //        href: "./routineCenter.htm#/routine/holiday",
    //        icon: "fa-group"
    //    }, {
    //        index: 1,
    //        name: "报销流程",
    //        target: "_blank",
    //        href: "./routineCenter.htm#/routine/expence",
    //        icon: "fa-gears"
    //    }, {
    //        index: 2,
    //        name: "采购流程",
    //        target: "_blank",
    //        href: "",
    //        icon: "fa-github"
    //    }, {
    //        index: 3,
    //        name: "出差流程",
    //        target: "_blank",
    //        href: "",
    //        icon: "fa-cloud"
    //    }, {
    //        index: 4,
    //        name: "建言献策",
    //        target: "_blank",
    //        href: "",
    //        icon: "fa-reorder"
    //    }, {
    //        index: 5,
    //        name: "我的任务",
    //        target: "_blank",
    //        href: "./routineCenter.htm#/routine/mytask",
    //        icon: "fa-reorder"
    //    }]
    //},
    //'FinanceCenter': {
    //    roles:[1],
    //    name: "财务管理",
    //    href: "./financeCenter.htm",
    //    menuItems: [{
    //        index: 0,
    //        name: "总账报表",
    //        href: "./financeCenter.htm#/finance/ledger",
    //        icon: "fa-group"
    //    }, {
    //        index: 1,
    //        name: "凭证记录",
    //        target: "_blank",
    //        href: "./financeCenter.htm#/finance/voucher",
    //        icon: "fa-gears"
    //    },
    //        {
    //            index: 2,
    //            name: "工资报表",
    //            target: "_blank",
    //            href: "./financeCenter.htm#/finance/wages",
    //            icon: "fa-plane"
    //        }]
    //},
    //'DecisionCenter': {
    //    roles:[1],
    //    name: "决策分析",
    //    href: "./decisionCenter.htm",
    //    menuItems: [{
    //        index: 0,
    //        name: "成本分析",
    //        href: "./decisionCenter.htm#/decision/cost",
    //        icon: "fa-group"
    //    }, {
    //        index: 1,
    //        name: "销售分析",
    //        target: "_blank",
    //        href: "./decisionCenter.htm#/decision/sales",
    //        icon: "fa-plane"
    //    }, {
    //        index: 2,
    //        name: "市场分析",
    //        target: "_blank",
    //        href: "./decisionCenter.htm#/decision/market",
    //        icon: "fa-magnet"
    //    }]
    //},

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


