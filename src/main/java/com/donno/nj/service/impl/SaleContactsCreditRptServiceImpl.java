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
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SaleContactsCreditRptServiceImpl implements SaleContactsCreditRptService
{
    @Autowired
    private SaleContactsRptCreditDao saleContactsRptCreditDao;

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
            saleContactsRpts = saleContactsRptCreditDao.getList(params);
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
            saleContactsRptList.addAll(saleContactsRptCreditDao.getList(params));
        }
    }

    @Override
    public Integer count(Map params) {
        return saleContactsRptCreditDao.count(params);
    }

}
