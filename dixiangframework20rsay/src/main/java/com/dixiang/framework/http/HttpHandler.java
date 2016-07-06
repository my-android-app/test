package com.dixiang.framework.http;

import com.dixiang.framework.common.QJNetUICallback;
import com.google.gson.reflect.TypeToken;

public class HttpHandler implements IDataInterface{
	
	private static HttpHandler httpHandler;
	public static HttpHandler getInitial() {
		if(httpHandler == null){
			httpHandler = new HttpHandler();
		}
		return httpHandler;
	}

	public HttpHandler(){
		
	}
	public void doHttpRequest(String url, Object params, TypeToken typeToken,
			QJNetUICallback callback, String flg) {
		HttpAccess.doHttpRequest(url, params, typeToken, callback, flg);
	}
}
