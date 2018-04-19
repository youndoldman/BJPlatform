package com.donno.nj.service.impl;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.GasCylinderSpecDao;
import com.donno.nj.dao.GasCyrChargeSpecDao;
import com.donno.nj.dao.GasCyrDynDetailDao;
import com.donno.nj.domain.GasCylinderSpec;
import com.donno.nj.domain.GasCyrChargeSpec;
import com.donno.nj.domain.GasCyrDynDetail;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GasCyrChargeSpecService;
import com.donno.nj.service.GasCyrDynDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GasCyrDynDetailServiceImpl implements GasCyrDynDetailService
{
    @Autowired
    private GasCyrDynDetailDao gasCyrDynDetailDao;

    @Autowired
    private GasCylinderSpecDao gasCylinderSpecDao;

    @Override
    @OperationLog(desc = "查询钢瓶动态信息")
    public List<GasCyrDynDetail> retrieve(Map params)
    {
        List<GasCyrDynDetail> gasCyrDynDetails = gasCyrDynDetailDao.getList(params);
        return gasCyrDynDetails;
    }

    @Override
    @OperationLog(desc = "查询钢瓶动态信息数量")
    public Integer count(Map params) {
        return gasCyrDynDetailDao.count(params);
    }


    public void checkGasCyrSpec(String specCode)
    {
        if (specCode == null || specCode.trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少钢瓶规格信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        GasCylinderSpec  target = gasCylinderSpecDao.findByCode(specCode);
        if (target == null)
        {
            throw new ServerSideBusinessException("钢瓶规格信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
    }



    @Override
    @OperationLog(desc = "添加钢瓶动态信息")
    public void create(GasCyrDynDetail gasCyrDynDetail)
    {
        /*参数校验*/
        if (gasCyrDynDetail == null)
        {
            throw new ServerSideBusinessException("钢瓶动态信息不全，请补充信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*规格检查*/
        checkGasCyrSpec(gasCyrDynDetail.getGasCyrSpecCode()) ;

        gasCyrDynDetailDao.insert(gasCyrDynDetail);
    }

    @Override
    @OperationLog(desc = "修改钢瓶动态信息")
    public void update(Integer id, GasCyrDynDetail   newGasCyrDynDetail)
    {
        /*参数校验*/
        if (newGasCyrDynDetail == null )
        {
            throw new ServerSideBusinessException("钢瓶动态信息不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*是否存在*/
        GasCyrDynDetail gasCyrDynDetail = gasCyrDynDetailDao.findById(id);
        if (gasCyrDynDetail == null)
        {
            throw new ServerSideBusinessException("钢瓶动态信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }


        /*更新数据*/
        newGasCyrDynDetail.setId(gasCyrDynDetail.getId());
        gasCyrDynDetailDao.update(newGasCyrDynDetail);
    }



    @Override
    @OperationLog(desc = "删除钢瓶动态信息")
    public void deleteById(Integer id)
    {
        GasCyrDynDetail gasCyrDynDetail = gasCyrDynDetailDao.findById(id);
        if (gasCyrDynDetail == null)
        {
            throw new ServerSideBusinessException("钢瓶动态信息不存在！", HttpStatus.NOT_FOUND);
        }

        gasCyrDynDetailDao.delete(id);
    }

}
