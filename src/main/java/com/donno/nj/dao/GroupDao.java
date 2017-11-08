package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Group;

public interface GroupDao extends BaseDao<Group>
{
    Group findByCode(String code);
}
