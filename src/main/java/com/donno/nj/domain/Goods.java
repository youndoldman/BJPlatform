package com.donno.nj.domain;

import com.google.common.base.MoreObjects;
import com.google.common.net.InetAddresses;
import java.util.Date;

import java.io.Serializable;

public class Goods implements Serializable
{
    private Integer id;
    private String name;
    private float price;
    private String  info;

    private String  note;
    private Date createTime;
    private Date updateTime;

    private GoodsType goodsType;

    public Goods()
    {
    }

    /*
    * 属性读取
    * */

    public Integer getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public GoodsType getGoodsType()
    {
        return goodsType;
    }

    public float getPrice()
    {
        return price;
    }

    public  String getInfo()
    {
        return  info;
    }

    public  String getNote()
    {
        return  note;
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

    public  void setName(String name)
    {
        this.name = name;
    }

    public void setPrice(float price)
    {
        this.price = price;
    }

    public void setGoodsType(GoodsType goodsType)
    {
        this.goodsType = goodsType;
    }

    public void setInfo(String info)
    {
        this.info = info;
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
                .add("name", name)
                .add("goodType",goodsType.getName())
                .add("price",price)
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}