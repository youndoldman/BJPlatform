package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.*;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.WriteOffDetailService;
import com.donno.nj.util.AppUtil;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WriteOffDetailServiceImpl implements WriteOffDetailService
{
    @Autowired
    private WriteOffDetailDao writeOffDetailDao;

    @Autowired
    private CustomerCreditDao customerCreditDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomerDao customerDao;

    @Override
    @OperationLog(desc = "查询客户回款记录信息")
    public List<WriteOffDetail> retrieve(Map params)
    {
        List<WriteOffDetail> writeOffDetails = writeOffDetailDao.getList(params);
        return writeOffDetails;
    }

    @Override
    @OperationLog(desc = "查询客户客户回款记录信息条数")
    public Integer count(Map params) {
        return writeOffDetailDao.count(params);
    }

    public void checkCustomer(WriteOffDetail writeOffDetail )
    {
        String userId = writeOffDetail.getUserId();
        if (userId == null || userId.trim().length() == 0)
        {
            throw new ServerSideBusinessException("客户信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        Customer target = customerDao.findByCstUserId(userId);
        if (target == null)
        {
            throw new ServerSideBusinessException("客户不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (target.getSettlementType().getCode().equals( ServerConstantValue.SETTLEMENT_TYPE_COMMON_USER) )
        {
            writeOffDetail.setCreditType( CreditType.CTCommonCredit);
        }
        else if (target.getSettlementType().getCode().equals( ServerConstantValue.SETTLEMENT_TYPE_MONTHLY_CREDIT) )
        {
            writeOffDetail.setCreditType( CreditType.CTMonthlyCredit);
        }
        else
        {
            throw new ServerSideBusinessException("客户不具备回款功能！", HttpStatus.NOT_ACCEPTABLE);
        }
    }

//    public void checkCustomer(String userId,CreditType creditType)
//    {
//        if (userId == null || userId.trim().length() == 0)
//        {
//            throw new ServerSideBusinessException("客户信息不全，请补充！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        Customer target = customerDao.findByUserId(userId);
//        if (target == null)
//        {
//            throw new ServerSideBusinessException("客户不存在！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//    }

    @Override
    @OperationLog(desc = "增加客户回款记录")
    public void create(WriteOffDetail writeOffDetail)
    {
        /*参数校验*/
        if (writeOffDetail == null)
        {
            throw new ServerSideBusinessException("回款信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户信息校验*/
        checkCustomer(writeOffDetail);

        /*操作员信息*/
        Optional<User> userOptional =  AppUtil.getCurrentLoginUser();
        if (!userOptional.isPresent())
        {
            throw new ServerSideBusinessException("未读取到操作员信息，请重新登录！", HttpStatus.NOT_ACCEPTABLE);
        }

        writeOffDetail.setOperId(userOptional.get().getUserId());

        /*更改客户当前的欠款数据*/
        CustomerCredit customerCredit = customerCreditDao.findByUserIdCreditType(writeOffDetail.getUserId(),writeOffDetail.getCreditType());
        if (customerCredit == null)
        {
            customerCredit = new CustomerCredit();
            customerCredit.setUserId(writeOffDetail.getUserId());
            customerCredit.setCreditType(writeOffDetail.getCreditType());
            customerCredit.setAmount(writeOffDetail.getAmount());
            customerCreditDao.insert(customerCredit);
        }
        else
        {
            customerCredit.setAmount(customerCredit.getAmount() - writeOffDetail.getAmount());
            customerCreditDao.update(customerCredit);
        }

        /*添加更改记录*/
        writeOffDetailDao.insert(writeOffDetail);
    }



    @Override
    @OperationLog(desc = "删除客户回款信息")
    public void deleteById(Integer id)
    {
        WriteOffDetail writeOffDetail = writeOffDetailDao.findById(id);
        if (writeOffDetail == null)
        {
            throw new ServerSideBusinessException("回款记录信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        writeOffDetailDao.delete(id);
    }

    @Override
    @OperationLog(desc = "修改客户回款信息")
    public void update(Integer id, WriteOffDetail newWriteOffDetail)
    {
        /*参数校验*/
        if (newWriteOffDetail == null )
        {
            throw new ServerSideBusinessException("客户回款信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户回款是否存在*/
        WriteOffDetail target = writeOffDetailDao.findById(id);
        if (target == null)
        {
            throw new ServerSideBusinessException("该回款数据不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (newWriteOffDetail.getUserId() != null)
        {
            checkCustomer(newWriteOffDetail);
        }

        /*更新数据*/
        newWriteOffDetail.setId(id);
        writeOffDetailDao.update(newWriteOffDetail);
    }
}
