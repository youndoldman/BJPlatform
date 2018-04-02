package com.donno.nj.service.impl;

import com.donno.nj.dao.DepartmentDao;
import com.donno.nj.dao.GasCyrSaleContactsRptDao;
import com.donno.nj.dao.GoodsDao;
import com.donno.nj.domain.Department;
import com.donno.nj.domain.Goods;
import com.donno.nj.domain.GasCyrSaleContactsRpt;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GasCyrSaleContactsRptService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class GasCyrSaleContactsRptServiceImpl implements GasCyrSaleContactsRptService
{
    @Autowired
    private GasCyrSaleContactsRptDao gasCyrSaleContactsRptDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public Optional<GasCyrSaleContactsRpt > findById(Integer id)
    {
        return Optional.fromNullable(gasCyrSaleContactsRptDao.findById(id));
    }

    @Override
    public List<GasCyrSaleContactsRpt> retrieveDailyRpt(Map params) {
        return gasCyrSaleContactsRptDao.getDailyRpt(params);
    }

    @Override
    public Integer countDailyRpt(Map params) {
        return gasCyrSaleContactsRptDao.countDailyRpt(params);
    }

    @Override
    public void create(GasCyrSaleContactsRpt gasCyrSaleContactsRpt)
    {
        /*参数校验*/
        if (gasCyrSaleContactsRpt.getDepartment() == null || gasCyrSaleContactsRpt.getDepartment().getCode().trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少门店信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (gasCyrSaleContactsRpt.getGoods() == null || gasCyrSaleContactsRpt.getGoods().getCode().trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少门店信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (gasCyrSaleContactsRpt.getDate() == null || gasCyrSaleContactsRpt.getGoods().getCode().trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少日期信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*部门是否存在*/
        Department department = departmentDao.findByCode(gasCyrSaleContactsRpt.getDepartment().getCode());
        if ( department == null)
        {
            throw new ServerSideBusinessException("部门信息错误！", HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {
            gasCyrSaleContactsRpt.setDepartment(department);
        }

        /*商品是否存在*/
        Goods goods = goodsDao.findByCode(gasCyrSaleContactsRpt.getGoods().getCode());
        if ( goods == null)
        {
            throw new ServerSideBusinessException("商品信息错误！", HttpStatus.NOT_ACCEPTABLE);
        }
        else
        {
            gasCyrSaleContactsRpt.setGoods(goods);
        }

        /*该天的日报表是否已经存在*/
       if (gasCyrSaleContactsRptDao.findByDaily(gasCyrSaleContactsRpt.getDepartment().getCode(), gasCyrSaleContactsRpt.getGoods().getCode(),gasCyrSaleContactsRpt.getDate()) != null)
       {
           throw new ServerSideBusinessException("已经存在该信息！", HttpStatus.CONFLICT);
       }

        gasCyrSaleContactsRptDao.insert(gasCyrSaleContactsRpt);
    }

    @Override
    public void update(Integer id, GasCyrSaleContactsRpt gasCyrSaleContactsRpt)
    {
        if (gasCyrSaleContactsRptDao.findById(id) == null)
        {
            throw new ServerSideBusinessException("不存在该数据！", HttpStatus.NOT_ACCEPTABLE);
        }

        gasCyrSaleContactsRpt.setId(id);
        gasCyrSaleContactsRptDao.update(gasCyrSaleContactsRpt);
    }

    @Override
    public void deleteById(Integer id)
    {
        if (gasCyrSaleContactsRptDao.findById(id) == null)
        {
            throw new ServerSideBusinessException("不存在该数据！", HttpStatus.NOT_FOUND);
        }
        gasCyrSaleContactsRptDao.delete(id);
    }
}
