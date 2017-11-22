package com.donno.nj.domain;

/**
 * Created by wyb on 2017/10/24.
 */
import com.donno.nj.domain.Process;


public class Task
{
    protected String id;                   //任务ID
    protected String taskName;             //任务节点名称
    protected Process process;             //对应的流程




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


}
