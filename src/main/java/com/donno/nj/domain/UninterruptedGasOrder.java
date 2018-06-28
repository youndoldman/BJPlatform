package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UninterruptedGasOrder implements Serializable
{
    private Integer id;
    private String  orderSn;//订单编号

    private Customer customer;
    private SysUser dispatcher;
    private GasCylinder gasCylinder;
    private Order dispatchOrder;
    private Goods goods;

    private Float fullWeight;
    private Float emptyWeight;

    private Float  originalPrice;//原始单价
    private Float  dealPrice;//成交单价
    private Float  originalAmount;//原始总金额
    private Float  dealAmount;//成交总金额
    private PayType payType;//支付方式

    private PayStatus payStatus;

    private String  note;
    private Date createTime;
    private Date payTime;//支付时间
    private Date updateTime;

    public UninterruptedGasOrder()
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

    public Order getDispatchOrder()
    {
        return dispatchOrder;
    }

    public Goods getGoods()
    {
        return goods;
    }

    public GasCylinder getGasCylinder()
    {
        return gasCylinder;
    }

    public Float getFullWeight()
    {
        return fullWeight;
    }

    public Float getEmptyWeight()
    {
        return emptyWeight;
    }

    public Float getOriginalPrice()
    {
        return originalPrice;
    }

    public Float getDealPrice()
    {
        return dealPrice;
    }

    public Float getDealAmount()
    {
        return dealAmount;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public SysUser getDispatcher()
    {
        return  dispatcher;
    }


    public Float getOriginalAmount() {
        return originalAmount;
    }

    public PayType getPayType()
    {
        return payType;
    }

    public PayStatus getPayStatus()
    {
        return payStatus;
    }

    public  Date getPayTime()
    {
        return  payTime;
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

    public void setGasCylinder(GasCylinder gasCylinder) {
        this.gasCylinder = gasCylinder;
    }

    public void setDispatchOrder(Order dispatchOrder) {
        this.dispatchOrder = dispatchOrder;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public void setFullWeight(Float fullWeight) {
        this.fullWeight = fullWeight;
    }

    public void setEmptyWeight(Float emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public void setDispatcher(SysUser dispatcher)
    {
        this.dispatcher = dispatcher;
    }


    public void setOriginalPrice(Float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setDealPrice(Float dealPrice) {
        this.dealPrice = dealPrice;
    }

    public void setPayStatus(PayStatus payStatus) {
        this.payStatus = payStatus;
    }

    public void setDealAmount(Float dealAmount) {
        this.dealAmount = dealAmount;
    }

    public void setOriginalAmount(Float originalAmount) {
        this.originalAmount = originalAmount;
    }

    public void setPayType(PayType payType)
    {
        this.payType = payType;
    }

    public void setPayTime(Date payTime)
    {
        this.payTime = payTime;
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
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}