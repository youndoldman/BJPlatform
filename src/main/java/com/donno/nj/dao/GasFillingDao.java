package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasFilling;
import com.donno.nj.domain.Ticket;
import org.apache.ibatis.annotations.Param;

public interface GasFillingDao extends BaseDao<GasFilling>
{
    GasFilling find(@Param("stationNumber") String stationNumber,@Param("machineNumber") Integer machineNumber);

    void merge(GasFilling gasFilling);
}
