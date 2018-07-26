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

    private Float  originalAmount;//订单原始金额
    private Float  orderAmount;//订单金额
    private Float  refoundSum;//退款金额
    private PayType payType;//支付方式

    private Integer orderStatus;//订单状态
    private PayStatus payStatus;

    private InvoiceStatus invoiceStatus; //是否已开发票
    private Boolean urgent;//是否加急

    private Integer timeSpan;//订单创建到当前时间的时间差，分钟为单位,前台用于 警示 提示

    private AccessType accessType;//订单接入类型

    private OrderTriggerType orderTriggerType;//触发生成订单类型

    private CustomerAddress recvAddr; //收货地址
    private Double recvLongitude;
    private Double recvLatitude;

    private String recvName;//收货人名称
    private String recvPhone;//收货人电话
    private Date reserveTime;
    private String comment; //订单附言

    private SysUser dispatcher;//订单派送人

    private String  note;
    private String  recycleGasCylinder;//回收的钢瓶号
    private String  deliveryGasCylinder;//配送的钢瓶号

    private Date createTime;
    private Date payTime;//支付时间
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

    public SysUser getDispatcher()
    {
        return  dispatcher;
    }

    public Float getOrderAmount()
    {
        return orderAmount;
    }

    public Float getOriginalAmount() {
        return originalAmount;
    }

    public Float getRefoundSum() {
        return refoundSum;
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

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public OrderTriggerType getOrderTriggerType() {
        return orderTriggerType;
    }

    public Boolean getUrgent()
    {
        return  urgent;
    }

    public Integer getTimeSpan()
    {
        return timeSpan;
    }

    public AccessType getAccessType()
    {
        return accessType;
    }

    public void setOrderTriggerType(OrderTriggerType orderTriggerType)
    {
        this.orderTriggerType = orderTriggerType;
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

    public  Date getPayTime()
    {
        return  payTime;
    }

    public String getNote()
    {
        return note;
    }

    public String getRecycleGasCylinder()
    {
        return recycleGasCylinder;
    }

    public String getDeliveryGasCylinder()
    {
        return deliveryGasCylinder;
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

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public void setUrgent(Boolean urgent)
    {
        this.urgent = urgent;
    }

    public void setTimeSpan(Integer timeSpan)
    {
        this.timeSpan = timeSpan;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public void setDispatcher(SysUser dispatcher)
    {
        this.dispatcher = dispatcher;
    }

    public void setOrderAmount(Float orderAmount)
    {
        this.orderAmount = orderAmount;
    }

    public void setOriginalAmount(Float originalAmount) {
        this.originalAmount = originalAmount;
    }

    public void setPayType(PayType payType)
    {
        this.payType = payType;
    }

    public void setRefoundSum(Float refoundSum) {
        this.refoundSum = refoundSum;
    }

    public void setAccessType(AccessType accessType)
    {
        this.accessType = accessType;
    }

    public void setRecvAddr(CustomerAddress recvAddr)
    {
        this.recvAddr = recvAddr;
    }

    public void setRecvLongitude(Double recvLongitude)
    {
        this.recvLongitude = recvLongitude;
    }

    public  void setRecvLatitude(Double recvLatitude)
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

    public void setPayTime(Date payTime)
    {
        this.payTime = payTime;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public void setRecycleGasCylinder(String recycleGasCylinder)
    {
        this.recycleGasCylinder = recycleGasCylinder;
    }

    public void setDeliveryGasCylinder(String deliveryGasCylinder)
    {
        this.deliveryGasCylinder = deliveryGasCylinder;
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
                .add("recycleGasCylinder", recycleGasCylinder)
                .add("deliveryGasCylinder", deliveryGasCylinder)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}