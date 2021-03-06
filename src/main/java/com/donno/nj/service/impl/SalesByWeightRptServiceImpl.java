package com.donno.nj.service.impl;

import com.donno.nj.dao.DepartmentDao;
import com.donno.nj.dao.SalesByCstTypeRptDao;
import com.donno.nj.dao.SalesByPayTypeRptDao;
import com.donno.nj.dao.SalesByWeightRptDao;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.SalesByWeightRptService;
import com.donno.nj.service.SalesRptService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SalesByWeightRptServiceImpl implements SalesByWeightRptService
{
    @Autowired
    private SalesByWeightRptDao salesByWeightRptDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public List<SalesRptByWeight> retrieveSaleRpt(Map params)
    {
        List<SalesRptByWeight> salesRptList = new ArrayList<SalesRptByWeight>();
        if (params.containsKey("departmentCode"))
        {
            recurseCalSaleRpt(params,salesRptList);
        }
        else
        {
            CalSaleRptByWeight(params,salesRptList);
        }

        return salesRptList;
    }

    /*子公司递归统计*/
    public void recurseCalSaleRpt(Map params, List<SalesRptByWeight> salesRptList)
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

                List<SalesRptByWeight> subSaleRpt  = new ArrayList<SalesRptByWeight>();
                recurseCalSaleRpt(subParam,subSaleRpt);

                mergeSaleRpt(subSaleRpt,salesRptList);
            }
        }
        else
        {
            CalSaleRptByWeight(params,salesRptList);
        }
    }

    public void CalSaleRptByWeight(Map params, List<SalesRptByWeight> salesRptList)
    {
        if (params.containsKey("payType"))
        {
            List<SalesRptByWeight> getSaleRpt = salesByWeightRptDao.getSaleRptByWeight(params);

            salesRptList.addAll(getSaleRpt);
        }
        else //无paytype 计算 和值
        {
            /*电子*/
            Map rptParams = new HashMap<String,String>();
            rptParams.putAll(params);
            rptParams.putAll(ImmutableMap.of("payType", PayType.PTOnLine.getIndex()));
            List<SalesRptByWeight> salesOnlineRptList = salesByWeightRptDao.getSaleRptByWeight(rptParams);

            mergeSaleRpt(salesOnlineRptList,salesRptList);

            /*现金*/
            rptParams.remove("payType");
            rptParams.putAll(ImmutableMap.of("payType", PayType.PTCash.getIndex()));
            List<SalesRptByWeight> salesCashRptList = salesByWeightRptDao.getSaleRptByWeight(rptParams);

            mergeSaleRpt(salesCashRptList,salesRptList);

             /*赊销*/
            rptParams.remove("payType");
            rptParams.putAll(ImmutableMap.of("payType", PayType.PTDebtCredit.getIndex()));
            List<SalesRptByWeight> salesCreditRptList = salesByWeightRptDao.getSaleRptByWeight(rptParams);

            mergeSaleRpt(salesCreditRptList,salesRptList);

             /*月结*/
            rptParams.remove("payType");
            rptParams.putAll(ImmutableMap.of("payType", PayType.PTMonthlyCredit.getIndex()));
            List<SalesRptByWeight> salesMonthlyCreditRptList = salesByWeightRptDao.getSaleRptByWeight(rptParams);

            mergeSaleRpt(salesMonthlyCreditRptList,salesRptList);

            /*气票*/
            rptParams.remove("payType");
            rptParams.putAll(ImmutableMap.of("payType", PayType.PTTicket.getIndex() ));
            List<SalesRptByWeight> salesTicketRptList = salesByWeightRptDao.getSaleRptByWeight(rptParams);

            mergeSaleRpt(salesTicketRptList,salesRptList);

            /*优惠券*/
            rptParams.remove("payType");
            rptParams.putAll(ImmutableMap.of("payType", PayType.PTCoupon.getIndex() ));
            List<SalesRptByWeight> salesCouponRptList = salesByWeightRptDao.getSaleRptByWeight(rptParams);

            mergeSaleRpt(salesCouponRptList,salesRptList);
        }
    }



    public void mergeSaleRpt(List<SalesRptByWeight> salesRpts,List<SalesRptByWeight> targets)
    {
        boolean finded = false;
        for (SalesRptByWeight salesRpt :salesRpts)
        {
            finded = false;
            for (SalesRptByWeight target :targets)
            {
                if (salesRpt.getCustomerTypeCode().equals(target.getCustomerTypeCode()))
                {
                    finded = true;
                    target.setSaleSum(target.getSaleSum() + salesRpt.getSaleSum() );
                    target.setSaleWeight(target.getSaleWeight() + salesRpt.getSaleWeight() );
                    break;
                }
            }

            if (!finded)
            {
                targets.add(salesRpt);
            }
        }
    }
}
