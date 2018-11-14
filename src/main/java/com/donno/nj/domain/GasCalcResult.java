package com.donno.nj.domain;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/13 0013.
 */
public class GasCalcResult implements Serializable
{
    private  String gasCynNumber; //空瓶号
    private  Boolean success;//计算成功or失败
    private  String note;//说明

    private GasRefoundDetail gasRefoundDetail;

    public String getGasCynNumber() {
        return gasCynNumber;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getNote() {
        return note;
    }

    public GasRefoundDetail getGasRefoundDetail() {
        return gasRefoundDetail;
    }

    public void setGasCynNumber(String gasCynNumber) {
        this.gasCynNumber = gasCynNumber;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setGasRefoundDetail(GasRefoundDetail gasRefoundDetail) {
        this.gasRefoundDetail = gasRefoundDetail;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}

