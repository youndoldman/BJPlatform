package com.donno.nj.service;

import com.donno.nj.service.WeiXinPayService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;


/**
 * Created by Administrator on 2017\11\21 0021.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/spring/spring-*.xml")
public class WeiXinPayServiceTest {

    @Autowired
    private WeiXinPayService weiXinPayService;



    //获取openid
    @Test
    public void testgetOpenId() throws Exception {
        String openId = weiXinPayService.getOpenId("011ReWzj0nkien14dBwj0g5Rzj0ReWzE");
        System.out.println("获取openid： " + openId);

    }

    //获取code_url
    @Test
    public void testdoUnifiedOrderForScan() throws Exception {
        String code_url = weiXinPayService.doUnifiedOrderForScan(
                "111", "2");
        System.out.println("获取code_url： " + code_url);
    }

    //小程序统一下单
    @Test
    public void testdoUnifiedOrderForMicroApp() throws Exception {

    }

}
