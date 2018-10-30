package com.donno.nj.task;

import com.donno.nj.service.AdjustPriceScheduleService;
import com.donno.nj.service.DiscountStrategyService;
import com.donno.nj.service.OrderService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class OrderOvertimeJobs extends QuartzJobBean
{
    @Autowired
    private OrderService orderService;

    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        orderService.checkOverTime();
    }
}