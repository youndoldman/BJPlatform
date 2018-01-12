package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class GasCylinder implements Serializable
{
    private Integer id;
    private String number;

    private GasCylinderSpec spec;

    private Date productionDate;
    private Date verifyDate;
    private Date nextVerifyDate;
    private Date scrapDate;

    private Double longitude;
    private Double latitude;
    private DeviceStatus status;

    private LocationDevice locationDevice;

    private String  note;
    private Date createTime;
    private Date updateTime;

    public GasCylinder()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public String getNumber()
    {
        return number;
    }

    public GasCylinderSpec getSpec()
    {
        return spec;
    }

    public Date getProductionDate()
    {
        return productionDate;
    }

    public Date getVerifyDate()
    {
        return verifyDate;
    }

    public Date getNextVerifyDate()
    {
        return nextVerifyDate;
    }

    public Date getScrapDate()
    {
        return scrapDate;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public Double getLatitude()
    {
        return latitude;
    }

   public DeviceStatus getStatus()
   {
       return  status;
   }

    public LocationDevice getLocationDevice()
    {
        return locationDevice;
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

    public void setNumber(String number)
    {
        this.number = number;
    }

    public void setSpec(GasCylinderSpec spec)
    {
        this.spec = spec;
    }

    public void setProductionDate(Date productionDate)
    {
        this.productionDate = productionDate;
    }

    public void setVerifyDate(Date verifyDate)
    {
        this.verifyDate = verifyDate;
    }

    public void setNextVerifyDate(Date nextVerifyDate)
    {
        this.nextVerifyDate = nextVerifyDate;
    }

    public void setScrapDate(Date scrapDate)
    {
        this.scrapDate = scrapDate;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public void setStatus(DeviceStatus status)
    {
        this.status = status;
    }

    public void setLocationDevice(LocationDevice locationDevice)
    {
        this.locationDevice = locationDevice;
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
                .add("number", number)
                .add("spec", spec)
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}