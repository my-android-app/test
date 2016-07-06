package com.shoushuzhitongche.app.view.localdata.server;

import java.util.Map;
import com.dixiang.framework.common.IDataLoadListenterByFlg;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.http.DataUtils;
import com.dixiang.framework.utils.SharedPreferenceUtils;
import com.dixiang.framework.utils.Utils;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.view.localdata.bean.LocalDataEntity;
import com.shoushuzhitongche.app.view.localdata.dao.LocalDataDao;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class DataCheckService extends Service{
	private Context mContext;
	public final static int GET_CITY = 1;
	public final static int GET_LOCAL_DATA = 2;
	public final static int SUCCESS = 3;
	public String localUrl = "";
	
	private int sum = 0;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
				CommonUtils.saveLocalData(mContext, true);
				stopSelf();
				break;	
			default:
				break;
			}
		}
		
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	private void getLocalData(){
		String actionUrl = localUrl;
		DataUtils.getHttpGet(mContext, actionUrl, loadListenter, Constants.HTTP_INDEX.First, new TypeToken<QjResult<LocalDataEntity>>() {}, Constants.SERVER_KEY);
	}
	
	private IDataLoadListenterByFlg loadListenter = new IDataLoadListenterByFlg(){
		public void onSuccess(QjResult result, String flg) {
			if(flg.equals(Constants.HTTP_INDEX.First)){
				LocalDataEntity lDataEntity = (LocalDataEntity) result.getResults();
				if(lDataEntity != null ){
					LocalDataDao.newInstance(mContext).saveLocalData(lDataEntity,handler);
				}
			}else if(flg.equals(Constants.HTTP_INDEX.Second)){
				Map<String , Object> map = (Map<String, Object>) result.getResults();
				String them =String.valueOf(SharedPreferenceUtils.getInstance(mContext, Constants.APPINFO).get(Constants.LOACLE_DATA_TIME));
				String value = String.valueOf(map.get("version"));
				String localdataUrl = String.valueOf(map.get("localdataUrl"));
				if((!Utils.isStringEmpty(them) && !value.equals(them)) || Utils.isStringEmpty(them)){
					SharedPreferenceUtils.getInstance(mContext, Constants.APPINFO).save(Constants.LOACLE_DATA_TIME, value);
					SharedPreferenceUtils.getInstance(mContext, Constants.APPINFO).save(Constants.LOACLE_DATA,false);
					localUrl = localdataUrl;
					getLocalData();
				}
			}
		};
		
		@Override
		public void onError(Exception ex, Object result, String flg) {
			super.onError(ex, result, flg);
			stopSelf();
		}

		@Override
		public void onFailure(QjResult result, String flg) {
			super.onFailure(result, flg);
			stopSelf();
		}

		@Override
		public void onCompleted(Exception e, String flg) {
			super.onCompleted(e, flg);
		}

		@Override
		public void onHttpError(int msg , String flg) {
			super.onHttpError(msg);
			onMaxFailure(flg);
		}
	};
	
	private void onMaxFailure(String flg){
		sum++;
		if (sum >= 6 ){
			stopSelf();
		}else if(flg.equals(Constants.HTTP_INDEX.First)){
			getLocalData();
		}else if(flg.equals(Constants.HTTP_INDEX.Second)){
			checkDataversion();
		}
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.e("====", "service onstart");
		checkDataversion();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("====", "service onDestroy");
	}
	
	//检测本地数据更新
    private void checkDataversion(){
    	String url = CommonUtils.getDataVersion(getApplicationContext());
		DataUtils.getHttpGet(this, url, loadListenter, Constants.HTTP_INDEX.Second, new TypeToken<QjResult<Map<String , Object>>>() {}, Constants.SERVER_KEY);
    }
	
	
}
