package com.donno.nj.service.impl;

import com.donno.nj.domain.pay.OrderInfo;
import com.donno.nj.domain.pay.OrderReturnInfo;
import com.donno.nj.domain.pay.SignInfo;
import com.donno.nj.util.RandomStringGenerator;
import com.donno.nj.util.Signature;
import com.donno.nj.util.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;
import com.donno.nj.service.WeiXinPayService;
import com.thoughtworks.xstream.XStream;
import org.apache.cxf.common.i18n.Exception;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.donno.nj.util.Configure;

import java.io.IOException;


@Service
public class WeiXinPayServiceImpl implements WeiXinPayService
{
    //获取用户的OpenID
    @Override
    public String getOpenId(String userCode)throws IOException
    {
        try {
            HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/sns/jscode2session?appid=" + Configure.getAppID() + "&secret=" + Configure.getSecret() + "&js_code=" + userCode + "&grant_type=authorization_code");
            //设置请求器的配置
            HttpClient httpClient = HttpClients.createDefault();
            HttpResponse res = httpClient.execute(httpGet);
            HttpEntity entity = res.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            JSONObject json = new JSONObject(result);
            String openid = json.getString("openid");
            return openid;
        }catch (JSONException e) {
            System.out.print(e.getMessage());
            return null;
        }
    }

    //统一下单接口,获取PrepayId

    @Override
    public String getPrepayId(String openId)throws IOException
    {
        try {
            OrderInfo order = new OrderInfo();
            order.setAppid(Configure.getAppID());
            order.setMch_id(Configure.getMch_id());
            order.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
            order.setBody("dfdfdf");
            order.setOut_trade_no(RandomStringGenerator.getRandomStringByLength(32));
            order.setTotal_fee(10);
            order.setSpbill_create_ip(Configure.getServerIp());
            order.setNotify_url("https://www.see-source.com/weixinpay/PayResult");
            order.setTrade_type("JSAPI");
            order.setOpenid(openId);
            order.setSign_type("MD5");
            //生成签名
            String sign = Signature.getSign(order);
            order.setSign(sign);


            String result = HttpRequest.sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", order);
            System.out.println(result);

            XStream xStream = new XStream();
            xStream.alias("xml", OrderReturnInfo.class);

            OrderReturnInfo returnInfo = (OrderReturnInfo)xStream.fromXML(result);
            return returnInfo.getPrepay_id();

        } catch (java.lang.Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //生成签名
    @Override
    public String getSign(String prepayId)throws IOException {
        try {
            SignInfo signInfo = new SignInfo();
            signInfo.setAppId(Configure.getAppID());
            long time = System.currentTimeMillis()/1000;
            signInfo.setTimeStamp(String.valueOf(time));
            signInfo.setNonceStr(RandomStringGenerator.getRandomStringByLength(32));
            signInfo.setRepay_id("prepay_id="+prepayId);
            signInfo.setSignType("MD5");
            //生成签名
            String sign = Signature.getSign(signInfo);

            JSONObject json = new JSONObject();
            json.put("timeStamp", signInfo.getTimeStamp());
            json.put("nonceStr", signInfo.getNonceStr());
            json.put("package", signInfo.getRepay_id());
            json.put("signType", signInfo.getSignType());
            json.put("paySign", sign);
            System.out.print("-------再签名:"+json.toString());
            return json.toString();
        } catch (java.lang.Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
