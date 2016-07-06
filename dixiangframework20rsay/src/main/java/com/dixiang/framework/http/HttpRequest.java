package com.dixiang.framework.http;

import java.util.Map;
import android.content.Context;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.IDataLoadListenterByFlg;
import com.dixiang.framework.common.QJNetUICallback;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.http.HttpHandler;
import com.google.gson.reflect.TypeToken;

public class HttpRequest{
	
	public static void  doHttpGet(Context context , String url, String flg ,final IDataLoadListenterByFlg listenter ,String key, TypeToken typeToken){
		doHttpRequest(context, url, "", flg, listenter, key, typeToken);
	}
	
	public static void  doHttpPost(BaseActivity activity , String url, Map<String,Object> map , String flg ,final IDataLoadListenterByFlg listenter ,TypeToken typeToken){
		doHttpRequest(activity.getApplicationContext(), url, map, flg, listenter, activity.getClass().getSimpleName(), typeToken);
	}
	
	public static void  doHttpPut(BaseActivity activity , String url, StringBuffer stringBuffer, String flg ,final IDataLoadListenterByFlg listenter ,TypeToken typeToken){
		doHttpRequest(activity.getApplicationContext(), url, stringBuffer, flg, listenter, activity.getClass().getSimpleName(), typeToken);
	}
	
	public static void doHttpRequest(Context context , String url , Object params, String flg ,final IDataLoadListenterByFlg listenter,String key ,TypeToken typeToken ){
		QJNetUICallback callback = new QJNetUICallback(
				context,key) {
			
			@Override
			public void onSuccess(Object result, String flg) {
					QjResult object = (QjResult) result;
					listenter.onSuccess(object, flg);
			}
			
			@Override
			public void onSuccess(Object result) {
				super.onSuccess(result);
				QjResult object = (QjResult) result;
				listenter.onSuccess(object);
			}
			
			@Override
			public void onFailure(Object result, String flg) {
				super.onFailure(result, flg);
				QjResult object = (QjResult) result;
				listenter.onFailure(object, flg);
			}
			
			@Override
			public void onFailure(Object result) {
				super.onFailure(result);
				QjResult object = (QjResult) result;
				listenter.onFailure(object);
			}
			
			
			@Override
			public void onHttpError(int msg) {
				super.onHttpError(msg);
				listenter.onHttpError(msg);
			}
			
			@Override
			public void onHttpError(int msg, String flg) {
				super.onHttpError(msg, flg);
				listenter.onHttpError(msg, flg);
			}
			
			@Override
			public void onError(Exception ex, Object result, String flg) {
				super.onError(ex, result, flg);
				listenter.onError(ex, result, flg);
			}
			
			@Override
			public void onError(Exception ex, Object result) {
				super.onError(ex, result);
				listenter.onError(ex, result);
			}
			
			@Override
			public void onCompleted(Exception e, String flg) {
				super.onCompleted(e, flg);
				listenter.onCompleted(e, flg);
			}
			
		};
		
		HttpHandler.getInitial().doHttpRequest(url, params, typeToken, callback, flg);
		
	}
}
