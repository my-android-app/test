package com.dixiang.framework.network;
import com.dixiang.framework.config.SystemConfig;
import com.dixiang.framework.utils.Utils;

public class URLHandler {
	public static String getUrl_API(String url){
		if(!url.contains("?")){
			url+="?api="+SystemConfig.API;
		}else if(url.contains("?")){
			url+="&api="+SystemConfig.API;
		}
		return url;
	}
	

	public static String finalUrl(String url){
		return  url+SystemConfig.PARAM_SIGN+Utils.Md5String(url+SystemConfig.API_KEY);
	}
	
	/**
	 * get 请求最终地址
	 * @param url
	 * @return
	 */
	public static String getEncryptUrl(String url){
		if(!SystemConfig.IS_MD5){
			return url;
		}
		StringBuffer buffer = new StringBuffer(getUrl_API(url));
		buffer.append(SystemConfig.API_ID);
		buffer.append(SystemConfig.PARAM_TIME+System.currentTimeMillis()/1000);
		return finalUrl(buffer.toString());
	}
	
	/**
	 * post请求拼接地址
	 * @param url
	 * @return
	 */
	public static String getPostEncryptUrl(String url){
		StringBuffer buffer = new StringBuffer(getUrl_API(url));
		buffer.append(SystemConfig.API_ID);
		buffer.append(SystemConfig.PARAM_TIME+System.currentTimeMillis()/1000);
		return buffer.toString();
	}
	
	/**
	 * 生成sign参数
	 * @param url
	 * @return
	 */
	public static String getPostSignParam(String url){
		return Utils.Md5String(url+SystemConfig.API_KEY);
	}
}
