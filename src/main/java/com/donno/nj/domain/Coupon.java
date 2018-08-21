package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*气票*/
public class Coupon implements Serializable
{
    private Integer id;
    private String couponSn;
    private User customer;  //客户

    private String specCode;
    private String specName;
    private Float price;
    private User operator;  //操作员
    private TicketStatus  couponStatus;
    private Date startDate;  //有效期
    private Date endDate;   //有效期
    private Date useTime;  //气票使用日期


    protected String  note;
    protected Date createTime;
    protected Date updateTime;

    public Coupon()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public String getCouponSn() {
        return couponSn;
    }

    public User getCustomer() {
        return customer;
    }

    public String getSpecCode() {
        return specCode;
    }

    public String getSpecName() {
        return specName;
    }

    public TicketStatus getCouponStatus()
    {
        return couponStatus;
    }

    public Float getPrice()
    {
        return  price;
    }

    public User getOperator() {
        return operator;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return  endDate;
    }

    public Date getUseTime()
    {
        return useTime;
    }

    public String getNote()
    {
        return note;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setCouponSn(String couponSn)
    {
        this.couponSn = couponSn;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public void setCouponStatus(TicketStatus couponStatus)
    {
        this.couponStatus = couponStatus;
    }

    public void setPrice(Float price)
    {
        this.price = price;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public  void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}