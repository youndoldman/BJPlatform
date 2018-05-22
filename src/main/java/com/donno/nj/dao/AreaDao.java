package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Area;
import org.apache.ibatis.annotations.Param;


public interface AreaDao extends BaseDao<Area>
{
    Area findByArea(@Param("province")  String province, @Param("city") String city, @Param("county") String county);
}
