package com.donno.nj.service;

import com.donno.nj.domain.AdjustPriceSchedule;
import com.google.common.base.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Transactional
public interface DailyStaticsService
{
    void dailyStatics() throws ParseException;
}
