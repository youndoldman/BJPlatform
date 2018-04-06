package com.donno.nj.service.impl;


import com.donno.nj.dao.SettlementTypeDao;
import com.donno.nj.domain.SettlementType;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.SettlementTypeService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SettlementTypeServiceImpl implements SettlementTypeService
{

    @Autowired
    private SettlementTypeDao settlementTypeDao;

    @Override
    public Optional<SettlementType> findByCode(String code) {
        return Optional.fromNullable(settlementTypeDao.findByCode(code));
    }

    @Override
    public List<SettlementType> retrieve(Map params) {
        return settlementTypeDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return settlementTypeDao.count(params);
    }

    @Override
    public SettlementType create(SettlementType settlementType)
    {
        /*参数校验*/
        if (settlementType.getCode() == null || settlementType.getCode().trim().length() ==0)
        {
            throw new ServerSideBusinessException("请填写结算类型编码信息", HttpStatus.NOT_ACCEPTABLE);
        }

        if (settlementType.getName() == null || settlementType.getName().trim().length() == 0)
        {
            throw new ServerSideBusinessException("请填写结算类型名称信息", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查是否已经存在*/
        if (settlementTypeDao.findByCode(settlementType.getCode()) != null)
        {
            throw new ServerSideBusinessException("结算类型编码信息已经存在", HttpStatus.CONFLICT);
        }

        settlementTypeDao.insert(settlementType);
        return settlementType;
    }


    @Override
    public void update(Integer id, SettlementType newSettlementType)
    {
        newSettlementType.setId(id);

        /*目的客户类型编码是否已经存在*/
        if ( newSettlementType.getCode() != null && newSettlementType.getCode().trim().length() !=0)
        {
            SettlementType settlementType = settlementTypeDao.findByCode(newSettlementType.getCode());
            if (settlementType != null)
            {
                if (settlementType.getId() != id)
                {
                    throw new ServerSideBusinessException("结算类型编码信息已经存在", HttpStatus.CONFLICT);
                }
                else
                {
                    newSettlementType.setCode(null);
                }
            }
        }

        settlementTypeDao.update(newSettlementType);
    }

    @Override
    public void delete(Integer id)
    {
        settlementTypeDao.delete(id);
    }

}
