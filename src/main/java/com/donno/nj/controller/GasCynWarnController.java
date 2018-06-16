package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.GasCylinder;
import com.donno.nj.domain.GasCylinderSvcStatusOpHis;
import com.donno.nj.domain.GasCynForceTakeOverWarn;
import com.donno.nj.logger.DebugLogger;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GasCylinderService;
import com.donno.nj.service.GasCylinderSvcOpHisService;
import com.donno.nj.service.GasCylinderWarnService;
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
public class GasCynWarnController
{
    @Autowired
    private GasCylinderWarnService gasCylinderWarnService;


    @RequestMapping(value = "/api/GasCylinderWarn", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取钢瓶告警信息列表")
    public ResponseEntity retrieve(@RequestParam(value = "gasNumber", defaultValue = "") String gasNumber,
                                   @RequestParam(value = "srcUserId", defaultValue = "") String srcUserId,
                                   @RequestParam(value = "gasCynWarnStatus", required = false) Integer gasCynWarnStatus,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (gasNumber.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("gasNumber", gasNumber));
        }

        if (srcUserId.trim().length() != 0)
        {
            params.putAll(ImmutableMap.of("srcUserId", srcUserId));
        }

        if (gasCynWarnStatus != null)
        {
            params.putAll(ImmutableMap.of("gasCynWarnStatus", gasCynWarnStatus));
        }


        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<GasCynForceTakeOverWarn> gasCynForceTakeOverWarnList = gasCylinderWarnService.retrieve(params);
        Integer count = gasCylinderWarnService.count(params);


        return ResponseEntity.ok(ListRep.assemble(gasCynForceTakeOverWarnList, count));
    }


    @OperationLog(desc = "修改钢瓶告警状态信息")
    @RequestMapping(value = "/api/GasCylinderWarn/{id}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("id") Integer id, @RequestBody GasCynForceTakeOverWarn newGasCynForceTakeOverWarn)
    {
        ResponseEntity responseEntity;
        gasCylinderWarnService.update(id,newGasCynForceTakeOverWarn);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

}
