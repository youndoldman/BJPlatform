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

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomerCreditDao customerCreditDao;

    @Autowired
    private CustomerCreditDetailDao customerCreditDetailDao;

    @Autowired
    private TicketOrderDao ticketOrderDao;

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
        if (order == null || order.getOrderDetailList() == null || order.getOrderDetailList().size() == 0)
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
            order.setOrderStatus(OrderStatus.OSUnprocessed.getIndex());
        }

        /*默认为待支付*/
        order.setPayStatus(PayStatus.PSUnpaid);
//        if (order.getPayStatus() == null)
//        {
//            order.setPayStatus(PayStatus.PSUnpaid);
//        }


//        if (order.getPayType() == null)
//        {
//            throw new ServerSideBusinessException("订单信息不全，请补充支付类型信息！", HttpStatus.NOT_ACCEPTABLE);
//        }

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

        /*根据结算类型自动设置支付方式*/
        setPayType(order.getCustomer().getSettlementType(),order);

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

//
//    public  void associateTicket(Order order)
//    {
//        List<OrderDetail> orderDetails = order.getOrderDetailList();
//        for(OrderDetail orderDetail : orderDetails)
//        {
//            String specCode = orderDetail.getGoods().getCode();
//
//            Map params = new HashMap<String,String>();
//            params.putAll(ImmutableMap.of("customerUserId", order.getCustomer().getUserId()));
//            params.putAll(ImmutableMap.of("specCode", specCode));
//            params.putAll(ImmutableMap.of("useStatus", TicketStatus.TSUnUsed.getIndex()));
//            params.putAll(ImmutableMap.of("expireType", 0));//未过期的
//            params.putAll(ImmutableMap.of("limit", 1));
//
//            List<Ticket> tickets = ticketDao.getList(params);
//            if (tickets.size() == 0)
//            {
//                String message = String.format("规格为%s的气票不足，下单失败",orderDetail.getGoods().getName());
//                throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
//            }
//
//            /*将一张气票状态更改为订购中*/
//            for(Ticket ticket : tickets)
//            {
//                ticket.setTicketStatus(TicketStatus.TSOrdering);
//                ticketDao.update(ticket);
//
//                TicketOrder ticketOrder = new TicketOrder();
//                ticketOrder.setTicketIdx(ticket.getId());
//                ticketOrder.setOrderSn(order.getOrderSn());
//                ticketOrder.setNote("订购中");
//                ticketOrderDao.insert(ticketOrder);
//            }
//        }
//    }
//
//    public void unassociateTicket(Order order)
//    {
//        Map params = new HashMap<String,String>();
//        params.putAll(ImmutableMap.of("orderSn", order.getOrderSn()));
//        params.putAll(ImmutableMap.of("userId", order.getCustomer().getUserId()));
//
//        List<TicketOrder> ticketOrders = ticketOrderDao.getList(params);
//
//        for(TicketOrder ticketOrder : ticketOrders)
//        {
//            /*将关联气票状态更改为待使用*/
//            Ticket ticket = new Ticket();
//            ticket.setId(ticketOrder.getTicketIdx());
//            ticket.setTicketStatus(TicketStatus.TSOrdering);
//            ticketDao.update(ticket);
//
//            /*删除气票-订单关联记录*/
//            ticketOrderDao.delete(ticketOrder.getId());
//        }
//    }

    public void checkTicket(Order order)
    {
        /*to do ......
        * 检查客户手中气票是否足够
        *
        * */

        List<OrderDetail> orderDetails = order.getOrderDetailList();
        for(OrderDetail orderDetail : orderDetails)
        {
            String specCode = orderDetail.getGoods().getCode();

            Map params = new HashMap<String,String>();
            params.putAll(ImmutableMap.of("customerUserId", order.getCustomer().getUserId()));
            params.putAll(ImmutableMap.of("specCode", specCode));
            params.putAll(ImmutableMap.of("useStatus", TicketStatus.TSUnUsed.getIndex()));
            params.putAll(ImmutableMap.of("expireType", 0));//未过期的
            params.putAll(ImmutableMap.of("limit", 1));

            List<Ticket> tickets = ticketDao.getList(params);
            if (tickets.size() == 0)
            {
                String message = String.format("规格为%s的气票数量不足",orderDetail.getGoods().getCode());
                throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
            }
        }

    }

    public void setPayType(SettlementType settlementType,Order order)
    {
        if (settlementType == null)
        {
            throw new ServerSideBusinessException("客户数据错误，缺少结算类型信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        String code = settlementType.getCode();
        if (code == null || code.trim().length() == 0)
        {
            throw new ServerSideBusinessException("客户数据错误，缺少结算类型信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (code.equals(ServerConstantValue.SETTLEMENT_TYPE_TICKET) )//气票用户
        {
            order.setPayType(PayType.PTTicket);
            checkTicket(order);
        }
        else if (code.equals(ServerConstantValue.SETTLEMENT_TYPE_MONTHLY_CREDIT))//月结用户
        {
            order.setPayType(PayType.PTMonthlyCredit);
        }
        else  if (code.equals(ServerConstantValue.SETTLEMENT_TYPE_COMMON_USER) )//普通用户
        {
            order.setPayType(PayType.PTOnLine);
        }
        else
        {
            throw new ServerSideBusinessException("客户结算类型信息错误！", HttpStatus.NOT_ACCEPTABLE);
        }
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
    public void update(Order srcOrder, Order newOrder)
    {
        if(newOrder.getPayType() != null)
        {
//            Optional<User> userOptional = AppUtil.getCurrentLoginUser();
//            if (userOptional.isPresent() || userOptional.get()== null)
//            {
//                throw new ServerSideBusinessException("获取当前用户信息失败！", HttpStatus.UNAUTHORIZED);
//            }
//
//            /*支付类型修改，只有当前订单的派送工派送到家时才能修改*/
//            SysUser sysUser = newOrder.getDispatcher();
//            if (sysUser == null)
//            {
//                throw new ServerSideBusinessException("不允许修改订单支付类型！", HttpStatus.UNAUTHORIZED);
//            }
//            if (sysUser.getUserId().compareTo(userOptional.get().getUserId()) != 0)
//            {
//                throw new ServerSideBusinessException("不允许修改订单支付类型！", HttpStatus.UNAUTHORIZED);
//            }

            checkPayType(newOrder.getPayType(),srcOrder.getCustomer());
        }

        /*订单支付状态修改*/
        if(newOrder.getPayStatus() != null)
        {
            PayType payType;
            if(newOrder.getPayType() != null)
            {
                payType = newOrder.getPayType();
            }
            else
            {
                payType = srcOrder.getPayType();
            }
            updatePayStatus(newOrder.getPayStatus(),payType,srcOrder.getCustomer(),srcOrder);
        }

        /*更新数据*/
        newOrder.setId(srcOrder.getId());
        orderDao.update(newOrder);
    }

    public void addCredit(CreditType creditType,Customer customer,Order order)
    {
        CustomerCredit customerCredit = customerCreditDao.findByUserIdCreditType(customer.getUserId(),creditType);
        if (customerCredit == null)
        {
            /*记录当前欠款额*/
            customerCredit = new CustomerCredit();
            customerCredit.setAmount( order.getOrderAmount());
            customerCredit.setUserId(customer.getUserId());
            customerCredit.setCreditType(creditType);
            customerCredit.setNote("");
            customerCreditDao.insert(customerCredit);
        }
        else
        {
            /*更新当前欠款额*/
            customerCredit.setAmount(customerCredit.getAmount() + order.getOrderAmount());
            customerCreditDao.update(customerCredit);
        }

        /*记录交易明细*/
        CustomerCreditDetail customerCreditDetail = new CustomerCreditDetail();
        customerCreditDetail.setCreditType(creditType);
        customerCreditDetail.setUserId(customer.getUserId());
        customerCreditDetail.setOrderSn(order.getOrderSn());
        customerCreditDetail.setAmount( order.getOrderAmount());
        customerCreditDetailDao.insert(customerCreditDetail);
    }

    public void updatePayStatus(PayStatus payStatus,PayType payType,Customer customer,Order srcOrder)
    {
        if (payStatus == PayStatus.PSPaied )
        {
            SettlementType settlementType = customer.getSettlementType();
            String settlementCode =  settlementType.getCode();
            if (settlementType == null || settlementCode == null || settlementCode.trim().length() == 0)
            {
                throw new ServerSideBusinessException("订单数据错误，缺少客户结算类型信息！", HttpStatus.NOT_ACCEPTABLE);
            }

            if (settlementCode.equals(ServerConstantValue.SETTLEMENT_TYPE_COMMON_USER))//普通用户
            {
                if (payType == PayType.PTDebtCredit)
                {
                    addCredit(CreditType.CTCommonCredit,customer,srcOrder);//赊销记录
                }
            }
            else  if (settlementCode.equals(ServerConstantValue.SETTLEMENT_TYPE_MONTHLY_CREDIT))
            {
                addCredit(CreditType.CTMonthlyCredit,customer,srcOrder);//赊销记录
            }
        }
    }

    public void checkPayType(PayType payType,Customer customer)
    {
        if(customer == null)
        {
            throw new ServerSideBusinessException("订单数据错误，缺少客户信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        SettlementType settlementType = customer.getSettlementType();
        String settlementCode =  settlementType.getCode();
        if (settlementType == null || settlementCode == null || settlementCode.trim().length() == 0)
        {
            throw new ServerSideBusinessException("订单数据错误，缺少客户结算类型信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (settlementCode.equals(ServerConstantValue.SETTLEMENT_TYPE_COMMON_USER))//普通用户
        {
            if (payType == PayType.PTOnLine || payType == PayType.PTCash ||payType == PayType.PTDebtCredit )
            {
                //to do nothing
            }
            else
            {
                throw new ServerSideBusinessException("客户支付方式不允许修改！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            throw new ServerSideBusinessException("客户支付方式不允许修改！", HttpStatus.NOT_ACCEPTABLE);
        }
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

        /*订单指派关系*/
        if (newOrder.getOrderStatus() == OrderStatus.OSDispatching.getIndex())
        {
            String strCandiUser =  (String)variables.get(ServerConstantValue.ACT_FW_STG_2_CANDI_USERS);

            SysUser candiUser = sysUserDao.findByUserId(strCandiUser);
            orderDao.insertDistatcher(newOrder.getId(),candiUser.getId());
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

    @Override
    @OperationLog(desc = "订单作废")
    public void cancelOrder(String orderSn)
    {
        Order order = orderDao.findBySn(orderSn);

        if (order == null)
        {
            throw new ServerSideBusinessException("订单不存在！", HttpStatus.NOT_FOUND);
        }

        order.setOrderStatus( OrderStatus.OSCanceled.getIndex());

        /*更新订单状态为作废*/
        orderDao.update(order);

        /*更新订单关联的气票状态为未使用*/
//        unassociateTicket(order);

        /*结束订单流程*/
        if(workFlowService.deleteProcess(orderSn) != 0)
        {
            throw new ServerSideBusinessException("订单作废失败！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*退款*/
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
            if (orderStatus == OrderStatus.OSUnprocessed.getIndex()) {
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


    @OperationLog(desc = "微信支付成功")
    public void weixinPayOk(String orderSn,String weixinOrderSn,Integer amount)
    {
        /*参数校验*/
        if (orderSn == null || orderSn.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("订单号参数为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (weixinOrderSn == null || weixinOrderSn.trim().length() == 0)
        {
            throw new ServerSideBusinessException("微信订单号参数为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*订单号检查*/
        Order order = orderDao.findBySn(orderSn);
        if (order == null)
        {
            throw new ServerSideBusinessException("微信订单号不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*订单状态更改为已支付*/
        order.setPayStatus(PayStatus.PSPaied);
        order.setPayTime(new Date());
        orderDao.update(order);

        /*订单号与微信订单号关联*/
        orderDao.bindWeixinOrderSn(order.getId(),weixinOrderSn,amount);
    }

    public void ticketPay(Order order,List<Ticket> tickets)
    {
        /*校验客户是否气票用户*/
        if (!order.getCustomer().getSettlementType().getCode().equals(ServerConstantValue.SETTLEMENT_TYPE_TICKET) )
        {
            throw new ServerSideBusinessException("客户不是气票用户！", HttpStatus.NOT_ACCEPTABLE);
        }

        for (Ticket ticket :tickets)
        {
            if (ticket.getTicketSn() == null || ticket.getTicketSn().trim().length() == 0)
            {
                throw new ServerSideBusinessException("缺少气票编号！", HttpStatus.NOT_ACCEPTABLE);
            }

            Ticket target = ticketDao.findBySn(ticket.getTicketSn());
            if (target == null)
            {
                String message = String.format("系统中没有%s该气票",ticket.getTicketSn());
                throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
            }

            /*将气票状态设置为已使用*/
            target.setTicketStatus(TicketStatus.TSUsed);
            target.setUseTime(new Date());
            ticketDao.update(target);

            /*增加气票订单记录*/
            TicketOrder ticketOrder = new TicketOrder();
            ticketOrder.setTicketIdx(target.getId());
            ticketOrder.setOrderSn(order.getOrderSn());
            ticketOrder.setNote("已支付");
            ticketOrderDao.insert(ticketOrder);
        }

        /*更改订单支付状态为已支付*/
        order.setPayStatus(PayStatus.PSPaied);
        order.setOrderStatus(OrderStatus.OSSigned.getIndex());
        order.setPayTime(new Date());
        orderDao.update(order);
    }
}
