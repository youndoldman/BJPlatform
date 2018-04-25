package com.donno.nj.service.impl;

import com.donno.nj.dao.SalesByCstTypeRptDao;
import com.donno.nj.dao.SalesByPayTypeRptDao;
import com.donno.nj.domain.PayType;
import com.donno.nj.domain.SalesRpt;

import com.donno.nj.service.SalesRptService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SalesRptServiceImpl implements SalesRptService
{
    @Autowired
    private SalesByPayTypeRptDao salesByPayTypeRptDao;

    @Autowired
    private SalesByCstTypeRptDao salesByCstTypeRptDao;

    @Override
    public List<SalesRpt> retrieveSaleRptByPayType(Map params)
    {
        List<SalesRpt> salesRptList = null;
        if (params.containsKey("payType"))
        {
            salesRptList = salesByPayTypeRptDao.getSaleRptByPayType(params);
        }
        else //无paytype 计算 和值
        {
            salesRptList = new ArrayList<SalesRpt>();

            /*电子*/
            Map rptParams = new HashMap<String,String>();
            rptParams.putAll(params);
            rptParams.putAll(ImmutableMap.of("payType", PayType.PTOnLine.getIndex()));
            List<SalesRpt> salesOnlineRptList = salesByPayTypeRptDao.getSaleRptByPayType(rptParams);

            mergeSaleRpt(salesOnlineRptList,salesRptList);

            /*现金*/
            rptParams.remove("payType");
            rptParams.putAll(ImmutableMap.of("payType", PayType.PTCash.getIndex()));
            List<SalesRpt> salesCashRptList = salesByPayTypeRptDao.getSaleRptByPayType(rptParams);

            mergeSaleRpt(salesCashRptList,salesRptList);

             /*赊销*/
            rptParams.remove("payType");
            rptParams.putAll(ImmutableMap.of("payType", PayType.PTDebtCredit.getIndex()));
            List<SalesRpt> salesCreditRptList = salesByPayTypeRptDao.getSaleRptByPayType(rptParams);

            mergeSaleRpt(salesCreditRptList,salesRptList);

             /*月结*/
            rptParams.remove("payType");
            rptParams.putAll(ImmutableMap.of("payType", PayType.PTMonthlyCredit.getIndex()));
            List<SalesRpt> salesMonthlyCreditRptList = salesByPayTypeRptDao.getSaleRptByPayType(rptParams);

            mergeSaleRpt(salesMonthlyCreditRptList,salesRptList);

            /*气票*/
            rptParams.remove("payType");
            rptParams.putAll(ImmutableMap.of("payType", PayType.PTTicket.getIndex() ));
            List<SalesRpt> salesTicketRptList = salesByPayTypeRptDao.getSaleRptByPayType(rptParams);

            mergeSaleRpt(salesTicketRptList,salesRptList);

            /*优惠券*/
            rptParams.remove("payType");
            rptParams.putAll(ImmutableMap.of("payType", PayType.PTCoupon.getIndex() ));
            List<SalesRpt> salesCouponRptList = salesByPayTypeRptDao.getSaleRptByPayType(rptParams);

            mergeSaleRpt(salesCouponRptList,salesRptList);
        }

        return salesRptList;
    }

    @Override
    public Integer countSaleRptByPayType(Map params) {
        return salesByPayTypeRptDao.countSaleRptByPayType(params);
    }


    @Override
    public List<SalesRpt> retrieveSaleRptByCstType(Map params) {
        return salesByCstTypeRptDao.getSaleRptByCstType(params);
    }

    @Override
    public Integer countSaleRptByCstType(Map params) {
        return salesByCstTypeRptDao.countSaleRptByCstType(params);
    }


    public void mergeSaleRpt(List<SalesRpt> salesRpts,List<SalesRpt> targets)
    {
        boolean finded = false;
        for (SalesRpt salesRpt :salesRpts)
        {
            finded = false;
            for (SalesRpt target :targets)
            {
                if (salesRpt.getSpecCode().equals(target.getSpecCode()))
                {
                    finded = true;
                    target.setSum(target.getSum() + salesRpt.getSum() );
                    target.setCount(target.getCount() + salesRpt.getCount() );
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
