package com.donno.nj.util.WeiXinPaySdk;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.HashMap;
public class WXPayConfigImpl extends WXPayConfig{

    private byte[] certData;
    private static WXPayConfigImpl INSTANCE;
    private static Map<String,String>m_officialIDMap;//公众号ID MAP
    static
    {
        m_officialIDMap = new HashMap<String, String>();
        m_officialIDMap.put("LJ-PAY", "wx79b9da605c0e56bd");//丽江公众号
        m_officialIDMap.put("KM-PAY", "wxa0520852c6131227");//昆明公众号
        m_officialIDMap.put("QJ-PAY", "wxa0520852c6131227");//曲靖公众号
    }

    private static Map<String,String>m_mchIDMap;//商户号ID MAP
    static
    {
        m_mchIDMap = new HashMap<String, String>();
        m_mchIDMap.put("LJ-PAY", "1509570551");//丽江商户号
        m_mchIDMap.put("KM-PAY", "1313996901");//昆明商户号
        m_mchIDMap.put("QJ-PAY", "1313996901");//曲靖商户号
    }


    private WXPayConfigImpl() throws Exception{
//        String certPath = "D://apiclient_cert.p12";
//        File file = new File(certPath);
//        InputStream certStream = new FileInputStream(file);
//        this.certData = new byte[(int) file.length()];
//        certStream.read(this.certData);
//        certStream.close();
    }

    public static WXPayConfigImpl getInstance() throws Exception{
        if (INSTANCE == null) {
            synchronized (WXPayConfigImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPayConfigImpl();
                }
            }
        }
        return INSTANCE;
    }

    public String getAppID() {
        return "wxba30d188da9e1764";
    }//用户端小程序APPID
    public String getAppSecret() {
        return "c1c9b7a09f220ecd9ebc0babc8472406";
    }//用户端小程序Secret
//    public String getOfficialID() {
//        return "wxa0520852c6131227";
//    }//公众号APPID
    public String getOfficialID() {
    return "wx79b9da605c0e56bd";
}//公众号APPID
//    public String getMchID() {
//        return "1313996901";
//    }//商户号
    public String getMchID() {
        return "1509570551";
    }//商户号
    public String getSubMchID() {
        return "1509646181";
    }//sub商户号
    public String getKey() {
        return "panva95007panva95007panva95007PA";
    }//商户号秘钥
    public String getSeverIP() {
        return "120.78.241.67";
    }//服务器IP
    public String getNotifyUrl() {
        return "https://www.yunnanbaijiang.com/api/pay/notify";
    }//服务器回调地址

    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }


    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    IWXPayDomain getWXPayDomain() {
        return WXPayDomainSimpleImpl.instance();
    }

    public String getPrimaryDomain() {
        return "api.mch.weixin.qq.com";
    }

    public String getAlternateDomain() {
        return "api2.mch.weixin.qq.com";
    }

    @Override
    public int getReportWorkerNum() {
        return 1;
    }

    @Override
    public int getReportBatchSize() {
        return 2;
    }


    public String getOfficialIDByCompanyCode(String companyCode) {
        return m_officialIDMap.get(companyCode);
    }//公众号APPID

    public String getmachIDByCompanyCode(String companyCode) {
        return m_mchIDMap.get(companyCode);
    }//商户号APPID
}
