package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class OrderUrgency implements Serializable
{
    private Integer id;
    private String orderSn;
    private String userId;
    private String userName;
    private String note;
    private Date createTime;
    private Date updateTime;

    public OrderUrgency()
    {
    }

    /*
    * 属性读取
    * */

    public Integer getId()
    {
        return id;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getNote() {
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

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNote(String note) {
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
                .toString();
    }
}