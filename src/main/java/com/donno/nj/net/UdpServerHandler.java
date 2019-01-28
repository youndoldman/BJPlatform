package com.donno.nj.net;

import com.donno.nj.dao.GasCynTrayDao;
import com.donno.nj.dao.SystemParamDao;

import com.donno.nj.domain.Customer;
import com.donno.nj.domain.GasCynTray;
import com.donno.nj.domain.WarnningStatus;

import com.donno.nj.service.CustomerService;
import com.donno.nj.service.GasCynTrayService;
import com.donno.nj.service.SmsService;
import com.donno.nj.service.GasCynTrayTSService;
import com.google.common.base.Optional;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.donno.nj.domain.User;

@Component
public class UdpServerHandler
        extends SimpleChannelInboundHandler<DatagramPacket>
{

    @Autowired
    private GasCynTrayDao  gasCynTrayDao;


    @Autowired
    private GasCynTrayTSService trayService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private CustomerService customerService;


    @Autowired
    private SystemParamDao systemParamDao;

    @Override
    public void messageReceived(ChannelHandlerContext channelHandlerContext,
                                DatagramPacket datagramPacket) throws Exception
    {
        String revMessage = datagramPacket.content().toString(CharsetUtil.UTF_8);

        try
        {
            JSONObject json = new JSONObject(revMessage);

            /*接收数据解析*/
            GasCynTray gasCynTray = new GasCynTray();
            gasCynTray.setNumber(json.getString("code"));
            gasCynTray.setWeight((float)json.getDouble("weight"));
            gasCynTray.setLeakStatus(WarnningStatus.values()[(int)json.getDouble("leak")]);
            gasCynTray.setLongitude( json.getDouble("lon"));
            gasCynTray.setLatitude( json.getDouble("lat"));
            //gasCynTray.setTimestamp( json.getString("timestamp"));
            //忽略托盘时戳，引用本地时戳
            Date curDate = new Date();
            String dateFmt =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(curDate);
            gasCynTray.setTimestamp(dateFmt);


            GasCynTray target =  gasCynTrayDao.findByNumber(gasCynTray.getNumber());
            if ( target == null)
            {
                //to do ...
            }
            else
            {
                //无效经纬度
                if((gasCynTray.getLatitude()<0.00001)||(gasCynTray.getLongitude()<0.00001)){
                    gasCynTray.setLatitude(target.getLatitude());
                    gasCynTray.setLongitude(target.getLongitude());
                }
                gasCynTray.setId(target.getId());
                gasCynTray.setValidWeight(gasCynTray.getWeight()+target.getCalibration());
                Integer warningWeight = systemParamDao.getTrayWarningWeight();
                if (gasCynTray.getValidWeight() <= warningWeight)
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
            //如果发现漏气，立刻短信提醒用户
            if(target!=null&&gasCynTray.getLeakStatus()!=WarnningStatus.WSNormal){

                User validUser = target.getUser();
                if(validUser!=null){
                    Optional<Customer> customerOptional = customerService.findByCstUserId(validUser.getUserId());
                    if(customerOptional.isPresent()){
                        String address = customerOptional.get().getAddress().getProvince()+customerOptional.get().getAddress().getCity()+customerOptional.get().getAddress().getCounty()
                                +customerOptional.get().getAddress().getDetail();
                        if(gasCynTray.getLeakStatus()==WarnningStatus.WSWarnning1){
                            smsService.sendGasLeakSms(customerOptional.get().getPhone(), customerOptional.get().getName(), address);
                        }
                        if(gasCynTray.getLeakStatus()==WarnningStatus.WSWarnning2){
                            smsService.sendGasLeakSms(customerOptional.get().getPhone(), customerOptional.get().getName(), address);
                            //發送消防部門
                            smsService.sendGasLeakSmsToFireDepartment("13608851223", customerOptional.get().getName(), address,customerOptional.get().getPhone());
                        }

                    }

                }
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)throws Exception{
        ctx.close();
        cause.printStackTrace();
    }
}