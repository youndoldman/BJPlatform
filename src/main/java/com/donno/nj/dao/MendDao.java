package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Mend;


public interface MendDao extends BaseDao<Mend>
{
    Mend findBySn(String sn);

}
