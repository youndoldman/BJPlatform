package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*回款*/
public class WriteOffDetail implements Serializable
{
    private Integer id;
    private  String userId;//客户ID
    private CreditType creditType;
    private  Float amount ;//金额

    private PayType payType;

    protected String  note;
    protected Date createTime;
    public WriteOffDetail()
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

    public PayType getPayType() {
        return payType;
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

    public void setPayType(PayType payType) {
        this.payType = payType;
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