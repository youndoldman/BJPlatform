package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.dao.UserDao;
import com.donno.nj.domain.PayType;
import com.donno.nj.domain.SalesRpt;
import com.donno.nj.domain.User;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.SalesRptService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

/*销售报表*/

@RestController
public class SaleRptController
{
    @Autowired
    private SalesRptService salesRptService;


    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/api/Report/Sales/ByPayType", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取门店销售报表（按支付方式）")
    public ResponseEntity retrieveByPayType(@RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
                                            @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                            @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                            @RequestParam(value = "payStatus", required = false) Integer payStatus,
                                            @RequestParam(value = "payType", required = false) Integer payType,
                                            @RequestParam(value = "dispatchUserId", defaultValue = "") String dispatchUserId,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                            @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (departmentCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("departmentCode", departmentCode));
        }

        if (dispatchUserId.trim().length() > 0)
        {
            User user =userDao.findByUserId(dispatchUserId);
            if (user == null)
            {
                throw new ServerSideBusinessException("配送工信息不存在！", HttpStatus.NOT_ACCEPTABLE);
            }

            Integer dispatchUserIdx = user.getId();
            params.putAll(ImmutableMap.of("dispatchUserIdx", dispatchUserIdx));
        }


        if (startTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("startTime", startTime));
        }

        if (endTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("endTime", endTime));
        }

        if (payStatus != null)
        {
            params.putAll(ImmutableMap.of("payStatus", payStatus));
        }

        if (payType != null)
        {
            if (payType >= PayType.PTOnLine.getIndex() && payType <= PayType.PTCoupon.getIndex() )
            {
                params.putAll(ImmutableMap.of("payType", payType));
            }
            else
            {
                throw new ServerSideBusinessException("支付类型错误", HttpStatus.NOT_ACCEPTABLE);
            }
        }


        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<SalesRpt> salesRptList = salesRptService.retrieveSaleRptByPayType(params);
        Integer count = salesRptService.countSaleRptByPayType(params);
        return ResponseEntity.ok(ListRep.assemble(salesRptList, count));
    }



    @RequestMapping(value = "/api/Report/Sales/ByCustomerType", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取门店销售报表(按用户类型)")
    public ResponseEntity retrieveByCstType(@RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
                                            @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                            @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                            @RequestParam(value = "cstTypeCode", defaultValue = "") String cstTypeCode,
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

        if (cstTypeCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("cstTypeCode", cstTypeCode));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<SalesRpt> salesRptList = salesRptService.retrieveSaleRptByCstType(params);
        Integer count = salesRptService.countSaleRptByCstType(params);
        return ResponseEntity.ok(ListRep.assemble(salesRptList, count));
    }
}
