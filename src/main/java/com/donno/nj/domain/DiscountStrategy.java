package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DiscountStrategy implements Serializable
{
    private Integer id;
    private String  name;
    private Date startTime;//起始时间
    private Date endtime;//起始时间

    private DiscountType discountType;//折扣类型
    private DiscountConditionType discountConditionType;//折扣条件
    private String discountConditionValue;  //折扣条件取值

    private DiscountStrategyStatus discountStrategyStatus;

    List<DiscountDetail> discountDetails;

    protected String note;
    protected Date createTime;
    protected Date updateTime;

    public DiscountStrategy()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public  String getName()
    {
        return  name;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public Date getEndtime()
    {
        return endtime;
    }

    public DiscountType getDiscountType()
    {
        return discountType;
    }

    public DiscountStrategyStatus getDiscountStrategyStatus( )
    {
        return  discountStrategyStatus;
    }

    public DiscountConditionType getDiscountConditionType()
    {
        return discountConditionType;
    }

    public String getDiscountConditionValue(){return discountConditionValue;}

    public List<DiscountDetail> getDiscountDetails()
    {
        return discountDetails;
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

    public void setName(String name)
    {
        this.name = name;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public void setEndtime(Date endtime)
    {
        this.endtime = endtime;
    }

    public  void setDiscountType(DiscountType discountType)
    {
        this.discountType = discountType;
    }

    public  void setDiscountConditionType(DiscountConditionType discountConditionType)
    {
        this.discountConditionType = discountConditionType;
    }

    public void  setDiscountConditionValue(String discountConditionValue)
    {
        this.discountConditionValue = discountConditionValue;
    }

    public void  setDiscountStrategyStatus(DiscountStrategyStatus discountStrategyStatus)
    {
        this.discountStrategyStatus = discountStrategyStatus;
    }

    public void setDiscountDetails(List<DiscountDetail> discountDetails)
    {
        this.discountDetails = discountDetails;
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
                .add("name", name)
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}