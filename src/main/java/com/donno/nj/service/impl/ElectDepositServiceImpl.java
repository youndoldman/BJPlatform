package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.ElectDepositDao;
import com.donno.nj.dao.ElectDepositDetailDao;
import com.donno.nj.dao.GasCylinderSpecDao;
import com.donno.nj.dao.UserDao;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.ElectDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ElectDepositServiceImpl implements ElectDepositService
{
    @Autowired
    private ElectDepositDao electDepositDao;

    @Autowired
    private ElectDepositDetailDao electDepositDetailDao;

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
        User customer =  userDao.findByUserId(electDeposit.getCustomerId());
        if (customer == null)
        {
            throw new ServerSideBusinessException("客户不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*派送工信息校验*/
        User operator =  userDao.findByUserId(electDeposit.getOperId());
        if (operator == null)
        {
            throw new ServerSideBusinessException("直销员信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }


         /*押金单信息校验*/
        if (electDeposit.getElectDepositDetails().size() == 0)
        {
            throw new ServerSideBusinessException("押金单明细信息缺失！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*押金单金额校验*/
        if (electDeposit.getAmountReceivable() == null)
        {
            throw new ServerSideBusinessException("押金单应收金额信息缺失！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*押金单实收金额校验*/
        if (electDeposit.getActualAmount() == null)
        {
            throw new ServerSideBusinessException("押金单实收金额信息缺失！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*押金单编号*/
        Date curDate = new Date();
        String sn =  new SimpleDateFormat("yyyyMMddHHmmssSSS").format(curDate);
        electDeposit.setDepositSn(sn);

        if (electDepositDao.findBySn(sn) != null)
        {
            throw new ServerSideBusinessException("押金单创建失败，请重新提交！", HttpStatus.NOT_ACCEPTABLE);
        }

        electDepositDao.insert(electDeposit);

        for (ElectDepositDetail electDepositDetail : electDeposit.getElectDepositDetails())
        {
            /*规格校验*/
            String specCode = electDepositDetail.getGasCylinderSpec().getCode();
            GasCylinderSpec gasCylinderSpec = gasCylinderSpecDao.findByCode(specCode);
            if (gasCylinderSpec == null)
            {
                throw new ServerSideBusinessException("规格信息错误！", HttpStatus.NOT_ACCEPTABLE);
            }
            electDepositDetail.setGasCylinderSpec(gasCylinderSpec);


            /*类型校验*/
            if (electDepositDetail.getElectDepositType() == null ||
                    electDepositDetail.getElectDepositType().getIndex() <= ElectDepositType.ElectDepositTypeStart.getIndex()
                    || electDepositDetail.getElectDepositType().getIndex() >= ElectDepositType.ElectDepositTypeEnd.getIndex())
            {
                throw new ServerSideBusinessException("电子押金单类型信息错误！", HttpStatus.NOT_ACCEPTABLE);
            }


            /*数量校验*/
            if (electDepositDetail.getQuantity() == null)
            {
                throw new ServerSideBusinessException("电子押金单数量信息错误！", HttpStatus.NOT_ACCEPTABLE);
            }

            electDepositDetail.setElectDepositIdx(electDeposit.getId());
            electDepositDetailDao.insert(electDepositDetail);
        }
    }

//    @Override
//    @OperationLog(desc = "修改电子押金单")
//    public void update(Integer id, ElectDeposit electDeposit)
//    {
//        /*参数校验*/
//        if (electDeposit == null)
//        {
//            throw new ServerSideBusinessException("缺少电子押金单信息，请补充！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//
//        ElectDeposit oriElectDeposit = electDepositDao.findById(id);
//        if (oriElectDeposit == null)
//        {
//            throw new ServerSideBusinessException("电子押金单信息不存在！", HttpStatus.NOT_ACCEPTABLE);
//        }
//
//        /*更新数据*/
//        electDeposit.setId(id);
//        electDepositDao.update(electDeposit);
//    }

    @Override
    @OperationLog(desc = "删除电子押金单")
    public void deleteById(Integer id)
    {
        ElectDeposit electDeposit = electDepositDao.findById(id);
        if (electDeposit == null)
        {
            throw new ServerSideBusinessException("电子押金单信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        electDepositDetailDao.deleteByElectDepositIdx(id);
        electDepositDao.delete(id);
    }
}
