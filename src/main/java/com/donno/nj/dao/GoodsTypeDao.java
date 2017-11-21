package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.GoodsType;

public interface GoodsTypeDao extends BaseDao<GoodsType>
{
    GoodsType findByCode(String code);
}
