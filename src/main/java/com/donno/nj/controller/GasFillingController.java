package com.donno.nj.controller;

import com.donno.nj.aspect.Auth;
import com.donno.nj.aspect.OperationLog;
import com.donno.nj.constant.Constant;
import com.donno.nj.domain.GasFilling;
import com.donno.nj.domain.Ticket;
import com.donno.nj.domain.WarnningStatus;
import com.donno.nj.logger.BusinessLogger;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.GasFillingService;
import com.google.common.collect.ImmutableMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.donno.nj.util.ParamMapBuilder.paginationParams;

@RestController
public class GasFillingController
{
    @Autowired
    GasFillingService gasFillingService ;

    @RequestMapping(value = "/api/GasFillingMerge", method = RequestMethod.GET, produces = "application/json")
    @OperationLog(desc = "查询充装信息列表")
    public ResponseEntity retrieve(@RequestParam(value = "stationNumber", defaultValue = "") String stationNumber,
                                   @RequestParam(value = "machineNumber", defaultValue = "") String machineNumber,
                                   @RequestParam(value = "cynNumber", defaultValue = "") String cynNumber,
                                   @RequestParam(value = "warningStatus", required = false) WarnningStatus warningStatus,
                                   @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                   @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                   @RequestParam(value = "pageSize", defaultValue = Constant.PAGE_SIZE) Integer pageSize,
                                   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo)
    {
        Map params = new HashMap<String,String>();

        if (stationNumber.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("stationNumber", stationNumber));
        }

        if (machineNumber.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("machineNumber", machineNumber));
        }

        if (cynNumber.trim().length() > 0)
        {
            params.putAll(ImmutableMap.of("cynNumber", cynNumber));
        }

        if (warningStatus != null)
        {
            params.putAll(ImmutableMap.of("warningStatus", warningStatus));
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

        List<GasFilling> gasFillingList = gasFillingService.retrieve(params);
        Integer count = gasFillingService.count(params);

        return ResponseEntity.ok(ListRep.assemble(gasFillingList, count));
    }


    @OperationLog(desc = "充装结束指示信息")
    @RequestMapping(value = "/api/GasFilling/Merge/{cynNumber}", method = RequestMethod.POST)
    public ResponseEntity GasFillingMerge(@PathVariable("cynNumber") String cynNumber,
                                         @RequestParam(value = "stationNumber", defaultValue = "") String stationNumber,
                                         @RequestParam(value = "machineNumbr", required = true) Integer machineNumbr)
    {
        ResponseEntity responseEntity;

        /*创建*/
        GasFilling gasFilling = gasFillingService.merge(stationNumber,machineNumbr,cynNumber);

        responseEntity = ResponseEntity.ok(gasFilling);

        return responseEntity;
    }

    @OperationLog(desc = "充装信息")
    @RequestMapping(value = "/api/GasFilling", method = RequestMethod.POST)
    public ResponseEntity GasFillingData(@RequestParam(value = "OrgCode", defaultValue = "") String OrgCode,
                                         @RequestParam(value = "Action", defaultValue = "") String Action,
                                         @RequestParam(value = "json", defaultValue = "") String json
    )
    {
        ResponseEntity responseEntity;

        try {
            JSONArray jsonArray = new JSONArray(json);
            responseEntity = ResponseEntity.ok("success");

            for (int i=0;i<jsonArray.length();i++)
            {
                GasFilling gasFilling = new GasFilling();
                gasFilling.setStationNumber(OrgCode);
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                gasFilling.setStationNumber(OrgCode);

                gasFilling.setMachineNumber(jsonObject.getInt("JH"));
                gasFilling.setClientCode(jsonObject.getInt("KHDM"));
                gasFilling.setUserId(Integer.toString(jsonObject.getInt("GH")));
                gasFilling.setStartTime(jsonObject.getString("KSGZSJ"));

                gasFilling.setUseTime((float)jsonObject.getDouble("GZYS"));
                gasFilling.setFillingType(jsonObject.getString("GZFS"));
                gasFilling.setTargetWeight((float)jsonObject.getDouble("MBL"));
                gasFilling.setTareWeight((float)jsonObject.getDouble("PZ"));
                gasFilling.setRealWeight((float)jsonObject.getDouble("GZL"));
                gasFilling.setDeviation((float)jsonObject.getDouble("WC"));
                gasFilling.setResult(jsonObject.getString("GZCW"));
                gasFilling.setSequence(jsonObject.getInt("BH"));

                if(json.indexOf("GasCylindersID") > 0)
                {
                    String strGasCynNumber = jsonObject.getString("GasCylindersID");
                    gasFilling.setCynNumber(strGasCynNumber);
                }

                gasFillingService.create(gasFilling);
            }
        }
        catch (Exception exception)
        {
            String message = exception.getMessage();
            responseEntity = ResponseEntity.ok("failure");
        }

        return responseEntity;
    }
}
