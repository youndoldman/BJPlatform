package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class DiscountDetail implements Serializable
{
    private Integer id;
    private Integer discountStrategyIdx;
    private Goods   goods;
    private Float   discount;

    private String note;
    private Date createTime;
    private Date updateTime;



    /*
    * 属性读取
    * */

    public Integer getId()
    {
        return id;
    }

    public Float getDiscount()
    {
        return discount;
    }

    public Integer getDiscountStrategyIdx()
    {
        return discountStrategyIdx;
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

    public void setDiscountStrategyIdx(Integer discountStrategyIdx)
    {
        this.discountStrategyIdx = discountStrategyIdx;
    }

    public void setGoods(Goods goods)
    {
        this.goods = goods;
    }


    public void setDiscount(Float price)
    {
        this.discount = discount;
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
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}