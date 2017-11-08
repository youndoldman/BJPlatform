package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.SysUser;
import com.google.common.base.Optional;

public interface SysUserDao extends BaseDao<SysUser>
{
    SysUser findByUserId(String userId);
    void deleteByUserId(String userId);
}
