package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao extends BaseDao<User>
{
    User findByUserId(String userId);
    User findByUserIdPwd(@Param("userId") String userId, @Param("password")String password);
    void deleteByUserId(String userId);

}
