package com.donno.nj.service;

import com.donno.nj.domain.GasCylinderPosition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\11\21 0021.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/spring/spring-*.xml")
public class TableStoreServiceTest {

    @Autowired
    private TableStoreService tableStoreService;



    //创建表
    @Test
    public void createTable() throws Exception {
        tableStoreService.createTable();

    }

    //删除表
    @Test
    public void deleteTable() throws Exception {
        tableStoreService.deleteTable();

    }




    //查看表
    @Test
    public void listTable() throws Exception {
        tableStoreService.listTable();

    }

    //批量插入数据
    @Test
    public void batchWriteRow() throws Exception {
        List<GasCylinderPosition> locations = new ArrayList<GasCylinderPosition>();
        for (int i=0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                GasCylinderPosition gasCylinderPosition = new GasCylinderPosition(Integer.toString(j), Integer.toString(i),
                        Integer.toString(i * j + j));
                locations.add(gasCylinderPosition);
            }
        }
        tableStoreService.batchWriteRow(locations);
    }

    //插入数据
    @Test
    public void putRow() throws Exception {
        for (int i=0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                GasCylinderPosition gasCylinderPosition = new GasCylinderPosition(Integer.toString(j), Integer.toString(i),
                        Integer.toString(i * j + j));
                tableStoreService.putRow(gasCylinderPosition);
            }
        }

    }

    //查询最新数据
    @Test
    public void getNearestRow() throws Exception {
        GasCylinderPosition gasCylinderPosition = tableStoreService.getNearestRow("0");
        System.out.println(gasCylinderPosition);
    }

    //批量查询数据
    @Test
    public void getRangeByIterator() throws Exception {
        List<GasCylinderPosition> locations = tableStoreService.getRangeByIterator("0", "2", "6");
        System.out.println(locations.toString());
    }

    //批量查询数据
    @Test
    public void getRange() throws Exception {
        List<GasCylinderPosition> locations = tableStoreService.getRange("6", "2", "7");
        System.out.println(locations.toString());
    }


}
