package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Department;
import com.donno.nj.domain.UserPosition;


public interface UserPositionDao extends BaseDao<UserPosition>
{
    void  deleteByUserId(String userId);
}
