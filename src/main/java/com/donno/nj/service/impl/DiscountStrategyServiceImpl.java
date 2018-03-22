package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.AdjustPriceScheduleService;
import com.donno.nj.service.DiscountStrategyService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiscountStrategyServiceImpl implements DiscountStrategyService
{

    @Autowired
    private DiscountStrategyDao discountStrategyDao;

    @Autowired
    private DiscountDetailDao discountDetailDao;

    @Autowired
    private  GoodsDao goodsDao;

    @Autowired
    private  CustomerLevelDao customerLevelDao;

    @Autowired
    private  CustomerTypeDao customerTypeDao;

    @Autowired
    private  DiscountConditionTypeDao discountConditionTypeDao;

    @Override
    public Optional<DiscountStrategy> findByName(String name) {
        return Optional.fromNullable(discountStrategyDao.findByName(name));
    }

    @Override
    public Optional<DiscountStrategy> findById(Integer id) {
        return Optional.fromNullable(discountStrategyDao.findById(id));
    }


    @Override
    @OperationLog(desc = "查询优惠策略信息")
    public List<DiscountStrategy> retrieve(Map params)
    {
        List<DiscountStrategy> discountStrategies = discountStrategyDao.getList(params);
        return discountStrategies;
    }

    @Override
    @OperationLog(desc = "查询优惠策略数量")
    public Integer count(Map params) {
        return discountStrategyDao.count(params);
    }

    @Override
    @OperationLog(desc = "创建优惠策略信息")
    public void create(DiscountStrategy discountStrategy)
    {
         /*参数校验*/
        if ( discountStrategy == null  )
        {
            throw new ServerSideBusinessException("优惠策略信息为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (discountStrategy.getDiscountType() == null)
        {
            throw new ServerSideBusinessException("优惠类型信息为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (discountStrategy.getDiscountConditionType() == null)
        {
            throw new ServerSideBusinessException("优惠策略类型信息为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (discountStrategy.getStartTime() == null)
        {
            throw new ServerSideBusinessException("开始时间为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (discountStrategy.getEndTime() == null)
        {
            throw new ServerSideBusinessException("结束时间为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (discountStrategy.getUseType() == null)
        {
            throw new ServerSideBusinessException("优惠策略使用方式为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*策略类型检查*/
        checkConditionType(discountStrategy);

         /*插入优惠策略总表*/
        discountStrategyDao.insert(discountStrategy);

        /*插入明细表*/
        insertDetail(discountStrategy);
    }

    @Override
    @OperationLog(desc = "修改优惠策略信息")
    public void update(Integer id, DiscountStrategy newDiscountStrategy)
    {
        /*查找优惠策略是否存在*/
        DiscountStrategy discountStrategy = discountStrategyDao.findById(id);
        if (discountStrategy == null)
        {
            throw new ServerSideBusinessException("优惠策略信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*状态为待执行才允许修改*/
        if (discountStrategy.getDiscountStrategyStatus() != DiscountStrategyStatus.DSSWaitForForce)
        {
            throw new ServerSideBusinessException("该优惠策略已经执行，不允许修改！", HttpStatus.NOT_ACCEPTABLE);
        }

        newDiscountStrategy.setId(id);

        /*策略类型检查*/
        if (discountStrategy.getDiscountConditionType() != null)
        {
            checkConditionType(discountStrategy);
        }

       /*更新主表数据*/
        discountStrategyDao.update(newDiscountStrategy);

        /*更新从表数据*/
        if (newDiscountStrategy.getDiscountDetails() != null && newDiscountStrategy.getDiscountDetails().size() > 0)
        {
            discountDetailDao.deleteByDiscountStrategyIdx(id);
            insertDetail(newDiscountStrategy);
        }
    }


    @Override
    @OperationLog(desc = "删除优惠策略信息")
    public void deleteById(Integer id)
    {
        /*删除优惠策略总表*/
        discountStrategyDao.delete(id);

        /*删除订单详细表*/
        discountDetailDao.deleteByDiscountStrategyIdx(id);
    }



    @OperationLog(desc = "检查优惠方案，未生效方案触发")
    public void DiscountTrigger()
    {
        /*查询待生效优惠方案*/
        Map params = new HashMap<String,String>();
        params.putAll(ImmutableMap.of("status", DiscountStrategyStatus.DSSWaitForForce.getIndex()));
        params.putAll(ImmutableMap.of("startTime", new Date()));
        List<DiscountStrategy> strategies = retrieve(params);

        for (DiscountStrategy strategy:strategies)
        {
            /*优惠方案状态修改为已生效，生效后不允许修改*/
            strategy.setDiscountStrategyStatus(DiscountStrategyStatus.DSSEffecitve);
            update(strategy.getId(),strategy);
        }
    }

    public void checkConditionType(DiscountStrategy discountStrategy)
    {
        DiscountConditionType discountConditionType = discountStrategy.getDiscountConditionType();
        if (discountConditionType.getCode() == null || discountConditionType.getCode().trim().length() == 0)
        {
            throw new ServerSideBusinessException("优惠策略类型信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        if (discountConditionTypeDao.findByCode(discountConditionType.getCode()) == null)
        {
            throw new ServerSideBusinessException("优惠策略类型信息错误！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*用户级别策略检查*/
        if (discountConditionType.getCode().compareTo(ServerConstantValue.DISCOUNT_CONDITION_TYPE_CUSTOMER_LEVEL) == 0)
        {
            checkCustomerLevel(discountStrategy.getDiscountConditionValue());
        }
        else if (discountConditionType.getCode().compareTo(ServerConstantValue.DISCOUNT_CONDITION_TYPE_CUSTOMER_TYPE) == 0)/*用户类型策略检查*/
        {
            checkCustomerType(discountStrategy.getDiscountConditionValue());
        }
    }

    public void checkCustomerLevel(String customerLevelCode)
    {
        if ( customerLevelCode == null || customerLevelCode.trim().length() == 0)
        {
            throw new ServerSideBusinessException("优惠策略信息缺少用户级别信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        CustomerLevel customerLevel = customerLevelDao.findByCode(customerLevelCode);
        if (customerLevel == null)
        {
            throw new ServerSideBusinessException("用户级别信息错误！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public void checkCustomerType(String customerTypeCode)
    {
        if ( customerTypeCode == null || customerTypeCode.trim().length() == 0)
        {
            throw new ServerSideBusinessException("优惠策略信息缺少用户类型信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        CustomerType customerType = customerTypeDao.findByCode(customerTypeCode);
        if (customerType == null)
        {
            throw new ServerSideBusinessException("用户类型信息错误！", HttpStatus.NOT_ACCEPTABLE);
        }
    }


    public void checkGoods(Goods goods)
    {
         /*校验商品信息是否存在*/
        if (goods == null || goods.getCode() == null || goods.getCode().trim().length() == 0)
        {
            throw new ServerSideBusinessException("商品信息不正确！", HttpStatus.NOT_ACCEPTABLE);
        }

        Goods good = goodsDao.findByCode(goods.getCode());
        if (good == null)
        {
            throw new ServerSideBusinessException("商品信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public void insertDetail(DiscountStrategy discountStrategy)
    {
        if (discountStrategy.getDiscountDetails() != null)
        {
            for(DiscountDetail discountDetail: discountStrategy.getDiscountDetails())
            {
            /*校验商品信息是否存在*/
                checkGoods(discountDetail.getGoods());
                discountDetail.setDiscountStrategyIdx(discountStrategy.getId());
                discountDetail.setGoods(discountDetail.getGoods());

                discountDetailDao.insert(discountDetail);
            }
        }
    }
}
