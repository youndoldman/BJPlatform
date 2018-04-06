package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.User;

public interface UserDao extends BaseDao<User>
{
    User findByUserId(String userId);
    void deleteByUserId(String userId);

}
