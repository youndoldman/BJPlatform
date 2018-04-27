package com.donno.nj.service.impl;

import com.donno.nj.activiti.WorkFlowTypes;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.AdjustPriceScheduleService;
import com.donno.nj.service.OrderService;
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
public class AdjustPriceScheduleServiceImpl implements AdjustPriceScheduleService
{

    @Autowired
    private AdjustPriceScheduleDao adjustPriceScheduleDao;

    @Autowired
    private  AdjustPriceScheduleDetailDao adjustPriceScheduleDetailDao;

    @Autowired
    private  GoodsDao goodsDao;

    @Override
    public Optional<AdjustPriceSchedule> findByName(String name) {
        return Optional.fromNullable(adjustPriceScheduleDao.findByName(name));
    }

    @Override
    public Optional<AdjustPriceSchedule> findById(Integer id) {
        return Optional.fromNullable(adjustPriceScheduleDao.findById(id));
    }


    @Override
    @OperationLog(desc = "查询调价计划信息")
    public List<AdjustPriceSchedule> retrieve(Map params)
    {
        List<AdjustPriceSchedule> schedules = adjustPriceScheduleDao.getList(params);
        return schedules;
    }

    @Override
    @OperationLog(desc = "查询调价计划数量")
    public Integer count(Map params) {
        return adjustPriceScheduleDao.count(params);
    }

    @Override
    @OperationLog(desc = "创建调价计划信息")
    public void create(AdjustPriceSchedule adjustPriceSchedule)
    {
         /*参数校验*/
        if (adjustPriceSchedule == null || adjustPriceSchedule.getAdjustPriceDetailList() == null || adjustPriceSchedule.getAdjustPriceDetailList().size() == 0
                )
        {
            throw new ServerSideBusinessException("缺少调价计划明细信息，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*默认为0*/
        if (adjustPriceSchedule.getStatus() == null)
        {
            adjustPriceSchedule.setStatus(AdjustPriceScheduleStatus.APSWaitForForce);
        }

        /*生效时间检查*/
        if ( adjustPriceSchedule.getEffectTime() == null )
        {
            throw new ServerSideBusinessException("缺少调价计划执行时间信息，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        if(adjustPriceSchedule.getNote() == null)
        {
            adjustPriceSchedule.setNote("");
        }


        /*插入调价计划总表*/
        adjustPriceScheduleDao.insert(adjustPriceSchedule);

        /*插入明细表*/
        for(AdjustPriceDetail adjustPriceDetail : adjustPriceSchedule.getAdjustPriceDetailList())
        {
            /*校验商品信息是否存在*/
            if (adjustPriceDetail.getGoods() == null || adjustPriceDetail.getGoods().getCode() == null || adjustPriceDetail.getGoods().getCode().trim().length() == 0)
            {
                throw new ServerSideBusinessException("商品信息不正确！", HttpStatus.NOT_ACCEPTABLE);
            }
            Goods good = goodsDao.findByCode(adjustPriceDetail.getGoods().getCode());
            if (good == null)
            {
                throw new ServerSideBusinessException("商品信息不存在！", HttpStatus.NOT_ACCEPTABLE);
            }

            adjustPriceDetail.setAdjPriceScheduleIdx(adjustPriceSchedule.getId());
            adjustPriceDetail.setGoods(good);

            adjustPriceScheduleDetailDao.insert(adjustPriceDetail);//插入调价计划明细表
        }

    }

    @Override
    @OperationLog(desc = "修改调价计划信息")
    public void update(Integer id, AdjustPriceSchedule newAdjustPriceSchedule)
    {
        /*查找调价计划是否存在*/
        AdjustPriceSchedule schedule = adjustPriceScheduleDao.findById(id);
        if (schedule == null)
        {
            throw new ServerSideBusinessException("调价计划不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*只有未执行的计划可以修改*/
        if (schedule.getStatus() != AdjustPriceScheduleStatus.APSWaitForForce)
        {
            throw new ServerSideBusinessException("该调价计划不允许修改！", HttpStatus.NOT_ACCEPTABLE);
        }

        newAdjustPriceSchedule.setId(id);

        /*更新主表数据*/
        adjustPriceScheduleDao.update(newAdjustPriceSchedule);

        /*更新从表数据*/
        if (newAdjustPriceSchedule.getAdjustPriceDetailList() != null && newAdjustPriceSchedule.getAdjustPriceDetailList().size() > 0)
        {
            adjustPriceScheduleDetailDao.deleteByScheduleIdx(id);

            for(AdjustPriceDetail adjustPriceDetail: newAdjustPriceSchedule.getAdjustPriceDetailList())
            {
                 /*校验商品信息是否存在*/
                if (adjustPriceDetail.getGoods() == null || adjustPriceDetail.getGoods().getCode() == null || adjustPriceDetail.getGoods().getCode().trim().length() == 0)
                {
                    throw new ServerSideBusinessException("商品信息不正确！", HttpStatus.NOT_ACCEPTABLE);
                }
                Goods good = goodsDao.findByCode(adjustPriceDetail.getGoods().getCode());
                if (good == null)
                {
                    throw new ServerSideBusinessException("商品信息不存在！", HttpStatus.NOT_ACCEPTABLE);
                }

                adjustPriceDetail.setAdjPriceScheduleIdx(newAdjustPriceSchedule.getId());
                adjustPriceDetail.setGoods(good);

                adjustPriceScheduleDetailDao.insert(adjustPriceDetail);
            }
        }

    }


    @Override
    @OperationLog(desc = "删除调价计划信息")
    public void deleteById(Integer id)
    {
        /*删除计划总表*/
        adjustPriceScheduleDao.delete(id);

        /*删除详表*/
        adjustPriceScheduleDetailDao.deleteByScheduleIdx(id);
    }

    @Override
    @OperationLog(desc = "执行调价任务")
    public void adjustPrice()
    {
        /*查询待调价计划*/
        Map params = new HashMap<String,String>();
        params.putAll(ImmutableMap.of("status", AdjustPriceScheduleStatus.APSWaitForForce.getIndex()));
        params.putAll(ImmutableMap.of("endTime", new Date()));
        List<AdjustPriceSchedule> schedules = retrieve(params);

        for (AdjustPriceSchedule schedule:schedules)
        {
            for (AdjustPriceDetail adjustPriceDetail:schedule.getAdjustPriceDetailList())
            {
                /*更改商品价格*/
                Goods goods = adjustPriceDetail.getGoods();
                if(goods!= null)
                {
                    goods.setPrice(adjustPriceDetail.getPrice());
                    goodsDao.update(goods);

                    /*操作日志记录*/
                }

            }

            /*调价计划表修改为已调价*/
            schedule.setStatus(AdjustPriceScheduleStatus.APSEffecitve);
            update(schedule.getId(),schedule);
        }
    }

}
