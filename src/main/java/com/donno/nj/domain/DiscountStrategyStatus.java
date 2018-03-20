package com.donno.nj.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DiscountStrategyStatus implements IEnum
{
    DSSWaitForForce("待生效", 0), DSSEffecitve("已生效", 1),DSSInvalid("已废除", 2);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    DiscountStrategyStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (DiscountStrategyStatus c : DiscountStrategyStatus.values()) {
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

