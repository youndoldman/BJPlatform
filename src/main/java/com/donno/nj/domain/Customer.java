package com.donno.nj.domain;

import com.google.common.base.MoreObjects;
import java.io.Serializable;


/** 
  @class Customer
  @brief 客户信息
  @author wyb
  @note 
 */
public class Customer extends User  implements Serializable
{
    private String number;
    private Integer status;
    private String  typeCode;
    private String characterCode;
    private String sourceCode;
    private String districtCode;
    private String gasStoreCode;
    private CustomerAddress address;
    private String phone1;


    public Customer()
    {

    }

    /*
    * 属性读取
    * */



    public String getNumber()
    {
        return number;
    }

    public Integer getStatus()
    {
        return status;
    }

    public String getTypeCode()
    {
        return typeCode;
    }

    public String getCharacterCode()
    {
        return characterCode;
    }

    public String getSourceCode()
    {
        return sourceCode;
    }

    public String getDistrictCode()
    {
        return districtCode;
    }

    public String getGasStoreCode()
    {
        return gasStoreCode;
    }

    public CustomerAddress getAddress()
    {
        return address;
    }

    public String getPhone1()
    {
        return phone1;
    }


    /*
    * 属性设置
    * */


    public void setNumber(String number)
    {
        this.number = number;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }

    public void setCharacterCode(String characterCode)
    {
        this.characterCode = characterCode;
    }

    public void setSourceCode(String sourceCode)
    {
        this.sourceCode = sourceCode;
    }

    public void setDistrictCode(String districtCode)
    {
        this.districtCode = districtCode;
    }

    public void setGasStoreCode(String gasStoreCode)
    {
        this.gasStoreCode = gasStoreCode;
    }


    public void setAddress(CustomerAddress address)
    {
        this.address = address;
    }

    public  void setPhone1(String phone1)
    {
        this.phone1 = phone1;
    }





    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("userId", userId)
                .add("number",number)
                .add("name", name)
                .add("password",password)
                .add("status",status)
                .add("typeCode",typeCode)
                .add("characterCode",characterCode)
                .add("sourceCode",sourceCode)
                .add("districtCode",districtCode)
                .add("gasStoreCode",gasStoreCode)
                .add("address",address)
                .add("phone1", phone1)
                .add("note", note)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}