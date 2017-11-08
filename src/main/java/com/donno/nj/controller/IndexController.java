package com.donno.nj.controller;

import com.donno.nj.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    public static final String FIRST_PAGE = "/pages/userCenter.htm";

    @Value("${server.host:''}")
    private String serverHost;

//    @RequestMapping(value = "/api/login")
//    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        response.sendRedirect(String.format("%s%s", serverHost, FIRST_PAGE));
//    }

    @RequestMapping(value = "/api/test")
    public void logint(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(String.format("%s%s", serverHost, FIRST_PAGE));
    }

}
