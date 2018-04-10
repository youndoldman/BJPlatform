package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*欠款*/
public class CustomerCreditDetail implements Serializable
{
    private Integer id;
    private  String userId;//客户ID
    private CreditType creditType;
    private  Float amount ;//金额

    private String orderSn;//订单编号

    protected String  note;
    protected Date createTime;
    public CustomerCreditDetail()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public Float getAmount() {
        return amount;
    }

    public String getOrderSn()
    {
        return orderSn;
    }

    public String getNote()
    {
        return note;
    }

    public Date getCreateTime()
    {
        return createTime;
    }



    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }


    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}