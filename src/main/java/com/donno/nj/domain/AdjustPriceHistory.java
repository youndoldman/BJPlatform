package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class AdjustPriceHistory implements Serializable
{

    private String  code;
    private String  name;
    private Float   price;

    private Date effect_time;

    public AdjustPriceHistory()
    {
    }

    /*
    * 属性读取
    * */

    public Float getPrice()
    {
        return price;
    }

    public String getName()
    {
        return name;
    }

    public String getCode()
    {
        return code;
    }

    public  Date getEffectTime()
    {
        return  effect_time;
    }


    /*
    * 属性设置
    * */

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setPrice(Float price)
    {
        this.price = price;
    }

    public void setEffectTime(Date effect_time)
    {
        this.effect_time = effect_time;
    }




    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("code", code)
                .add("name", name)
                .add("price",price)
                .add("effect_time", effect_time)
                .toString();
    }
}