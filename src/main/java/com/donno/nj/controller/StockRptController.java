package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.GasCyrDailyStockRpt;
import com.donno.nj.domain.GasCyrSaleContactsRpt;
import com.donno.nj.domain.SalesRpt;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GasCyrDailyStockRptService;
import com.donno.nj.service.GasCyrSaleContactsRptService;
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


/*库存报表*/

@RestController
public class StockRptController
{
    @Autowired
    private GasCyrDailyStockRptService gasCyrDailyStockRptService;

//
//    @RequestMapping(value = "/api/Report/Daily/Stock", method = RequestMethod.GET, produces = "application/json")
//    @OperationLog(desc = "获取门店库存日报表")
//    public ResponseEntity retrieveDailyStockRpt(@RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
//                                   @RequestParam(value = "specCode", defaultValue = "") String specCode,
//                                   @RequestParam(value = "date", defaultValue = "") String date,
//                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
//                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
//                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
//    {
//        Map params = new HashMap<String,String>();
//
//
//        if (departmentCode.trim().length() > 0)
//        {
//            params.putAll(ImmutableMap.of("departmentCode", departmentCode));
//        }
//
//        if (specCode.trim().length() > 0)
//        {
//            params.putAll(ImmutableMap.of("specCode", specCode));
//        }
//
//        if (date.trim().length() > 0)
//        {
//            params.putAll(ImmutableMap.of("date", date));
//        }
//
//        params.putAll(paginationParams(pageNo, pageSize, orderBy));
//
//        List<GasCyrDailyStockRpt> gasCyrDailyStockRpts = gasCyrDailyStockRptService.retrieve(params);
//        Integer count = gasCyrDailyStockRptService.count(params);
//        return ResponseEntity.ok(ListRep.assemble(gasCyrDailyStockRpts, count));
//    }


}
