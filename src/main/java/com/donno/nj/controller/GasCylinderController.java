package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.GasCylinder;
import com.donno.nj.domain.GasCylinderSvcStatusOpHis;
import com.donno.nj.domain.ServerConstantValue;
import com.donno.nj.logger.DebugLogger;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GasCylinderService;
import com.donno.nj.service.GasCylinderSvcOpHisService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class GasCylinderController
{
    @Autowired
    private GasCylinderService gasCylinderService;

    @Autowired
    private GasCylinderSvcOpHisService gasCylinderSvcOpHisService;

    @RequestMapping(value = "/api/GasCylinder", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取钢瓶列表")
    //@Auth(allowedBizOp = {})
    public ResponseEntity retrieve(@RequestParam(value = "number", defaultValue = "") String number,
                                   @RequestParam(value = "factoryCode", defaultValue = "") String factoryCode,
                                   @RequestParam(value = "specCode", defaultValue = "") String specCode,
                                   @RequestParam(value = "lifeStatus", required = false) Integer lifeStatus,
                                   @RequestParam(value = "serviceStatus", required = false) Integer serviceStatus,
                                   @RequestParam(value = "liableUserId", defaultValue = "") String liableUserId,
                                   @RequestParam(value = "liableDepartmentCode", defaultValue = "") String liableDepartmentCode,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        String logInfo = String.format("start query :%s",new Date());
        DebugLogger.log(logInfo);

        Map params = new HashMap<String,String>();

        if (number.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("number", number));
        }

        if (factoryCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("factoryCode", factoryCode));
        }

        if (specCode.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("specCode", specCode));
        }


        if (lifeStatus != null)
        {
            params.putAll(ImmutableMap.of("lifeStatus", lifeStatus));
        }

        if (serviceStatus != null)
        {
            params.putAll(ImmutableMap.of("serviceStatus", serviceStatus));
        }

        if (liableUserId.trim().length() != 0)
        {
            params.putAll(ImmutableMap.of("liableUserId", liableUserId));
        }

        if (liableDepartmentCode.trim().length() != 0)
        {
            params.putAll(ImmutableMap.of("liableDepartmentCode", liableDepartmentCode));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<GasCylinder> gasCylinders = gasCylinderService.retrieve(params);
        Integer count = gasCylinderService.count(params);

        logInfo = String.format("end query :%s",new Date());
        DebugLogger.log(logInfo);

        return ResponseEntity.ok(ListRep.assemble(gasCylinders, count));
    }

    @OperationLog(desc = "创建钢瓶")
    @RequestMapping(value = "/api/GasCylinder", method = RequestMethod.POST)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_GAS_STORE_LEADER })
    public ResponseEntity create(@RequestBody GasCylinder gasCylinder, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        gasCylinderService.create(gasCylinder);

        URI uri = ucBuilder.path("/api/GasCylinder/{number}").buildAndExpand(gasCylinder.getNumber()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }

    @OperationLog(desc = "修改钢瓶信息")
    @RequestMapping(value = "/api/GasCylinder/{number}", method = RequestMethod.PUT)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_GAS_STORE_LEADER })
    public ResponseEntity update(@PathVariable("number") String number, @RequestBody GasCylinder newGasCylinder)
    {
        ResponseEntity responseEntity;
        gasCylinderService.update(number,newGasCylinder);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }


    @OperationLog(desc = "修改钢瓶业务状态")
    @RequestMapping(value = "/api/GasCylinder/TakeOver/{number}", method = RequestMethod.PUT)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_GAS_STORE_LEADER })
    public ResponseEntity updateCynSvcStatus(@PathVariable("number") String number,
                                             @RequestParam(value = "srcUserId", defaultValue = "",required = true) String srcUserId,
                                             @RequestParam(value = "targetUserId", defaultValue = "",required = true) String targetUserId,
                                             @RequestParam(value = "serviceStatus", required = true) Integer serviceStatus,
                                             @RequestParam(value = "enableForce", required = true) Boolean enableForce,
                                             @RequestParam(value = "note", defaultValue = "")  String note)
    {
        ResponseEntity responseEntity;
        gasCylinderService.updateSvcStatus(number,serviceStatus,srcUserId,targetUserId,enableForce,note);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

    @OperationLog(desc = "查询钢瓶业务状态变化历史")
    @RequestMapping(value = "/api/GasCylinder/TakeOver/History/{number}", method = RequestMethod.GET)
    //@Auth(allowedBizOp = {})
    public ResponseEntity getCynSvcStatusOpHis(@PathVariable("number") String number,
                                             @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                             @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                             @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                             @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                             @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        ResponseEntity responseEntity;

        Map params = new HashMap<String,String>();

        if (number.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("number", number));
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

        List<GasCylinderSvcStatusOpHis> gasCylinderSvcStatusOpHises =   gasCylinderSvcOpHisService.retrieve(params);
        Integer count = gasCylinderSvcOpHisService.count(params);

        return ResponseEntity.ok(ListRep.assemble(gasCylinderSvcStatusOpHises, count));
    }

    @OperationLog(desc = "删除钢瓶信息")
    @RequestMapping(value = "/api/GasCylinder/{number}", method = RequestMethod.DELETE)
    //@Auth(allowedBizOp = {ServerConstantValue.GP_ADMIN,ServerConstantValue.GP_GAS_STORE_LEADER })
    public ResponseEntity delete(@PathVariable("number") String number)
    {
        ResponseEntity responseEntity;

        Optional<GasCylinder> gasCylinderOptional = gasCylinderService.findByNumber(number);
        if (gasCylinderOptional.isPresent())
        {
            gasCylinderService.deleteById(gasCylinderOptional.get().getId());
            responseEntity = ResponseEntity.noContent().build();
        }
        else
        {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @OperationLog(desc = "钢瓶绑定定位终端")
    @RequestMapping(value = "/api/GasCylinder/Bind/{gasCylinderNumber}", method = RequestMethod.PUT)
    //@Auth(allowedBizOp = {})
    public ResponseEntity bindLocationDev(@PathVariable("gasCylinderNumber") String gasCylinderNumber,
                                          @RequestParam(value = "locationDevNumber", defaultValue = "") String locationDevNumber)
    {
        ResponseEntity responseEntity;

        gasCylinderService.bindLocationDev(gasCylinderNumber,locationDevNumber);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }


    @OperationLog(desc = "钢瓶解除绑定定位终端")
    @RequestMapping(value = "/api/GasCylinder/UnBind/{gasCylinderNumber}", method = RequestMethod.PUT)
    //@Auth(allowedBizOp = {})
    public ResponseEntity unBindLocationDev(@PathVariable("gasCylinderNumber") String gasCylinderNumber,
                                            @RequestParam(value = "locationDevNumber", defaultValue = "") String locationDevNumber)
    {
        ResponseEntity responseEntity;
        gasCylinderService.unBindLocationDev(gasCylinderNumber,locationDevNumber);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

}
