package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.OrderDao;
import com.donno.nj.dao.OrderUrgencyDao;
import com.donno.nj.domain.Order;
import com.donno.nj.domain.OrderUrgency;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.OrderUrgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderUrgencyServiceImpl implements OrderUrgencyService
{
    @Autowired
    private OrderUrgencyDao orderUrgencyDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    @OperationLog(desc = "查询催单信息")
    public List<OrderUrgency> retrieve(Map params)
    {
        return orderUrgencyDao.getList(params);
    }

    @Override
    @OperationLog(desc = "查询催单数量")
    public Integer count(Map params)
    {
        return  orderUrgencyDao.count(params);
    }

    @Override
    @OperationLog(desc = "添加催单信息")
    public void create(OrderUrgency orderUrgency)
    {
        /*参数校验*/
        if (orderUrgency == null)
        {
            throw new ServerSideBusinessException("缺少催单信息，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (orderUrgency.getNote() == null || orderUrgency.getNote().trim().length() == 0)
        {
            orderUrgency.setNote("");
        }

        /*订单信息校验*/
        Order order=  orderDao.findBySn(orderUrgency.getOrderSn());
        if (order == null)
        {
            throw new ServerSideBusinessException("订单不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        orderUrgencyDao.insert(orderUrgency);
    }

    @Override
    @OperationLog(desc = "修改催单信息")
    public void update(Integer id, OrderUrgency orderUrgency)
    {
        /*参数校验*/
        if (orderUrgency == null)
        {
            throw new ServerSideBusinessException("缺少催单信息，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

         /*订单信息校验*/
        Order order=  orderDao.findBySn(orderUrgency.getOrderSn());
        if (order == null)
        {
            throw new ServerSideBusinessException("催单不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*更新数据*/
        orderUrgency.setId(id);
        orderUrgencyDao.update(orderUrgency);
    }

    @Override
    @OperationLog(desc = "删除催单信息")
    public void deleteById(Integer id)
    {
        OrderUrgency orderUrgency = orderUrgencyDao.findById(id);
        if (orderUrgency == null)
        {
            throw new ServerSideBusinessException("催单信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        orderUrgencyDao.delete(id);
    }

}
