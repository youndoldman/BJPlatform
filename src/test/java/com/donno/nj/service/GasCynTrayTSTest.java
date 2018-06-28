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


        for (int i = 1;i < 101;i++)
        {
            for (int k = 1;k<31;k++)
            {
                for(int j = 1 ; j < 4 ;j++)
                {
                    gasCynTray.setNumber( String.valueOf(i));
                    gasCynTray.setWeight( (4 - j)*1.0f );

                    String strTime;
                    if (j == 1)
                    {
                        strTime = String.format("2018-06-%d 06:00:00",k);
                    }
                    else if (j == 2)
                    {
                        strTime = String.format("2018-06-%d 12:00:00",k);
                    }
                    else
                    {
                        strTime = String.format("2018-06-%d 18:00:00",k);
                    }

                    gasCynTray.setTimestamp(strTime);
                    gasCynTray.setLongitude(160.0);
                    gasCynTray.setLatitude(23.0);

                    gasCynTrayTSService.putRow(gasCynTray);
                }
            }
        }

    }



    //批量查询数据
    @Test
    public void getRange() throws Exception {
        List<GasCynTray> gasCynTrays = gasCynTrayTSService.getRange("1", "2018-06-3 00:00:00", "2018-06-4 00:00:00");
        System.out.println(gasCynTrays.toString());
    }


}
