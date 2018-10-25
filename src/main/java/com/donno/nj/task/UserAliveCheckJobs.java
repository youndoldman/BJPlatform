package com.donno.nj.task;

import com.donno.nj.service.SysUserService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class UserAliveCheckJobs extends QuartzJobBean
{
    @Autowired
    private SysUserService sysUserService;

    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        sysUserService.checkAlive();
    }
}