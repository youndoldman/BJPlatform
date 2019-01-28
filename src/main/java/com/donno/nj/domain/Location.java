package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;


public class Location implements Serializable
{


    private Double longitude;
    private Double latitude;


    public Location()
    {
    }



    public Double getLongitude()
    {
        return longitude;
    }

    public Double getLatitude()
    {
        return latitude;
    }



    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }




    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}