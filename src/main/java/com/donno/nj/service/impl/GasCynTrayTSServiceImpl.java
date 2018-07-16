package com.donno.nj.service.impl;


import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.*;
import com.donno.nj.domain.DeviceStatus;
import com.donno.nj.domain.GasCylinderPosition;
import com.donno.nj.domain.GasCynTray;
import com.donno.nj.domain.WarnningStatus;
import com.donno.nj.service.GasCynTrayTSService;
import com.donno.nj.service.TableStoreService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class GasCynTrayTSServiceImpl implements GasCynTrayTSService
{
    final String endPoint = "https://bjrq.cn-shenzhen.ots.aliyuncs.com";
    final String accessId = "LTAIQCESkaBZRBKo";
    final String accessKey = "0abALsZaVOyAe7aURqeutOHVaQmjzh";
    final String instanceName = "bjrq";

    final String FIELD_ID = "id";
    final String FIELD_NUMBER = "number";
    final String FIELD_WEIGHT = "weight";
    final String FIELD_LEAK = "leak";
    final String FIELD_TIME = "time_stamp";
    final String FIELD_LON = "longitude";
    final String FIELD_LAT = "latitude";

    private static final String TABLE_NAME = "t_gas_cyn_tray_data";

    private SyncClient client;

    private GasCynTrayTSServiceImpl() throws Exception{
        client = new SyncClient(endPoint, accessId, accessKey, instanceName);
    }



    @Override
    public void createTable() {
        TableMeta tableMeta = new TableMeta(TABLE_NAME);
//        tableMeta.addAutoIncrementPrimaryKeyColumn(FIELD_ID);
        tableMeta.addPrimaryKeyColumn(new PrimaryKeySchema(FIELD_NUMBER, PrimaryKeyType.STRING));
        tableMeta.addPrimaryKeyColumn(new PrimaryKeySchema(FIELD_TIME, PrimaryKeyType.STRING));

        int timeToLive = -1; // 数据的过期时间, 单位秒, -1代表永不过期. 假如设置过期时间为一年, 即为 365 * 24 * 3600.
        int maxVersions = 3; // 保存的最大版本数, 设置为3即代表每列上最多保存3个最新的版本.

        TableOptions tableOptions = new TableOptions(timeToLive, maxVersions);

        CreateTableRequest request = new CreateTableRequest(tableMeta, tableOptions);

        client.createTable(request);
    }


    @Override
    public void deleteTable() {
        DeleteTableRequest request = new DeleteTableRequest(TABLE_NAME);
        client.deleteTable(request);
    }



    @Override
    public void putRow(GasCynTray gasCynTray)
    {
        // 构造主键
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(FIELD_NUMBER, PrimaryKeyValue.fromString(gasCynTray.getNumber()));
        primaryKeyBuilder.addPrimaryKeyColumn(FIELD_TIME, PrimaryKeyValue.fromString(gasCynTray.getTimestamp().toString()));
        PrimaryKey primaryKey = primaryKeyBuilder.build();

        RowPutChange rowPutChange = new RowPutChange(TABLE_NAME, primaryKey);

        //加入其他数据列
        rowPutChange.addColumn(new Column(FIELD_WEIGHT, ColumnValue.fromDouble(gasCynTray.getWeight())));
        rowPutChange.addColumn(new Column(FIELD_LEAK, ColumnValue.fromLong(gasCynTray.getLeakStatus().getIndex())));
        rowPutChange.addColumn(new Column(FIELD_LON, ColumnValue.fromDouble(gasCynTray.getLongitude())));
        rowPutChange.addColumn(new Column(FIELD_LAT, ColumnValue.fromDouble(gasCynTray.getLatitude())));



        client.putRow(new PutRowRequest(rowPutChange));
    }


    @Override
    public List<GasCynTray>  getRange(String number, String startTime, String endTime)
    {
        RangeRowQueryCriteria rangeRowQueryCriteria = new RangeRowQueryCriteria(TABLE_NAME);

        // 设置起始主键
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(FIELD_NUMBER, PrimaryKeyValue.fromString(number));
        if (startTime.trim().length() > 0)
        {
            primaryKeyBuilder.addPrimaryKeyColumn(FIELD_TIME, PrimaryKeyValue.fromString(startTime));
        }
        rangeRowQueryCriteria.setInclusiveStartPrimaryKey(primaryKeyBuilder.build());

        // 设置结束主键
        primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(FIELD_NUMBER, PrimaryKeyValue.fromString(number));
        if (endTime.trim().length() > 0)
        {
            primaryKeyBuilder.addPrimaryKeyColumn(FIELD_TIME, PrimaryKeyValue.fromString(endTime));
        }
        rangeRowQueryCriteria.setExclusiveEndPrimaryKey(primaryKeyBuilder.build());

        rangeRowQueryCriteria.setMaxVersions(1);
        rangeRowQueryCriteria.addColumnsToGet(FIELD_WEIGHT);
        rangeRowQueryCriteria.addColumnsToGet(FIELD_LEAK);
        rangeRowQueryCriteria.addColumnsToGet(FIELD_TIME);
        rangeRowQueryCriteria.addColumnsToGet(FIELD_LON);
        rangeRowQueryCriteria.addColumnsToGet(FIELD_LAT);

        List<GasCynTray> gasCynTrays = new ArrayList<GasCynTray>();
        GetRangeResponse getRangeResponse = client.getRange(new GetRangeRequest(rangeRowQueryCriteria));
        for (Row row : getRangeResponse.getRows())
        {
            GasCynTray gasCynTray = new GasCynTray();
            PrimaryKey primaryKey = row.getPrimaryKey();
            gasCynTray.setNumber(primaryKey.getPrimaryKeyColumn(FIELD_NUMBER).getValue().toString());
            gasCynTray.setTimestamp(primaryKey.getPrimaryKeyColumn(FIELD_TIME).getValue().toString());
            gasCynTray.setWeight((float)row.getColumn(FIELD_WEIGHT).get(0).getValue().asDouble());

            int warningStatus = (int)row.getColumn(FIELD_LEAK).get(0).getValue().asLong();
            if ( warningStatus >= WarnningStatus.WSNormal.getIndex() && warningStatus <= WarnningStatus.WSWarnning1.getIndex())
            {
                gasCynTray.setLeakStatus(WarnningStatus.values()[warningStatus]);
            }

            gasCynTray.setLatitude(row.getColumn(FIELD_LAT).get(0).getValue().asDouble());
            gasCynTray.setLongitude(row.getColumn(FIELD_LAT).get(0).getValue().asDouble());
            gasCynTrays.add(gasCynTray);
        }

        return gasCynTrays;
    }

//    @Override
//    public void batchWriteRow(List<GasCylinderPosition> gasCylinderPositionList) {
//        BatchWriteRowRequest batchWriteRowRequest = new BatchWriteRowRequest();
//
//        for (GasCylinderPosition gasCylinderPosition:gasCylinderPositionList){
//            // 构造主键
//            PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
//            primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_CODE, PrimaryKeyValue.fromString(gasCylinderPosition.getCode()));
//            primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_TIME, PrimaryKeyValue.fromString(gasCylinderPosition.getCreateTime().toString()));
//            PrimaryKey primaryKey = primaryKeyBuilder.build();
//            RowPutChange rowPutChange = new RowPutChange(TABLE_NAME, primaryKey);
//            //加入定位的数据列
//            rowPutChange.addColumn(new Column("location", ColumnValue.fromString(gasCylinderPosition.getLocation())));
//            batchWriteRowRequest.addRowChange(rowPutChange);
//        }
//
//        BatchWriteRowResponse response = client.batchWriteRow(batchWriteRowRequest);
//        if (!response.isAllSucceed()) {
//            /**
//             * 可以通过createRequestForRetry方法再构造一个请求对失败的行进行重试.这里只给出构造重试请求的部分.
//             * 推荐的重试方法是使用SDK的自定义重试策略功能, 支持对batch操作的部分行错误进行重试. 设定重试策略后, 调用接口处即不需要增加重试代码.
//             */
//            BatchWriteRowRequest retryRequest = batchWriteRowRequest.createRequestForRetry(response.getFailedRows());
//        }
//    }

}
