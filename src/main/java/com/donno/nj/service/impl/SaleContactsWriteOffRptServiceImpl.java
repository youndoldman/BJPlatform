package com.donno.nj.service.impl;

import com.donno.nj.dao.DepartmentDao;
import com.donno.nj.dao.SaleContactsRptWriteOffDao;
import com.donno.nj.domain.Department;
import com.donno.nj.domain.DepositDetail;
import com.donno.nj.domain.SaleContactsRpt;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.SaleContactsWriteOffRptService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SaleContactsWriteOffRptServiceImpl implements SaleContactsWriteOffRptService
{
    @Autowired
    private SaleContactsRptWriteOffDao saleContactsRptWriteOffDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public List<SaleContactsRpt> retrieve(Map params)
    {
        List<SaleContactsRpt> saleContactsRpts = new ArrayList<SaleContactsRpt>();

        if (params.containsKey("departmentCode"))
        {
            recurseRetrieve(params,saleContactsRpts);
        }
        else
        {
            saleContactsRpts = saleContactsRptWriteOffDao.getList(params);
        }

        return saleContactsRpts;
    }


    /*子公司递归统计*/
    public void recurseRetrieve(Map params, List<SaleContactsRpt> saleContactsRptList)
    {
        String departmentCode = params.get("departmentCode").toString();
        Department department = departmentDao.findByCode(departmentCode);
        if(department == null)
        {
            throw new ServerSideBusinessException("部门信息不存在！", HttpStatus.NOT_ACCEPTABLE);
        }
        department.setLstSubDepartment(departmentDao.findSubDep(department.getId()));
        if ( department.getLstSubDepartment() != null && department.getLstSubDepartment().size() > 0 )
        {
            for (Department childDep : department.getLstSubDepartment())
            {
                Map subParam = new HashMap<String,String>();
                subParam.putAll(params);
                subParam.remove("departmentCode");
                subParam.putAll(ImmutableMap.of("departmentCode", childDep.getCode()));

                List<SaleContactsRpt> subSaleContactsRpt = new ArrayList<SaleContactsRpt>();
                recurseRetrieve(subParam,subSaleContactsRpt);

                saleContactsRptList.addAll(subSaleContactsRpt);
            }
        }
        else
        {
            saleContactsRptList.addAll(saleContactsRptWriteOffDao.getList(params));
        }
    }

    @Override
    public Integer count(Map params) {
        return saleContactsRptWriteOffDao.count(params);
    }

}
