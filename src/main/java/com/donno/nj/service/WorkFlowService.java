package com.donno.nj.service;

import com.donno.nj.activiti.WorkFlowTypes;
import com.donno.nj.domain.Process;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


public interface WorkFlowService
{
    //启动流程 wfType－流程类型　　strUserID－启动流程的用户ID  strBuinessKey-自己流程表的流程索引值
    String createWorkFlow(WorkFlowTypes wfType, String strUserID, String strBuinessKey);

    //查询当前任务 strUserID－启动流程的用户ID
    List<String> getTasksByUserId(String strUserID);

    //办理任务 strTaskId－任务ID
    boolean completeTask(String strTaskId);

    //查询当前任务对应的流程
    Process getProcessByTaskId(String strTaskID);

    //查询对应用户启动的历史流程
    List<Process> getProcessByStartUser(String strUserId);

    //查询流程图
    byte[] getImageByProcessId(String strProcessID);
}
