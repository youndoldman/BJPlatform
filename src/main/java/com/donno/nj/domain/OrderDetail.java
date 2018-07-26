package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class OrderDetail implements Serializable
{
    private Integer id;
    private Integer orderIdx;
//    private Integer goodsIdx;

//    private String  goodsName;
    private Float   originalPrice;//原单价

    private Goods goods;

    private Float   dealPrice;//成交单价
    private Integer quantity;//数量

    private Float refoundSum;//退款金额
    private Float subtotal;//金额小计

    private Date createTime;
    private Date updateTime;

    public OrderDetail()
    {
    }

    /*
    * 属性读取
    * */

    public Integer getId()
    {
        return id;
    }

//    public Integer getGoodsIdx()
//    {
//        return goodsIdx;
//    }
//
//    public Integer getOrderIdx()
//    {
//        return orderIdx;
//    }
//
//    public String getGoodsName()
//    {
//        return goodsName;
//    }
//
    public Float getOriginalPrice()
    {
        return originalPrice;
    }

    public Integer getOrderIdx()
    {
        return orderIdx;
    }

    public Goods getGoods()
    {
        return goods;
    }

    public Float getDealPrice()
    {
        return dealPrice;
    }

    public  Integer getQuantity()
    {
        return  quantity;
    }

    public Float getRefoundSum() {
        return refoundSum;
    }

    public  Float getSubtotal()
    {
        return  subtotal;
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

    public void setOrderIdx(Integer orderIdx)
    {
        this.orderIdx = orderIdx;
    }

    public void setGoods(Goods goods)
    {
        this.goods = goods;
    }

//    public void setGoodsIdx(Integer goodsIdx)
//    {
//        this.goodsIdx = goodsIdx;
//    }
//
//    public void setOrderIdx(Integer orderIdx)
//    {
//        this.orderIdx = orderIdx;
//    }
//
//    public  void setGoodsName(String goodsName)
//    {
//        this.goodsName = goodsName;
//    }
//
    public void setOriginalPrice(Float originalPrice)
    {
        this.originalPrice = originalPrice;
    }

    public void setDealPrice(Float dealPrice)
    {
        this.dealPrice = dealPrice;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public void setRefoundSum(Float refoundSum) {
        this.refoundSum = refoundSum;
    }

    public void setSubtotal(Float subtotal)
    {
        this.subtotal = subtotal;
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
                .add("dealPrice",dealPrice)
                .add("quantity",quantity)
                .add("subtotal",subtotal)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}