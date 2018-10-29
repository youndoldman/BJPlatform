package com.donno.nj.domain;

import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.push.model.v20160801.PushNoticeToAndroidRequest;
import com.aliyuncs.push.model.v20160801.PushNoticeToAndroidResponse;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.utils.ParameterHelper;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Administrator on 2018/9/6 0006.
 */
public class MsgPush {

    /**
     * 推送通知给android
     * <p>
     * 参见文档 https://help.aliyun.com/document_detail/48087.html
     */

    public void PushNotice(String targetVal,String title,String body) throws Exception
    {

//        MsgPushCfg.readCfg();
//
//        PushRequest pushRequest = new PushRequest();
//        //安全性比较高的内容建议使用HTTPS
//        pushRequest.setProtocol(ProtocolType.HTTPS);
//        //内容较大的请求，使用POST请求
//        pushRequest.setMethod(MethodType.POST);
//
//        // 推送目标
//        pushRequest.setAppKey(MsgPushCfg.getAppKey());
//        pushRequest.setTarget("ALIAS"); //推送目标: DEVICE:按设备推送 ALIAS : 按别名推送 ACCOUNT:按帐号推送  TAG:按标签推送; ALL: 广播推送
//        pushRequest.setTargetValue(targetVal); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
//        pushRequest.setPushType("NOTICE"); // 消息类型 MESSAGE NOTICE
//        pushRequest.setDeviceType("ALL"); // 设备类型 ANDROID iOS ALL.
//
//        // 推送配置
//        pushRequest.setTitle(title);
//        pushRequest.setBody(body);
//
//        // 推送配置: iOS
//        pushRequest.setIOSBadge(5); // iOS应用图标右上角角标
//        pushRequest.setIOSSilentNotification(false);//开启静默通知
//        pushRequest.setIOSMusic("default"); // iOS通知声音
//        pushRequest.setIOSSubtitle("iOS10 subtitle");//iOS10通知副标题的内容
//        pushRequest.setIOSNotificationCategory("iOS10 Notification Category");//指定iOS10通知Category
//        pushRequest.setIOSMutableContent(true);//是否允许扩展iOS通知内容
//        pushRequest.setIOSApnsEnv("DEV");//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
//        pushRequest.setIOSRemind(true); // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
//        pushRequest.setIOSRemindBody("iOSRemindBody");//iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效
//        pushRequest.setIOSExtParameters("{\"_ENV_\":\"DEV\",\"k2\":\"v2\"}"); //通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)
//
//        // 推送配置: Android
//        pushRequest.setAndroidNotifyType("BOTH");//通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
//        pushRequest.setAndroidNotificationBarType(1);//通知栏自定义样式0-100
//        pushRequest.setAndroidNotificationBarPriority(1);//通知栏自定义样式0-100
//        pushRequest.setAndroidOpenType("ACTIVITY"); //点击通知后动作 "APPLICATION" : 打开应用 "ACTIVITY" : 打开AndroidActivity "URL" : 打开URL "NONE" : 无跳转
//        //pushRequest.setAndroidOpenUrl("https://www.yunnanbaijiang.com"); //Android收到推送后打开对应的url,仅当AndroidOpenType="URL"有效
//        pushRequest.setAndroidActivity("com.gc.nfc.ui.MainlyActivity"); // 设定通知打开的activity，仅当AndroidOpenType="Activity"有效
//        pushRequest.setAndroidMusic("default"); // Android通知音乐
//        pushRequest.setAndroidXiaoMiActivity("com.gc.nfc.ui.MainlyActivity");//设置该参数后启动小米托管弹窗功能, 此处指定通知点击后跳转的Activity（托管弹窗的前提条件：1. 集成小米辅助通道；2. StoreOffline参数设为true）
//        pushRequest.setAndroidXiaoMiNotifyTitle("Mi title");
//        pushRequest.setAndroidXiaoMiNotifyBody("MainlyActivity Body");
//        pushRequest.setAndroidExtParameters("{\"k1\":\"android\",\"k2\":\"v2\"}"); //设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
//
//
//        // 推送控制
//        Date pushDate = new Date(System.currentTimeMillis()+10); // 30秒之间的时间点, 也可以设置成你指定固定时间
//        String pushTime = ParameterHelper.getISO8601Time(pushDate);
//        pushRequest.setPushTime(pushTime); // 延后推送。可选，如果不设置表示立即推送
//        String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000)); // 12小时后消息失效, 不会再发送
//        pushRequest.setExpireTime(expireTime);
//        pushRequest.setStoreOffline(true); // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
//// 指定notificaitonchannel id
//        pushRequest.setAndroidNotificationChannel("1");
//
//        PushResponse pushResponse = MsgPushCfg.getClient().getAcsResponse(pushRequest);
//        System.out.printf("RequestId: %s, MessageID: %s\n",
//                pushResponse.getRequestId(), pushResponse.getMessageId());
//
//




//        PushNoticeToAndroidRequest androidRequest = new PushNoticeToAndroidRequest();
//        //安全性比较高的内容建议使用HTTPS
//        androidRequest.setProtocol(ProtocolType.HTTPS);
//        //内容较大的请求，使用POST请求
//        androidRequest.setMethod(MethodType.POST);
//        androidRequest.setAppKey(MsgPushCfg.getAppKey());
//        androidRequest.setTarget("ALIAS");
//
//        androidRequest.setTargetValue(targetVal);
//        androidRequest.setTitle(title);
//        androidRequest.setBody(body);
//        androidRequest.setExtParameters("{\"k1\":\"v1\"}");
//
//        PushNoticeToAndroidResponse pushNoticeToAndroidResponse = MsgPushCfg.getClient().getAcsResponse(androidRequest);
//        System.out.printf("RequestId: %s, MessageId: %s\n",
//                pushNoticeToAndroidResponse.getRequestId(), pushNoticeToAndroidResponse.getMessageId());
    }
}
