package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CustomerDistrict;

/**
 * Created by T470P on 2017/10/30.
 */
public interface CustomerDistrictDao extends BaseDao<CustomerDistrict>
{
    CustomerDistrict findByCode(String code);
}
