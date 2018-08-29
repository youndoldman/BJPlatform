package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.GasCyrDynRpt;
import com.donno.nj.domain.SaleContactsRpt;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GasCyrDynRptService;
import com.donno.nj.service.SaleContactsCreditRptService;
import com.donno.nj.service.SaleContactsWriteOffRptService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

/*钢瓶动态（领用、送检、退维修、退报废瓶、退押金瓶、押瓶）信息统计（门店）*/

@RestController
public class GasCyrDynRptController
{
    @Autowired
    private GasCyrDynRptService gasCyrDynRptService;

    @RequestMapping(value = "/api/Report/GasCyrDyn", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取门店钢瓶动态统计")
    public ResponseEntity retrieveSaleContactsCreditRpt(@RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
                                                        @RequestParam(value = "operType", required = false) Integer operType,
                                                        @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                                        @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                                        @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                                        @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                                        @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (departmentCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("departmentCode", departmentCode));
        }

        if (operType != null)
        {
            params.putAll(ImmutableMap.of("operType", operType));
        }

        if (startTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("startTime", startTime));
        }

        if (endTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("endTime", endTime));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<GasCyrDynRpt> gasCyrDynRpts = gasCyrDynRptService.retrieve(params);
        Integer count = gasCyrDynRptService.count(params);

        return ResponseEntity.ok(ListRep.assemble(gasCyrDynRpts, count));
    }

}
