package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.dao.UserDao;
import com.donno.nj.domain.*;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.RefoundByWeightRptService;
import com.donno.nj.service.SalesByWeightRptService;
import com.donno.nj.service.SalesRptService;
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

/*按瓶业务销售报表*/

@RestController
public class SaleByWeightRptController
{
    @Autowired
    private SalesByWeightRptService salesByWeightRptService;

    @Autowired
    private RefoundByWeightRptService refoundByWeightRptService;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/api/Report/SaleByWeight/ByPayType", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取门店销售报表（按公斤销售，按支付方式）")
    public ResponseEntity retrieveByWeightPayType(@RequestParam(value = "departmentCode", defaultValue = "") String departmentCode,
                                            @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                            @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                            @RequestParam(value = "payStatus", required = false) PayStatus payStatus,
                                            @RequestParam(value = "payType", required = false) PayType payType,
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
            if (payType.getIndex() >= PayType.PTOnLine.getIndex() && payType.getIndex() <= PayType.PTCoupon.getIndex() )
            {
                params.putAll(ImmutableMap.of("payType", payType));
            }
            else
            {
                throw new ServerSideBusinessException("支付类型错误", HttpStatus.NOT_ACCEPTABLE);
            }
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));
        List<SalesRptByWeight> salesRptList = salesByWeightRptService.retrieveSaleRpt(params);
        List<SalesRptByWeight> refoudRptList = refoundByWeightRptService.retrieveRefoundRpt(params);

        for (SalesRptByWeight salesRptByWeight :salesRptList)
        {
            for (SalesRptByWeight refoundRptByWeight:refoudRptList)
            {
                if (salesRptByWeight.getCustomerTypeCode().equals(refoundRptByWeight.getCustomerTypeCode()))
                {
                    salesRptByWeight.setRefoundWeight(salesRptByWeight.getRefoundWeight() + refoundRptByWeight.getRefoundWeight() );
                    salesRptByWeight.setRefoundSum(salesRptByWeight.getRefoundSum() + refoundRptByWeight.getRefoundSum() );
                }
            }
        }

        return ResponseEntity.ok(ListRep.assemble(salesRptList, salesRptList.size()));
    }



}
