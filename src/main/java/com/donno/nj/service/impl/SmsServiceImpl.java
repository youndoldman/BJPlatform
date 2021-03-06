package com.donno.nj.service.impl;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.donno.nj.service.SmsService;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;

import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsResponse;

import com.aliyuncs.dysmsapi.transform.v20170525.SendSmsResponseUnmarshaller;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.ArrayList;
import java.util.List;
import com.aliyuncs.http.MethodType;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.json.JSONArray;
@Service
public class SmsServiceImpl implements SmsService
{
    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";
    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAIQCESkaBZRBKo";
    static final String accessKeySecret = "0abALsZaVOyAe7aURqeutOHVaQmjzh";

    //发送短信
    @Override
    public SendSmsResponse sendGasLeakSms(String phoneNumber, String userName, String address)throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("云南百江燃气");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_141582772");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        if(address.length()>20){//超過20就截斷
            String address1 = address.substring(0,19);
            String address2 = address.substring(20,address.length());
            request.setTemplateParam("{\"userName\":\""+userName+"\", \"address1\":\""+address1+"\",\"address2\":\""+address2+"\"}");
        }else {
            request.setTemplateParam("{\"userName\":\""+userName+"\", \"address1\":\""+address+"\",\"address2\":\""+"\"}");
        }

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("test");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }

    //发送短信
    @Override
    public SendSmsResponse sendGasLeakSmsToFireDepartment(String phoneNumber, String userName, String address, String userPhone)throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("云南百江燃气");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_141597540");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        if(address.length()>20){//超過20就截斷
            String address1 = address.substring(0,19);
            String address2 = address.substring(20,address.length());
            request.setTemplateParam("{\"userName\":\""+userName+"\", \"address1\":\""+address1+"\",\"address2\":\""+address2+"\", \"phone\":\""+userPhone+"\"}");
        }else {
            request.setTemplateParam("{\"userName\":\""+userName+"\", \"address1\":\""+address+"\",\"address2\":\""+"\", \"phone\":\""+userPhone+"\"}");
        }
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("test");
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        return sendSmsResponse;
    }




    //发送短信
    @Override
    public SendSmsResponse sendDispatchOk(String phoneNumber, String userName, String orderSn,Float amount)
            throws ClientException
    {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("云南百江燃气");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_149416135");

        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"userName\":\""+userName+"\", \"orderSN\":\""+orderSn+"\",\"amount\":\"\"+amount+\"\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("test");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }


    //批量发送自定义短信
    @Override
    public SendBatchSmsResponse sendBatchSmsCommon(List<String> phoneNumberList, String smCode)
            throws ClientException
    {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendBatchSmsRequest request = new SendBatchSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);

        ArrayList arrayListPhone = new ArrayList(phoneNumberList);


        //必填:待发送手机号。支持JSON格式的批量调用，批量上限为100个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        JSONArray jsonArrayPhoneNumber=new JSONArray(arrayListPhone);
        request.setPhoneNumberJson(jsonArrayPhoneNumber.toString());
        //必填:短信签名-支持不同的号码发送不同的短信签名

        List<String>SignNameList = new ArrayList<String>();
        for (int i=0; i<phoneNumberList.size(); i++){
            SignNameList.add("云南百江燃气");
        }
        ArrayList arrayListSignName = new ArrayList(SignNameList);
        JSONArray jsonArraySignName=new JSONArray(arrayListSignName);
        request.setSignNameJson(jsonArraySignName.toString());
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(smCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        JSONArray codeJSONArray =new JSONArray();
        for (int i=0; i<phoneNumberList.size(); i++){
            JSONObject tempObject = new JSONObject();
            codeJSONArray.put(tempObject);
        }

        request.setTemplateParamJson(codeJSONArray.toString());


        //hint 此处可能会抛出异常，注意catch
        SendBatchSmsResponse sendBatchSmsResponse = acsClient.getAcsResponse(request);

        return sendBatchSmsResponse;
    }

}
