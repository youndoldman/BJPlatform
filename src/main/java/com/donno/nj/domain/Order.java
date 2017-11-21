package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable
{
    private Integer id;
    private String  orderSn;//订单编号

    private Integer customerIdx;//客户IDx
    private String  customerId;//客户ID
    private String  customerName;//客户名称

    private Float   orderSum;//订单金额
    private PayType payType;//支付方式
    private String orderStatus;//订单状态
    private AccessType accessType;//订单接入类型

    private String recvAddr; //收货地址
    private String recvName;//收货人名称
    private String recvPhohe;//收货人电话
    private String comment; //订单附言

    private String  note;
    private Date createTime;
    private Date updateTime;

    List<OrderDetail> orderDetailList;

    public Order()
    {
    }

    /*
    * 属性读取
    * */

    public Integer getId()
    {
        return id;
    }


    public  String getOrderSn()
    {
        return  orderSn;
    }

    public Integer getCustomerIdx()
    {
        return customerIdx;
    }


    public String getCustomerId()
    {
        return customerId;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public Float getOrderSum()
    {
        return orderSum;
    }

    public PayType getPayType()
    {
        return payType;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public AccessType getAccessType()
    {
        return accessType;
    }

    public String getRecvAddr()
    {
        return recvAddr;
    }

    public String getRecvName()
    {
        return recvName;
    }

    public String getRecvPhohe()
    {
        return recvPhohe;
    }

    public List<OrderDetail> getOrderDetailList()
    {
        return orderDetailList;
    }

    public String getComment()
    {
        return comment;
    }

    public String getNote()
    {
        return note;
    }


    public  Date getCreateTime()
    {
        return  createTime;
    }

    public  Date getUpdateTime()
    {
        return  updateTime;
    }

    /*
    * 属性设置
    * */


    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setOrderSn(String orderSn)
    {
        this.orderSn = orderSn;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public void setCustomerIdx(Integer customerIdx)
    {
        this.customerIdx = customerIdx;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public void setOrderSum(Float orderSum)
    {
        this.orderSum = orderSum;
    }

    public void setPayType(PayType payType)
    {
        this.payType = payType;
    }

    public void setRecvAddr(String recvAddr)
    {
        this.recvAddr = recvAddr;
    }

    public void setRecvName(String recvName)
    {
        this.recvName = recvName;
    }

    public void setRecvPhohe(String recvPhohe)
    {
        this.recvPhohe = recvPhohe;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public  void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }



    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("orderSn", orderSn)
                .add("customerId", customerId)
                .add("orderSum",orderSum)
                .add("payType",payType)
                .add("accessType",accessType)
                .add("recvAddr",recvAddr)
                .add("recvName",recvName)
                .add("recvPhohe",recvPhohe)
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}