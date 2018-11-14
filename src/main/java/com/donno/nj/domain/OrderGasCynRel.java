package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/11/13 0013.
 */
public class OrderGasCynRel implements Serializable
{
    private  Integer id;
    private String orderSn;
    private  String gasCynNumber;
    OrderGasCynRelStatus orderGasCynRelStatus;

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

    public OrderGasCynRelStatus getOrderGasCynRelStatus() {
        return orderGasCynRelStatus;
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

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public void setGasCynNumber(String gasCynNumber) {
        this.gasCynNumber = gasCynNumber;
    }

    public void setOrderGasCynRelStatus(OrderGasCynRelStatus orderGasCynRelStatus) {
        this.orderGasCynRelStatus = orderGasCynRelStatus;
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

