package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class Advice implements Serializable
{
    private Integer id;
    private String userId;
    private String userName;
    private String advice;
    private Date createTime;
    private Date updateTime;

    public Advice()
    {
    }

    /*
    * 属性读取
    * */

    public Integer getId()
    {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAdvice() {
        return advice;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
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