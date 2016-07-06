package com.dixiang.framework.network;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.config.SystemConstants;
import com.dixiang.framework.utils.KeyValue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Handler;

public class HttpAsyncTask extends AsyncTask{
	protected Fragment fragment;
	private final static int HTTP_SCCUESS = 0;
	private final static int HTTP_FAILURE = 1;
	private final static int HTTP_ERROR = 2;
	private final static int HTTP_UNKNOWN = 3;
	
    public HttpAsyncTask() {
    }
    
    public HttpAsyncTask(Fragment fragment) {
    	this.fragment = fragment;
    }
    
	@Override
	protected Object doInBackground(Object... arg0) {
		NetUICallback httpLoader = null;
		String flg = "0";
		try{
			String httpUrl = arg0[0].toString().trim();
			Object param = arg0[1];
			TypeToken typeToken  = ((TypeToken)arg0[2]);
			httpLoader = (NetUICallback)arg0[3];
			
			if(arg0.length > 4) {
				flg = arg0[4]+"";
			}
			
			KeyValue keyValue = null;
			if(param instanceof String  || param == null){
				String paramStr = null;
				keyValue = HttpUtils.doGet(httpUrl, null);
			}else if(param instanceof Map){
				String paramStr = fixPostParam((Map<String,Object>)param);
				keyValue = HttpUtils.doPost(httpUrl, paramStr);
			}else if(param instanceof StringBuffer){
				String paramStr = param.toString();
				keyValue = HttpUtils.doPut(httpUrl, paramStr);
			}
			parseResult(typeToken ,keyValue ,httpLoader ,flg);
			
		}catch(Exception ex){
			ex.printStackTrace();
			httpLoader.onError(ex,null,flg);
			httpLoader.onCompleted(null, flg);
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
	}
	
	private <T> QjResult<T> parseResult(TypeToken typeToken ,KeyValue keyValue ,final NetUICallback httpLoader ,final String flg){
		
		Gson gson = new Gson();  
		java.lang.reflect.Type type = typeToken.getType();  
		  
		if(keyValue.first == HTTP_SCCUESS){
			String json = keyValue.second+"";
			final QjResult<T> result = gson.fromJson(json, type);
			if (result != null && "ok".equals(result.getStatus())){
				Handler handler = SystemConstants.getHandler();
				if(handler != null && SystemConstants.isHandlerExist(httpLoader.getKey())){
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							httpLoader.onSuccess(result,flg);
							httpLoader.onSuccess(result);
						}
					});
					handler = null;
				}else if(SystemConstants.isServerHandler(httpLoader.getKey())){
					httpLoader.onSuccess(result,flg);
					httpLoader.onSuccess(result);
				}
			}else if(result != null && "no".equals(result.getStatus())){
				httpLoader.onFailure(result,flg);
				httpLoader.onFailure(result);
			}else if(result == null){
				httpLoader.onFailure(result,flg);
				httpLoader.onFailure(result); 
			}
		}else if(keyValue.first == HTTP_FAILURE){
			httpLoader.onFailure(null,flg);
			httpLoader.onFailure(null);
		}else if(keyValue.first == HTTP_ERROR){
			httpLoader.onError((Exception)keyValue.second,null, flg);
			httpLoader.onError((Exception)keyValue.second,null);
		}else{
//			httpLoader.onError(null,, flg);
//			httpLoader.onError((Exception)keyValue.second,null);
			httpLoader.onHttpError(keyValue.first);
			httpLoader.onHttpError(keyValue.first,flg);
		}
		httpLoader.onCompleted(null, flg);
		return null;
	}
	
	public boolean isNumeric(String str){ 
	   Pattern pattern = Pattern.compile("[0-9]*"); 
	   Matcher isNum = pattern.matcher(str);
	   if( !isNum.matches() ){
	       return false; 
	   } 
	   return true; 
	}
	
	private String fixPostParam(Map<String,Object> map) throws Exception{
		
		if(map != null && !map.isEmpty()){
			String key = map.get("key")+"";
			map.remove("key");
			StringBuffer stringBuffer = new StringBuffer("{");
			stringBuffer.append("\"");
			stringBuffer.append(key);
			stringBuffer.append("\":");
			stringBuffer.append("{");
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				stringBuffer.append("\"");
				stringBuffer.append(entry.getKey());
				stringBuffer.append("\":\"");
				stringBuffer.append(entry.getValue()+"\"");
				stringBuffer.append(",");
			}
			return stringBuffer.substring(0, stringBuffer.length()-1) +"}}";
		}
		return "";
	}
}
