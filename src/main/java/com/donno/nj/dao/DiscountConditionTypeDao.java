package com.donno.nj.dao;

import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.DiscountConditionType;
import com.donno.nj.domain.GoodsType;

public interface DiscountConditionTypeDao extends BaseDao<DiscountConditionType>
{
    DiscountConditionType findByCode(String code);
}
