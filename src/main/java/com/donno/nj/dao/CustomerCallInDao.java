package com.donno.nj.dao;

import java.util.Map;
import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CustomerCallIn;

public interface CustomerCallInDao extends BaseDao<CustomerCallIn>
{
    void delete(Map params);
}
