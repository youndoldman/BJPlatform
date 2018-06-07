package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*钢瓶费用标准*/
public class GasCyrChargeSpec implements Serializable
{
    private Integer id;
    private String gasCyrSpecCode; //钢瓶规格编码
    private String gasCyrSpecName;
    private GasCyrChargeType gasCyrChargeType;//收费类型

    private Float price;


    protected String  note;
    protected Date createTime;
    protected Date updateTime;


    public GasCyrChargeSpec()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public String getGasCyrSpecCode() {
        return gasCyrSpecCode;
    }

    public String getGasCyrSpecName() {
        return gasCyrSpecName;
    }

    public GasCyrChargeType getGasCyrChargeType() {
        return gasCyrChargeType;
    }

    public Float getPrice() {
        return price;
    }

    public String getNote()
    {
        return note;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setGasCyrSpecCode(String gasCyrSpecCode) {
        this.gasCyrSpecCode = gasCyrSpecCode;
    }

    public void setGasCyrSpecName(String gasCyrSpecName) {
        this.gasCyrSpecName = gasCyrSpecName;
    }

    public void setGasCyrChargeType(GasCyrChargeType gasCyrChargeType) {
        this.gasCyrChargeType = gasCyrChargeType;
    }

    public void setPrice(Float price) {
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

    public void setUpdateTime(Date updateTime)
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