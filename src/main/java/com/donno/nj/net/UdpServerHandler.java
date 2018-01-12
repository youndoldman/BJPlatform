package com.donno.nj.net;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.json.JSONObject;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2016/1/21 0021.
 */
public class UdpServerHandler
        extends SimpleChannelInboundHandler<DatagramPacket>
{

    @Override
    public void messageReceived(ChannelHandlerContext channelHandlerContext,
                                DatagramPacket datagramPacket) throws Exception
    {
        String revMessage = datagramPacket.content().toString(CharsetUtil.UTF_8);

        JSONObject json = new JSONObject(revMessage);
        Integer openid = json.getInt("id");
        System.out.println(openid);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)throws Exception{
        ctx.close();
        cause.printStackTrace();
    }
}