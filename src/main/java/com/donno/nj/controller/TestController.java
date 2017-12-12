package com.donno.nj.controller;

import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.WeiXinPayService;
import com.donno.nj.aspect.OperationLog;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import com.donno.nj.util.QRCodeUtil;

import org.springframework.web.bind.annotation.*;


@RestController
public class TestController {

    @Autowired
    WeiXinPayService weiXinPayService;

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);


    @RequestMapping(value = "/api/test/QRCode/Create", method = RequestMethod.GET)
    public ResponseEntity testQRCode(HttpServletResponse response, @RequestParam(value = "text") String text) throws IOException {
        response.setContentType("image/png;charset=UTF-8");


        InputStream imageStream = QRCodeUtil.zxingCodeCreate(text, 500, 500);

        if (imageStream != null) {
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = imageStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @RequestMapping(value = "/api/test/Pay/MicroApp", method = RequestMethod.GET)
        public ResponseEntity testQRCode(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "totalFree") String totalFree,
                                     @RequestParam(value = "orderIndex") String orderIndex,
                                     @RequestParam(value = "userCode") String userCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        String openId = weiXinPayService.getOpenId(userCode);
        if (openId == null)
        {
            throw new ServerSideBusinessException("openId获取失败！", HttpStatus.NOT_ACCEPTABLE);
        }

        Map<String, String> result = weiXinPayService.doUnifiedOrderForMicroApp(openId, orderIndex, totalFree, request.getRemoteAddr());

        if (result.size()==0)
        {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/api/test/Pay/Scan", method = RequestMethod.GET)
    public ResponseEntity testQRCode(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "totalFree") String totalFee,
                                     @RequestParam(value = "orderIndex") String orderIndex) throws IOException {
        response.setContentType("image/png;charset=UTF-8");

        try {


            String codeUrl = weiXinPayService.doUnifiedOrderForScan(orderIndex, totalFee);

            if (codeUrl == null)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            InputStream imageStream = QRCodeUtil.zxingCodeCreate(codeUrl, 500, 500);

            if (imageStream != null) {
                int len = 0;
                byte[] b = new byte[1024];
                while ((len = imageStream.read(b, 0, 1024)) != -1) {
                    response.getOutputStream().write(b, 0, len);
                }
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }
}
