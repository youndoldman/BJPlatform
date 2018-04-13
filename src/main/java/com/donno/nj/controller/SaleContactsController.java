package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.dao.SaleContactsRptCreditDao;
import com.donno.nj.domain.GasCyrDailyStockRpt;
import com.donno.nj.domain.GasCyrSaleContactsRpt;
import com.donno.nj.domain.SaleContactsRpt;
import com.donno.nj.domain.SalesRpt;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GasCyrDailyStockRptService;
import com.donno.nj.service.GasCyrSaleContactsRptService;
import com.donno.nj.service.SaleContactsCreditRptService;
import com.donno.nj.service.SaleContactsWriteOffRptService;
import com.donno.nj.service.SalesRptService;
import com.donno.nj.util.Clock;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

/*销售往来报表*/

@RestController
public class SaleContactsController
{
    @Autowired
    private SaleContactsCreditRptService saleContactsCreditRptService;

    @Autowired
    private SaleContactsWriteOffRptService saleContactsWriteOffRptService;


    @RequestMapping(value = "/api/Report/SaleContacts", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取门店销售销售往来日报表(赊销、月结、赊销回款、月结回款)")
    public ResponseEntity retrieveSaleContactsCreditRpt(@RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
                                                        @RequestParam(value = "creditType", required = true) Integer creditType,
                                                        @RequestParam(value = "writeOffType", required = true) Integer writeOffType,
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

        if (creditType != null)
        {
            params.putAll(ImmutableMap.of("creditType", creditType));
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

        List<SaleContactsRpt> saleContactsRpts = null;
        Integer count = 0;

        if (writeOffType == 0)//欠款
        {
            saleContactsRpts= saleContactsCreditRptService.retrieve(params);
            count = saleContactsCreditRptService.count(params);
        }
        else if (writeOffType == 1)//回款
        {
            saleContactsRpts = saleContactsWriteOffRptService.retrieve(params);
             count = saleContactsWriteOffRptService.count(params);
        }
        else
        {
            throw new ServerSideBusinessException("欠款回款类型错误！",HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(ListRep.assemble(saleContactsRpts, count));
    }

}
