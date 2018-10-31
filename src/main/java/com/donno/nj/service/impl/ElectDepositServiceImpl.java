package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.ElectDepositDao;
import com.donno.nj.dao.GasCylinderSpecDao;
import com.donno.nj.dao.UserDao;
import com.donno.nj.domain.ElectDeposit;
import com.donno.nj.domain.GasCylinderSpec;
import com.donno.nj.domain.User;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.ElectDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ElectDepositServiceImpl implements ElectDepositService
{
    @Autowired
    private ElectDepositDao electDepositDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private GasCylinderSpecDao gasCylinderSpecDao;

    @Override
    @OperationLog(desc = "查询电子押金单信息")
    public List<ElectDeposit> retrieve(Map params)
    {
        return electDepositDao.getList(params);
    }

    @Override
    @OperationLog(desc = "查询电子押金单数量")
    public Integer count(Map params)
    {
        return  electDepositDao.count(params);
    }

    @Override
    @OperationLog(desc = "添加电子押金单")
    public void create(ElectDeposit electDeposit)
    {
        /*参数校验*/
        if (electDeposit == null)
        {
            throw new ServerSideBusinessException("缺少电子押金单信息，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*客户信息校验*/
        User user=  userDao.findByUserId(electDeposit.getUserId());
        if (user == null)
        {
            throw new ServerSideBusinessException("客户不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*规格校验*/
        GasCylinderSpec gasCylinderSpec=  gasCylinderSpecDao.findByCode(electDeposit.getGasCylinderSpec().getCode());
        if (user == null)
        {
            throw new ServerSideBusinessException("规格编码不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        electDepositDao.insert(electDeposit);
    }

    @Override
    @OperationLog(desc = "修改电子押金单")
    public void update(Integer id, ElectDeposit electDeposit)
    {
        /*参数校验*/
        if (electDeposit == null)
        {
            throw new ServerSideBusinessException("缺少电子押金单信息，请补充！", HttpStatus.NOT_ACCEPTABLE);
        }


        ElectDeposit oriElectDeposit = electDepositDao.findById(id);
        if (oriElectDeposit == null)
        {
            throw new ServerSideBusinessException("电子押金单信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*更新数据*/
        electDeposit.setId(id);
        electDepositDao.update(electDeposit);
    }

    @Override
    @OperationLog(desc = "删除电子押金单")
    public void deleteById(Integer id)
    {
        ElectDeposit electDeposit = electDepositDao.findById(id);
        if (electDeposit == null)
        {
            throw new ServerSideBusinessException("电子押金单信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        electDepositDao.delete(id);
    }
}
