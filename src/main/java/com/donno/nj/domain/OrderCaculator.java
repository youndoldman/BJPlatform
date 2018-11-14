package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/13 0013.
 */
public class OrderCaculator implements Serializable
{
    private  String gasCynNumber; //空瓶瓶号
    private Float standWeight;//标称重量
    private Float refoundWeight;//回瓶重量
    private Boolean forceCaculate;//强制计算
    private Float dealPrice;//成交价格,强制计算时用此价格计算，非强制时，通过关联订单查询


    public String getGasCynNumber() {
        return gasCynNumber;
    }

    public Float getStandWeight() {
        return standWeight;
    }

    public Float getRefoundWeight() {
        return refoundWeight;
    }

    public Float getDealPrice() {
        return dealPrice;
    }

    public Boolean getForceCaculate() {
        return forceCaculate;
    }


    public void setGasCynNumber(String gasCynNumber) {
        this.gasCynNumber = gasCynNumber;
    }

    public void setForceCaculate(Boolean forceCaculate) {
        this.forceCaculate = forceCaculate;
    }

    public void setDealPrice(Float dealPrice) {
        this.dealPrice = dealPrice;
    }

    public void setStandWeight(Float standWeight) {
        this.standWeight = standWeight;
    }

    public void setRefoundWeight(Float refoundWeight) {
        this.refoundWeight = refoundWeight;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}

