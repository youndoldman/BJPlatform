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
        String openId = weiXinPayService.getOpenId("071V1pxP1p4IK31k1sxP1GqsxP1V1pxm");
        System.out.println("获取openid： " + openId);

    }




}
