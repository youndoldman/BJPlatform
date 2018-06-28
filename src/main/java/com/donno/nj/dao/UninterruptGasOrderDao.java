package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Order;
import com.donno.nj.domain.UninterruptedGasOrder;
import org.apache.ibatis.annotations.Param;

public interface UninterruptGasOrderDao extends BaseDao<UninterruptedGasOrder>
{
    UninterruptedGasOrder findBySn(String sn);

    void bindWeixinOrderSn(@Param("orderIdx") Integer orderIdx, @Param("weixinOrderSn") String weixinOrderSn, @Param("amount") Integer amount);
}
