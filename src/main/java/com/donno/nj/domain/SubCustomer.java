package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Date;

public class SubCustomer implements Serializable
{
    public SubCustomer()
    {
    }

    private Integer id;
    private Integer customerIdx;

    private String number;        /**< 客户编号*/
    private boolean haveCylinder; /**<是否携瓶*/
    private Integer status;       /**<客户状态*/
    private String typeCode;      /**<客户类型编码*/
    private String levelCode;    /**<客户级别编码*/
    private String sourceCode;   /**<客户来源编码*/
    private String companyCode;   /**<客户公司编码*/
    CustomerAddress address;     /**<客户地址，只能修改，不能删除*/
    private String phone;        /**<客户电话信息*/

    private String districtCode;
    private String gasStoreCode;

    protected String note;
    protected Date createTime;
    protected Date updateTime;

    public Integer getId()
    {
        return id;
    }


    public Integer getCustomerIdx()
    {
        return customerIdx;
    }


    /**
     * @fn getNumber
     * @brief 取客户编号
     * @return  String 客户编号
     */
    public String getNumber()
    {
        return number;
    }

    /**
     * @fn   getHaveCylinder
     * @brief 取客户携瓶入网状态信息
     * @return boolean 是否携瓶入网
     */
    public boolean getHaveCylinder()
    {
        return haveCylinder;
    }

    /**
     * @fn   getStatus
     * @brief 取客户状态
     * @return Integer  客户状态
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * @fn   getTypeCode
     * @brief 取客户类型编码
     * @return String  客户类型编码
     */
    public String getTypeCode()
    {
        return typeCode;
    }

    /**
     * @fn   getLevelCodeCode
     * @brief 取客户级别编码
     * @return String  客户级别编码
     */
    public String getLevelCodeCode()
    {
        return levelCode;
    }

    /**
     * @fn   getSourceCode
     * @brief 取客户来源编码
     * @return String  客户来源编码
     */
    public String getSourceCode()
    {
        return sourceCode;
    }

    /**
     * @fn   getCompanyCode
     * @brief 取客户公司编码
     * @return String  客户公司编码
     */
    public String getCompanyCode()
    {
        return companyCode;
    }

    public String getDistrictCode()
    {
        return districtCode;
    }

    public String getGasStoreCode()
    {
        return gasStoreCode;
    }

    /**
     * @fn   getAddress
     * @brief 取客户地址信息
     * @return String  客户来源编码
     */
    public CustomerAddress getAddress()
    {
        return address;
    }

    /**
     * @fn   getPhone1
     * @brief 取客户电话信息
     * @return String  客户电话
     */
    public String getPhone()
    {
        return phone;
    }


    public Date getCreateTime()
    {
        return createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }



    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setCustomerIdx(Integer customerIdx)
    {
        this.customerIdx = customerIdx;
    }

    /**
     * @fn  setNumber
     * @brief    设置客户编号信息
     * @param[in]  number 客户编号
     */
    public void setNumber(String number)
    {
        this.number = number;
    }

    /**
     * @fn  setNumber
     * @brief    设置客户携瓶状态
     * @param[in]  haveCylinder 携瓶状态
     */
    public void setHaveCylinder(boolean haveCylinder)
    {
        this.haveCylinder = haveCylinder;
    }

    /**
     * @fn  setStatus
     * @brief    设置客户状态
     * @param[in]  status 客户状态
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    /**
     * @fn  setTypeCode
     * @brief    设置客户类型编码
     * @param[in]  typeCode 类型编码
     */
    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }

    /**
     * @fn  setLevelCode
     * @brief    设置客户级别编码
     * @param[in]  levelCode 级别编码
     */
    public void setLevelCode(String levelCode)
    {
        this.levelCode = levelCode;
    }

    /**
     * @fn  setSourceCode
     * @brief    设置客户来源编码
     * @param[in]  sourceCode 来源编码
     */
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


    /**
     * @fn  setAddress
     * @brief    设置客户地址
     * @param[in]  address 客户地址
     */
    public void setAddress(CustomerAddress address)
    {
        this.address = address;
    }

    /**
     * @fn  setPhone
     * @brief    设置客户电话
     * @param[in]  phone 客户电话
     */
    public  void setPhone(String phone)
    {
        this.phone = phone;
    }


    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("createTime", createTime)
                .add("updateTime", updateTime)
                .toString();
    }
}
