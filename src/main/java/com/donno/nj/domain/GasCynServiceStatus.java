package com.donno.nj.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.MoreObjects;

import java.io.Serializable;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GasCynServiceStatus implements IEnum,Serializable
{

    UnUsed("待使用", 0),StationStock("气站库存", 1),StoreStock("门店库存", 2),Transporting("在途运输", 3),Dispatching("在途派送", 4),CustomerUsing("客户使用", 5),EmptyCynRetrieve("空瓶回收", 6);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    GasCynServiceStatus(String name, int index)
    {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index)
    {
        for (GasCynServiceStatus c : GasCynServiceStatus.values())
        {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }


    // get set 方法
    public String getName()
    {
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

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("index", index)
                .toString();
    }
};

