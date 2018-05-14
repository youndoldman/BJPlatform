package com.donno.nj.service.impl;


import com.alicloud.openservices.tablestore.ClientException;
import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.TableStoreException;
import com.alicloud.openservices.tablestore.model.*;

import com.donno.nj.domain.GasCylinderPosition;
import com.donno.nj.service.TableStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class TableStoreServiceImpl implements TableStoreService
{


    final String endPoint = "https://bjrq.cn-shenzhen.ots.aliyuncs.com";
    final String accessId = "LTAIQCESkaBZRBKo";
    final String accessKey = "0abALsZaVOyAe7aURqeutOHVaQmjzh";
    final String instanceName = "bjrq";

    final String PRIMARY_KEY_CODE = "code";
    final String PRIMARY_KEY_TIME = "create_time";

    private static final String TABLE_NAME = "t_gasCylindersLocation";


    private SyncClient client;

    private TableStoreServiceImpl() throws Exception{
        client = new SyncClient(endPoint, accessId, accessKey, instanceName);
    }



    @Override
    public void createTable() {
        TableMeta tableMeta = new TableMeta(TABLE_NAME);
        tableMeta.addPrimaryKeyColumn(new PrimaryKeySchema(PRIMARY_KEY_CODE, PrimaryKeyType.STRING));
        tableMeta.addPrimaryKeyColumn(new PrimaryKeySchema(PRIMARY_KEY_TIME, PrimaryKeyType.STRING));
        int timeToLive = -1; // 数据的过期时间, 单位秒, -1代表永不过期. 假如设置过期时间为一年, 即为 365 * 24 * 3600.
        int maxVersions = 3; // 保存的最大版本数, 设置为3即代表每列上最多保存3个最新的版本.

        TableOptions tableOptions = new TableOptions(timeToLive, maxVersions);

        CreateTableRequest request = new CreateTableRequest(tableMeta, tableOptions);

        client.createTable(request);
    }

    @Override
    public void updateTable() {
        int timeToLive = -1;
        int maxVersions = 5; //更新最大版本数为5.

        TableOptions tableOptions = new TableOptions(timeToLive, maxVersions);

        UpdateTableRequest request = new UpdateTableRequest(TABLE_NAME);
        request.setTableOptionsForUpdate(tableOptions);

        client.updateTable(request);
    }

    @Override
    public void describeTable() {
        DescribeTableRequest request = new DescribeTableRequest(TABLE_NAME);
        DescribeTableResponse response = client.describeTable(request);

        TableMeta tableMeta = response.getTableMeta();
        System.out.println("表的名称：" + tableMeta.getTableName());
        System.out.println("表的主键：");
        for (PrimaryKeySchema primaryKeySchema : tableMeta.getPrimaryKeyList()) {
            System.out.println(primaryKeySchema);
        }
        TableOptions tableOptions = response.getTableOptions();
        System.out.println("表的TTL:" + tableOptions.getTimeToLive());
        System.out.println("表的MaxVersions:" + tableOptions.getMaxVersions());
        ReservedThroughputDetails reservedThroughputDetails = response.getReservedThroughputDetails();
        System.out.println("表的预留读吞吐量："
                + reservedThroughputDetails.getCapacityUnit().getReadCapacityUnit());
        System.out.println("表的预留写吞吐量："
                + reservedThroughputDetails.getCapacityUnit().getWriteCapacityUnit());
    }

    @Override
    public void deleteTable() {
        DeleteTableRequest request = new DeleteTableRequest(TABLE_NAME);
        client.deleteTable(request);
    }

    @Override
    public void listTable() {
        ListTableResponse response = client.listTable();
        System.out.println("表的列表如下：");
        for (String tableName : response.getTableNames()) {
            System.out.println(tableName);
        }
    }

    @Override
    public void putRow(GasCylinderPosition gasCylinderPosition) {
        // 构造主键
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_CODE, PrimaryKeyValue.fromString(gasCylinderPosition.getCode()));
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_TIME, PrimaryKeyValue.fromString(gasCylinderPosition.getCreateTime().toString()));
        PrimaryKey primaryKey = primaryKeyBuilder.build();

        RowPutChange rowPutChange = new RowPutChange(TABLE_NAME, primaryKey);

        //加入定位的数据列

        rowPutChange.addColumn(new Column("location", ColumnValue.fromString(gasCylinderPosition.getLocation())));
        client.putRow(new PutRowRequest(rowPutChange));
    }

    @Override
    public GasCylinderPosition getNearestRow(String code) {
        RangeIteratorParameter rangeIteratorParameter = new RangeIteratorParameter(TABLE_NAME);

        // 设置起始主键
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_CODE, PrimaryKeyValue.fromString(code));
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_TIME, PrimaryKeyValue.INF_MAX);
        rangeIteratorParameter.setInclusiveStartPrimaryKey(primaryKeyBuilder.build());

        // 设置结束主键
        primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_CODE, PrimaryKeyValue.fromString(code));
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_TIME, PrimaryKeyValue.INF_MIN);
        rangeIteratorParameter.setExclusiveEndPrimaryKey(primaryKeyBuilder.build());

        rangeIteratorParameter.setDirection(Direction.BACKWARD);
        rangeIteratorParameter.setMaxVersions(1);
        rangeIteratorParameter.setMaxCount(1);
        rangeIteratorParameter.addColumnsToGet("location");

        Iterator<Row> iterator = client.createRangeIterator(rangeIteratorParameter);


        List<GasCylinderPosition> locationList = new ArrayList<GasCylinderPosition>();
        if (iterator.hasNext()) {
            Row row = iterator.next();
            GasCylinderPosition gasCylinderPosition = new GasCylinderPosition();
            PrimaryKey primaryKey = row.getPrimaryKey();
            gasCylinderPosition.setCode(primaryKey.getPrimaryKeyColumn("code").getValue().toString());
            gasCylinderPosition.setCreateTime(primaryKey.getPrimaryKeyColumn("create_time").getValue().toString());
            gasCylinderPosition.setLocation(row.getColumn("location").get(0).getValue().toString());
            return gasCylinderPosition;
        }
        return null;
    }

    @Override
    public List<GasCylinderPosition> getRangeByIterator(String code, String startTime, String endTime) {
        RangeIteratorParameter rangeIteratorParameter = new RangeIteratorParameter(TABLE_NAME);

        // 设置起始主键
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_CODE, PrimaryKeyValue.fromString(code));
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_TIME, PrimaryKeyValue.fromString(startTime));
        rangeIteratorParameter.setInclusiveStartPrimaryKey(primaryKeyBuilder.build());

        // 设置结束主键
        primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_CODE, PrimaryKeyValue.fromString(code));
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_TIME, PrimaryKeyValue.fromString(endTime));
        rangeIteratorParameter.setExclusiveEndPrimaryKey(primaryKeyBuilder.build());

        rangeIteratorParameter.setMaxVersions(1);
        rangeIteratorParameter.addColumnsToGet("location");

        Iterator<Row> iterator = client.createRangeIterator(rangeIteratorParameter);


        List<GasCylinderPosition> locationList = new ArrayList<GasCylinderPosition>();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            GasCylinderPosition gasCylinderPosition = new GasCylinderPosition();
            PrimaryKey primaryKey = row.getPrimaryKey();
            gasCylinderPosition.setCode(primaryKey.getPrimaryKeyColumn("code").getValue().toString());
            gasCylinderPosition.setCreateTime(primaryKey.getPrimaryKeyColumn("create_time").getValue().toString());
            gasCylinderPosition.setLocation(row.getColumn("location").get(0).getValue().toString());
            locationList.add(gasCylinderPosition);

        }
        return locationList;
    }

    @Override
    public List<GasCylinderPosition>  getRange(String code, String startTime, String endTime) {
        RangeRowQueryCriteria rangeRowQueryCriteria = new RangeRowQueryCriteria(TABLE_NAME);

        // 设置起始主键
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_CODE, PrimaryKeyValue.fromString(code));
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_TIME, PrimaryKeyValue.fromString(startTime));
        rangeRowQueryCriteria.setInclusiveStartPrimaryKey(primaryKeyBuilder.build());

        // 设置结束主键
        primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_CODE, PrimaryKeyValue.fromString(code));
        primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_TIME, PrimaryKeyValue.fromString(endTime));
        rangeRowQueryCriteria.setExclusiveEndPrimaryKey(primaryKeyBuilder.build());

        rangeRowQueryCriteria.setMaxVersions(1);
        rangeRowQueryCriteria.addColumnsToGet("location");

        List<GasCylinderPosition> locationList = new ArrayList<GasCylinderPosition>();
