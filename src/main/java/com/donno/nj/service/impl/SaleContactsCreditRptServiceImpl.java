package com.donno.nj.service.impl;

import com.donno.nj.dao.DepartmentDao;
import com.donno.nj.dao.SaleContactsRptCreditDao;
import com.donno.nj.dao.GoodsDao;
import com.donno.nj.domain.Department;
import com.donno.nj.domain.Goods;
import com.donno.nj.domain.SaleContactsRpt;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.SaleContactsCreditRptService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class SaleContactsCreditRptServiceImpl implements SaleContactsCreditRptService
{
    @Autowired
    private SaleContactsRptCreditDao saleContactsRptCreditDao;

    @Override
    public List<SaleContactsRpt> retrieve(Map params) {
        return saleContactsRptCreditDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return saleContactsRptCreditDao.count(params);
    }

}
