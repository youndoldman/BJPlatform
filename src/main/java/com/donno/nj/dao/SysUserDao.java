package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.SysUser;
import com.donno.nj.domain.UserPosition;
import com.google.common.base.Optional;

public interface SysUserDao extends BaseDao<SysUser>
{
    SysUser findByUserId(String userId);
    void deleteByUserIdx(Integer userIdx);


}
