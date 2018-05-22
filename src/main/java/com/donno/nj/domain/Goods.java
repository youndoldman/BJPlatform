package com.donno.nj.domain;

import com.google.common.base.MoreObjects;
import java.util.Date;

import java.io.Serializable;

public class Goods implements Serializable
{
    private Integer id;
    private String code;
    private String name;
    private String specifications;
    private String unit;
    private Float  weight;
    private Float price;
    private Integer status;
    private Integer lifeExpectancy;

    private GoodsType goodsType;

    private Area area;

    private String  note;
    private Date createTime;
    private Date updateTime;

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

    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

    public String getSpecifications()
    {
        return specifications;
    }

    public String getUnit()
    {
        return unit;
    }

    public Float getWeight()
    {
        return weight;
    }

    public Integer getStatus()
    {
        return status;
    }

    public Integer getLifeExpectancy()
    {
        return lifeExpectancy;
    }

    public GoodsType getGoodsType()
    {
        return goodsType;
    }

    public Area getArea() {
        return area;
    }

    public Float getPrice()
    {
        return price;
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

    public void setCode(String code)
    {
        this.code = code;
    }

    public  void setName(String name)
    {
        this.name = name;
    }

    public void setSpecifications(String specifications)
    {
        this.specifications = specifications;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public  void  setWeight(Float weight)
    {
        this.weight = weight;
    }

    public void setLifeExpectancy(Integer lifeExpectancy)
    {
        this.lifeExpectancy = lifeExpectancy;
    }

    public void setPrice(Float price)
    {
        this.price = price;
    }

    public void setGoodsType(GoodsType goodsType)
    {
        this.goodsType = goodsType;
    }

    public void setArea(Area area) {
        this.area = area;
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