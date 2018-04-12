package com.donno.nj.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EProcessStatus implements IEnum
{
    PTSuspending("待处理", 0), PTHandling("处理中", 1), PTSolved("已解决", 2);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    EProcessStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (EProcessStatus c : EProcessStatus.values()) {
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

