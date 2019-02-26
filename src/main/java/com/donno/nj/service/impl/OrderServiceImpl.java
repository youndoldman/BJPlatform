package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.OrderService;
import com.donno.nj.service.SmsService;
import com.donno.nj.service.SysUserService;
import com.donno.nj.service.WorkFlowService;
import com.donno.nj.util.AppUtil;
import com.donno.nj.util.DistanceHelper;
import com.donno.nj.activiti.WorkFlowTypes;
import com.donno.nj.util.RandomHelper;
import com.google.common.base.Optional;
import groovy.lang.Tuple;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.text.SimpleDateFormat;
import com.donno.nj.util.Clock;


import com.google.common.collect.ImmutableMap;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import java.math.BigDecimal;

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
    private CustomerCallInDao customerCallInDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private SystemParamDao systemParamDao;

    @Autowired
    private GasCylinderDao gasCylinderDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private CouponOrderDao couponOrderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomerCreditDao customerCreditDao;

    @Autowired
    private CustomerCreditDetailDao customerCreditDetailDao;

    @Autowired
    private TicketOrderDao ticketOrderDao;

    @Autowired
    private DiscountStrategyDao discountStrategyDao;

    @Autowired
    private DiscountDetailDao discountDetailDao;

    @Autowired
    private SmsService smsService;

    @Autowired
    private GasRefoundDetailDao gasRefoundDetailDao;

    @Autowired
    private GasCylinderSpecDao gasCylinderSpecDao;

    @Autowired
    private OrderGasCynRelDao orderGasCynRelDao;

    @Autowired
    private CstRefereeRelDao cstRefereeRelDao;

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

        /*默认为0*/
        if (order.getOrderTriggerType() == null)
        {
            order.setOrderTriggerType(OrderTriggerType.OTTNormal);
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

        Customer customer = customerDao.findByCstUserId(order.getCustomer().getUserId());
        if ( customer == null)
        {
            throw new ServerSideBusinessException("客户信息不正确，没有客户信息！", HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {
            order.setCustomer(customer);
        }

        OrderTriggerType orderTriggerType = order.getOrderTriggerType();

        //生成定单编号
        Date curDate = new Date();
        String dateFmt =  new SimpleDateFormat("yyyyMMddHHmmssSSS-").format(curDate);
        dateFmt = dateFmt + RandomHelper.getRandomNumber();
        order.setOrderSn(dateFmt);

        /*避免重复编号*/
        if (orderDao.findBySn(dateFmt) != null)
        {
            throw new ServerSideBusinessException("订单生成失败，请重新提交！", HttpStatus.NOT_ACCEPTABLE);
        }

        order.setOriginalAmount(0f);
        order.setOrderAmount(0f);

        /*根据结算类型自动设置支付方式*/
        setPayType(order.getCustomer().getSettlementType(),order);

        /*插入订单总表*/
        orderDao.insert(order);

        Float dealAmount = 0f; //订单实际成交总金额
        Float originalAmount = 0f;//订单原始价格总金额

        /*插入详单表*/
        for(OrderDetail orderDetail : order.getOrderDetailList())
        {
            if (orderDetail.getQuantity() == null)
            {
                throw new ServerSideBusinessException("请填写商品数量！", HttpStatus.NOT_ACCEPTABLE);
            }

            Float dealPrice = 0f;   //订单中每个商品成交单价

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
            orderDetail.setGoods(good);

            /*检查商品是否停止销售或者下架*/
            if (orderDetail.getGoods().getStatus() != 0)
            {
                String message = String.format("商品%s已经暂停销售!",orderDetail.getGoods().getName());
                throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
            }

            /*查询优惠,计算满足条件的每件商品优惠后价格，及订单总金额*/
            if (customer.getSettlementType().getCode().equals(ServerConstantValue.SETTLEMENT_TYPE_COMMON_USER) ||
                    customer.getSettlementType().getCode().equals(ServerConstantValue.SETTLEMENT_TYPE_MONTHLY_CREDIT   ))//普通用户,月结用户 可以享受优惠
            {
                dealPrice = discount(orderDetail,customer);
            }
            else
            {
                dealPrice = good.getPrice()  ;
            }

            Float originalSubtotal = good.getPrice() *  orderDetail.getQuantity();//原价小计
            originalAmount = originalAmount + originalSubtotal ; //原价总计

            Float dealSubtotal = dealPrice * orderDetail.getQuantity();//成交价格小计
            dealAmount = dealAmount + dealSubtotal; //成交价格总计

            orderDetail.setOriginalPrice(good.getPrice());
            orderDetail.setDealPrice(dealPrice);
            orderDetail.setSubtotal(dealSubtotal);
            orderDetail.setOrderIdx(order.getId());
            orderDetail.setGoods(good);

            orderDetailDao.insert(orderDetail);//插入数据库订单详情表
        }

        /*更新订单总金额*/
        Order orderUpdateAmount = new Order();
        orderUpdateAmount.setId(order.getId());
        orderUpdateAmount.setOriginalAmount(originalAmount);

//        if(orderTriggerType != null && orderTriggerType == OrderTriggerType.OTTTrayWarning )
//        {
//            orderUpdateAmount.setOrderAmount(0f);//托盘告警生成的订单，总金额为0，不付费
//        }
//        else
//        {
        orderUpdateAmount.setOrderAmount(dealAmount);
//        }

        orderDao.update(orderUpdateAmount);

        /*启动流程*/
        createWorkFlow(order);

        /*增加呼叫记录*/
        addCallIn(customer,order);

        /*订单创建日志*/
        OrderOperHistory(order,order.getOrderStatus());
    }

    void addCallIn(Customer customer,Order order)
    {
        if(order.getAccessType() == AccessType.ATCustomService)
        {
            if (order.getCallInPhone() == null && order.getCallInPhone().trim().length() == 0)
            {
                throw new ServerSideBusinessException("缺少客户呼入电话信息！", HttpStatus.NOT_ACCEPTABLE);
            }
            CustomerCallIn customerCallIn = new CustomerCallIn();
            customerCallIn.setPhone(order.getCallInPhone());
            customerCallIn.setCustomer(customer);
            customerCallIn.setCounty(customer.getAddress().getCounty());
            customerCallIn.setCity(customer.getAddress().getCity());
            customerCallIn.setProvince(customer.getAddress().getProvince());
            customerCallIn.setDetail(customer.getAddress().getDetail());


            /*检查呼叫信息是否已经存在，若存在，更新时间*/
            Map params = new HashMap<String,String>();
            params.putAll(ImmutableMap.of("phone", customerCallIn.getPhone()));
            params.putAll(ImmutableMap.of("userId", customer.getUserId()));
            params.putAll(ImmutableMap.of("province", customerCallIn.getProvince()));
            params.putAll(ImmutableMap.of("city", customerCallIn.getCity()));
            params.putAll(ImmutableMap.of("county", customerCallIn.getCounty()));
            params.putAll(ImmutableMap.of("detail", customerCallIn.getDetail()));
            List<CustomerCallIn> customerCallIns =  customerCallInDao.getList(params);
            if (customerCallIns.size() == 0)
            {
                customerCallIn.setCustomer(customer);
                customerCallInDao.insert(customerCallIn);
            }
//            else
//            {
//                customerCallInDao.update(customerCallIn);
//            }
        }
    }

    public  Float discount(OrderDetail orderDetail,Customer customer)
    {
        Float dealPrice = orderDetail.getGoods().getPrice();

        Map params = new HashMap<String,String>();

        params.putAll(ImmutableMap.of("status", DiscountStrategyStatus.DSSEffecitve.getIndex()));
        params.putAll(ImmutableMap.of("startTime", new Date()));
        params.putAll(ImmutableMap.of("endTime", new Date()));

        List<DiscountStrategy>  discountStrategies = discountStrategyDao.getList(params);
        Boolean findTarget = false;
        for (DiscountStrategy discountStrategy : discountStrategies)
        {
            if ( (discountStrategy.getDiscountConditionType().getCode().equals(ServerConstantValue.DISCOUNT_CONDITION_TYPE_CUSTOMER_LEVEL)
                    && customer.getCustomerLevel().getCode().equals(discountStrategy.getDiscountConditionValue()))    /*按用户级别*/
                    || (discountStrategy.getDiscountConditionType().getCode().equals(ServerConstantValue.DISCOUNT_CONDITION_TYPE_CUSTOMER_TYPE)
                    && customer.getCustomerType().getCode().equals(discountStrategy.getDiscountConditionValue()))   //按客户类别
                    )
            {
                /*找到该策略中对应的订单商品*/
                for (DiscountDetail discountDetail : discountStrategy.getDiscountDetails())
                {
                    if (orderDetail.getGoods().getCode().equals(discountDetail.getGoods().getCode()))//订单详单中的商品
                    {
                        if (discountStrategy.getDiscountType() == DiscountType.DTCheapX )//直减
                        {
                            dealPrice = dealPrice - discountDetail.getDiscount();
                            findTarget = true;
                            break;
                        }
                        else if(discountStrategy.getDiscountType() == DiscountType.DTCheapX )//百分比折扣
                        {
                            dealPrice = dealPrice - dealPrice * discountDetail.getDiscount() / 100;
                            findTarget = true;
                            break;
                        }
                    }
                }
            }

            /*优惠是否叠加*/
            if (findTarget)
            {
                if (discountStrategy.getUseType() == DiscountUseType.DUTExclusive)
                {
                    break;
                }
            }
        }

        return dealPrice;
    }

    @Override
    @OperationLog(desc = "订单关联钢瓶号")
    public void orderBindGasCynNumber(String orderSn,String gasCynNumbers)
    {
        /*订单号校验*/
        Order order = orderDao.findBySn(orderSn);
        if(order == null)
        {
            throw new ServerSideBusinessException("订单不存在", HttpStatus.NOT_ACCEPTABLE);
        }

        if (gasCynNumbers == null || gasCynNumbers.trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少钢瓶号信息", HttpStatus.NOT_ACCEPTABLE);
        }

        String[] gasCynNumberArray = gasCynNumbers.split(",");
        for (String gasCynNumber :gasCynNumberArray)
        {
            /*钢瓶号校验*/
            GasCylinder gasCylinder = gasCylinderDao.findByNumber(gasCynNumber);
            if (gasCylinder == null) {
                throw new ServerSideBusinessException("钢瓶不存在", HttpStatus.NOT_ACCEPTABLE);
            }

            /*记录唯一*/
            if(orderDao.ifBindGasCynNumber(gasCylinder.getId()) != null)
            {
                //避免重复绑定
                orderDao.unBindGasCynNumber(gasCylinder.getId());
            }

            /*记录关联订单-钢瓶信息*/
            orderDao.bindGasCynNumber(order.getId(),gasCylinder.getId());
        }
    }


    @Override
    @OperationLog(desc = "计算订单价格")
    public  List<GasCalcResult> caculate(String orderSn,String customerId,List<OrderCaculator> orderCaculators)
    {
        List<GasCalcResult> gasCalcResults = new ArrayList<GasCalcResult>() ;

        StringBuilder stringBuilder = new StringBuilder();
        if (orderSn == null || orderSn.trim().length() == 0 )
        {
            throw new ServerSideBusinessException("缺少订单号", HttpStatus.NOT_ACCEPTABLE);
        }

        Order order = orderDao.findBySn(orderSn);
        if (order == null)
        {
            throw new ServerSideBusinessException("订单不存在", HttpStatus.NOT_ACCEPTABLE);
        }
        if (order.getOrderStatus() != OrderStatus.OSDispatching.getIndex())//其他状态不允许执行
        {
            throw new ServerSideBusinessException("当前订单状态不允许执行该操作", HttpStatus.NOT_ACCEPTABLE);
        }

        /*不需要计算残液回收*/
        if (orderCaculators == null || orderCaculators.size() == 0 )
        {
            return gasCalcResults;
        }

        /*客户校验*/
        User user = userDao.findByUserId(customerId);
        if (user == null)
        {
            throw new ServerSideBusinessException("未查询到客户信息", HttpStatus.NOT_ACCEPTABLE);
        }

        /*避免接口重复调用时重复计算，重置订单实际价格*/
        Float amount = 0f;
        for(OrderDetail orderDetail : order.getOrderDetailList())
        {
            amount = amount + orderDetail.getSubtotal() ;
        }
        order.setOrderAmount(amount);

        /*接口可能重复调用，查询gasRefoundDetail1表，如果当前订单的回款详情存在，则找到对应的prevOrderIdx，
        * 重置 订单-钢瓶关联表中 prevOrder 记录的标记为 未计算，待所有计算工作完成后，标记为 已计算
        * */
        Map paramsGasRefoundDetail = new HashMap<String,String>();
        paramsGasRefoundDetail.putAll(ImmutableMap.of("orderSn", orderSn));
        List<GasRefoundDetail>  gasRefoundDetails = gasRefoundDetailDao.getList(paramsGasRefoundDetail);
        if (gasRefoundDetails.size() > 0)
        {
            for (GasRefoundDetail gasRefoundDetail:gasRefoundDetails)
            {
                if (gasRefoundDetail.getPrevOrder() != null)
                {
                    Map paramsOrderGasCyn = new HashMap<String,String>();
                    paramsOrderGasCyn.putAll(ImmutableMap.of("orderSn", gasRefoundDetail.getPrevOrder()));
                    List<OrderGasCynRel>  orderGasCynRels = orderGasCynRelDao.getList(paramsOrderGasCyn);

                    if (orderGasCynRels.size() > 0)
                    {
                        for (OrderGasCynRel orderGasCynRel : orderGasCynRels)
                        {
                            orderGasCynRel.setOrderGasCynRelStatus(OrderGasCynRelStatus.OGRSUnused);
                            orderGasCynRelDao.update(orderGasCynRel);
                        }
                    }
                }
                gasRefoundDetailDao.delete(gasRefoundDetail.getId());
            }
        }


        Boolean needUpdateOrder =  true;
        Float refoundSum = 0.0f;
        for (OrderCaculator caculator :orderCaculators)
        {
            GasCalcResult gasCalcResult = new GasCalcResult();

            /*空瓶重量数据校验*/
            if (caculator.getRefoundWeight() == null)
            {
                stringBuilder.append("钢瓶");
                stringBuilder.append(caculator.getGasCynNumber());
                stringBuilder.append("缺少回收重量数据");
                throw new ServerSideBusinessException(stringBuilder.toString(), HttpStatus.NOT_ACCEPTABLE);
            }

            /*钢瓶校验*/
            GasCylinder gasCylinder = gasCylinderDao.findByNumber(caculator.getGasCynNumber());
            if (gasCylinder == null)
            {
                gasCalcResult.setGasCynNumber(caculator.getGasCynNumber());
                gasCalcResult.setSuccess(false);
                gasCalcResult.setNote("系统没有该钢瓶信息");
                gasCalcResults.add(gasCalcResult);
                continue;
            }

            Float unitPrice = 0.0f;//单价
            GasRefoundDetail gasRefoundDetailInfo = new GasRefoundDetail();
            if (caculator.getForceCaculate() == null)
            {
                caculator.setForceCaculate(false);
            }
            if (!caculator.getForceCaculate())
            {
                /*从钢瓶-订单对应表 查找钢瓶对应的订单*/
                Order prevOrder = orderDao.findByGasCynNumber(caculator.getGasCynNumber());
                if (prevOrder == null)
                {
                    gasCalcResult.setGasCynNumber(caculator.getGasCynNumber());
                    gasCalcResult.setSuccess(false);
                    gasCalcResult.setNote("系统无该钢瓶关联订单信息");
                    gasCalcResults.add(gasCalcResult);
                    continue;
                }

                /*
                * 关联订单校验：
                *避免误操作将空瓶号与重瓶号输入成一样，
                *判断当前订单号应与关联订单号不一样，确保关联订单为上一单单号
                * */
                if (prevOrder.getId().equals(order.getId()))
                {
                    gasCalcResult.setGasCynNumber(caculator.getGasCynNumber());
                    gasCalcResult.setSuccess(false);
                    gasCalcResult.setNote("客户关联订单信息错误，钢瓶信息为当前订单");
                    gasCalcResults.add(gasCalcResult);
                    continue;
                }

                if (!prevOrder.getCustomer().getId().equals(user.getId()))
                {
                    gasCalcResult.setGasCynNumber(caculator.getGasCynNumber());
                    gasCalcResult.setSuccess(false);
                    gasCalcResult.setNote("客户关联订单信息错误");
                    gasCalcResults.add(gasCalcResult);
                    continue;
                }

                /*把订单-钢瓶关联信息的状态改为已使用*/
                {
                    Map paramsOrderGasCyn = new HashMap<String,String>();
                    paramsOrderGasCyn.putAll(ImmutableMap.of("orderSn", prevOrder.getOrderSn()));
                    paramsOrderGasCyn.putAll(ImmutableMap.of("gasCynNumber", caculator.getGasCynNumber()));
                    List<OrderGasCynRel>  orderGasCynRels = orderGasCynRelDao.getList(paramsOrderGasCyn);
                    if (orderGasCynRels.size() > 0)
                    {
                        for (OrderGasCynRel orderGasCynRel : orderGasCynRels)
                        {
                            orderGasCynRel.setOrderGasCynRelStatus(OrderGasCynRelStatus.OGRSUsed);
                            orderGasCynRelDao.update(orderGasCynRel);
                        }
                    }
                }

                 /*对订单中的每个商品，查找与钢瓶规格一致的商品的价格*/
                Boolean bFindTarget = false;
                for (OrderDetail orderDetail:prevOrder.getOrderDetailList())
                {
                    if ((orderDetail.getGoods() == null) ||
                            (orderDetail.getGoods().getGasCylinderSpec() == null) )
                    {
                        gasCalcResult.setGasCynNumber(caculator.getGasCynNumber());
                        gasCalcResult.setSuccess(false);
                        gasCalcResult.setNote("客户关联订单信息商品或规格信息错误");
                        gasCalcResults.add(gasCalcResult);
                        continue;
                    }
                    else
                    {
                        if (orderDetail.getGoods().getGasCylinderSpec().getId() == gasCylinder.getSpec().getId())
                        {
                            unitPrice = orderDetail.getDealPrice() / orderDetail.getGoods().getWeight();//成交价/重量

                        /*回款详情*/
                            gasRefoundDetailInfo.setOrderSn(orderSn);
                            gasRefoundDetailInfo.setGasCynNumber(caculator.getGasCynNumber());
                            gasRefoundDetailInfo.setForceCaculate(false);
                            gasRefoundDetailInfo.setStandWeight(orderDetail.getGoods().getWeight());
                            gasRefoundDetailInfo.setRefoundWeight(caculator.getRefoundWeight());
                            gasRefoundDetailInfo.setTareWeight( gasCylinder.getTareWeight().floatValue());
                            gasRefoundDetailInfo.setRemainGas( gasRefoundDetailInfo.getRefoundWeight() - gasRefoundDetailInfo.getTareWeight() );
                            gasRefoundDetailInfo.setUnitPrice(unitPrice);
                            gasRefoundDetailInfo.setDealPrice( orderDetail.getDealPrice());
                            gasRefoundDetailInfo.setRefoundSum( gasRefoundDetailInfo.getRemainGas() * gasRefoundDetailInfo.getUnitPrice());
                            refoundSum = gasRefoundDetailInfo.getRefoundSum() + refoundSum;//回款总价
                            gasRefoundDetailInfo.setPrevOrder(prevOrder.getOrderSn());
                            gasRefoundDetailInfo.setPrevGoodsCode(orderDetail.getGoods().getCode());
                            gasRefoundDetailInfo.setNote("");
                            gasRefoundDetailDao.insert(gasRefoundDetailInfo);

                            /*返回结果信息*/
                            gasCalcResult.setGasCynNumber(caculator.getGasCynNumber());
                            gasCalcResult.setSuccess(true);
                            gasCalcResult.setNote("系统关联订单信息");
                            gasCalcResult.setGasRefoundDetail(gasRefoundDetailInfo);
                            gasCalcResults.add(gasCalcResult);
                            bFindTarget = true;
                            break;
                        }
                    }
                }

                /*订单中无该规格商品*/
                if (!bFindTarget)
                {
                    gasCalcResult.setGasCynNumber(caculator.getGasCynNumber());
                    gasCalcResult.setSuccess(false);
                    gasCalcResult.setNote("系统关联订单无该规格商品信息");
                    gasCalcResults.add(gasCalcResult);
                }
            }
            else//强制输入信息
            {

                /*标称重量校验*/
                if(caculator.getStandWeight() == null)
                {
                    stringBuilder.append("钢瓶");
                    stringBuilder.append(caculator.getGasCynNumber());
                    stringBuilder.append("缺少该钢瓶规格液化气重量数据");
                    throw new ServerSideBusinessException(stringBuilder.toString(), HttpStatus.NOT_ACCEPTABLE);
                }

                /*价格数据校验*/
                if(caculator.getDealPrice() == null)
                {
                    stringBuilder.append("钢瓶");
                    stringBuilder.append(caculator.getGasCynNumber());
                    stringBuilder.append("缺少该钢瓶上次交付价格数据");
                    throw new ServerSideBusinessException(stringBuilder.toString(), HttpStatus.NOT_ACCEPTABLE);
                }

                unitPrice = caculator.getDealPrice() / caculator.getStandWeight();

                /*回款详情*/
                gasRefoundDetailInfo.setOrderSn(orderSn);
                gasRefoundDetailInfo.setGasCynNumber(caculator.getGasCynNumber());
                gasRefoundDetailInfo.setForceCaculate(true);
                gasRefoundDetailInfo.setStandWeight(caculator.getStandWeight());
                gasRefoundDetailInfo.setRefoundWeight(caculator.getRefoundWeight());
                gasRefoundDetailInfo.setTareWeight( gasCylinder.getTareWeight().floatValue());
                gasRefoundDetailInfo.setRemainGas( gasRefoundDetailInfo.getRefoundWeight() - gasRefoundDetailInfo.getTareWeight() );
                gasRefoundDetailInfo.setUnitPrice(unitPrice);
                gasRefoundDetailInfo.setDealPrice( caculator.getDealPrice());
                gasRefoundDetailInfo.setRefoundSum( gasRefoundDetailInfo.getRemainGas() * gasRefoundDetailInfo.getUnitPrice());
                refoundSum = gasRefoundDetailInfo.getRefoundSum() + refoundSum;//回款总价
                gasRefoundDetailDao.insert(gasRefoundDetailInfo);

                /*返回结果信息*/
                gasCalcResult.setGasCynNumber(caculator.getGasCynNumber());
                gasCalcResult.setSuccess(true);
                gasCalcResult.setNote("系统关联订单信息");
                gasCalcResult.setGasRefoundDetail(gasRefoundDetailInfo);
                gasCalcResults.add(gasCalcResult);
            }
        }

        for(GasCalcResult gasCalcResultCheck:gasCalcResults)
        {
            if(!gasCalcResultCheck.getSuccess())
            {
                needUpdateOrder = false;
            }
        }


        if (needUpdateOrder)
        {
            Order opderUpd = new Order();
            opderUpd.setId(order.getId());
            order.setRefoundSum(refoundSum);
            Float lastAmount = order.getOrderAmount() - refoundSum;
            int   scale   =  1;//设置位数
            int   roundingMode   =   4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
            BigDecimal   bd   =   new   BigDecimal((double)lastAmount);
            bd   =   bd.setScale(scale,roundingMode);
            lastAmount   =   bd.floatValue();
            order.setOrderAmount(lastAmount);
            orderDao.update(order);
        }
        else
        {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return  gasCalcResults;
    }

/*
* 2018.10.23废除残液计算方式
* */

//
//    @Override
//    @OperationLog(desc = "计算订单价格")
//    public Order caculate(String orderSn,String gasCynNumbers)
//    {
//        if (orderSn == null || orderSn.trim().length() == 0 )
//        {
//            throw new ServerSideBusinessException("缺少订单号", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        Order order = orderDao.findBySn(orderSn);
//        if (order == null)
//        {
//            throw new ServerSideBusinessException("订单不存在", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        if (gasCynNumbers == null || gasCynNumbers.trim().length() == 0 )
//        {
//            throw new ServerSideBusinessException("缺少钢瓶编号", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        /*避免接口重复调用时重复计算，重置订单实际价格*/
//        Float amount = 0f;
//        for(OrderDetail orderDetail : order.getOrderDetailList())
//        {
//            amount = amount + orderDetail.getDealPrice();
//        }
//
//        order.setId(order.getId());
//        order.setRefoundSum(0f);
//        order.setOrderAmount(amount);
//
//
//        /*查订单地址对应的燃气价格*/
//        CustomerAddress customerAddress = order.getRecvAddr();
//        Map params = new HashMap<String,String>();
//        params.putAll(ImmutableMap.of("province", customerAddress.getProvince()));
//        params.putAll(ImmutableMap.of("city", customerAddress.getCity()));
//        params.putAll(ImmutableMap.of("county", customerAddress.getCounty()));
//        params.putAll(ImmutableMap.of("typeCode", ServerConstantValue.GOODS_TYPE_GAS));
//        List<Goods> goodsList =  goodsDao.getList(params);
//        Float gasPrice = Float.POSITIVE_INFINITY;
//        if (goodsList.size() > 0)
//        {
//            for (Goods goods : goodsList) {
//                if (Math.round(goods.getPrice() * 100) / 100.0 < Math.round(gasPrice * 100) / 100.0) {
//                    gasPrice = goods.getPrice();
//                }
//            }
//        }
//        else
//        {
//            throw new ServerSideBusinessException("缺少本区域燃气价格信息，请系统管理员先进行添加", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        String[] gasCynNumberArray = gasCynNumbers.split(",");
//        for (String gasCynNumber :gasCynNumberArray)
//        {
//            GasCylinder gasCylinder = gasCylinderDao.findByNumber(gasCynNumber);
//            if (gasCylinder == null)
//            {
//                throw new ServerSideBusinessException("钢瓶不存在", HttpStatus.NOT_ACCEPTABLE);
//            }
//
//            gasCylinder.setGasPrice(gasPrice);
//            gasCylinderDao.update(gasCylinder);
//
//            Float maxGasWeight = 10f;
//
//                /*临时对应规格的气量*/
//            if (gasCylinder.getSpec().getCode().equals("0001") )
//            {
//                maxGasWeight = 4f;
//            }
//            else if(gasCylinder.getSpec().getCode().equals("0002") )
//            {
//                maxGasWeight = 12f;
//            }
//            else
//            {
//                maxGasWeight = 48f;
//            }
//
//            Float refoundSum = gasPrice * ( maxGasWeight -( gasCylinder.getFullWeight() - gasCylinder.getEmptyWeight()));
//
//
//            order.setId(order.getId());
//            order.setRefoundSum(order.getRefoundSum() + refoundSum);
//            order.setOrderAmount(order.getOrderAmount() - refoundSum);
//
//            orderDao.update(order);
//
//        }
//
//        return  order;
//    }

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


    public void checkCoupun(Order order,Coupon coupon)
    {
        /*检查优惠券规格、使用状态、是否过期*/
        String message;
        if (coupon.getCouponStatus() == TicketStatus.TSUsed)
        {
            throw new ServerSideBusinessException("优惠券已经被使用", HttpStatus.NOT_ACCEPTABLE);
        }

        /*规格*/

        /*期限*/
        if (coupon.getEndDate().before(new Date()))
        {
            throw new ServerSideBusinessException("优惠券已经过期", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public void checkTicket(Order order,Ticket ticket)
    {
        /*检查气票规格、使用状态、是否过期*/
        String message;
        if (ticket.getTicketStatus() == TicketStatus.TSUsed)
        {
            message = String.format("气票%s的已经被使用",ticket.getTicketSn());
            throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
        }

        /*规格*/

        /*期限*/
        if (ticket.getEndDate().before(new Date()))
        {
            message = String.format("气票%s的已经过期",ticket.getTicketSn());
            throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public void checkTicket(Order order)
    {
        /*to do ......
        * 检查客户手中气票是否足够
        *
        * */

        Map params = new HashMap<String,String>();
        params.putAll(ImmutableMap.of("userId", order.getCustomer().getUserId()));
        params.putAll(ImmutableMap.of("payStatus", PayStatus.PSUnpaid));
        params.putAll(ImmutableMap.of("payType", PayType.PTTicket));
        List<Order> unpaidOrders = orderDao.getList(params);

        /*优惠券和气票查询条件*/
        params.clear();
        params.putAll(ImmutableMap.of("customerUserId", order.getCustomer().getUserId()));
        params.putAll(ImmutableMap.of("useStatus", TicketStatus.TSUnUsed.getIndex()));
        params.putAll(ImmutableMap.of("expireType", 0));//未过期的


        if (unpaidOrders.size() == 0 )
        {
            List<OrderDetail> orderDetails = order.getOrderDetailList();//当前订单详情
            for(OrderDetail orderDetail : orderDetails)
            {
                String specCode = orderDetail.getGoods().getGasCylinderSpec().getCode();
                params.putAll(ImmutableMap.of("specCode", specCode));

                /*优惠券*/
                Integer couponCount = couponDao.count(params);
                Integer ticketCount = ticketDao.count(params);

                if (couponCount + ticketCount < orderDetail.getQuantity())
                {
                    String message = String.format("规格为%s的优惠券和气票数量不足",orderDetail.getGoods().getCode());
                    throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
                }
            }
        }
        else
        {
            //to do ...
//            for (Order unpaidOrder : unpaidOrders )
//            {
//                for(OrderDetail unpaidOrderDetail : unpaidOrder.getOrderDetailList())
//                {
//                    String specCode = unpaidOrderDetail.getGoods().getCode();
//                    params.putAll(ImmutableMap.of("specCode", specCode));
//
//                        /*优惠券*/
//                    Integer couponCount = couponDao.count(params);
//                    Integer ticketCount = ticketDao.count(params);
//
//                    if (couponCount + ticketCount < unpaidOrderDetail.getQuantity())
//                    {
//                        String message = String.format("规格为%s的优惠券和气票数量不足",unpaidOrderDetail.getGoods().getCode());
//                        throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
//                    }
//                }
//            }

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
            //order.setPayType(PayType.PTOnLine);
            //不做处理
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

        /*指定可办理的组，客服组都可以办理*/
        variables.put(ServerConstantValue.ACT_FW_STG_1_CANDI_GROUPS,String.valueOf(group.getId()));

         /*取系统中可推送派送员的范围参数*/
        Integer sysDispatchRange = systemParamDao.getDispatchRange();
        if (sysDispatchRange == null)
        {
            sysDispatchRange = 5;
        }

        Integer maxDistance = systemParamDao.getMaxDispatchRange();
        if (maxDistance == null || maxDistance == 0)
        {
            maxDistance = 10;
        }

        /*可以抢单的候选人*/
        String candUser = "";

        /*订单同时要允许推荐人抢单*/
        Map refereeParam = new HashMap<String,String>();
        refereeParam.putAll(ImmutableMap.of("customerId", order.getCustomer().getUserId()));
        List<CstRefereeRel> cstRefereeRels = cstRefereeRelDao.getList(refereeParam);
        if (cstRefereeRels.size() > 0)
        {
            for (CstRefereeRel refereeRel :cstRefereeRels)
            {
                SysUser RefereeUser = sysUserDao.findBySysUserId(refereeRel.getReferee().getUserId());

                if(RefereeUser==null||RefereeUser.getAliveStatus()==AliveStatus.ASOffline){
                    continue;
                }
                Double distance = DistanceHelper.Distance(order.getRecvLatitude(),order.getRecvLongitude(),
                        RefereeUser.getUserPosition().getLatitude(),RefereeUser.getUserPosition().getLongitude());
                if ( distance < sysDispatchRange)
                {
                    if (candUser.trim().length() > 0 )
                    {
                        candUser = candUser + ",";
                    }
                    candUser = candUser + refereeRel.getReferee().getUserId();
                }
            }
        }
        if(candUser.trim().length() == 0){
             /*指定可办理该流程用户,根据经纬度寻找合适的派送工*/
            Map findDispatchParams = new HashMap<String,String>();
            findDispatchParams.putAll(ImmutableMap.of("groupCode", ServerConstantValue.GP_DISPATCH));
            List<SysUser> sysUsersList = sysUserDao.getList(findDispatchParams);

            if (sysUsersList.size() > 0)
            {
                Integer dispatchRange = sysDispatchRange;
                while (candUser.trim().length() == 0)
                {
                    for (SysUser sysUser:sysUsersList)
                    {
                        if (sysUser.getUserPosition() != null)
                        {
                        /*候选人已经包含当前直销员*/
                            if (candUser.contains(sysUser.getUserId()))
                            {
                                continue;
                            }
                            else
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
                    }

                    if (candUser.trim().length() > 0)
                    {
                        break;
                    }
                    else
                    {
                        dispatchRange = dispatchRange +  1;
                    }

                    if (dispatchRange > maxDistance)
                    {
                        break;
                    }
                }
            }



//            if (candUser.trim().length() > 0 )
//            {
//                variables.put(ServerConstantValue.ACT_FW_STG_1_CANDI_USERS,candUser);
//            }
//            else
//            {
//                throw new ServerSideBusinessException("附近没有配送人员，无法创建订单！", HttpStatus.NOT_ACCEPTABLE);
//            }
        }
//        else
//        {
//            variables.put(ServerConstantValue.ACT_FW_STG_1_CANDI_USERS,candUser);
//            //throw new ServerSideBusinessException("系统尚无配送人员，无法创建订单！", HttpStatus.NOT_ACCEPTABLE);
//        }



        variables.put(ServerConstantValue.ACT_FW_STG_1_CANDI_USERS,candUser);
        if (workFlowService.createWorkFlow(WorkFlowTypes.GAS_ORDER_FLOW,order.getCustomer().getUserId(),variables,order.getOrderSn()) < 0)
        {
            throw new ServerSideBusinessException("流程控制器创建失败！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*发送系统推送消息*/
        if (candUser.trim().length() > 0)
        {
            try
            {
                MsgPush msgPush = new MsgPush();
                msgPush.PushNotice(candUser, ServerConstantValue.NEW_ORDER_TITLE,order.getRecvAddr().getCity()
                        +order.getRecvAddr().getCounty()+order.getRecvAddr().getDetail());
            }
            catch(Exception e)
            {
                System.out.print(e.getMessage());
                //消息推送失败
            }
        }

    }

    @Override
    @OperationLog(desc = "修改订单信息")
    public void update(Order srcOrder, Order newOrder)
    {
        if(newOrder.getPayType() != null)
        {
            Optional<User> userOptional = AppUtil.getCurrentLoginUser();
            if (!userOptional.isPresent() || userOptional.get()== null)
            {
                throw new ServerSideBusinessException("获取当前用户信息失败！", HttpStatus.UNAUTHORIZED);
            }

            /*支付类型修改，只有当前订单的派送工派送到家时才能修改*/
            SysUser sysUser = srcOrder.getDispatcher();
            if (sysUser == null)
            {
                throw new ServerSideBusinessException("不允许修改订单支付类型！", HttpStatus.UNAUTHORIZED);
            }
            if (sysUser.getUserId().compareTo(userOptional.get().getUserId()) != 0)
            {
                throw new ServerSideBusinessException("不允许修改订单支付类型！", HttpStatus.UNAUTHORIZED);
            }

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

            if(srcOrder.getPayStatus() == PayStatus.PSUnpaid &&
                    newOrder.getPayStatus() == PayStatus.PSPaied)
            {
                newOrder.setPayTime(new Date());
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
        if (srcOrder.getPayStatus() == PayStatus.PSUnpaid &&
                payStatus == PayStatus.PSPaied )
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

        //2018.08.21 放开限制
//        if (settlementCode.equals(ServerConstantValue.SETTLEMENT_TYPE_COMMON_USER))//普通用户
//        {
//            if (payType == PayType.PTOnLine || payType == PayType.PTCash ||payType == PayType.PTDebtCredit )
//            {
//                //to do nothing
//            }
//            else
//            {
//                throw new ServerSideBusinessException("客户支付方式错误！", HttpStatus.NOT_ACCEPTABLE);
//            }
//        }
//        else
//        {
//            throw new ServerSideBusinessException("客户支付方式错误！", HttpStatus.NOT_ACCEPTABLE);
//        }
    }

    @Override
    @OperationLog(desc = "订单任务更新信息")
    public void taskModify(String taskId,String orderSn,String  userId)
    {
        /*检查订单是否存在*/
        Order order=  orderDao.findBySn(orderSn);
        if (order == null)
        {
            throw new ServerSideBusinessException("定单不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户信息检查*/
        if (userId == null || userId.trim().length() == 0)
        {
            throw new ServerSideBusinessException("用户参数不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        User user = userDao.findByUserId(userId);
        if (user == null)
        {
            throw new ServerSideBusinessException("用户不存在！", HttpStatus.NOT_ACCEPTABLE);
        }


        String oldUserId = order.getDispatcher().getUserId();
        if(oldUserId==null){
            throw new ServerSideBusinessException("未接订单！", HttpStatus.NOT_ACCEPTABLE);
        }
        if (order.getOrderStatus() == OrderStatus.OSDispatching.getIndex())//只有派送中的才能重新指派
        {

        }
        else
        {
            throw new ServerSideBusinessException("订单不允许重新指派！", HttpStatus.NOT_ACCEPTABLE);
        }
        /*删除原处理人*/
        if (workFlowService.deleteCandidateUsers(taskId,oldUserId) != 0 )
        {
            throw new ServerSideBusinessException("订单任务更新失败！原处理人无法移除！", HttpStatus.EXPECTATION_FAILED);
        }

        /*转派处理人*/
        if (workFlowService.addCandidateUsers(taskId,userId) != 0 )
        {
            throw new ServerSideBusinessException("订单任务更新失败！现处理人无法新增！", HttpStatus.EXPECTATION_FAILED);
        }


        /*更新订单指派关系*/
        orderDao.updateDistatcher(order.getId(), user.getId());

         /*订单指派，消息推送*/
        try
        {
            MsgPush msgPush = new MsgPush();
            msgPush.PushNotice(userId, ServerConstantValue.FORCE_ORDER_TITLE, order.getRecvAddr().getCity()
                    +order.getRecvAddr().getCounty()+order.getRecvAddr().getDetail());
        }
        catch (Exception e)
        {
            //消息推送失败
        }

    }


    @Override
    @OperationLog(desc = "订单任务更新信息")
    public void taskUpdate(String taskId,String orderSn,Integer newOrderStatus,String candiUser,Boolean forceDispatch)
    {
        /*检查订单是否存在，更新订单状态*/
        Order srcOrder =  orderDao.findBySn(orderSn);
        if ( srcOrder == null)
        {
            throw new ServerSideBusinessException("定单不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        Order newOrder = new Order();
        newOrder.setId(srcOrder.getId());
        newOrder.setOrderStatus(newOrderStatus);

        /*检查candiuser是否存在*/
        SysUser targetUser = sysUserDao.findBySysUserId(candiUser);

        //这个地方有问题，多门店情况要考虑
//        if (targetUser == null)
//        {
//            throw new ServerSideBusinessException("用户不存在！", HttpStatus.NOT_ACCEPTABLE);
//        }

        Map<String, Object> variables = new HashMap<String, Object>();//任务变量
        Group  group = groupDao.findByCode(ServerConstantValue.GP_CUSTOMER_SERVICE);//始终将客服加入组，客服始终能具有处理权限
        if(group == null)
        {
            throw new ServerSideBusinessException("用户组信息错误！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*抢单或指派订单*/
        if (newOrderStatus == OrderStatus.OSDispatching.getIndex())
        {
            variables.put(ServerConstantValue.ACT_FW_STG_2_CANDI_USERS,candiUser);
            variables.put(ServerConstantValue.ACT_FW_STG_2_CANDI_GROUPS,String.valueOf(group.getId()));

            if (!forceDispatch) //抢单
            {
                /*如果当前派送工状态已经被禁用，不允许抢单*/
                if (targetUser.getServiceStatus() == SysUserServiceStatus.SUSForbidden)
                {
                    throw new ServerSideBusinessException("抢单失败，您已被系统禁止订单！", HttpStatus.FORBIDDEN);
                }

                /*获取该派送工当前正在派送的订单*/
                Integer sysOverTime = systemParamDao.getOrderOverTime();

                Map params = new HashMap<String,String>();
                params.putAll(ImmutableMap.of("orderStatus", OrderStatus.OSDispatching.getIndex()));
                params.putAll(ImmutableMap.of("dispatcherId", candiUser));
                List<Order> orderList = orderDao.getList(params);
                for (Order dispatchingOrder : orderList)
                {
                    /*预约订单*/
                    if(dispatchingOrder.getReserveTime() != null)
                    {
                        if ( Clock.differMinute(dispatchingOrder.getReserveTime(),new Date()) >= 1)
                        {
                            throw new ServerSideBusinessException("抢单失败，请先完成当前已超时配送的订单！", HttpStatus.FORBIDDEN);
                        }
                    }
                    else//实时订单
                    {
                        Map paramsDispatch = new HashMap<String,String>();
                        paramsDispatch.putAll(ImmutableMap.of("userId", candiUser));
                        paramsDispatch.putAll(ImmutableMap.of("orderSn",dispatchingOrder.getOrderSn()));
                        paramsDispatch.putAll(ImmutableMap.of("orderStatus", OrderStatus.OSDispatching.getIndex()));
                        List<OrderOpHistory> orderOpHistories = orderOpHistoryDao.getList(paramsDispatch);
                        for (OrderOpHistory orderOpHistory :orderOpHistories)
                        {
                            if ( Clock.differMinute(orderOpHistory.getUpdateTime(),new Date()) >= sysOverTime)
                            {
                                throw new ServerSideBusinessException("抢单失败，请先完成当前已超时配送的订单！", HttpStatus.FORBIDDEN);
                            }
                        }
                    }
                }
            }
            else  /*订单指派，消息推送*/
            {
                try
                {
                    MsgPush msgPush = new MsgPush();
                    msgPush.PushNotice(candiUser, ServerConstantValue.FORCE_ORDER_TITLE, srcOrder.getRecvAddr().getCity()
                            +srcOrder.getRecvAddr().getCounty()+srcOrder.getRecvAddr().getDetail());
                }
                catch (Exception e)
                {
                    //消息推送失败
                }
            }

            orderDao.insertDistatcher(srcOrder.getId(), targetUser.getId());
        }
        else if (newOrderStatus == OrderStatus.OSSigned.getIndex())//客户签收
        {
            variables.put(ServerConstantValue.ACT_FW_STG_3_CANDI_USERS,candiUser);
            variables.put(ServerConstantValue.ACT_FW_STG_3_CANDI_GROUPS,String.valueOf(group.getId()));

            //发送短信告知用户服务成功
            try
            {
                smsService.sendDispatchOk(srcOrder.getCustomer().getPhone(), srcOrder.getCustomer().getName(),srcOrder.getOrderSn(),srcOrder.getOrderAmount() );
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }

        }
        else if (newOrderStatus == OrderStatus.OSCompleted.getIndex())//核单
        {
            newOrder.setCompleteTime(new Date());//记录核单时间
        }
        else if (newOrderStatus == OrderStatus.OSCanceled.getIndex())//订单作废
        {
            /*作废订单如果已经在派送，则将派送记录删除，并通知派送工该订单已作废*/
            if (srcOrder.getOrderStatus() == OrderStatus.OSDispatching.getIndex())
            {
                Map params = new HashMap<String,String>();
                params.putAll(ImmutableMap.of("userId", candiUser));
                params.putAll(ImmutableMap.of("orderSn",orderSn));
                params.putAll(ImmutableMap.of("orderStatus", OrderStatus.OSDispatching.getIndex()));
                List<OrderOpHistory> orderOpHistories = orderOpHistoryDao.getList(params);
                for (OrderOpHistory orderOpHistory :orderOpHistories)
                {
                    try
                    {
                        MsgPush msgPush = new MsgPush();
                        StringBuilder strBuilder = new StringBuilder();
                        strBuilder.append(ServerConstantValue.CANCEL_ORDER_TITLE);
                        strBuilder.append(",订单号：");
                        strBuilder.append(srcOrder.getOrderSn());
                        strBuilder.append(",");
                        strBuilder.append("地址：");
                        strBuilder.append(srcOrder.getRecvAddr().getCity());
                        strBuilder.append(srcOrder.getRecvAddr().getCounty());
                        strBuilder.append(srcOrder.getRecvAddr().getDetail());
                        msgPush.PushNotice(candiUser, ServerConstantValue.CANCEL_ORDER_TITLE,strBuilder.toString());
                    }
                    catch (Exception e)
                    {
                        //消息推送失败
                    }
                    orderOpHistoryDao.delete(orderOpHistory.getId());
                }
            }
        }
        else
        {
            throw new ServerSideBusinessException("查询参数错误，订单状态不正确！", HttpStatus.NOT_ACCEPTABLE);
        }

         /*更新数据*/
        orderDao.update(newOrder);

        /*检查任务是否存在，处理任务*/
        int retCode = workFlowService.completeTask(taskId,variables);
        if (retCode != 0 )
        {
            throw new ServerSideBusinessException("订单处理失败！", HttpStatus.EXPECTATION_FAILED);
        }

        /*订单变更历史记录*/
        OrderOperHistory(newOrder,newOrder.getOrderStatus());


    }


//    @Override
//    @OperationLog(desc = "订单任务更新信息")
//    public void update(String taskId,Map<String, Object> variables,Integer id, Order newOrder,Boolean forceDispatch)
//    {
//        newOrder.setId(id);
//
//        /*抢单或指派订单*/
//        if (newOrder.getOrderStatus() == OrderStatus.OSDispatching.getIndex())
//        {
//            String strCandiUser = (String) variables.get(ServerConstantValue.ACT_FW_STG_2_CANDI_USERS);
//
//            SysUser candiUser = sysUserDao.findBySysUserId(strCandiUser);
//            if (!forceDispatch) //抢单
//            {
//                /*获取该派送工当前正在派送的订单*/
//                Integer sysOverTime = systemParamDao.getOrderOverTime();
//                Map params = new HashMap<String,String>();
//                params.putAll(ImmutableMap.of("userId", candiUser.getId()));
//                params.putAll(ImmutableMap.of("orderSn",newOrder.getOrderSn()));
//                params.putAll(ImmutableMap.of("orderStatus", OrderStatus.OSDispatching.getIndex()));
//                List<OrderOpHistory> orderOpHistories = orderOpHistoryDao.getList(params);
//                for (OrderOpHistory orderOpHistory :orderOpHistories)
//                {
//                    if ( Clock.differMinute(orderOpHistory.getUpdateTime(),new Date()) >= sysOverTime)
//                    {
//                        throw new ServerSideBusinessException("抢单失败，请先完成当前已超时配送的订单！", HttpStatus.FORBIDDEN);
//                    }
//                }
//            }
//            else  /*订单指派，消息推送*/
//            {
//                try
//                {
//                    MsgPush msgPush = new MsgPush();
//                    Order targetOrder = orderDao.findById(id);
//                    msgPush.PushNotice(strCandiUser, ServerConstantValue.FORCE_ORDER_TITLE, targetOrder.getRecvAddr().getCity()
//                            +targetOrder.getRecvAddr().getCounty()+targetOrder.getRecvAddr().getDetail());
//                }
//                catch (Exception e)
//                {
//                    //消息推送失败
//                }
//            }
//
//            orderDao.insertDistatcher(newOrder.getId(), candiUser.getId());
//        }
//        else if (newOrder.getOrderStatus() == OrderStatus.OSSigned.getIndex())//客户签收
//        {
//            //发送短信告知用户服务成功
//        }
//        else if (newOrder.getOrderStatus() == OrderStatus.OSCanceled.getIndex())//订单作废
//        {
//            /*作废订单如果已经在派送，则将派送记录删除，并通知派送工该订单已作废*/
//
//        }
//        else if (newOrder.getOrderStatus() == OrderStatus.OSCompleted.getIndex())//核单
//        {
//            newOrder.setCompleteTime(new Date());//记录核单时间
//        }
//        else
//        {
//            // to do nothing
//        }
//
//         /*更新数据*/
//        orderDao.update(newOrder);
//
//        /*检查任务是否存在，处理任务*/
//        int retCode = workFlowService.completeTask(taskId,variables);
//        if (retCode != 0 )
//        {
//            throw new ServerSideBusinessException("订单处理失败！", HttpStatus.EXPECTATION_FAILED);
//        }
//
//        /*订单变更历史记录*/
//        OrderOperHistory(newOrder,newOrder.getOrderStatus());
//    }


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

        /*订单已经结束，不能作废*/
        if (order.getOrderStatus() == OrderStatus.OSSigned.getIndex())
        {
            throw new ServerSideBusinessException("订单已签收，不能取消！", HttpStatus.NOT_FOUND);
        }

        order.setOrderStatus( OrderStatus.OSCanceled.getIndex());

        /*更新订单状态为作废*/
        orderDao.update(order);

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
            orderOpHistory.setOrderStatus(OrderStatus.values()[orderStatus]);
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


    public void orderPay(Order order, PayType payType)
    {
        checkPayType(payType,order.getCustomer());
        updatePayStatus(PayStatus.PSPaied,payType,order.getCustomer(),order);

        order.setId(order.getId());
        order.setPayType(payType);
        order.setPayStatus(PayStatus.PSPaied);
        orderDao.update(order);
    }

    public void ticketPay(Order order,String coupuns,String tickets)
    {
        /*校验客户是否气票用户*/
        if (!order.getCustomer().getSettlementType().getCode().equals(ServerConstantValue.SETTLEMENT_TYPE_TICKET) )
        {
            throw new ServerSideBusinessException("客户不是气票用户！", HttpStatus.NOT_ACCEPTABLE);
        }

        String message = "";
        /*优惠券*/
        String[] couponList = coupuns.split(",");
        Float couponSum = 0f;
        for (String couponId :couponList)
        {
            if (couponId.trim().length() > 0)
            {
                Coupon target = couponDao.findById( Integer.valueOf(couponId).intValue());
                if (target == null)
                {
                    throw new ServerSideBusinessException("系统中没有该优惠券", HttpStatus.NOT_ACCEPTABLE);
                }

                checkCoupun(order,target);//检查优惠券是否满足使用条件

                /*将优惠券状态设置为已使用*/
                target.setCouponStatus(TicketStatus.TSUsed);
                target.setUseTime(new Date());
                couponDao.update(target);

                couponSum = couponSum + target.getPrice();

                /*增加优惠券订单关联消费记录*/
                CouponOrder couponOrder = new CouponOrder();
                couponOrder.setCouponIdx(target.getId());
                couponOrder.setOrderSn(order.getOrderSn());
                couponOrder.setNote("已支付");
                couponOrderDao.insert(couponOrder);

                String couponInfo = String.format("规格：%s",target.getSpecName());
                message = String.format("%s;",couponInfo);
            }
        }


        /*气票*/
        String[] ticketList = tickets.split(",");
        for (String ticketSn :ticketList)
        {
            if (ticketSn.trim().length() > 0)
            {
                Ticket target = ticketDao.findBySn(ticketSn);
                if (target == null)
                {
                    String ticketInfo = String.format("系统中没有%s该气票",ticketSn);
                    throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
                }

                checkTicket(order,target);
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
        }

        /*更改订单支付状态为已支付*/
        order.setPayStatus(PayStatus.PSPaied);
        order.setPayType(PayType.PTTicket);
        order.setOrderAmount(order.getOrderAmount() - couponSum);//交掉优惠卷抵扣金额

        order.setPayTime(new Date());
        if (message.trim().length() >0 )
        {
            message = String.format("使用优惠券：%s",message);
            order.setNote(message);
        }

        orderDao.update(order);
    }

    @Override
    @OperationLog(desc = "订单超时检查")
    public void checkOverTime()
    {
        /*查找处于派送状态，超时的订单关联的派送工*/
        Map params = new HashMap<String,String>();
        params.putAll(ImmutableMap.of("orderStatus", OrderStatus.OSDispatching.getIndex()));
        List<Order> orders =  orderDao.getList(params);

        Integer overTime = systemParamDao.getOrderOverTime();

        for (Order order:orders)
        {
            if ( (order.getDispatcher() != null) &&  (order.getDispatcherStartTime() != null))
            {
                if (Clock.differMinute(order.getDispatcherStartTime(),(new Date())) >= overTime)
                {
                    try
                    {
                        MsgPush msgPush = new MsgPush();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(ServerConstantValue.OVERTIME_ORDER_TITLE);
                        stringBuilder.append(",订单号：");
                        stringBuilder.append(order.getOrderSn());
                        stringBuilder.append(",");
                        stringBuilder.append("地址：");
                        stringBuilder.append(order.getRecvAddr().getCity());
                        stringBuilder.append(order.getRecvAddr().getCounty());
                        stringBuilder.append(order.getRecvAddr().getDetail());
                        msgPush.PushNotice(order.getDispatcher().getUserId(), ServerConstantValue.OVERTIME_ORDER_TITLE,stringBuilder.toString());
                    }
                    catch (Exception e)
                    {
                        //消息推送失败
                    }
                }
            }

        }
    }
}
