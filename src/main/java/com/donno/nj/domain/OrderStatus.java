package com.donno.nj.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderStatus implements IEnum
{
    OSUnprocessed("待处理", 0), OSDispatching("派送中", 1),OSSigned("已签收", 2),OSCompleted("已结束", 3),OSCanceled("作废", 4);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    OrderStatus(String name, int index)
    {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index)
    {
        for (OrderStatus c : OrderStatus.values())
        {
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

