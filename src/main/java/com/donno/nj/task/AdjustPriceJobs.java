package com.donno.nj.task;

import com.donno.nj.service.AdjustPriceScheduleService;
import com.donno.nj.service.DiscountStrategyService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class AdjustPriceJobs extends QuartzJobBean
{
    @Autowired
    private AdjustPriceScheduleService adjustPriceScheduleService;

    @Autowired
    private DiscountStrategyService discountStrategyService;

    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        adjustPriceScheduleService.adjustPrice();
        discountStrategyService.discountTrigger();
    }
}