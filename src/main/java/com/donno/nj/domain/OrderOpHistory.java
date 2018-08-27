package com.donno.nj.domain;

import java.util.Date;

public class OrderOpHistory
{
    private Integer id;

    private Integer orderIdx;
    private String orderSn;
    private String userId;

    private OrderStatus orderStatus;
    private String opLog;
    private Date updateTime;

    public Integer getId()
    {
        return id;
    }

    public String getOrderSn()
    {
        return  orderSn;
    }

    public Integer getOrderIdx()
    {
        return orderIdx;
    }


    public String getUserId()
    {
        return userId;
    }

    public String getOpLog()
    {
        return opLog;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setOrderSn(String orderSn)
    {
        this.orderSn = orderSn;
    }

    public void setOrderIdx(Integer orderIdx)
    {
        this.orderIdx = orderIdx;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setOpLog(String opLog)
    {
        this.opLog = opLog;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
}
