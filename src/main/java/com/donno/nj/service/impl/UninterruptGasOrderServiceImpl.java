package com.donno.nj.service.impl;

import com.donno.nj.activiti.WorkFlowTypes;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.OrderService;
import com.donno.nj.service.UninterruptGasOrderService;
import com.donno.nj.service.WorkFlowService;
import com.donno.nj.util.AppUtil;
import com.donno.nj.util.DistanceHelper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UninterruptGasOrderServiceImpl implements UninterruptGasOrderService
{

    @Autowired
    private OrderDao dispatchOrderDao;

    @Autowired
    private  UninterruptGasOrderDao uninterruptGasOrderDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private GasCylinderDao gasCylinderDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private CustomerCreditDao customerCreditDao;

    @Autowired
    private CustomerCreditDetailDao customerCreditDetailDao;


    @Override
    public Optional<UninterruptedGasOrder> findBySn(String sn) {
        return Optional.fromNullable(uninterruptGasOrderDao.findBySn(sn));
    }

    @Override
    @OperationLog(desc ="查询不间断供气订单信息")
    public List<UninterruptedGasOrder> retrieve(Map params)
    {
        List<UninterruptedGasOrder> orders = uninterruptGasOrderDao.getList(params);
        return orders;
    }

    @Override
    @OperationLog(desc = "查询不间断供气订单数量")
    public Integer count(Map params) {
        return uninterruptGasOrderDao.count(params);
    }

    @Override
    @OperationLog(desc = "创建不间断供气订单信息")
    public void create(UninterruptedGasOrder uninterruptedGasOrder)
    {
         /*参数校验*/
        if (uninterruptedGasOrder == null )
        {
            throw new ServerSideBusinessException("订单信息不全，请补充订单信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*派送员工*/
        if(uninterruptedGasOrder.getDispatcher() == null || uninterruptedGasOrder.getDispatcher().getUserId() == null)
        {
            throw new ServerSideBusinessException("请补充派送人员信息！", HttpStatus.NOT_ACCEPTABLE);
        }
        SysUser dispatcher = sysUserDao.findByUId(uninterruptedGasOrder.getDispatcher().getUserId());
        if (dispatcher == null)
        {
            throw new ServerSideBusinessException("不存在派送人员信息！", HttpStatus.NOT_ACCEPTABLE);
        }
        uninterruptedGasOrder.setDispatcher(dispatcher);

        /*客户*/
        if(uninterruptedGasOrder.getCustomer() == null || uninterruptedGasOrder.getCustomer().getUserId() == null)
        {
            throw new ServerSideBusinessException("请补充客户信息！", HttpStatus.NOT_ACCEPTABLE);
        }
        Customer customer = customerDao.findByUserId(uninterruptedGasOrder.getCustomer().getUserId());
        if (customer == null)
        {
            throw new ServerSideBusinessException("不存在客户信息！", HttpStatus.NOT_ACCEPTABLE);
        }
        uninterruptedGasOrder.setCustomer(customer);

        /*钢瓶*/
        if(uninterruptedGasOrder.getGasCylinder() == null || uninterruptedGasOrder.getGasCylinder().getNumber() == null)
        {
            throw new ServerSideBusinessException("请补充钢瓶信息！", HttpStatus.NOT_ACCEPTABLE);
        }
        GasCylinder gasCylinder = gasCylinderDao.findByNumber(uninterruptedGasOrder.getGasCylinder().getNumber());
        if (gasCylinder == null)
        {
            throw new ServerSideBusinessException("不存在钢瓶信息！", HttpStatus.NOT_ACCEPTABLE);
        }
        uninterruptedGasOrder.setGasCylinder(gasCylinder);

        /*派送订单*/
        if(uninterruptedGasOrder.getDispatchOrder() == null || uninterruptedGasOrder.getDispatchOrder().getOrderSn() == null)
        {
            throw new ServerSideBusinessException("请补充派送订单信息！", HttpStatus.NOT_ACCEPTABLE);
        }
        Order order = dispatchOrderDao.findBySn(uninterruptedGasOrder.getDispatchOrder().getOrderSn());
        if (order == null)
        {
            throw new ServerSideBusinessException("不存在派送订单信息！", HttpStatus.NOT_ACCEPTABLE);
        }
        uninterruptedGasOrder.setDispatchOrder(order);

        /*满瓶重量*/
        if ( uninterruptedGasOrder.getFullWeight() == null)
        {
            throw new ServerSideBusinessException("缺少重瓶液化气重量数据！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*商品*/
        if (uninterruptedGasOrder.getGoods() == null || uninterruptedGasOrder.getGoods().getCode() == null || uninterruptedGasOrder.getGoods().getCode().trim().length() == 0)
        {
            throw new ServerSideBusinessException("商品信息不正确！", HttpStatus.NOT_ACCEPTABLE);
        }
        Goods goods = goodsDao.findByCode(uninterruptedGasOrder.getGoods().getCode());
        if (goods == null)
        {
            throw new ServerSideBusinessException("商品信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        uninterruptedGasOrder.setGoods(goods);

        /*检查商品是否停止销售或者下架*/
        if (uninterruptedGasOrder.getGoods().getStatus() != 0)
        {
            String message = String.format("商品%s已经暂停销售!",uninterruptedGasOrder.getGoods().getName());
            throw new ServerSideBusinessException(message, HttpStatus.NOT_ACCEPTABLE);
        }

        /*根据钢瓶号和订单号，判断是否已经存在该订单*/
        Map params = new HashMap<String,String>();
        params.putAll(ImmutableMap.of("payStatus", PayStatus.PSUnpaid.getIndex()));
        params.putAll(ImmutableMap.of("gasCynNumber", uninterruptedGasOrder.getGasCylinder().getNumber()));
        params.putAll(ImmutableMap.of("dispatchOrderSn", uninterruptedGasOrder.getDispatchOrder().getOrderSn()));
        List<UninterruptedGasOrder> uninterruptedGasOrderList = uninterruptGasOrderDao.getList(params);
        if (uninterruptedGasOrderList.size() >0 )
        {
            throw new ServerSideBusinessException("该钢瓶已经创建计费订单！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*默认为待支付*/
        uninterruptedGasOrder.setPayStatus(PayStatus.PSUnpaid);

        /*根据结算类型自动设置支付方式*/
        setPayType(uninterruptedGasOrder.getCustomer().getSettlementType(),uninterruptedGasOrder);

        /*生成定单编号*/
        Date curDate = new Date();
        String dateFmt =  new SimpleDateFormat("yyyyMMddHHmmssSSS").format(curDate);
        uninterruptedGasOrder.setOrderSn(dateFmt);

        uninterruptGasOrderDao.insert(uninterruptedGasOrder);
    }

    public void setPayType(SettlementType settlementType,UninterruptedGasOrder uninterruptedGasOrder)
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

        if (code.equals(ServerConstantValue.SETTLEMENT_TYPE_MONTHLY_CREDIT))//月结用户
        {
            uninterruptedGasOrder.setPayType(PayType.PTMonthlyCredit);
        }
        else  if (code.equals(ServerConstantValue.SETTLEMENT_TYPE_COMMON_USER) )//普通用户
        {
            //不做处理
        }
        else
        {
            throw new ServerSideBusinessException("客户结算类型信息错误！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    @OperationLog(desc = "支付金额计算")
    public UninterruptedGasOrder GasPayCaculate(String orderSn, Float emptyWeight)
    {
        UninterruptedGasOrder gasOrder = uninterruptGasOrderDao.findBySn(orderSn);
        if (gasOrder == null)
        {
            throw new ServerSideBusinessException("订单信息错误！", HttpStatus.NOT_ACCEPTABLE);
        }

        gasOrder.setEmptyWeight(emptyWeight);

        Float diffGas = gasOrder.getFullWeight() - gasOrder.getEmptyWeight();
        if (diffGas < 0)
        {
            throw new ServerSideBusinessException("气量使用数据异常！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (gasOrder.getGoods() == null)
        {
            throw new ServerSideBusinessException("数据异常,缺少商品信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*计算金额*/
        gasOrder.setOriginalPrice(gasOrder.getGoods().getPrice());
        gasOrder.setOriginalAmount(gasOrder.getGoods().getPrice() * diffGas);

        gasOrder.setDealPrice(gasOrder.getGoods().getPrice());
        gasOrder.setDealAmount(gasOrder.getGoods().getPrice() * diffGas);

        return gasOrder;
    }


    @Override
    @OperationLog(desc = "订单支付")
    public void GasPay(String orderSn, PayType payType,Float emptyWeight)
    {
        UninterruptedGasOrder gasOrder = uninterruptGasOrderDao.findBySn(orderSn);
        if (gasOrder == null)
        {
            throw new ServerSideBusinessException("订单信息错误！", HttpStatus.NOT_ACCEPTABLE);
        }

        gasOrder.setEmptyWeight(emptyWeight);

        Float diffGas = gasOrder.getFullWeight() - gasOrder.getEmptyWeight();
        if (diffGas < 0)
        {
            throw new ServerSideBusinessException("气量使用数据异常！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*计算金额*/
        gasOrder.setOriginalPrice(gasOrder.getGoods().getPrice());
        gasOrder.setOriginalAmount(gasOrder.getGoods().getPrice() * diffGas);

        gasOrder.setDealPrice(gasOrder.getGoods().getPrice());
        gasOrder.setDealAmount(gasOrder.getGoods().getPrice() * diffGas);

        checkPayType(payType,gasOrder);

        gasOrder.setPayStatus(PayStatus.PSPaied);

        uninterruptGasOrderDao.update(gasOrder);
    }


    public void addCredit(CreditType creditType,UninterruptedGasOrder order)
    {
        CustomerCredit customerCredit = customerCreditDao.findByUserIdCreditType(order.getCustomer().getUserId(),creditType);
        if (customerCredit == null)
        {
            /*记录当前欠款额*/
            customerCredit = new CustomerCredit();
            customerCredit.setAmount( order.getDealAmount());
            customerCredit.setUserId(order.getCustomer().getUserId());
            customerCredit.setCreditType(creditType);
            customerCredit.setNote("");
            customerCreditDao.insert(customerCredit);
        }
        else
        {
            /*更新当前欠款额*/
            customerCredit.setAmount(customerCredit.getAmount() + order.getDealAmount());
            customerCreditDao.update(customerCredit);
        }

        /*记录交易明细*/
        CustomerCreditDetail customerCreditDetail = new CustomerCreditDetail();
        customerCreditDetail.setCreditType(creditType);
        customerCreditDetail.setUserId(order.getCustomer().getUserId());
        customerCreditDetail.setOrderSn(order.getOrderSn());
        customerCreditDetail.setAmount( order.getDealAmount());
        customerCreditDetailDao.insert(customerCreditDetail);
    }


    public void checkPayType(PayType payType,UninterruptedGasOrder uninterruptedGasOrder)
    {
        if(uninterruptedGasOrder == null)
        {
            throw new ServerSideBusinessException("订单数据错误，缺少客户信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        SettlementType settlementType = uninterruptedGasOrder.getCustomer().getSettlementType();
        String settlementCode =  settlementType.getCode();
        if (settlementType == null || settlementCode == null || settlementCode.trim().length() == 0)
        {
            throw new ServerSideBusinessException("订单数据错误，缺少客户结算类型信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (settlementCode.equals(ServerConstantValue.SETTLEMENT_TYPE_COMMON_USER))//普通用户
        {
            if (payType == PayType.PTOnLine || payType == PayType.PTCash ||payType == PayType.PTDebtCredit )
            {
                uninterruptedGasOrder.setPayType(payType);
                uninterruptedGasOrder.setPayTime(new Date());

                if (payType == PayType.PTDebtCredit)
                {
                    addCredit(CreditType.CTCommonCredit,uninterruptedGasOrder);//赊销记录
                }
            }
            else
            {
                throw new ServerSideBusinessException("客户支付方式不允许修改！", HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else  if (settlementCode.equals(ServerConstantValue.SETTLEMENT_TYPE_MONTHLY_CREDIT))
        {
            addCredit(CreditType.CTMonthlyCredit,uninterruptedGasOrder);//赊销记录
        }
        else
        {
            throw new ServerSideBusinessException("此操作不支持该客户！", HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @Override
    @OperationLog(desc = "删除订单信息")
    public void deleteById(Integer id)
    {
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
        UninterruptedGasOrder order = uninterruptGasOrderDao.findBySn(orderSn);
        if (order == null)
        {
            throw new ServerSideBusinessException("微信订单号不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*订单状态更改为已支付*/
        order.setPayStatus(PayStatus.PSPaied);
        order.setPayTime(new Date());
        uninterruptGasOrderDao.update(order);

        /*订单号与微信订单号关联*/
        uninterruptGasOrderDao.bindWeixinOrderSn(order.getId(),weixinOrderSn,amount);
    }

}
