package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GasCynUserRel;
import com.donno.nj.domain.User;
import org.apache.ibatis.annotations.Param;

public interface GasCynUserRelDao extends BaseDao<GasCynUserRel>
{
    void deleteByIdx(Integer id);

    GasCynUserRel findBindRel(Integer gasCylinderIdx);
    void bindUser(@Param("gasCylinderIdx") Integer gasCylinderIdx, @Param("userIdx") Integer userIdx);
    void updateBindedUser(@Param("id") Integer id, @Param("userIdx") Integer userIdx);

    User findLiableUserByCylinderId(Integer gasCylinderIdx);
}
