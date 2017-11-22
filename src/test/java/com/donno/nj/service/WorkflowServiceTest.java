package com.donno.nj.service;
import com.donno.nj.activiti.WorkFlowTypes;
import com.donno.nj.domain.Task;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2017\11\21 0021.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/spring/spring-*.xml")
public class WorkflowServiceTest {

    @Autowired
    private WorkFlowService workFlowService;



    //创建流程
    @Test
    public void testcreateWorkFlow() throws Exception {
        String workFlowId = workFlowService.createWorkFlow(WorkFlowTypes.GAS_ORDER_FLOW, "kehu-1", "005");
        System.out.println("创建流程： " + workFlowId);

    }


    //查询当前任务
    @Test
    public void testTasksByUserId() throws Exception {
        List<Task> taskIdList = workFlowService.getTasksByUserId("kefu-1");
        System.out.println("查询当前任务： "+taskIdList.toString());

    }

}
