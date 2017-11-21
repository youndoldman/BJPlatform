package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.GoodsDao;
import com.donno.nj.dao.OrderDao;
import com.donno.nj.dao.OrderDetailDao;
import com.donno.nj.domain.Order;
import com.donno.nj.domain.Goods;
import com.donno.nj.domain.OrderDetail;
import com.donno.nj.service.OrderService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class OrderServiceImpl implements OrderService
{

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private  OrderDetailDao orderDetailDao;

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public Optional<Order> findBySn(String sn) {
        return Optional.fromNullable(orderDao.findBySn(sn));
    }

    @Override
    @OperationLog(desc = "查询订单信息")
    public List<Order> retrieve(Map params)
    {
        List<Order> orders = orderDao.getList(params);
        return orders;
    }

    @Override
    @OperationLog(desc = "查询订单数量")
    public Integer count(Map params) {
        return orderDao.count(params);
    }

    @Override
    @OperationLog(desc = "创建订单信息")
    public void create(Order order)
    {
        //插入订单表
        order.setOrderSn("test");

        orderDao.insert(order);

        //遍历订单详单信息，插入详单表
        for(OrderDetail orderDetail : order.getOrderDetailList())
        {
            /*查询对应的商品idx*/
            Goods good = goodsDao.findByName(orderDetail.getGoodsName());
            if ( good != null)
            {
                orderDetail.setOrderIdx(order.getId());
                orderDetail.setGoodsIdx(good.getId());
                orderDetailDao.insert(orderDetail);
            }
            else
            {
                //抛出异常
                throw new RuntimeException();
            }
        }
    }

    @Override
    @OperationLog(desc = "修改订单信息")
    public void update(Integer id, Order newOrder)
    {
        newOrder.setId(id);

        /*更新数据*/
        orderDao.update(newOrder);

        /*订单详情表修改*/
    }

    @Override
    @OperationLog(desc = "删除订单信息")
    public void deleteById(Integer id)
    {
        /*删除子表*/

        /*删除订单表*/
        orderDao.deleteByIdx(id);
    }
}
