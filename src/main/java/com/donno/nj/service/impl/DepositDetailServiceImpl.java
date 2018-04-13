package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.DepositDetailService;
import com.donno.nj.service.WriteOffDetailService;
import com.donno.nj.util.AppUtil;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DepositDetailServiceImpl implements DepositDetailService
{
    @Autowired
    private DepositDetailDao depositDetailDao;

    @Autowired
    private UserDao userDao;

    @Override
    @OperationLog(desc = "查询存入银行记录信息")
    public List<DepositDetail> retrieve(Map params)
    {
        List<DepositDetail> depositDetails = depositDetailDao.getList(params);
        return depositDetails;
    }

    @Override
    @OperationLog(desc = "查询存入银行记录信息条数")
    public Integer count(Map params) {
        return depositDetailDao.count(params);
    }


    @Override
    @OperationLog(desc = "增加存入银行记录")
    public void create(DepositDetail depositDetail)
    {
        /*参数校验*/
        if (depositDetail == null)
        {
            throw new ServerSideBusinessException("入银行款信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (depositDetail.getOperTime() == null)
        {
            depositDetail.setOperTime(new Date());
        }

        /*操作员信息*/
        Optional<User> userOptional =  AppUtil.getCurrentLoginUser();
        if (!userOptional.isPresent())
        {
            throw new ServerSideBusinessException("未读取到操作员信息，请重新登录！", HttpStatus.NOT_ACCEPTABLE);
        }



        depositDetail.setOperId(userOptional.get().getUserId());

        /*添加记录*/
        depositDetailDao.insert(depositDetail);
    }



    @Override
    @OperationLog(desc = "删除存入银行记录信息")
    public void deleteById(Integer id)
    {
        DepositDetail depositDetail = depositDetailDao.findById(id);
        if (depositDetail == null)
        {
            throw new ServerSideBusinessException("入银行款记录信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        depositDetailDao.delete(id);
    }

    @Override
    @OperationLog(desc = "修改入银行款信息")
    public void update(Integer id, DepositDetail   newDepositDetail)
    {
        /*参数校验*/
        if (newDepositDetail == null )
        {
            throw new ServerSideBusinessException("入银行款信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户回款是否存在*/
        DepositDetail target = depositDetailDao.findById(id);
        if (target == null)
        {
            throw new ServerSideBusinessException("该入银行款数据不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*更新数据*/
        newDepositDetail.setId(id);
        depositDetailDao.update(newDepositDetail);
    }
}
