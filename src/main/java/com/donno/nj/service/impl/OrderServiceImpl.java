package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.CustomerDao;
import com.donno.nj.dao.GoodsDao;
import com.donno.nj.dao.OrderDao;
import com.donno.nj.dao.OrderDetailDao;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.OrderService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.text.SimpleDateFormat;

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

    @Autowired
    private CustomerDao customerDao;

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
         /*参数校验*/
        if (order == null || order.getOrderDetailList() == null || order.getOrderDetailList().size() == 0
                )
        {
            throw new ServerSideBusinessException("订单信息不全，请补充订单信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (order.getUrgent() == null)
        {
            order.setUrgent(Boolean.FALSE);
        }

        if (order.getPayType() == null)
        {
            throw new ServerSideBusinessException("订单信息不全，请补充支付类型信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (order.getAccessType() == null)
        {
            throw new ServerSideBusinessException("订单信息不全，请补充接入类型信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (order.getRecvLongitude() == null)
        {
            throw new ServerSideBusinessException("订单信息不全，请补充位置经度信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (order.getRecvLatitude() == null)
        {
            throw new ServerSideBusinessException("订单信息不全，请补充位置纬度信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*校验用户是否存在*/
        if (order.getCustomer() == null || order.getCustomer().getUserId() == null )
        {
            throw new ServerSideBusinessException("订单信息不全，请补充订单客户信息！", HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {
            Customer customer = customerDao.findByUserId(order.getCustomer().getUserId());
            if ( customer == null)
            {
                throw new ServerSideBusinessException("客户信息不正确！", HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                order.setCustomer(customer);
            }
        }


        //生成点单编号
        Date curDate = new Date();
        String dateFmt =  new SimpleDateFormat("yyyyMMddHHmmssSSS").format(curDate);
        order.setOrderSn(dateFmt);

        /*插入订单总表*/
        orderDao.insert(order);

        /*插入详单表*/
        for(OrderDetail orderDetail : order.getOrderDetailList())
        {
            /*校验商品信息是否存在*/
            if (orderDetail.getGoods() == null || orderDetail.getGoods().getCode() == null || orderDetail.getGoods().getCode().trim().length() == 0)
            {
                throw new ServerSideBusinessException("商品信息不正确！", HttpStatus.NOT_ACCEPTABLE);
            }
            Goods good = goodsDao.findByCode(orderDetail.getGoods().getCode());
            if (good == null)
            {
                throw new ServerSideBusinessException("商品信息不存在！", HttpStatus.NOT_ACCEPTABLE);
            }

            orderDetail.setOrderIdx(order.getId());
            orderDetail.setGoods(good);

            orderDetailDao.insert(orderDetail);//插入数据库订单详情表
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
        /*删除订单总表*/
        orderDao.delete(id);

        /*删除订单详细表*/
        orderDetailDao.deleteByOrderIdx(id);
    }
}
