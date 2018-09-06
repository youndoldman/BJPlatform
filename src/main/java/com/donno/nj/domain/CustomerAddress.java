package com.donno.nj.domain;

import com.google.common.base.MoreObjects;
import java.io.Serializable;

public class CustomerAddress implements Serializable
{
    public CustomerAddress()
    {
    }

    private String province;  //省
    private String city;      //市
    private String county;   //区县
    private String detail;    //门牌号


    public String getProvince()
    {
        return province;
    }

    public  String getCity()
    {
        return city;
    }

    public String getCounty()
    {
        return county;
    }

    public String getDetail()
    {
        return detail;
    }


    public void setProvince(String province)
    {
        this.province = province;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setCounty(String county)
    {
        this.county = county;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }


    @Override
    public String toString()
    {
//        return MoreObjects.toStringHelper(this)
//                .add("province", province)
//                .add("city", city)
//                .add("county",county)
//                .add("detail", detail)
//                .toString();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(province).append(city).append(county).append(detail);
        return  stringBuffer.toString();
    }
}
