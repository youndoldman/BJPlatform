package com.donno.nj.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.donno.nj.activiti.WorkFlowTypes;
import com.donno.nj.domain.Process;
import com.donno.nj.domain.Task;

import java.util.List;
import java.util.Map;


public interface SmsService {
    //发送短信
    SendSmsResponse sendGasLeakSms(String phoneNumber, String userName, String address) throws ClientException;

    SendSmsResponse sendDispatchOk(String phoneNumber, String userName, String  orderSn,Float amount) throws ClientException;

    SendSmsResponse sendGasLeakSmsToFireDepartment(String phoneNumber, String userName, String address, String userPhone) throws ClientException;

    SendBatchSmsResponse sendBatchSmsCommon(List<String> phoneNumberList, String text) throws ClientException;



}