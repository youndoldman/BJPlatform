package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.GasCyrDailyStockRpt;
import com.donno.nj.domain.SalesRpt;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GasCyrDailyStockRptService;
import com.donno.nj.service.SalesRptService;
import com.donno.nj.util.Clock;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class ReportController
{
    @Autowired
    private GasCyrDailyStockRptService gasCyrDailyStockRptService;

    @Autowired
    private SalesRptService salesRptService;

    @RequestMapping(value = "/api/Report/Daily/Stock", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取门店库存日报表")
    public ResponseEntity retrieveDailyStockRpt(@RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
                                   @RequestParam(value = "specCode", defaultValue = "") String specCode,
                                   @RequestParam(value = "date", defaultValue = "") String date,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();


        if (departmentCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("departmentCode", departmentCode));
        }

        if (specCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("specCode", specCode));
        }

        if (date.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("date", date));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<GasCyrDailyStockRpt> gasCyrDailyStockRpts = gasCyrDailyStockRptService.retrieve(params);
        Integer count = gasCyrDailyStockRptService.count(params);
        return ResponseEntity.ok(ListRep.assemble(gasCyrDailyStockRpts, count));
    }


    @RequestMapping(value = "/api/Report/Daily/Sales", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取门店销售日报表")
    public ResponseEntity retrieveDailySalesRpt(@RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
                                                @RequestParam(value = "date", defaultValue = "") String date,
                                                @RequestParam(value = "customerTypeCode", defaultValue = "") String customerTypeCode,
                                                @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                                @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                                @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (departmentCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("departmentCode", departmentCode));
        }

        if (customerTypeCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("customerTypeCode", customerTypeCode));
        }

        if (date.trim().length() > 0)
        {
            try
            {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date endDate = df.parse(date);
                Date startDate = Clock.getLastDay(endDate);
                params.putAll(ImmutableMap.of("startDate", startDate));
                params.putAll(ImmutableMap.of("endDate", endDate));
            }
            catch(ParseException e)
            {
                throw new ServerSideBusinessException("日期错误！", HttpStatus.NOT_ACCEPTABLE);
            }
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<SalesRpt> salesRptList = salesRptService.retrieveDailyRpt(params);
        Integer count = salesRptService.countDailyRpt(params);
        return ResponseEntity.ok(ListRep.assemble(salesRptList, count));
    }




}
