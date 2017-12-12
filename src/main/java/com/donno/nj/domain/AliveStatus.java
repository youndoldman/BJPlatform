package com.donno.nj.domain;


public enum AliveStatus implements IEnum
{
    ASOffline("离线", 0), ASOnline("在线", 1);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    AliveStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (AliveStatus c : AliveStatus.values()) {
            if (c.getIndex() == index) {
                return c.name;
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

