package com.donno.nj.service.impl;

import com.donno.nj.dao.GasCynTrayDao;
import com.donno.nj.domain.GasCynTray;
import com.donno.nj.net.UdpServerHandler;
import com.donno.nj.service.UdpServerService;
import com.donno.nj.util.ConfigUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UdpServerServiceImpl implements UdpServerService
{
    @Autowired
    private ConfigUtil configUtil;

    @Autowired
    private GasCynTrayDao gasCynTrayDao;

    @Autowired
    private UdpServerHandler udpServerHandler;


    @Override
    public void run()
    {
        try
        {
            while (configUtil == null || gasCynTrayDao == null || udpServerHandler == null)
            {
                Thread.sleep(10000);
            }

            run(configUtil.getRecvPositionPort());
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void run(int port) throws Exception
    {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(udpServerHandler);

            b.bind(port).sync().channel().closeFuture().await();
        }
        finally {
            group.shutdownGracefully();
        }
    }
}
