package com.donno.nj.test;

import com.donno.nj.domain.*;
import org.junit.Test;

/**
 * Created by Administrator on 2018/9/6 0006.
 */
public class PushTest
{
    @Test
    public void testPushNewOrderNotice() throws Exception
    {
        try
        {
            String strCandiUser = "pscs";
            MsgPush msgPush = new MsgPush();
            msgPush.PushNotice(strCandiUser, ServerConstantValue.NEW_ORDER_TITLE, "test");
        }
        catch (Exception e)
        {
            //消息推送失败
        }
    }

    @Test
    public void testPushForceOrderNotice() throws Exception
    {
        try
        {
            String strCandiUser = "pscs,pscs2";
            MsgPush msgPush = new MsgPush();
            msgPush.PushNotice(strCandiUser, ServerConstantValue.FORCE_ORDER_TITLE, "系统测试");
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
            //消息推送失败
        }
    }

}
