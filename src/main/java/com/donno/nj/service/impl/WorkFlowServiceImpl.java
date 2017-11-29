package com.donno.nj.service.impl;
import com.donno.nj.activiti.WorkFlowTypes;
import com.donno.nj.service.WorkFlowService;
import com.donno.nj.logger.DebugLogger;
import com.donno.nj.domain.Process;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.logger.DebugLogger;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class WorkFlowServiceImpl implements WorkFlowService
{
    @Autowired
    DebugLogger debugLogger;

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    IdentityService identityService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    @Autowired
    protected ManagementService managementService;

    @Autowired
    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    //启动流程
    @Override
    public int createWorkFlow(WorkFlowTypes wfType, String strUserID, Map<String, Object> variables,String strBuinessKey) {
        DebugLogger.log("启动流程:"+wfType.getName()+"   启动人:"+strUserID+"   流程表索引值:"+strBuinessKey);
        ProcessInstance processInstance = null;
        try {
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(strUserID);
            processInstance = runtimeService.startProcessInstanceByKey(wfType.getName(), strBuinessKey, variables);
            identityService.setAuthenticatedUserId(null);
        }catch (Exception e)
        {
            debugLogger.log(e.getMessage());
            return -1;
        }
        //return processInstance.getProcessDefinitionId();
        return 0;
    }

    //查询当前任务
    @Override
    public List<com.donno.nj.domain.Task> getTasksByUserId(String strUserID) {
        // 根据当前人的ID查询
        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateOrAssigned(strUserID);

        List<Task> tasks = taskQuery.list();
        List<com.donno.nj.domain.Task>currentTaskList = new ArrayList<com.donno.nj.domain.Task>();

        for (Task task : tasks) {
            //任务号
            com.donno.nj.domain.Task myTask = new com.donno.nj.domain.Task();
            myTask.setId(task.getId());
            myTask.setTaskName(task.getName());
            //查询当前任务对应的流程
            Process process = getProcessByTaskId(task.getId());
            myTask.setProcess(process);
            currentTaskList.add(myTask);
        }
        return currentTaskList;
    }

    //查询当前任务对应的流程
    @Override
    public Process getProcessByTaskId(String strTaskID) {

        try {
            Task task = taskService.createTaskQuery().taskId(strTaskID).active().singleResult();

            Process curProcess = new Process();

            //对应的流程号
            String processInstanceId = task.getProcessInstanceId();
            curProcess.setId(processInstanceId);
            //对应的流程表索引
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
            String businessKey = processInstance.getBusinessKey();
            curProcess.setBuinessKey(businessKey);

            //对应的流程类型
            String WorkFlowName = processInstance.getProcessDefinitionName();
            curProcess.setWorkFlowType(WorkFlowTypes.getByName(WorkFlowName));
            return curProcess;

        } catch (Exception e)
        {
            debugLogger.log(e.getMessage());
            throw new ServerSideBusinessException("查询当前任务对应的流程 异常错误");
        }


    }


    //办理任务 strTaskId－任务ID,variables-同意还是不同意等审批结论
    @Override
    public int completeTask(String strTaskId, Map<String, Object> variables) {
        try {
            taskService.complete(strTaskId, variables);
            return 0;
        } catch (Exception e) {
            debugLogger.log(e.getMessage());
            return -1;
        }
    }

    //查询对应用户启动的历史流程
    @Override
    public List<Process> getProcessByStartUser(String strUserId) {
        try {
            List<Process> processList = new ArrayList<Process>();
            List<HistoricProcessInstance> hisProcessList = historyService.createHistoricProcessInstanceQuery().startedBy(strUserId).list();
            if(hisProcessList.size()==0)
            {
                return processList;
            }
            for (HistoricProcessInstance historicProcessInstance : hisProcessList) {
                Process process = new Process();
                //任务号
                process.setId(historicProcessInstance.getId());
                //对应的流程表索引
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(historicProcessInstance.getId()).active().singleResult();
                if (processInstance == null) {
                    continue;
                }
                String businessKey = processInstance.getBusinessKey();
                process.setBuinessKey(businessKey);

                //对应的流程类型
                String WorkFlowName = processInstance.getProcessDefinitionName();
                process.setWorkFlowType(WorkFlowTypes.valueOf(WorkFlowName));

                processList.add(process);
            }
            return processList;


        } catch (Exception e)
        {
            debugLogger.log(e.getMessage());
            throw new ServerSideBusinessException("查询对应用户启动的历史流程 异常错误");
        }
    }

    //查询流程图
    @Override
    public byte[] getImageByProcessId(String strProcessID)
    {
        try {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(strProcessID).active().singleResult();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(strProcessID);

            // 使用spring注入引擎请使用下面的这行代码
            ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
            InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds,
                    Collections.<String>emptyList(), this.processEngine.getProcessEngineConfiguration().getActivityFontName(),
                    this.processEngine.getProcessEngineConfiguration().getLabelFontName(), this.processEngine.getProcessEngineConfiguration().getAnnotationFontName(),
                    processEngine.getProcessEngineConfiguration().getClassLoader(), 1.0);

            byte[] image = new byte[10240];
            int iIndex = 0;
            if (imageStream != null) {
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = imageStream.read(b, 0, 1024)) != -1) {
                    System.arraycopy(b, 0, image, iIndex, len);
                    iIndex += len;
                }
            }
            return  image;
        }catch (Exception e)
        {

            debugLogger.log(e.getMessage());
            throw new ServerSideBusinessException("查询流程图 异常错误");
        }
    }
}
