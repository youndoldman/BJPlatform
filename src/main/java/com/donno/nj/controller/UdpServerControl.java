package com.donno.nj.controller;

import com.donno.nj.net.UdpServer;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UdpServerControl
{
    private static UdpServer udpServer;

    public UdpServerControl() throws Exception
    {
        udpServer = new UdpServer();
        udpServer.run(8090);
    }

}
