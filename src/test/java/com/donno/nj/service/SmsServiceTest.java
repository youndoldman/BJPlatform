package com.donno.nj.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;
/**
 * Created by Administrator on 2017\11\21 0021.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/spring/spring-*.xml")
public class SmsServiceTest {

    @Autowired
    private SmsService smsService;



    //发送短信
    @Test
    public void testSendSms() throws Exception {
        SendSmsResponse sendSmsResponse= smsService.sendGasLeakSms("13913015340","周周", "善水湾");
        System.out.println("发送短信:"+sendSmsResponse.getMessage());

    }



}
