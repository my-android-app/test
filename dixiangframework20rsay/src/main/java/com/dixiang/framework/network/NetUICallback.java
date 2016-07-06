package com.dixiang.framework.network;

import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.config.SystemConstants;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 网络回调抽象
 *
 * @param <T> 实现IResult接口
 * @author solo
 */
public abstract class NetUICallback  implements INetCallback  {

    private static final String TAG = NetUICallback.class.getSimpleName();
    private Context context;
    private final static int HTTP_SCCUESS = 0;
	private final static int HTTP_FAILURE = 1;
	private final static int HTTP_ERROR = 2;
	private final static int HTTP_UNKNOWN = 3;
	private String key;
	
    public NetUICallback(Context context) {
    	this.context = context;
    }
    
    public NetUICallback(Context context , String key) {
    	this.context = context;
    	this.key = key;
    }

    public void onSuccess(Object result ,String flg){}
    public void onSuccess(Object result ){}
    
    public void onFailure(Object result , String flg){
		showInfo(null, result, 0, HTTP_FAILURE);
	}
    public void onFailure(Object result ){}
    
	public void onError(Exception ex ,Object result , String flg){
		showInfo(ex, null, 0, HTTP_ERROR);
	}
	public void onError(Exception ex ,Object result ){
	}
	
	
	public void onHttpError(int msg){
		showInfo(null, null, msg, HTTP_UNKNOWN);
	}
	public void onHttpError(int msg,String flg){
	}
	
	public void onCompleted(Exception e, String flg) {
		
	}
	
	private void showInfo(final Exception ex, final Object result, final int msg, final int type){
		Handler handler = SystemConstants.getHandler();
		if(handler != null && SystemConstants.isHandlerExist(key)){
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					switch (type) {
						case HTTP_FAILURE:
							if(result != null){
								QjResult<Object> qjResult = (QjResult<Object>)result;
								showErrorAtBottom(qjResult.errorMsg());
							}
							break;
						case HTTP_ERROR:
							if(ex != null){
								showErrorAtBottom(ex.toString());
							}
							break;
						case HTTP_UNKNOWN:
							showHttpError(msg);
							break;
						default:
							break;
					}
				}
			});
		}
		
	}
	
	private void showErrorAtBottom(final String msg){
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();	
	}
	
	private void showHttpError(final int msg){
			Toast.makeText(context, "网络访问错误："+msg, Toast.LENGTH_LONG).show();	
	}
	
	public String getKey(){
		return this.key;
	}
}
