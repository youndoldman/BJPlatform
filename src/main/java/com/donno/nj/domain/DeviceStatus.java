package com.donno.nj.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.MoreObjects;

import java.io.Serializable;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeviceStatus implements IEnum,Serializable
{
    DevDisabled("未启用", 0), DevEnabled("已启用", 1),DevBlocked("已停用", 2),DevCanceled("已作废", 3);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    DeviceStatus(String name, int index)
    {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index)
    {
        for (DeviceStatus c : DeviceStatus.values())
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

