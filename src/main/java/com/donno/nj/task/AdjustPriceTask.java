package com.donno.nj.task;


import com.donno.nj.service.AdjustPriceScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component("AdjustPriceTask")
@Lazy(false)
public class AdjustPriceTask
{
    @Autowired
    private AdjustPriceScheduleService adjustPriceScheduleService;


    @Scheduled(cron="0/20 * * * * ?")
    public void run()
    {
        adjustPriceScheduleService.adjustPrice();
    }


}
