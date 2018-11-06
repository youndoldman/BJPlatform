package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.ElectDepositDetail;
import com.donno.nj.domain.OrderDetail;

public interface ElectDepositDetailDao extends BaseDao<ElectDepositDetail>
{
    void deleteByElectDepositIdx(Integer id);
}
