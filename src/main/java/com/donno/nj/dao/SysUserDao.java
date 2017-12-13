package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.SysUser;
import java.util.List;
import java.util.Date;
public interface SysUserDao extends BaseDao<SysUser>
{
    SysUser findByUserId(String userId);
    void deleteByUserIdx(Integer userIdx);

    void checkAlive(Date now);

    List<String> getDepLeaderByUserId(String userId);
}
