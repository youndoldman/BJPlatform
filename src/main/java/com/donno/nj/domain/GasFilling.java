package com.donno.nj.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

/*充装站上报的数据格式*/
public class GasFilling implements Serializable
{
    private Integer id;

    private String stationNumber; //充装站站号

    private Integer machineNumber;//秤号，机器号

    private Integer clientCode;//客户代码

    private String userId;//员工工号,实际为员工userid

    private String startTime;//开始灌装时间

    private Float useTime;  //灌装用时
    private String fillingType ;//灌装方式

    private Float targetWeight;//目标量

    private Float tareWeight;//钢瓶瓶重（皮重）

    private Float realWeight;//灌装量

    private Float deviation ;//误差值

    private String result;//灌装结果

    private Integer sequence ;//编号（唯一）

    private String cynNumber;//瓶号

    private WarnningStatus warnningStatus;//皮重称重告警

    private Float tareDifferWeight;//皮重差值

    Date mergeTime;
    private String  note;
    private Date createTime;
    private Date updateTime;

    public GasFilling()
    {
    }


    /*
    * 属性读取
    * */

    public Integer getId()
    {
        return id;
    }

    public String getStationNumber() {
        return stationNumber;
    }

    public Integer getMachineNumber() {
        return machineNumber;
    }

    public Integer getClientCode() {
        return clientCode;
    }

    public String getUserId() {
        return userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public Float getUseTime() {
        return useTime;
    }

    public String getFillingType() {
        return fillingType;
    }

    public Float getTareWeight() {
        return tareWeight;
    }

    public Float getTargetWeight() {
        return targetWeight;
    }

    public Float getRealWeight() {
        return realWeight;
    }

    public Float getDeviation() {
        return deviation;
    }

    public String getResult() {
        return result;
    }

    public Integer getSequence() {
        return sequence;
    }

    public String getCynNumber() {
        return cynNumber;
    }

    public Date getMergeTime() {
        return mergeTime;
    }

    public WarnningStatus getWarnningStatus() {
        return warnningStatus;
    }


    public Float getTareDifferWeight() {
        return tareDifferWeight;
    }

    public  String getNote()
    {
        return  note;
    }

    public  Date getCreateTime()
    {
        return  createTime;
    }

    public  Date getUpdateTime()
    {
        return  updateTime;
    }

    /*
    * 属性设置
    * */


    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setStationNumber(String stationNumber) {
        this.stationNumber = stationNumber;
    }

    public void setMachineNumber(Integer machineNumber) {
        this.machineNumber = machineNumber;
    }

    public void setClientCode(Integer clientCode) {
        this.clientCode = clientCode;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setUseTime(Float useTime) {
        this.useTime = useTime;
    }


    public void setFillingType(String fillingType) {
        this.fillingType = fillingType;
    }

    public void setTargetWeight(Float targetWeight) {
        this.targetWeight = targetWeight;
    }

    public void setTareWeight(Float tareWeight) {
        this.tareWeight = tareWeight;
    }

    public void setRealWeight(Float realWeight) {
        this.realWeight = realWeight;
    }

    public void setDeviation(Float deviation) {
        this.deviation = deviation;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public void setCynNumber(String cynNumber) {
        this.cynNumber = cynNumber;
    }

    public void setMergeTime(Date mergeTime) {
        this.mergeTime = mergeTime;
    }

    public void setWarnningStatus(WarnningStatus warnningStatus) {
        this.warnningStatus = warnningStatus;
    }

    public void setTareDifferWeight(Float tareDifferWeight) {
        this.tareDifferWeight = tareDifferWeight;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public  void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }


    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}