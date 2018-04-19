package com.donno.nj.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GasCyrDynOperType implements IEnum
{
    GCOTReceive("领用", 0),
    GCOTVerify("送检", 1),
    GCOTRecvVerify("收取钢检瓶", 2),
    GCOTUntreadVerify("退维修瓶", 3),
    GCOTUntreadDiscard("退报废瓶", 4),
    GCOTRecvGuaranty("押瓶", 5),
    GCOTUntreadGuaranty("退押瓶", 6)   ;

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    GasCyrDynOperType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (GasCyrDynOperType c : GasCyrDynOperType.values()) {
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

