package com.donno.nj.service.impl;


import com.donno.nj.dao.SystemParamDao;
import com.donno.nj.domain.SystemParam;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.GoodsTypeService;
import com.donno.nj.service.SystemParamService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemParamServiceImpl implements SystemParamService
{
    @Autowired
    private SystemParamDao systemParamDao;

    @Override
    public Integer getDispatchRange()
    {
        return systemParamDao.getDispatchRange();
    }

}
