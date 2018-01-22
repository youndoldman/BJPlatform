package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CloudUser;


public interface CloudUserDao extends BaseDao<CloudUser>
{
    CloudUser findByCloudUserId(String userId);
}
