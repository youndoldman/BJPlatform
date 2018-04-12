package com.donno.nj.service.impl;


import com.donno.nj.dao.SecurityDao;
import com.donno.nj.dao.SecurityTypeDao;
import com.donno.nj.domain.SecurityType;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.SecurityTypeService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SecurityTypeServiceImpl implements SecurityTypeService
{

    @Autowired
    private SecurityDao securityDao;

    @Autowired
    private SecurityTypeDao securityTypeDao;



    @Override
    public Optional<SecurityType> findByCode(String code) {
        return Optional.fromNullable(securityTypeDao.findByCode(code));
    }

    @Override
    public List<SecurityType> retrieve(Map params)
    {
        return securityTypeDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return securityTypeDao.count(params);
    }

    @Override
    public SecurityType create(SecurityType securityType)
    {
        /*参数校验*/
        if(securityType == null ||
                securityType.getCode() == null || securityType.getName() == null ||
                securityType.getCode().trim().length() == 0 || securityType.getName().trim().length() == 0  )
        {
            throw new ServerSideBusinessException("安检类型数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查商品类型是否存在*/
        if (findByCode(securityType.getCode()).isPresent())
        {
            throw new ServerSideBusinessException("安检类型已经存在！", HttpStatus.CONFLICT);
        }

        securityTypeDao.insert(securityType);
        return securityType;
    }


    @Override
    public void update(String code, SecurityType newsecurityType)
    {
        /*参数校验*/
        if (code == null)
        {
            throw new ServerSideBusinessException("安检类型数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*参数校验*/
        if(newsecurityType == null || (newsecurityType.getCode() == null &&  newsecurityType.getName() == null) )
        {
            throw new ServerSideBusinessException("安检类型数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*当前商品类型是否存在*/
        SecurityType securityType = findByCode(code).get();
        if (securityType == null)
        {
            throw new ServerSideBusinessException("当前安检类型不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查商品类型是否存在*/
        if (newsecurityType.getCode() != null && newsecurityType.getCode().trim().length() >0)
        {
            if (!code.equals(newsecurityType.getCode()))
            {

                if (findByCode(newsecurityType.getCode()).isPresent())
                {
                    throw new ServerSideBusinessException("安检类型已经存在！", HttpStatus.CONFLICT);
                }
            }
            else
            {
                newsecurityType.setCode(null);
            }
        }
        else
        {
            newsecurityType.setCode(null);
        }

        /**/
        if (newsecurityType.getName() == null || newsecurityType.getName().trim().length() == 0)
        {
            newsecurityType.setCode(null);
        }

        newsecurityType.setId(securityType.getId());
        securityTypeDao.update(newsecurityType);
    }

    @Override
    public  void deleteByCode(String code)
    {
        Optional<SecurityType> securityType = findByCode(code);
        if (securityType.isPresent())
        {
            /*如有已经有该类型的商品存在，则不允许删除该商品类型*/
            Map params = new HashMap<String,String>();
            params.putAll(ImmutableMap.of("typeCode", code));

            if(securityDao.count(params) > 0)
            {
                throw new ServerSideBusinessException("安检类型不能删除，请先删除该类型的安检单信息！", HttpStatus.NOT_ACCEPTABLE);
            }

            securityTypeDao.delete(securityType.get().getId());
        }
        else
        {
            throw new ServerSideBusinessException("安检类型不存在！", HttpStatus.NOT_FOUND);
        }


    }



}
