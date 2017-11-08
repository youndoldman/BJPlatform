package com.donno.nj.domain.sys;

import com.google.common.base.MoreObjects;

import java.util.Date;

public class OperatorOpLog {
    private Long id;

    private String method;

    private String methodDesc;

    private String operator;

    private Date opTime;

    private String opContent;

    public OperatorOpLog() {

    }

    public static OperatorOpLog log(String methodName, String desc, String opParams, String id) {
        OperatorOpLog log = new OperatorOpLog();
        log.method = methodName;
        log.methodDesc = desc;
        log.opContent = opParams;
        log.operator = id;
        return log;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc == null ? null : methodDesc.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public String getOpContent() {
        return opContent;
    }

    public void setOpContent(String opContent) {
        this.opContent = opContent == null ? null : opContent.trim();
    }

    @Override
    public String toString() {

        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("method", method)
                .add("methodDesc", methodDesc)
                .add("opContent", opContent)
                .add("opTime", opTime)
                .add("operator", operator)
                .toString();

    }
}