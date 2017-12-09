package com.donno.nj.domain;

public class Task
{
    protected String id;                   //任务ID
    protected String taskName;             //任务节点名称
    protected Process process;             //对应的流程

    protected  Object object;

    public Task()
    {

    }

    public  String getId()
    {
            return id;
    }

    public Process getProcess()
    {
        return process;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public Object getObject()
    {
        return object;
    }



    public void setId(String id)
    {
        this.id = id;
    }

    public void setProcess(Process process)
    {
        this.process = process;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public void setObject(Object object)
    {
        this.object = object;
    }


}
