package com.donno.nj.domain;


public enum PayStatus implements IEnum
{
    PSUnpaid("待支付", 0), PSPaied("已支付", 1),PSRefounding("退款中", 2),PSRefounded("已退款", 3);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    PayStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (PayStatus c : PayStatus.values()) {
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

