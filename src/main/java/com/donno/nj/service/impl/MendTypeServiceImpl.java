package com.donno.nj.service.impl;


import com.donno.nj.dao.MendDao;
import com.donno.nj.dao.MendTypeDao;
import com.donno.nj.domain.MendType;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.MendTypeService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MendTypeServiceImpl implements MendTypeService
{

    @Autowired
    private MendDao mendDao;

    @Autowired
    private MendTypeDao mendTypeDao;



    @Override
    public Optional<MendType> findByCode(String code) {
        return Optional.fromNullable(mendTypeDao.findByCode(code));
    }

    @Override
    public List<MendType> retrieve(Map params)
    {
        return mendTypeDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return mendTypeDao.count(params);
    }

    @Override
    public MendType create(MendType mendType)
    {
        /*参数校验*/
        if(mendType == null ||
                mendType.getCode() == null || mendType.getName() == null ||
                mendType.getCode().trim().length() == 0 || mendType.getName().trim().length() == 0  )
        {
            throw new ServerSideBusinessException("报修类型数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查商品类型是否存在*/
        if (findByCode(mendType.getCode()).isPresent())
        {
            throw new ServerSideBusinessException("报修类型已经存在！", HttpStatus.CONFLICT);
        }

        mendTypeDao.insert(mendType);
        return mendType;
    }


    @Override
    public void update(String code, MendType newMendType)
    {
        /*参数校验*/
        if (code == null)
        {
            throw new ServerSideBusinessException("报修类型数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*参数校验*/
        if(newMendType == null || (newMendType.getCode() == null &&  newMendType.getName() == null) )
        {
            throw new ServerSideBusinessException("报修类型数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*当前商品类型是否存在*/
        MendType mendType = findByCode(code).get();
        if (mendType == null)
        {
            throw new ServerSideBusinessException("当前报修类型不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查商品类型是否存在*/
        if (newMendType.getCode() != null && newMendType.getCode().trim().length() >0)
        {
            if (!code.equals(newMendType.getCode()))
            {

                if (findByCode(newMendType.getCode()).isPresent())
                {
                    throw new ServerSideBusinessException("报修类型已经存在！", HttpStatus.CONFLICT);
                }
            }
            else
            {
                newMendType.setCode(null);
            }
        }
        else
        {
            newMendType.setCode(null);
        }

        /**/
        if (newMendType.getName() == null || newMendType.getName().trim().length() == 0)
        {
            newMendType.setCode(null);
        }

        newMendType.setId(mendType.getId());
        mendTypeDao.update(newMendType);
    }

    @Override
    public  void deleteByCode(String code)
    {
        Optional<MendType> mendType = findByCode(code);
        if (mendType.isPresent())
        {
            /*如有已经有该类型的商品存在，则不允许删除该商品类型*/
            Map params = new HashMap<String,String>();
            params.putAll(ImmutableMap.of("typeCode", code));

            if(mendDao.count(params) > 0)
            {
                throw new ServerSideBusinessException("报修类型不能删除，请先删除该类型的报修单信息！", HttpStatus.NOT_ACCEPTABLE);
            }

            mendTypeDao.delete(mendType.get().getId());
        }
        else
        {
            throw new ServerSideBusinessException("报修类型不存在！", HttpStatus.NOT_FOUND);
        }


    }



}
