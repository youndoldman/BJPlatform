package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Date;
import java.util.Map;
public interface SysUserDao extends BaseDao<SysUser>
{
    SysUser findBySysUserId(String userId);
    void deleteByUserIdx(Integer userIdx);

    void checkAlive(Date now);

    List<SysUser> getDepLeaderByUserId(@Param("userId") String userId, @Param("groupCode" )String groupCode);
    void uploadPhoto(@Param("userId") String userId, @Param("photo") byte[] photo);

    Map<String,Object>  downloadPhoto(@Param("userId") String userId);

}
