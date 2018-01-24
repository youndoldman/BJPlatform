package com.donno.nj.service;

import com.donno.nj.net.UdpServerHandler;
import com.donno.nj.util.ConfigUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional

public interface UdpServerService extends Runnable
{

    void run(int port) throws Exception;

}