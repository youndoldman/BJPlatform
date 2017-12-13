package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.OrderService;
import com.donno.nj.service.WorkFlowService;
import com.donno.nj.util.AppUtil;
import com.donno.nj.util.DistanceHelper;
import com.donno.nj.activiti.WorkFlowTypes;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.text.SimpleDateFormat;


import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.google.common.collect.ImmutableMap;

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

    @Autowired
    private OrderOpHistoryDao orderOpHistoryDao;

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private SystemParamDao systemParamDao;

    @Autowired
    private SysUserDao sysUserDao;

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

        /*默认非加急*/
        if (order.getUrgent() == null)
        {
            order.setUrgent(Boolean.FALSE);
        }

        /*默认为0*/
        if (order.getOrderStatus() == null)
        {
            order.setOrderStatus(0);
        }

        /*默认为待支付*/
        if (order.getPayStatus() == null)
        {
            order.setPayStatus(PayStatus.PSUnpaid);
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
                throw new ServerSideBusinessException("客户信息不正确，没有客户信息！", HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                order.setCustomer(customer);
            }
        }


        //生成定单编号
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

        /*启动流程*/
        createWorkFlow(order);

        /*订单创建日志*/
        OrderOperHistory(order,order.getOrderStatus());

    }

    public void createWorkFlow(Order order)
    {
        /*启动流程*/
        Map<String, Object> variables = new HashMap<String, Object>();
        Group  group = groupDao.findByCode(ServerConstantValue.GP_CUSTOMER_SERVICE);

        if(group == null)
        {
            throw new ServerSideBusinessException("创建定单失败，系统用户组信息错误！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*指定可办理的组*/
        variables.put(ServerConstantValue.ACT_FW_STG_1_CANDI_GROUPS,String.valueOf(group.getId()));

         /*取系统中可推送派送员的范围参数*/
        Integer dispatchRange = systemParamDao.getDispatchRange();
        if (dispatchRange == null)
        {
            dispatchRange = 5;
        }


        /*指定可办理该流程用户,根据经纬度寻找合适的派送工*/
        Map findDispatchParams = new HashMap<String,String>();
        findDispatchParams.putAll(ImmutableMap.of("groupCode", ServerConstantValue.GP_DISPATCH));
        List<SysUser> sysUsersList = sysUserDao.getList(findDispatchParams);
        if (sysUsersList.size() > 0)
        {
            String candUser = "";
            for (SysUser sysUser:sysUsersList)
            {
                if (sysUser.getUserPosition() != null)
                {
                    Double distance = DistanceHelper.Distance(order.getRecvLatitude(),order.getRecvLongitude(),sysUser.getUserPosition().getLatitude(),sysUser.getUserPosition().getLongitude());
                    if ( distance < dispatchRange)
                    {
                        if (candUser.trim().length() > 0 )
                        {
                            candUser = candUser + ",";
                        }
                        candUser = candUser + sysUser.getUserId();
                    }
                }
            }

            variables.put(ServerConstantValue.ACT_FW_STG_1_CANDI_USERS,candUser);
        }

        workFlowService.createWorkFlow(WorkFlowTypes.GAS_ORDER_FLOW,order.getCustomer().getUserId(),variables,order.getOrderSn());

    }


    @Override
    @OperationLog(desc = "修改订单信息")
    public void update(Integer id, Order newOrder)
    {
        newOrder.setId(id);

        /*更新数据*/
        orderDao.update(newOrder);
    }

    @Override
    @OperationLog(desc = "订单任务更新信息")
    public void update(String taskId,Map<String, Object> variables,Integer id, Order newOrder)
    {
        newOrder.setId(id);

        /*更新数据*/
        orderDao.update(newOrder);

        /*检查任务是否存在，处理订单*/
        int retCode = workFlowService.completeTask(taskId,variables);
        if (retCode != 0 )
        {
            throw new ServerSideBusinessException("订单处理失败！", HttpStatus.EXPECTATION_FAILED);
        }

        /*订单变更历史记录*/
        OrderOperHistory(newOrder,newOrder.getOrderStatus());
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

    @OperationLog(desc = "订单修改历史信息")
    public void OrderOperHistory(Order order,Integer orderStatus)
    {
        Optional<User> user = AppUtil.getCurrentLoginUser();
        if (user.isPresent())
        {
            OrderOpHistory orderOpHistory = new OrderOpHistory();
            orderOpHistory.setOrderSn(order.getOrderSn());
            orderOpHistory.setOrderIdx(order.getId());
            orderOpHistory.setUserId(user.get().getUserId());

            String opLog = "";
            if (orderStatus == OrderStatus.OSUnprocessed.getIndex())
            {
                opLog = "创建订单";
            }
            else if (orderStatus == OrderStatus.OSDispatching.getIndex())
            {
                opLog = "订单派送中";
            }
            else if (orderStatus == OrderStatus.OSSigned.getIndex())
            {
                opLog = "已签收";
            }
            else if (orderStatus == OrderStatus.OSCompleted.getIndex())
            {
                opLog = "已结束";
            }
            else if (orderStatus == OrderStatus.OSCanceled.getIndex())
            {
                if (user.get().getUserGroup().getCode() == ServerConstantValue.GP_CUSTOMER)
                {
                    opLog = "已取消";
                }
                else
                {
                    opLog = "已作废";
                }
            }

            orderOpHistory.setOpLog(opLog);
            orderOpHistoryDao.insert(orderOpHistory);
        }
    }

}
