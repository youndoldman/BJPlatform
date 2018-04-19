package com.donno.nj.service.impl;

import com.donno.nj.dao.GasCyrDynRptDao;
import com.donno.nj.dao.SaleContactsRptCreditDao;
import com.donno.nj.domain.GasCyrDynRpt;
import com.donno.nj.domain.SaleContactsRpt;
import com.donno.nj.service.GasCyrDynRptService;
import com.donno.nj.service.SaleContactsCreditRptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
/*钢瓶动态（领用、送检、退维修、退报废瓶、退押金瓶、押瓶）信息统计（门店）*/

@Service
public class GasCyrDynRptServiceImpl implements GasCyrDynRptService
{
    @Autowired
    private GasCyrDynRptDao gasCyrDynRptDao;

    @Override
    public List<GasCyrDynRpt> retrieve(Map params) {
        return gasCyrDynRptDao.getList(params);
    }

    @Override
    public Integer count(Map params) {
        return gasCyrDynRptDao.count(params);
    }

}
