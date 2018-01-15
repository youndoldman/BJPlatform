package com.donno.nj.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations="classpath:config.properties")
@Component
public class ConfigUtil
{
    @Value("${recvPosition.port}")
    private Integer recvPositionPort;

    public Integer getRecvPositionPort()
    {
        return recvPositionPort;
    }

    public void setRecvPositionPort(Integer recvPositionPort)
    {
        this.recvPositionPort = recvPositionPort;
    }
}
