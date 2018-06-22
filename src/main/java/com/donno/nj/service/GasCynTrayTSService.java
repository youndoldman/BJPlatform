package com.donno.nj.service;
import com.donno.nj.domain.DeviceStatus;
import com.donno.nj.domain.GasCynTray;

import java.util.List;

/*托盘历史数据table store存储*/
public interface GasCynTrayTSService
{
    void createTable();
    void deleteTable();

    //插入单条数据
    void putRow(GasCynTray gasCynTray);

    //批量插入单条数据
    //void batchWriteRow(List<GasCynTray> gasCynTrayList);

    //普通查询方法 code－托盘编码 startTime-开始时间 endTime-结束时间
    List<GasCynTray>  getRange(String number, String startTime, String endTime);


}
