package com.donno.nj.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.MoreObjects;

import java.io.Serializable;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GasCynWarnStatus implements IEnum,Serializable
{
    Created("新告警待处理", 0),Processing("处理中", 1),Processed("已处理", 2);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    GasCynWarnStatus(String name, int index)
    {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index)
    {
        for (GasCynWarnStatus c : GasCynWarnStatus.values())
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

