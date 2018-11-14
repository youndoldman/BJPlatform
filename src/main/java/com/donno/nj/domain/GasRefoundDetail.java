package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/11/13 0013.
 */
public class GasRefoundDetail implements Serializable
{
    Integer id;
    private String orderSn;
    private  String gasCynNumber; //空瓶号
    private Float refoundWeight;//回收空瓶重量(含残液)
    private Float dealPrice;//成交价格（优惠后）
    private Float standWeight;//标称重量
    private Boolean forceCaculate;//强制计算
    private Float unitPrice;//单价
    private Float tareWeight;//皮重
    private Float remainGas;//残液
    private Float refoundSum;//回款

    private String prevOrder;//关联订单
    private String prevGoodsCode;//关联商品

    private String note;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public String getGasCynNumber() {
        return gasCynNumber;
    }

    public Float getRefoundWeight() {
        return refoundWeight;
    }

    public Float getStandWeight() {
        return standWeight;
    }

    public Float getDealPrice() {
        return dealPrice;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public Boolean getForceCaculate() {
        return forceCaculate;
    }

    public Float getTareWeight() {
        return tareWeight;
    }

    public Float getRemainGas() {
        return remainGas;
    }

    public Float getRefoundSum() {
        return refoundSum;
    }

    public String getNote() {
        return note;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRefoundWeight(Float refoundWeight) {
        this.refoundWeight = refoundWeight;
    }

    public void setStandWeight(Float standWeight) {
        this.standWeight = standWeight;
    }

    public void setDealPrice(Float dealPrice) {
        this.dealPrice = dealPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getPrevOrder() {
        return prevOrder;
    }

    public String getPrevGoodsCode() {
        return prevGoodsCode;
    }

    public void setGasCynNumber(String gasCynNumber) {
        this.gasCynNumber = gasCynNumber;
    }

    public void setForceCaculate(Boolean forceCaculate) {
        this.forceCaculate = forceCaculate;
    }

    public void setTareWeight(Float tareWeight) {
        this.tareWeight = tareWeight;
    }

    public void setRemainGas(Float remainGas) {
        this.remainGas = remainGas;
    }

    public void setRefoundSum(Float refoundSum) {
        this.refoundSum = refoundSum;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public void setPrevOrder(String prevOrder) {
        this.prevOrder = prevOrder;
    }

    public void setPrevGoodsCode(String prevGoodsCode) {
        this.prevGoodsCode = prevGoodsCode;
    }


    public void setNote(String note) {
        this.note = note;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}

