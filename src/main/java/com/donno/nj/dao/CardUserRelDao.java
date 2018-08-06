package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CardUserRelDao extends BaseDao<UserCardBindRelation>
{
    void deleteByIdx(Integer id);

    void bind(@Param("cardIdx") Integer cardIdx, @Param("customerIdx") Integer customerIdx);
    void unBind(@Param("cardIdx") Integer cardIdx, @Param("customerIdx") Integer customerIdx);

    UserCardBindRelation findRelationByNumber(String number);
    List<UserCardBindRelation> findUserCardByUserId(String userId)  ;
}
