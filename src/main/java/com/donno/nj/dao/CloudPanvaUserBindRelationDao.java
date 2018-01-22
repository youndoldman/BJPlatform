package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CloudUser;
import com.donno.nj.domain.User;
import com.donno.nj.domain.CloudPanvaUserBindRelation;

import org.apache.ibatis.annotations.Param;

public interface CloudPanvaUserBindRelationDao extends BaseDao<CloudPanvaUserBindRelation>
{
    void deleteByIdx(Integer id);

    void bindCloudPannaUser(@Param("cloudUserIdx") Integer cloudUserIdx, @Param("panvaUserIdx") Integer panvaUserIdx);
    void unBindCloudPannaUser(@Param("cloudUserIdx") Integer cloudUserIdx, @Param("panvaUserIdx") Integer panvaUserIdx);

    CloudUser findCloudUserByPanvaUserIdx(Integer panvaUserIdx);
    User  findPanvaUserByCloudUserIdx(Integer cloudUserIdx)  ;

    CloudPanvaUserBindRelation findBindRelation(@Param("cloudUserIdx") Integer cloudUserIdx, @Param("panvaUserIdx") Integer panvaUserIdx)  ;

}
