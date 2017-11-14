package com.donno.nj.domain;

/**
 * Created by wyb on 2017/10/24.
 */
import com.donno.nj.activiti.WorkFlowTypes;


public class Process
{
    protected String id;                   //流程ID
    protected WorkFlowTypes workFlowType;  //对应的流程类型
    protected String buinessKey;           //流程表的索引



    public Process()
    {

    }

    public  String getId()
    {
            return id;
    }

    public WorkFlowTypes getWorkFlowType()
    {
        return workFlowType;
    }

    public String getBuinessKey()
    {
        return buinessKey;
    }



    public void setId(String id)
    {
        this.id = id;
    }

    public void setWorkFlowType(WorkFlowTypes workFlowType)
    {
        this.workFlowType = workFlowType;
    }

    public void setBuinessKey(String buinessKey)
    {
        this.buinessKey = buinessKey;
    }


}
