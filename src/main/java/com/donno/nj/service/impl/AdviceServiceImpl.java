package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.AdviceService;
import com.donno.nj.service.TicketService;
import com.donno.nj.util.Clock;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdviceServiceImpl implements AdviceService
{
    @Autowired
    private AdviceDao adviceDao;

    @Autowired
    private UserDao userDao;

    @Override
    @OperationLog(desc = "查询客户建议信息")
    public List<Advice> retrieve(Map params)
    {
        return adviceDao.getList(params);
    }

    @Override
    @OperationLog(desc = "查询客户建议数量")
    public Integer count(Map params)
    {
        return  adviceDao.count(params);
    }

    @Override
    @OperationLog(desc = "添加客户建议信息")
    public void create(Advice advice)
    {
        /*参数校验*/
        if (advice == null)
        {
            throw new ServerSideBusinessException("缺少客户建议信息，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (advice.getAdvice() == null || advice.getAdvice().trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少客户建议信息，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户信息校验*/
        User user=  userDao.findByUserId(advice.getUserId());
        if (user == null)
        {
            throw new ServerSideBusinessException("客户不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        adviceDao.insert(advice);
    }

    @Override
    @OperationLog(desc = "修改客户建议信息")
    public void update(Integer id, Advice advice)
    {
        /*参数校验*/
        if (advice == null)
        {
            throw new ServerSideBusinessException("缺少客户建议信息，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (advice.getAdvice() == null || advice.getAdvice().trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少客户建议信息，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        Advice oriAdvice = adviceDao.findById(id);
        if (oriAdvice == null)
        {
            throw new ServerSideBusinessException("客户建议信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*更新数据*/
        advice.setId(id);
        adviceDao.update(advice);
    }

    @Override
    @OperationLog(desc = "删除客户建议信息")
    public void deleteById(Integer id)
    {
        Advice advice = adviceDao.findById(id);
        if (advice == null)
        {
            throw new ServerSideBusinessException("客户建议信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        adviceDao.delete(id);
    }

}
