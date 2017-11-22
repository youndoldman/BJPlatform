package com.donno.nj.activiti;


public enum WorkFlowTypes {
    GAS_PHONE_ORDER_FLOW("电话订气流程", 1), GAS_WEIXIN_ORDER_FLOW("微信订气流程", 2);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private WorkFlowTypes(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (WorkFlowTypes c : WorkFlowTypes.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // 普通方法
    public static WorkFlowTypes getByName(String name) {
        for (WorkFlowTypes c : WorkFlowTypes.values()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }


    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
};