//        while (true) {
            GetRangeResponse getRangeResponse = client.getRange(new GetRangeRequest(rangeRowQueryCriteria));
            for (Row row : getRangeResponse.getRows()) {
                GasCylinderPosition gasCylinderPosition = new GasCylinderPosition();
                PrimaryKey primaryKey = row.getPrimaryKey();
                gasCylinderPosition.setCode(primaryKey.getPrimaryKeyColumn("code").getValue().toString());
                gasCylinderPosition.setCreateTime(primaryKey.getPrimaryKeyColumn("create_time").getValue().toString());
                gasCylinderPosition.setLocation(row.getColumn("location").get(0).getValue().toString());
                locationList.add(gasCylinderPosition);
            }

            // 若nextStartPrimaryKey不为null, 则继续读取.
//            if (getRangeResponse.getNextStartPrimaryKey() != null) {
//                rangeRowQueryCriteria.setInclusiveStartPrimaryKey(getRangeResponse.getNextStartPrimaryKey());
//            } else {
//                break;
//            }
//        }
        return locationList;
    }

    @Override
    public void batchWriteRow(List<GasCylinderPosition> gasCylinderPositionList) {
        BatchWriteRowRequest batchWriteRowRequest = new BatchWriteRowRequest();

        for (GasCylinderPosition gasCylinderPosition:gasCylinderPositionList){
            // 构造主键
            PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
            primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_CODE, PrimaryKeyValue.fromString(gasCylinderPosition.getCode()));
            primaryKeyBuilder.addPrimaryKeyColumn(PRIMARY_KEY_TIME, PrimaryKeyValue.fromString(gasCylinderPosition.getCreateTime().toString()));
            PrimaryKey primaryKey = primaryKeyBuilder.build();
            RowPutChange rowPutChange = new RowPutChange(TABLE_NAME, primaryKey);
            //加入定位的数据列
            rowPutChange.addColumn(new Column("location", ColumnValue.fromString(gasCylinderPosition.getLocation())));
            batchWriteRowRequest.addRowChange(rowPutChange);
        }

        BatchWriteRowResponse response = client.batchWriteRow(batchWriteRowRequest);
        if (!response.isAllSucceed()) {
            /**
             * 可以通过createRequestForRetry方法再构造一个请求对失败的行进行重试.这里只给出构造重试请求的部分.
             * 推荐的重试方法是使用SDK的自定义重试策略功能, 支持对batch操作的部分行错误进行重试. 设定重试策略后, 调用接口处即不需要增加重试代码.
             */
            BatchWriteRowRequest retryRequest = batchWriteRowRequest.createRequestForRetry(response.getFailedRows());
        }
    }

}
