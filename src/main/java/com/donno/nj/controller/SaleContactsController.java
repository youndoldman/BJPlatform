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

/*销售往来报表*/

@RestController
public class SaleContactsController
{
//    @Autowired
//    private GasCyrSaleContactsRptService gasCyrSaleContactsRptService;
//
//
//    @RequestMapping(value = "/api/Report/Daily/SaleContacts", method = RequestMethod.GET, produces = "application/json")
//    @OperationLog(desc = "获取门店销售销售往来日报表")
//    public ResponseEntity retrieveDailySaleContactsRpt(@RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
//                                                @RequestParam(value = "date", defaultValue = "") String date,
//                                                @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
//                                                @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
//                                                @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
//    {
//        Map params = new HashMap<String,String>();
//
//        if (departmentCode.trim().length() > 0)
//        {
//            params.putAll(ImmutableMap.of("departmentCode", departmentCode));
//        }
//
//        if (date.trim().length() > 0)
//        {
//            params.putAll(ImmutableMap.of("date", date));
//        }
//
//        params.putAll(paginationParams(pageNo, pageSize, orderBy));
//
//        List<GasCyrSaleContactsRpt> gasCyrSaleContactsRpts = gasCyrSaleContactsRptService.retrieveDailyRpt(params);
//        Integer count = gasCyrSaleContactsRptService.countDailyRpt(params);
//        return ResponseEntity.ok(ListRep.assemble(gasCyrSaleContactsRpts, count));
//    }
//
//    @OperationLog(desc = "增加门店销售销售往来日报表信息")
//    @RequestMapping(value = "/api/Report/Daily/SaleContacts", method = RequestMethod.POST)
//    public ResponseEntity createSaleContacts(@RequestBody GasCyrSaleContactsRpt gasCyrSaleContactsRpt, UriComponentsBuilder ucBuilder)
//    {
//        ResponseEntity responseEntity;
//
//        gasCyrSaleContactsRptService.create(gasCyrSaleContactsRpt);
//        URI uri = ucBuilder.path("/api/Report/Daily/SaleContacts/{id}").buildAndExpand(gasCyrSaleContactsRpt.getId()).toUri();
//        responseEntity = ResponseEntity.created(uri).build();
//
//        return responseEntity;
//    }
//
//    @OperationLog(desc = "修改门店销售销售往来日报表信息")
//    @RequestMapping(value = "/api/Report/Daily/SaleContacts/{id}", method = RequestMethod.PUT)
//    public ResponseEntity updateSaleContacts(@PathVariable("id") Integer id, @RequestBody GasCyrSaleContactsRpt gasCyrSaleContactsRpt)
//    {
//        ResponseEntity responseEntity;
//
//        gasCyrSaleContactsRptService.update(id,gasCyrSaleContactsRpt);
//        responseEntity = ResponseEntity.ok().build();
//
//        return responseEntity;
//    }
//
//
//    @OperationLog(desc = "删除门店销售销售往来日报表信息")
//    @RequestMapping(value = "/api/Report/Daily/SaleContacts/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity deleteSaleContacts(@PathVariable("id") Integer id)
//    {
//        ResponseEntity responseEntity;
//
//        Optional<GasCyrSaleContactsRpt> gasCyrSaleContactsRptOptional = gasCyrSaleContactsRptService.findById(id);
//        if (gasCyrSaleContactsRptOptional.isPresent())
//        {
//            gasCyrSaleContactsRptService.deleteById(gasCyrSaleContactsRptOptional.get().getId());
//            responseEntity = ResponseEntity.noContent().build();
//        }
//        else
//        {
//            responseEntity = ResponseEntity.notFound().build();
//        }
//
//        return responseEntity;
//    }


}