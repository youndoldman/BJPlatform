package com.donno.nj.util;

public class Configure {
	private static String key = "你的商户的api秘钥";

	//小程序ID	
	private static String appID = "wxb137ebfa3dc90901";
	//商户号
	private static String mch_id = "你的商户号";
	//
	private static String secret = "4d6605c2daf5c9ed8066e4edbd412272";

	//服务器IP
	private static String server_ip = "117.1.1.1";

	public static String getServerIp() {
		return server_ip;
	}

	public static void setServerIp(String server_ip) {
		Configure.server_ip = server_ip;
	}

	public static String getSecret() {
		return secret;
	}

	public static void setSecret(String secret) {
		Configure.secret = secret;
	}

	public static String getKey() {
		return key;
	}

	public static void setKey(String key) {
		Configure.key = key;
	}

	public static String getAppID() {
		return appID;
	}

	public static void setAppID(String appID) {
		Configure.appID = appID;
	}

	public static String getMch_id() {
		return mch_id;
	}

	public static void setMch_id(String mch_id) {
		Configure.mch_id = mch_id;
	}

}
