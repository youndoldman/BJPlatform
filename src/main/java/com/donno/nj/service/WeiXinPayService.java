package com.donno.nj.service;

import com.donno.nj.activiti.WorkFlowTypes;
import com.donno.nj.domain.Process;
import com.donno.nj.domain.Task;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface WeiXinPayService
{
    //获取用户的OpenID
    String getOpenId(String userCode)throws IOException;


    //小程序 统一下单接口,返回wx.requestPayment接口所需的5个参数
    //timeStamp、nonceStr、package（prepay_id）、signType、paySign
    Map<String, String> doUnifiedOrderForMicroApp(String openId, String orderIndex, String totalFee, String remoteIpAddress)throws IOException;


    //扫码支付 统一下单接口,返回code_url
    String doUnifiedOrderForScan(String orderIndex, String totalFee)throws IOException;

    //退款接口
    boolean doRefund(String out_trade_no, String total_fee);
}
