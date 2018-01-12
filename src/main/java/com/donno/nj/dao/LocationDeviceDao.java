package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCylinder;
import com.donno.nj.domain.LocationDevice;

public interface LocationDeviceDao extends BaseDao<LocationDevice>
{
    LocationDevice findByNumber(String number);

    void deleteByIdx(Integer id);


}
