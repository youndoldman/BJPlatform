package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*气票*/
public class CouponOrder implements Serializable
{
    private Integer id;
    private String userId;  //客户名称
    private String orderSn;       //订单编号
    private String specName;
    private Integer couponIdx;
    private String couponSn;
    private Float price;
    private Date expStartDate;  //有效期
    private Date expEndDate;   //有效期
    private Date useTime;  //气票使用日期

    protected String  note;
    protected Date createTime;
    protected Date updateTime;


    public CouponOrder()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderSn()
    {
        return orderSn;
    }

    public String getSpecName() {
        return specName;
    }

    public Integer getCouponIdx() {
        return couponIdx;
    }

    public String getCouponSn() {
        return couponSn;
    }

    public Float getPrice() {
        return price;
    }

    public Date getExpStartDate() {
        return expStartDate;
    }

    public Date getExpEndDate() {
        return expEndDate;
    }

    public Date getUseTime() {
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


    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public void setCouponIdx(Integer couponIdx) {
        this.couponIdx = couponIdx;
    }

    public void setCouponSn(String couponSn) {
        this.couponSn = couponSn;
    }

    public void setExpStartDate(Date expStartDate) {
        this.expStartDate = expStartDate;
    }

    public void setExpEndDate(Date expEndDate) {
        this.expEndDate = expEndDate;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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