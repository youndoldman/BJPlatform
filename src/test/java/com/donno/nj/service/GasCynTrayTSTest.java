package com.donno.nj.service;

import com.donno.nj.domain.GasCylinderPosition;
import com.donno.nj.domain.GasCynTray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/spring/spring-*.xml")
public class GasCynTrayTSTest
{
    @Autowired
    private GasCynTrayTSService gasCynTrayTSService;

    //创建表
    @Test
    public void createTable() throws Exception {
        gasCynTrayTSService.createTable();
    }

    //删除表
    @Test
    public void deleteTable() throws Exception {
        gasCynTrayTSService.deleteTable();

    }



    //插入数据
    @Test
    public void putRow() throws Exception
    {
        GasCynTray gasCynTray = new GasCynTray();
        for (int i = 0;i < 10;i++)
        {
            gasCynTray.setNumber( String.valueOf(i));
            gasCynTray.setWeight(10.0f);
            gasCynTray.setTimestamp("2");
            gasCynTray.setLongitude(1.0);
            gasCynTray.setLatitude(1.0);

            gasCynTrayTSService.putRow(gasCynTray);
        }

    }



    //批量查询数据
    @Test
    public void getRange() throws Exception {
        List<GasCynTray> gasCynTrays = gasCynTrayTSService.getRange("1", "1", "4");
        System.out.println(gasCynTrays.toString());
    }


}
