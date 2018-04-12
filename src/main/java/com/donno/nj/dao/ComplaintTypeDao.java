package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.ComplaintType;

public interface ComplaintTypeDao extends BaseDao<ComplaintType>
{
    ComplaintType findByCode(String code);
}
