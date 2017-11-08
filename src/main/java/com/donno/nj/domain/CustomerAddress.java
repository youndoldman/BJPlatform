package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class CustomerAddress implements Serializable
{
    public CustomerAddress()
    {
    }

    private Integer id;
    private Integer customerIdx;
    private String province;  //省
    private String city;      //市
    private String county;   //区县
    private String detail;    //门牌号
    protected Date createTime;
    protected Date updateTime;

    public Integer getId()
    {
        return id;
    }

    public Integer getCustomerIdx()
    {
        return customerIdx;
    }

    public String getProvince()
    {
        return province;
    }

    public  String getCity()
    {
        return city;
    }

    public String getCounty()
    {
        return county;
    }

    public String getDetail()
    {
        return detail;
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

    public void setCustomerIdx(Integer customerIdx)
    {
        this.customerIdx = customerIdx;
    }
    public void setProvince(String province)
    {
        this.province = province;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setCounty(String county)
    {
        this.county = county;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
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
                .add("id", id)
                .add("customerIdx", customerIdx)
                .add("province", province)
                .add("city", city)
                .add("county",county)
                .add("detail", detail)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}
