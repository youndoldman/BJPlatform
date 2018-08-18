package com.donno.nj.controller;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.dao.GasCylinderDao;
import com.donno.nj.domain.StockRpt;
import com.donno.nj.domain.StockType;
import com.donno.nj.service.StockInOutRptService;
import com.donno.nj.service.StockRptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import static com.donno.nj.util.ParamMapBuilder.paginationParams;
import com.google.common.collect.ImmutableMap;
import com.donno.nj.representation.ListRep;
import com.donno.nj.constant.Constant;

/*库存报表*/

@RestController
public class StockRptController
{
    @Autowired
    private StockRptService stockRptService;

    @Autowired
    private StockInOutRptService stockInOutRptService;


    @RequestMapping(value = "/api/Report/Stock", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取门店库存")
    public ResponseEntity retrieveStockRpt(@RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
                                                @RequestParam(value = "loadStatus", required = false) Integer loadStatus,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (departmentCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("departmentCode", departmentCode));
        }

        if (loadStatus != null)
        {
            params.putAll(ImmutableMap.of("loadStatus", loadStatus));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<StockRpt> stockRptList = stockRptService.retrieve(params);
        Integer count = stockRptService.count(params);
        return ResponseEntity.ok(ListRep.assemble(stockRptList, count));
    }

    @RequestMapping(value = "/api/Report/StockInOut", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取门店库存")
    public ResponseEntity retrieveStockInRpt(@RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
                                             @RequestParam(value = "loadStatus", required = false) Integer loadStatus,
                                             @RequestParam(value = "stockType", required = false) Integer stockType,
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

        if (startTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("startTime", startTime));
        }

        if (endTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("endTime", endTime));
        }

        if (loadStatus != null)
    {
        params.putAll(ImmutableMap.of("loadStatus", loadStatus));
    }

        if (stockType != null)
        {
            params.putAll(ImmutableMap.of("stockType", stockType));
        }


        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<StockRpt> stockRptList = stockInOutRptService.retrieve(params);
        Integer count = stockInOutRptService.count(params);
        return ResponseEntity.ok(ListRep.assemble(stockRptList, count));
    }
}
