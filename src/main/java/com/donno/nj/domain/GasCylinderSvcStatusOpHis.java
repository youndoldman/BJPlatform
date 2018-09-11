package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

/*
* 钢瓶业务状态变更历史
* */
public class GasCylinderSvcStatusOpHis implements Serializable
{
    private Integer id;
    private GasCylinder gasCylinder;
    private GasCynServiceStatus srcServiceStatus;
    private GasCynServiceStatus serviceStatus;

    private User srcUser;
    private User targetUser;
    private Date optime;

    private Double longitude;
    private Double latitude;


    private String note;


    public GasCylinderSvcStatusOpHis()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public GasCylinder gasCylinder()
    {
        return gasCylinder;
    }

    public GasCynServiceStatus getServiceStatus()
    {
        return  serviceStatus;
    }

    public GasCynServiceStatus getSrcServiceStatus() {
        return srcServiceStatus;
    }

    public User getSrcUser()
    {
        return srcUser;
    }

    public User getTargetUser()
    {
        return targetUser;
    }

    public  Date getOptime()
    {
        return  optime;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getNote() {
        return note;
    }

    public GasCylinder getGasCylinder() {
        return gasCylinder;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setGasCylinder(GasCylinder gasCylinder)
    {
        this.gasCylinder = gasCylinder;
    }

    public void setSrcUser(User srcUser)
    {
        this.srcUser = srcUser;
    }

    public void setTargetUser(User targetUser)
    {
        this.targetUser = targetUser;
    }

    public void setSrcServiceStatus(GasCynServiceStatus srcServiceStatus) {
        this.srcServiceStatus = srcServiceStatus;
    }

    public void  setServiceStatus(GasCynServiceStatus serviceStatus)
    {
        this.serviceStatus = serviceStatus;
    }

    public void  setOptime(Date optime)
    {
        this.optime = optime;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}