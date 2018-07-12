package com.donno.nj.service.impl;


import com.donno.nj.dao.GasCylinderDao;
import com.donno.nj.dao.GasCylinderSpecDao;
import com.donno.nj.dao.GasCynFactoryDao;
import com.donno.nj.domain.GasCylinderSpec;
import com.donno.nj.domain.GasCynFactory;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GasCylinderSpecService;
import com.donno.nj.service.GasCynFactoryService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GasCynFactoryServiceImpl implements GasCynFactoryService
{

    @Autowired
    private GasCynFactoryDao gasCynFactoryDao;

    @Autowired
    private GasCylinderDao gasCylinderDao;

    @Override
    public Optional<GasCynFactory> findByCode(String code) {
        return Optional.fromNullable(gasCynFactoryDao.findByCode(code));
    }

    @Override
    public List<GasCynFactory> retrieve(Map params)
    {
        return gasCynFactoryDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return gasCynFactoryDao.count(params);
    }

    @Override
    public GasCynFactory create(GasCynFactory gasCynFactory)
    {
        /*参数校验*/
        if(gasCynFactory == null ||
                gasCynFactory.getCode() == null || gasCynFactory.getName() == null ||
                gasCynFactory.getCode().trim().length() == 0 || gasCynFactory.getName().trim().length() == 0  )
        {
            throw new ServerSideBusinessException("钢瓶厂家数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查商品类型是否存在*/
        if (findByCode(gasCynFactory.getCode()).isPresent())
        {
            throw new ServerSideBusinessException("钢瓶规格已经存在！", HttpStatus.CONFLICT);
        }

        gasCynFactoryDao.insert(gasCynFactory);
        return gasCynFactory;
    }


    @Override
    public void update(String code, GasCynFactory newGasCynFactory)
    {
        /*参数校验*/
        if (code == null)
        {
            throw new ServerSideBusinessException("钢瓶厂家代码不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*参数校验*/
        if(newGasCynFactory == null || (newGasCynFactory.getCode() == null &&  newGasCynFactory.getName() == null) )
        {
            throw new ServerSideBusinessException("钢瓶规格数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*当前厂家是否存在*/
        Optional<GasCynFactory> gasCynFactoryOptional = findByCode(code);
        if (!gasCynFactoryOptional.isPresent())
        {
            throw new ServerSideBusinessException("钢瓶厂家不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        GasCynFactory gasCynFactory = gasCynFactoryOptional.get();
        newGasCynFactory.setId(gasCynFactory.getId());

        /*检查厂家编码是否存在*/
        if (newGasCynFactory.getCode() != null && newGasCynFactory.getCode().trim().length() >0)
        {
            if (!code.equals(newGasCynFactory.getCode()))
            {

                if (findByCode(newGasCynFactory.getCode()).isPresent())
                {
                    throw new ServerSideBusinessException("钢瓶厂家已经存在！", HttpStatus.CONFLICT);
                }
            }
            else
            {
                newGasCynFactory.setCode(null);
            }
        }
        else
        {
            newGasCynFactory.setCode(null);
        }

        /**/
        if (newGasCynFactory.getName() == null || newGasCynFactory.getName().trim().length() == 0)
        {
            newGasCynFactory.setCode(null);
        }

        gasCynFactoryDao.update(newGasCynFactory);
    }

    @Override
    public  void deleteByCode(String code)
    {
        /*查找该规格是否存在*/
        Optional<GasCynFactory> gasCynFactoryOptional = findByCode(code);
        if (gasCynFactoryOptional.isPresent())
        {
            /*如有已经有该钢瓶规格的钢瓶存在，则不允许删除*/
            Map params = new HashMap<String,String>();
            params.putAll(ImmutableMap.of("factoryCode", code));

            if(gasCylinderDao.count(params) > 0)
            {
                throw new ServerSideBusinessException("不能删除钢瓶厂家，已存在该厂家的钢瓶！", HttpStatus.NOT_ACCEPTABLE);
            }

            gasCynFactoryDao.delete(gasCynFactoryOptional.get().getId());

        }
        else
        {
            throw new ServerSideBusinessException("钢瓶厂家不存在！", HttpStatus.NOT_FOUND);
        }
    }

}
