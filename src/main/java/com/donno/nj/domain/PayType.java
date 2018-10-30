package com.donno.nj.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PayType implements IEnum
{
    PTOnLine("微信扫码线上支付", 0), PTCash("现金支付", 1),PTDebtCredit("赊销", 2),PTMonthlyCredit("月结", 3),
    PTTicket("气票", 4),PTCoupon("优惠券票",5),PTCheck("支票",6),PTWxOffLine("微信扫码线下支付", 7)    ;

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    PayType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (PayType c : PayType.values()) {
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

