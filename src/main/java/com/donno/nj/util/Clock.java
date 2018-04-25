package com.donno.nj.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
public class Clock {

    public static Date now() {
        return new Date();
    }

    public static Date getLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    public static Date getStartDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        Date date = calendar.getTime();
        return date;
    }

    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        date = calendar.getTime();
        return date;
    }

    public static boolean beforeDate(Date Date1, Date Date2)
    {
        boolean before = false;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try
        {

            Date bt = df.parse(df.format(Date1));
            Date et = df.parse(df.format(Date2));
            if (bt.equals(et))
            {
                before = true;
            }
            else if (bt.before(et))
            {
                before = true;
            }
            else
            {
                before = false;
            }

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        return before;
    }
}
