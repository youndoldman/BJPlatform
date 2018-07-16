package com.donno.nj.controller;

import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.dao.GasCynTrayDao;
import com.donno.nj.domain.AdjustPriceHistory;
import com.donno.nj.domain.GasCynTray;
import com.donno.nj.domain.Goods;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GasCynTrayService;
import com.donno.nj.service.GasCynTrayTSService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class GasCynTrayController
{
    @Autowired
    private GasCynTrayService gasCynTrayService;

    @Autowired
    private GasCynTrayTSService gasCynTrayTSService;

    @RequestMapping(value = "/api/GasCynTray/Init", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "初始化")
    public ResponseEntity init()
    {
       // gasCynTrayTSService.deleteTable();
        gasCynTrayTSService.createTable();

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/api/GasCynTray", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取托盘列表")
    public ResponseEntity retrieve(
                                   @RequestParam(value = "number", defaultValue = "") String number,
                                   @RequestParam(value = "userId", defaultValue = "") String userId,
                                   @RequestParam(value = "warningStatus", required = false) Integer warningStatus,
                                   @RequestParam(value = "leakStatus", required = false) Integer leakStatus,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (number.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("number", number));
        }

        if (userId.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("userId", userId));
        }

        if (warningStatus != null)
        {
            params.putAll(ImmutableMap.of("warningStatus", warningStatus));
        }

        if (leakStatus != null)
        {
            params.putAll(ImmutableMap.of("leakStatus", leakStatus));
        }

        params.putAll(paginationParams(pageNo, pageSize, orderBy));

        List<GasCynTray> gasCynTrays = gasCynTrayService.retrieve(params);
        Integer count = gasCynTrayService.count(params);
        return ResponseEntity.ok(ListRep.assemble(gasCynTrays, count));
    }

    @RequestMapping(value = "/api/GasCynTray/History", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "获取托盘历史数据列表")
    public ResponseEntity retrieveHis(
            @RequestParam(value = "number", defaultValue = "") String number,
            @RequestParam(value = "startTime", defaultValue = "") String startTime,
            @RequestParam(value = "endTime", defaultValue = "") String endTime,
            @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
            @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {

        if (number.trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少托盘编码信息！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (startTime.trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少查询起始时间！", HttpStatus.NOT_ACCEPTABLE);
        }

        if (endTime.trim().length() == 0)
        {
            throw new ServerSideBusinessException("缺少查询结束时间！", HttpStatus.NOT_ACCEPTABLE);
        }

        List<GasCynTray> gasCynTrays = gasCynTrayTSService.getRange(number,startTime,endTime);
        return ResponseEntity.ok(ListRep.assemble(gasCynTrays, gasCynTrays.size()));
    }

    @OperationLog(desc = "创建托盘")
    @RequestMapping(value = "/api/GasCynTray", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody GasCynTray gasCynTray, UriComponentsBuilder ucBuilder)
    {
        ResponseEntity responseEntity;

        gasCynTrayService.create(gasCynTray);

        URI uri = ucBuilder.path("/api/GasCynTray/{number}").buildAndExpand(gasCynTray.getNumber()).toUri();
        responseEntity = ResponseEntity.created(uri).build();

        return responseEntity;
    }

    @OperationLog(desc = "修改托盘信息")
    @RequestMapping(value = "/api/GasCynTray/{number}", method = RequestMethod.PUT)
    public ResponseEntity update(@PathVariable("number") String number, @RequestBody GasCynTray newGasCynTray)
    {
        ResponseEntity responseEntity;

        gasCynTrayService.update(number,newGasCynTray);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

    @OperationLog(desc = "删除托盘信息")
    @RequestMapping(value = "/api/GasCynTray/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Integer id)
    {
        ResponseEntity responseEntity;

        gasCynTrayService.deleteById(id);
        responseEntity = ResponseEntity.noContent().build();

        return responseEntity;
    }



    @OperationLog(desc = "托盘绑定到客户")
    @RequestMapping(value = "/api/GasCynTray/Bind/{number}", method = RequestMethod.PUT)
    public ResponseEntity bind(@PathVariable("number") String number,
                                          @RequestParam(value = "userId", defaultValue = "") String userId)
    {
        ResponseEntity responseEntity;

        gasCynTrayService.bind(number,userId);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }


    @OperationLog(desc = "托盘解除绑定")
    @RequestMapping(value = "/api/GasCynTray/unBind/{number}", method = RequestMethod.PUT)
    public ResponseEntity unBind(@PathVariable("number") String number,
                                            @RequestParam(value = "userId", defaultValue = "") String userId)
    {
        ResponseEntity responseEntity;
        gasCynTrayService.unBind(number,userId);
        responseEntity = ResponseEntity.ok().build();

        return responseEntity;
    }

}
