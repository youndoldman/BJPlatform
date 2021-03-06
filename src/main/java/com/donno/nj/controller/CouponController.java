package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.Coupon;
import com.donno.nj.domain.ServerConstantValue;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.CouponService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class CouponController
{
    @Autowired
    CouponService couponService ;

    @RequestMapping(value = "/api/Coupon", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取优惠券信息列表")
    //@Auth(allowedBizOp = { })
    public ResponseEntity retrieve(@RequestParam(value = "couponSn", defaultValue = "") String couponSn,
                                   @RequestParam(value = "customerUserId", defaultValue = "") String customerUserId,
                                   @RequestParam(value = "operatorUserId", defaultValue = "") String operatorUserId,
                                   @RequestParam(value = "saleStartTime", defaultValue = "") String saleStartTime,
                                   @RequestParam(value = "saleEndTime", defaultValue = "") String saleEndTime,
                                   @RequestParam(value = "specCode", defaultValue = "") String specCode,
                                   @RequestParam(value = "useStatus", required = false) Integer useStatus,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (couponSn.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("couponSn", couponSn));
        }

        if (customerUserId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("customerUserId", customerUserId));
        }

        if (operatorUserId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("operatorUserId", operatorUserId));
        }

        if (specCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("specCode", specCode));
        }

        if (useStatus != null)
        {
            params.putAll(ImmutableMap.of("useStatus", useStatus));
        }

        if (saleStartTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("saleStartTime", saleStartTime));
        }

        if (saleEndTime.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("saleEndTime", saleEndTime));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<Coupon> coupons = couponService.retrieve(params);
        Integer count = couponService.count(params);

        return ResponseEntity.ok(ListRep.assemble(coupons, count));
    }


    @OperationLog(desc = "增加优惠券信息")
    @RequestMapping(value = "/api/Coupon", method = RequestMethod.POST)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN ,ServerConstantValue.GP_CUSTOMER_SERVICE })
    public ResponseEntity create(@RequestBody Coupon coupon,
                                 @RequestParam(value = "couponCount", required = true) Integer couponCount,
                                 UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        /*创建*/
        couponService.create(coupon,couponCount);

        URI uri = ucBuilder.path("/api/Coupon/{id}").buildAndExpand(coupon.getId()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }


    @OperationLog(desc = "修改优惠券信息")
    @RequestMapping(value = "/api/Coupon/{id}", method = RequestMethod.PUT)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN ,ServerConstantValue.GP_CUSTOMER_SERVICE })
    public ResponseEntity update(@PathVariable("id") Integer id,
                                 @RequestBody Coupon newCoupon)
    {
        ResponseEntity responseEntity;

        couponService.update(id, newCoupon);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }


    @OperationLog(desc = "删除优惠券信息")
    @RequestMapping(value = "/api/Coupon/{id}", method = RequestMethod.DELETE)
    @Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN ,ServerConstantValue.GP_CUSTOMER_SERVICE })
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        couponService.delete(id);

        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }
}
