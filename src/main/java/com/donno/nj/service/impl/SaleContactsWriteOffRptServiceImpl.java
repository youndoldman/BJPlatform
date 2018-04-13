package com.donno.nj.service.impl;

import com.donno.nj.dao.SaleContactsRptWriteOffDao;
import com.donno.nj.domain.SaleContactsRpt;
import com.donno.nj.service.SaleContactsWriteOffRptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class SaleContactsWriteOffRptServiceImpl implements SaleContactsWriteOffRptService
{
    @Autowired
    private SaleContactsRptWriteOffDao saleContactsRptWriteOffDao;

    @Override
    public List<SaleContactsRpt> retrieve(Map params) {
        return saleContactsRptWriteOffDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return saleContactsRptWriteOffDao.count(params);
    }

}
