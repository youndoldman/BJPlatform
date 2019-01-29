package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.CstRefereeRel;
import com.donno.nj.domain.GasCynTray;
import com.donno.nj.domain.GasCynTrayBindRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CstRefereeRelDao extends BaseDao<CstRefereeRel>
{
    void addReferee(@Param("customerIdx") Integer customerIdx, @Param("refereeIdx") Integer refereeIdx);
    void removeReferee(@Param("customerIdx") Integer customerIdx, @Param("refereeIdx") Integer refereeIdx);
}
