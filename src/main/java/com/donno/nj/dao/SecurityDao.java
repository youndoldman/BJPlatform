package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Security;


public interface SecurityDao extends BaseDao<Security>
{
    Security findBySn(String sn);

}
