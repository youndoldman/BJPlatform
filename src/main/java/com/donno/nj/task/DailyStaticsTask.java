package com.donno.nj.task;


import com.donno.nj.service.AdjustPriceScheduleService;
import com.donno.nj.service.DailyStaticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component("DailyStaticsTask")
@Lazy(false)
public class DailyStaticsTask
{
    @Autowired
    private DailyStaticsService dailyStaticsService;


    @Scheduled(cron="0/20 * * * * ?")
    public void run() throws ParseException
    {
        dailyStaticsService.dailyStatics();
    }


}
