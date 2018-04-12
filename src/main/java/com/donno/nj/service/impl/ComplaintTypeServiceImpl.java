package com.donno.nj.service.impl;


import com.donno.nj.dao.ComplaintDao;
import com.donno.nj.dao.ComplaintTypeDao;
import com.donno.nj.domain.ComplaintType;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.ComplaintTypeService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComplaintTypeServiceImpl implements ComplaintTypeService
{

    @Autowired
    private ComplaintDao complaintDao;

    @Autowired
    private ComplaintTypeDao complaintTypeDao;



    @Override
    public Optional<ComplaintType> findByCode(String code) {
        return Optional.fromNullable(complaintTypeDao.findByCode(code));
    }

    @Override
    public List<ComplaintType> retrieve(Map params)
    {
        return complaintTypeDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return complaintTypeDao.count(params);
    }

    @Override
    public ComplaintType create(ComplaintType complaintType)
    {
        /*参数校验*/
        if(complaintType == null ||
                complaintType.getCode() == null || complaintType.getName() == null ||
                complaintType.getCode().trim().length() == 0 || complaintType.getName().trim().length() == 0  )
        {
            throw new ServerSideBusinessException("投诉类型数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查商品类型是否存在*/
        if (findByCode(complaintType.getCode()).isPresent())
        {
            throw new ServerSideBusinessException("投诉类型已经存在！", HttpStatus.CONFLICT);
        }

        complaintTypeDao.insert(complaintType);
        return complaintType;
    }


    @Override
    public void update(String code, ComplaintType newcomplaintType)
    {
        /*参数校验*/
        if (code == null)
        {
            throw new ServerSideBusinessException("投诉类型数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*参数校验*/
        if(newcomplaintType == null || (newcomplaintType.getCode() == null &&  newcomplaintType.getName() == null) )
        {
            throw new ServerSideBusinessException("投诉类型数据不能为空！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*当前商品类型是否存在*/
        ComplaintType complaintType = findByCode(code).get();
        if (complaintType == null)
        {
            throw new ServerSideBusinessException("当前投诉类型不存在！", HttpStatus.NOT_ACCEPTABLE);
        }

        /*检查商品类型是否存在*/
        if (newcomplaintType.getCode() != null && newcomplaintType.getCode().trim().length() >0)
        {
            if (!code.equals(newcomplaintType.getCode()))
            {

                if (findByCode(newcomplaintType.getCode()).isPresent())
                {
                    throw new ServerSideBusinessException("投诉类型已经存在！", HttpStatus.CONFLICT);
                }
            }
            else
            {
                newcomplaintType.setCode(null);
            }
        }
        else
        {
            newcomplaintType.setCode(null);
        }

        /**/
        if (newcomplaintType.getName() == null || newcomplaintType.getName().trim().length() == 0)
        {
            newcomplaintType.setCode(null);
        }

        newcomplaintType.setId(complaintType.getId());
        complaintTypeDao.update(newcomplaintType);
    }

    @Override
    public  void deleteByCode(String code)
    {
        Optional<ComplaintType> complaintType = findByCode(code);
        if (complaintType.isPresent())
        {
            /*如有已经有该类型的商品存在，则不允许删除该商品类型*/
            Map params = new HashMap<String,String>();
            params.putAll(ImmutableMap.of("typeCode", code));

            if(complaintDao.count(params) > 0)
            {
                throw new ServerSideBusinessException("投诉类型不能删除，请先删除该类型的投诉单信息！", HttpStatus.NOT_ACCEPTABLE);
            }

            complaintTypeDao.delete(complaintType.get().getId());
        }
        else
        {
            throw new ServerSideBusinessException("投诉类型不存在！", HttpStatus.NOT_FOUND);
        }


    }



}
