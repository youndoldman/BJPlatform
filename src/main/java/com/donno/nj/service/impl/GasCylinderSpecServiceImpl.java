package com.donno.nj.service.impl;


import com.donno.nj.dao.GasCylinderDao;
import com.donno.nj.dao.GasCylinderSpecDao;
import com.donno.nj.domain.GasCylinderSpec;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GasCylinderSpecService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GasCylinderSpecServiceImpl implements GasCylinderSpecService
{

    @Autowired
    private GasCylinderSpecDao gasCylinderSpecDao;

    @Autowired
    private GasCylinderDao gasCylinderDao;

    @Override
    public Optional<GasCylinderSpec> findByCode(String code) {
        return Optional.fromNullable(gasCylinderSpecDao.findByCode(code));
    }

    @Override
    public List<GasCylinderSpec> retrieve(Map params)
    {
        return gasCylinderSpecDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return gasCylinderSpecDao.count(params);
    }

    @Override
    public GasCylinderSpec create(GasCylinderSpec gasCylinderSpec)
    {
        /*参数校验*/
        if(gasCylinderSpec == null ||
                gasCylinderSpec.getCode() == null || gasCylinderSpec.getName() == null ||
                gasCylinderSpec.getCode().trim().length() == 0 || gasCylinderSpec.getName().trim().length() == 0  )
        {
            throw new ServerSideBusinessException("钢瓶规格数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查商品类型是否存在*/
        if (findByCode(gasCylinderSpec.getCode()).isPresent())
        {
            throw new ServerSideBusinessException("钢瓶规格已经存在！", HttpStatus.CONFLICT);
        }

        gasCylinderSpecDao.insert(gasCylinderSpec);
        return gasCylinderSpec;
    }


    @Override
    public void update(String code, GasCylinderSpec newGasCylinderSpec)
    {
        /*参数校验*/
        if (code == null)
        {
            throw new ServerSideBusinessException("钢瓶规格代码不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*参数校验*/
        if(newGasCylinderSpec == null || (newGasCylinderSpec.getCode() == null &&  newGasCylinderSpec.getName() == null) )
        {
            throw new ServerSideBusinessException("钢瓶规格数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*当前商品类型是否存在*/
        Optional<GasCylinderSpec> gasCylinderSpecOptional = findByCode(code);
        if (!gasCylinderSpecOptional.isPresent())
        {
            throw new ServerSideBusinessException("钢瓶规格不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        GasCylinderSpec gasCylinderSpec = gasCylinderSpecOptional.get();
        newGasCylinderSpec.setId(gasCylinderSpec.getId());

        /*检查商品类型是否存在*/
        if (newGasCylinderSpec.getCode() != null && newGasCylinderSpec.getCode().trim().length() >0)
        {
            if (!code.equals(newGasCylinderSpec.getCode()))
            {

                if (findByCode(newGasCylinderSpec.getCode()).isPresent())
                {
                    throw new ServerSideBusinessException("钢瓶规格已经存在！", HttpStatus.CONFLICT);
                }
            }
            else
            {
                newGasCylinderSpec.setCode(null);
            }
        }
        else
        {
            newGasCylinderSpec.setCode(null);
        }

        /**/
        if (newGasCylinderSpec.getName() == null || newGasCylinderSpec.getName().trim().length() == 0)
        {
            newGasCylinderSpec.setCode(null);
        }

        gasCylinderSpecDao.update(newGasCylinderSpec);
    }

    @Override
    public  void deleteByCode(String code)
    {
        /*查找该规格是否存在*/
        Optional<GasCylinderSpec> gasCylinderSpecOptional = findByCode(code);
        if (gasCylinderSpecOptional.isPresent())
        {
            /*如有已经有该钢瓶规格的钢瓶存在，则不允许删除*/
            Map params = new HashMap<String,String>();
            params.putAll(ImmutableMap.of("specCode", code));

            if(gasCylinderDao.count(params) > 0)
            {
                throw new ServerSideBusinessException("不能删除钢瓶规格，已存在该规格的钢瓶！", HttpStatus.NOT_ACCEPTABLE);
            }

            gasCylinderSpecDao.delete(gasCylinderSpecOptional.get().getId());

        }
        else
        {
            throw new ServerSideBusinessException("钢瓶规格不存在！", HttpStatus.NOT_FOUND);
        }
    }

}
