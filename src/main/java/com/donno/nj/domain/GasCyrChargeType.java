package com.donno.nj.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GasCyrChargeType implements IEnum
{
    CTVerify("钢瓶检验", 0), CTGuaranty("钢瓶压瓶", 1);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    GasCyrChargeType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (GasCyrChargeType c : GasCyrChargeType.values()) {
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

