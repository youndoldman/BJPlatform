package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable
{
    private Integer id;
    private String  orderSn;//订单编号

    String callInPhone;
    Customer customer;

    private Float  orderAmount;//订单金额
    private PayType payType;//支付方式

    private Integer orderStatus;//订单状态
    private PayStatus payStatus;
    private Boolean urgent;//是否加急

    private AccessType accessType;//订单接入类型

    private CustomerAddress recvAddr; //收货地址
    private Double recvLongitude;
    private Double recvLatitude;

    private String recvName;//收货人名称
    private String recvPhone;//收货人电话
    private Date reserveTime;
    private String comment; //订单附言

    private String  note;
    private Date createTime;
    private Date updateTime;

    List<OrderDetail> orderDetailList;

    public Order()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public  String getOrderSn()
    {
        return  orderSn;
    }

    public Integer orderStatus()
    {
        return orderStatus;
    }

    public String getCallInPhone()
    {
        return callInPhone;
    }

    public Customer getCustomer()
    {
        return customer;
    }


    public Float getOrderAmount()
    {
        return orderAmount;
    }

    public PayType getPayType()
    {
        return payType;
    }

    public Integer getOrderStatus()
    {
        return orderStatus;
    }

    public PayStatus getPayStatus()
    {
        return payStatus;
    }

    public Boolean getUrgent()
    {
        return  urgent;
    }

    public AccessType getAccessType()
    {
        return accessType;
    }

    public CustomerAddress getRecvAddr()
    {
        return recvAddr;
    }

    public Double getRecvLongitude()
    {
        return recvLongitude;
    }

    public Double getRecvLatitude()
    {
        return recvLatitude;
    }

    public String getRecvName()
    {
        return recvName;
    }

    public String getRecvPhone()
    {
        return recvPhone;
    }

    public Date getReserveTime()
    {
        return reserveTime;
    }

    public String getComment()
    {
        return comment;
    }

    public List<OrderDetail> getOrderDetailList()
    {
        return orderDetailList;
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


    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setOrderSn(String orderSn)
    {
        this.orderSn = orderSn;
    }

    public void setCallInPhone(String callInPhone)
    {
        this.callInPhone = callInPhone;
    }

    public  void setOrderStatus(Integer orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public void setPayStatus(PayStatus payStatus)
    {
        this.payStatus = payStatus;
    }

    public void setUrgent(Boolean urgent)
    {
        this.urgent = urgent;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public void setOrderAmount(Float orderAmount)
    {
        this.orderAmount = orderAmount;
    }

    public void setPayType(PayType payType)
    {
        this.payType = payType;
    }

    public void setAccessType(AccessType accessType)
    {
        this.accessType = accessType;
    }

    public void setRecvAddr(CustomerAddress recvAddr)
    {
        this.recvAddr = recvAddr;
    }

    private void setRecvLongitude(Double recvLongitude)
    {
        this.recvLongitude = recvLongitude;
    }

    private  void setRecvLatitude(Double recvLatitude)
    {
        this.recvLatitude = recvLatitude;
    }

    public void setRecvName(String recvName)
    {
        this.recvName = recvName;
    }

    public void setRecvPhone(String recvPhone)
    {
        this.recvPhone = recvPhone;
    }

    public void setReserveTime(Date reserveTime)
    {
        this.reserveTime = reserveTime;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList)
    {
        this.orderDetailList = orderDetailList;
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
                .add("payType",payType)
                .add("accessType",accessType)
                .add("recvAddr",recvAddr)
                .add("recvName",recvName)
                .add("recvPhone",recvPhone)
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}