package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Complaint;


public interface ComplaintDao extends BaseDao<Complaint>
{
    Complaint findBySn(String sn);

}
