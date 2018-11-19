package com.donno.nj.dao;


import com.donno.nj.dao.base.BaseDao;
import com.donno.nj.domain.Order;
import org.apache.ibatis.annotations.Param;
public interface OrderDao extends BaseDao<Order>
{
    Order findBySn(String sn);

    Long insertDistatcher(@Param("orderIdx") Integer orderIdx,@Param("userIdx") Integer userIdx);

    Long updateDistatcher(@Param("orderIdx") Integer orderIdx,@Param("userIdx") Integer userIdx);

    void bindWeixinOrderSn(@Param("orderIdx") Integer orderIdx,@Param("weixinOrderSn") String weixinOrderSn,@Param("amount") Integer amount);

    void bindGasCynNumber(@Param("orderIdx") Integer orderIdx,@Param("gasCynIdx") Integer gasCynIdx);
    void unBindGasCynNumber(@Param("orderIdx") Integer orderIdx,@Param("gasCynIdx") Integer gasCynIdx);

    Integer ifBindGasCynNumber(@Param("orderIdx") Integer orderIdx,@Param("gasCynIdx") Integer gasCynIdx);

    Order findByGasCynNumber(String gasCynNumer);

}
