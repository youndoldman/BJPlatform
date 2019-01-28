package com.donno.nj.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsResponse;
import com.donno.nj.domain.Customer;
import com.donno.nj.domain.Department;
import com.donno.nj.domain.ServerConstantValue;
import com.donno.nj.domain.SysUser;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.representation.ListRep;
import com.donno.nj.service.CustomerService;
import com.donno.nj.service.SmsService;
import com.donno.nj.service.SysUserService;
import com.donno.nj.service.WeiXinPayService;
import com.donno.nj.util.QRCodeUtil;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;


@RestController
public class UntilsController {

    @Autowired
    SmsService smsService;

    @Autowired
    CustomerService customerService;

    private static final Logger logger = LoggerFactory.getLogger(UntilsController.class);


    @RequestMapping(value = "/api/Untils/SendBatchSms", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity SendBatchSms(HttpServletResponse response, @RequestParam(value = "text") String text,
                                     @RequestParam(value = "province") String province,
                                     @RequestParam(value = "city") String city) throws IOException {
        try {


            Map params = new HashMap<String, String>();
            params.putAll(ImmutableMap.of("province", province));
            params.putAll(ImmutableMap.of("city", city));

            List<String> phones = customerService.getPhones(params);


            int remainder = phones.size() % 100;  //(先计算出余数)
            int size = (phones.size() / 100);
            int offset = 0;//偏移量
            int sendTotalCount = 0;
            for (int i = 0; i < size; i++) {
                List<String> subset = null;
                subset = phones.subList(i * 100, (i + 1) * 100);

                SendBatchSmsResponse sendBatchSmsResponse = smsService.sendBatchSmsCommon(subset, text);
                if (sendBatchSmsResponse.getCode() != null && sendBatchSmsResponse.getCode().equals("OK")) {
                    //请求成功
                    sendTotalCount += 100;
                }
            }
            if (remainder > 0) {
                List<String> subset = null;
                subset = phones.subList(size * 100, size * 100 + remainder);
                SendBatchSmsResponse sendBatchSmsResponse = smsService.sendBatchSmsCommon(subset, text);
                if (sendBatchSmsResponse.getCode() != null && sendBatchSmsResponse.getCode().equals("OK")) {
                    //请求成功
                    sendTotalCount += subset.size();
                }
            }
            return ResponseEntity.ok(sendTotalCount);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }





}
