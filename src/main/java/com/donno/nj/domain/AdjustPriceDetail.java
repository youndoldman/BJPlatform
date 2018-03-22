package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class AdjustPriceDetail implements Serializable
{
    private Integer id;
    private Integer adjPriceScheduleIdx;
    private Goods goods;
    private Float   price;

    private String note;
    private Date createTime;
    private Date updateTime;

    public AdjustPriceDetail()
    {
    }

    /*
    * 属性读取
    * */

    public Integer getId()
    {
        return id;
    }

    public Float getPrice()
    {
        return price;
    }

    public Integer getAdjPriceScheduleIdx()
    {
        return adjPriceScheduleIdx;
    }

    public Goods getGoods()
    {
        return goods;
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

    /*
    * 属性设置
    * */

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setAdjPriceScheduleIdx(Integer adjPriceScheduleIdx)
    {
        this.adjPriceScheduleIdx = adjPriceScheduleIdx;
    }

    public void setGoods(Goods goods)
    {
        this.goods = goods;
    }


    public void setPrice(Float price)
    {
        this.price = price;
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
                .add("id", id)
                .add("price",price)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}