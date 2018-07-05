package com.donno.nj.net;

import com.donno.nj.dao.GasCynTrayDao;
import com.donno.nj.dao.SystemParamDao;
import com.donno.nj.domain.GasCynTray;
import com.donno.nj.domain.WarnningStatus;
import com.donno.nj.service.GasCynTrayService;
import com.donno.nj.service.GasCynTrayTSService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UdpServerHandler
        extends SimpleChannelInboundHandler<DatagramPacket>
{

    @Autowired
    private GasCynTrayDao  gasCynTrayDao;


    @Autowired
    private GasCynTrayTSService trayService;

    @Autowired
    private SystemParamDao systemParamDao;

    @Override
    public void messageReceived(ChannelHandlerContext channelHandlerContext,
                                DatagramPacket datagramPacket) throws Exception
    {
        String revMessage = datagramPacket.content().toString(CharsetUtil.UTF_8);

        JSONObject json = new JSONObject(revMessage);

        /*接收数据解析*/
        GasCynTray gasCynTray = new GasCynTray();
        gasCynTray.setNumber(json.getString("code"));
        gasCynTray.setWeight((float)json.getDouble("weight"));
        gasCynTray.setLongitude( json.getDouble("lon"));
        gasCynTray.setLatitude( json.getDouble("lat"));
        gasCynTray.setTimestamp( json.getString("timestamp"));


        GasCynTray target =  gasCynTrayDao.findByNumber(gasCynTray.getNumber());
        if ( target == null)
        {
            //to do ...
        }
        else
        {
            gasCynTray.setId(target.getId());
            Integer warningWeight = systemParamDao.getTrayWarningWeight();
            if (gasCynTray.getWeight() <= warningWeight)
            {
                gasCynTray.setWarnningStatus(WarnningStatus.WSWarnning1);
            }
            else
            {
                gasCynTray.setWarnningStatus(WarnningStatus.WSNormal);
            }

            gasCynTrayDao.update(gasCynTray);
            trayService.putRow(gasCynTray);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)throws Exception{
        ctx.close();
        cause.printStackTrace();
    }
}