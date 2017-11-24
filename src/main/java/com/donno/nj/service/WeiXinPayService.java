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

    //统一下单接口,获取PrepayId
    String getPrepayId(String openId)throws IOException;

    //生成签名
    String getSign(String prepayId)throws IOException;
}
