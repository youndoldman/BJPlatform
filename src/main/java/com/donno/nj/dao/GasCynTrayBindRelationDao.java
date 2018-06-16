package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.*;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface GasCynTrayBindRelationDao extends BaseDao<GasCynTrayBindRelation>
{
    void deleteByIdx(Integer id);

    void bind(@Param("gasCynTrayIdx") Integer gasCynTrayIdx, @Param("customerIdx") Integer customerIdx);
    void unBind(@Param("gasCynTrayIdx") Integer gasCynTrayIdx, @Param("customerIdx") Integer customerIdx);

    GasCynTrayBindRelation findRelationByNumber(String number);
    List<GasCynTray>  findGasCynTrayByUserId(String userId)  ;

}
