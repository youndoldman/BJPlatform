package com.donno.nj.service.impl;

import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.util.WeiXinPaySdk.*;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class WeiXinPayServiceImpl implements WeiXinPayService
{

    private WXPay wxpay;


    private WXPayConfigImpl wxPayConfigImpl;


    public WeiXinPayServiceImpl() throws java.lang.Exception {
        wxPayConfigImpl =  WXPayConfigImpl.getInstance();
        wxpay = new WXPay(wxPayConfigImpl);
    }

    //获取用户的OpenID
    @Override
    public String getOpenId(String userCode)throws IOException
    {
        try {
            HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/sns/jscode2session?appid=" + wxPayConfigImpl.getAppID() + "&secret=" + wxPayConfigImpl.getAppSecret() + "&js_code=" + userCode + "&grant_type=authorization_code");
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

    //小程序 统一下单接口,返回wx.requestPayment接口所需的5个参数
    // timeStamp、nonceStr、package（prepay_id）、signType、paySign
    @Override
    public Map<String, String> doUnifiedOrderForMicroApp(String openId, String orderIndex,
                                                         String totalFee, String remoteIpAddress)throws IOException
    {
        System.out.println("小程序支付");
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("openid", openId);
        data.put("appid", wxPayConfigImpl.getAppID());
        data.put("body", "云南百江燃气公司订气业务");
        //加上系统时间
        orderIndex = orderIndex+"x"+WXPayUtil.getCurrentTimestamp();
        data.put("out_trade_no", orderIndex);
        data.put("device_info", "");
        data.put("fee_type", "CNY");
        data.put("total_fee", totalFee);
        data.put("spbill_create_ip", remoteIpAddress);
        data.put("trade_type", "JSAPI");
        data.put("notify_url", wxPayConfigImpl.getNotifyUrl());
        //data.put("product_id", "12");
        // data.put("time_expire", "20170112104120");
        HashMap<String, String> result = new HashMap<String, String>();
        try {
            Map<String, String> response = wxpay.unifiedOrder(data);
            if (response.containsKey("prepay_id")) {
                result.put("appId", wxPayConfigImpl.getAppID());
                result.put("timeStamp", String.valueOf(WXPayUtil.getCurrentTimestamp()));
                result.put("nonceStr", WXPayUtil.generateUUID());
                result.put("package", "prepay_id="+response.get("prepay_id"));
                result.put("signType", WXPayConstants.HMACSHA256);
                result.put("paySign", WXPayUtil.generateSignature(result,wxPayConfigImpl.getKey(), WXPayConstants.SignType.HMACSHA256));

            }else {
                //统一下单失败
                if (response.containsKey("err_code_des")) {
                    System.out.println("err_code_des");
                    throw new ServerSideBusinessException("统一下单失败！"+response.get("err_code_des"), HttpStatus.NOT_ACCEPTABLE);
                }
            }
            return result;
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            result.clear();
            return result;
        }
    }

    //扫码支付 统一下单接口,返回code_url
    @Override
    public String doUnifiedOrderForScan(String orderIndex, String totalFee, String payCode)throws IOException
    {
        System.out.println("扫码支付 ");
        HashMap<String, String> data = new HashMap<String, String>();

        data.put("body", "云南百江燃气公司订气业务");
        //加上系统时间
        orderIndex = orderIndex+"x"+WXPayUtil.getCurrentTimestamp();
        data.put("out_trade_no", orderIndex);
        data.put("device_info", "");
        data.put("fee_type", "CNY");
        data.put("total_fee", totalFee);
        data.put("spbill_create_ip", wxPayConfigImpl.getSeverIP());
        data.put("trade_type", "NATIVE");
        data.put("product_id", "12");
        data.put("notify_url", wxPayConfigImpl.getNotifyUrl());
        data.put("mch_id", wxPayConfigImpl.getmachIDByCompanyCode(payCode));
        data.put("appid", wxPayConfigImpl.getOfficialIDByCompanyCode(payCode));
        data.put("sub_mch_id", wxPayConfigImpl.getSubMchID());
        // data.put("time_expire", "20170112104120");

        try {
            Map<String, String> response = wxpay.unifiedOrder(data);
            if (response.containsKey("code_url")) {
                String code_url = response.get("code_url");
                return code_url;
            }else {
                //统一下单失败
                if (response.containsKey("err_code_des")) {
                    System.out.println("err_code_des");
                }
                return null;
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //公众号退款 out_trade_no-退款订单号 total_fee-退款金额
    @Override
    public boolean doRefundOffical(String out_trade_no, String total_fee) {

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", wxPayConfigImpl.getOfficialID());
        data.put("out_refund_no", WXPayUtil.generateNonceStr());

        data.put("out_trade_no", out_trade_no);
        data.put("out_refund_no", out_trade_no);
        data.put("total_fee", total_fee);
        data.put("refund_fee", total_fee);
        data.put("refund_fee_type", "CNY");


        try {
            Map<String, String> r = wxpay.refund(data);
            if(r.containsKey("result_code")) {
                if (r.get("result_code").equals("SUCCESS")) {
                    return true;
                }
            }
            return false;
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    //小程序退款 out_trade_no-退款订单号 total_fee-退款金额
    @Override
    public boolean doRefundMicroApp(String out_trade_no, String total_fee) {

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", wxPayConfigImpl.getAppID());
        data.put("out_refund_no", WXPayUtil.generateNonceStr());


        data.put("out_trade_no", out_trade_no);
        data.put("out_refund_no", out_trade_no);
        data.put("total_fee", total_fee);
        data.put("refund_fee", total_fee);
        data.put("refund_fee_type", "CNY");


        try {
            Map<String, String> r = wxpay.refund(data);
            if(r.containsKey("result_code")) {
                if (r.get("result_code").equals("SUCCESS")) {
                    return true;
                }
            }
            return false;
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //支付结果通知接口
    @Override
    public Map<String, String> payNotify(String notify_msg) {

        HashMap<String, String> result = new HashMap<String, String>();
        try {
        Map<String, String> notifyMap = WXPayUtil.xmlToMap(notify_msg);  // 转换成map
            if (true/*wxpay.isPayResultNotifySignatureValid(notifyMap)*/) {
                result.put("out_trade_no", notifyMap.get("out_trade_no"));
                result.put("total_fee", notifyMap.get("total_fee"));
                return result;
            }
            else {
                return null;
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
