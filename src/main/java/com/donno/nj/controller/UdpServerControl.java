package com.donno.nj.controller;

import com.donno.nj.service.impl.UdpServerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.donno.nj.service.UdpServerService;

import java.util.Timer;
import java.util.TimerTask;

@RestController
public class UdpServerControl
{
    @Autowired
    UdpServerService udpServer;

    private static Timer udpSvrStarttimer;

    public UdpServerControl() throws Exception
    {
        if (udpServer != null)
        {
            new Thread(new UdpServerServiceImpl() ).start();
        }

        udpSvrStarttimer = new Timer();
        udpSvrStarttimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if (udpServer != null)
                {
                    new Thread(udpServer).start();

                    /*d定时器终止*/
                    this.cancel();
                }
            }
        }, 0, 5000);
    }

}
