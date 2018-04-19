package com.donno.nj.service;

import com.donno.nj.domain.GasCyrDynRpt;
import com.donno.nj.domain.SaleContactsRpt;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/*钢瓶动态（领用、送检、退维修、退报废瓶、退押金瓶、押瓶）信息统计（门店）*/
@Transactional
public interface GasCyrDynRptService
{
    List<GasCyrDynRpt> retrieve(Map params);

    Integer count(Map params);
}
