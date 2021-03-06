package com.donno.nj.service;

import com.donno.nj.activiti.WorkFlowTypes;
import com.donno.nj.domain.Process;
import com.donno.nj.domain.Task;

import java.util.List;
import java.util.Map;


public interface WorkFlowService
{
    //启动流程 wfType－流程类型　　strUserID－启动流程的用户ID  strBuinessKey-自己流程表的流程索引值
    int createWorkFlow(WorkFlowTypes wfType, String strUserID, Map<String, Object> variables,String strBuinessKey);

    //查询当前任务 strUserID－启动流程的用户ID  iTaskIndex-任务序号 pageNo-页数 pageSize-页容量
    List<Task> getTasksByUserId(String strUserID, int iTaskIndex, int pageNo, int pageSize);

    //查询当前任务的总数 strUserID－用户ID  iTaskIndex-任务序号
    int getTasksCountByUserId(String strUserID, int iTaskIndex);

    //办理任务 strTaskId－任务ID,variables-同意还是不同意等审批结论
    int completeTask(String strTaskId, Map<String, Object> variables);

    //查询当前任务对应的流程
    Process getProcessByTaskId(String strTaskID);

    //查询对应用户启动的历史流程
    List<Process> getProcessByStartUser(String strUserId);

    //查询流程图
    byte[] getImageByProcessId(String strProcessID);

    //流程作废 buinessKey-关联用户表的订单号等索键值
    int deleteProcess(String buinessKey);

    //增加流程的处理人 strTaskId－任务ID,strUserId-处理人的userId
    int addCandidateUsers(String strTaskId, String strUserId);

    //删除流程的处理人 strTaskId－任务ID,strUserId-处理人的userId
    int deleteCandidateUsers(String strTaskId, String strUserId);

    //获取流程的候选处理人 strTaskId－任务ID
    List<String> getCandidateUsers(String strTaskId);
}
