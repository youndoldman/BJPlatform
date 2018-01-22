package com.donno.nj.controller;

import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.service.WeiXinPayService;
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

import com.donno.nj.service.OrderService;


@RestController
public class PayController {

    @Autowired
    WeiXinPayService weiXinPayService;

    @Autowired
    OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(PayController.class);


    @RequestMapping(value = "/api/pay/QRCode", method = RequestMethod.GET, produces = "application/json")
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

    @RequestMapping(value = "/api/pay/microApp", method = RequestMethod.GET)
        public ResponseEntity testQRCode(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "totalFee") String totalFree,
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

    @RequestMapping(value = "/api/pay/scan", method = RequestMethod.GET)
    public ResponseEntity testQRCode(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "totalFee") String totalFee,
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

    @RequestMapping(value = "/api/pay/notify", method = RequestMethod.POST, produces = "application/xml")
    public ResponseEntity testPayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");

        try {
            InputStream inputStream = request.getInputStream();

            byte[] bytes = new byte[0];
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            String notifyData = new String(bytes);// 支付结果通知的xml格式数据
            Map<String, String> result = weiXinPayService.payNotify(notifyData);
            if(result!=null){
                //支付结果通知为成功TODO
                String out_trade_no = result.get("out_trade_no");
                String total_fee = result.get("total_fee");
                String original_no = "";
                String temps[] = out_trade_no.split("x");
                if (temps.length<2){
                    //错误订单格式
                }else {
                    original_no = temps[0];
                    orderService.weixinPayOk(original_no, out_trade_no, Integer.parseInt(total_fee));
                }



            }else{

            }
            String claimStr ="<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
            response.getOutputStream().write(claimStr.getBytes(), 0, claimStr.length());


            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    @RequestMapping(value = "/api/pay/refund", method = RequestMethod.GET)
    public ResponseEntity testQRCode(HttpServletRequest request, @RequestParam(value = "totalFee") String totalFee,
                                     @RequestParam(value = "outTradeNo") String outTradeNo) throws IOException {
        try {
            boolean result = weiXinPayService.doRefund(outTradeNo, totalFee);
            if (result){
                return ResponseEntity.status(HttpStatus.OK).build();
            }else {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }




}