package com.dixiang.framework.http;

import java.util.Map;
import android.content.Context;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.IDataLoadListenterByFlg;
import com.google.gson.reflect.TypeToken;

public class DataUtils {
	
	public static void getHttpGet(Context context,String url , IDataLoadListenterByFlg loadListenter,String flg ,TypeToken typeToken,String key){
		doHttpGet(context, url, loadListenter,flg,key,typeToken);
	}
	
	public static void getHttpGet(BaseActivity activity,String url , IDataLoadListenterByFlg loadListenter,String flg ,TypeToken typeToken){
		doHttpGet(activity.getApplicationContext(), url, loadListenter,flg,activity.getClass().getSimpleName(),typeToken);
	}
	
	public static void postHttp(BaseActivity activity,String url , Map map , IDataLoadListenterByFlg loadListenter,String flg ,TypeToken typeToken){
		HttpRequest.doHttpPost(activity, url, map , flg, loadListenter,typeToken);
	}
	
	public static void putHttp(BaseActivity activity,String url , StringBuffer buffer , IDataLoadListenterByFlg loadListenter,String flg ,TypeToken typeToken){
		HttpRequest.doHttpPut(activity, url, buffer ,flg , loadListenter,typeToken);
	}
	
	private static void doHttpGet(Context context, String url, IDataLoadListenterByFlg loadListenter,String flg ,String key,TypeToken typeToken){
		HttpRequest.doHttpGet(context, url, flg , loadListenter,key,typeToken);
	}
}

