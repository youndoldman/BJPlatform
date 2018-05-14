package com.donno.nj.controller;

import com.donno.nj.service.TestService;

import com.donno.nj.service.impl.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Timer;
import java.util.TimerTask;

@RestController
public class TestControl
{
    @Autowired
    TestService testService;

    private static Timer timer;

    public TestControl() throws Exception
    {
//        timer = new Timer();
//        timer.schedule(new TimerTask()
//        {
//            @Override
//            public void run()
//            {
//                if (testService != null)
//                {
//                    new Thread(testService).start();
//
//                    /*d定时器终止*/
//                    this.cancel();
//
//                }
//            }
//        }, 0, 500);
    }

}
