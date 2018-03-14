package com.donno.nj.service;

import com.donno.nj.domain.GasCylinderPosition;
import java.util.List;



public interface TableStoreService
{

    void createTable();
    void updateTable();
    void describeTable();
    void deleteTable();
    void listTable();
    //查询最新的钢瓶位置数据 code－钢瓶码
    GasCylinderPosition getNearestRow(String code);

    //插入单条钢瓶位置数据
    void putRow(GasCylinderPosition gasCylinderPosition);

    //批量插入单条钢瓶位置数据
    void batchWriteRow(List<GasCylinderPosition> gasCylinderPositionList);

    //迭代器查询方法 code－钢瓶码 startTime-开始时间 endTime-结束时间
    List<GasCylinderPosition>    getRangeByIterator(java.lang.String code, java.lang.String startTime, java.lang.String endTime);

    //普通查询方法 code－钢瓶码 startTime-开始时间 endTime-结束时间
    List<GasCylinderPosition>  getRange(String code, String startTime, String endTime);


}
