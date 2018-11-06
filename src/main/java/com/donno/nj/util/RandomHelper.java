package com.donno.nj.util;

import java.util.Random;

/**
 * Created by Administrator on 2018/11/6 0006.
 */
public class RandomHelper
{

    static public Integer getRandomNumber()
    {
        Random rdm = new Random(System.currentTimeMillis());
        Integer intRd = Math.abs((rdm.nextInt()))%100000+1;
        return  intRd ;
    }
}
