package com.shoushuzhitongche.app.utls;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.dixiang.framework.common.QJNetUICallback2;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.network2.NetOld;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

public class UploadUtils {
	public final static String PATIENT_QN_TOKEN = "http://file.mingyizhudao.com/api/tokendrcert";//医生证明    获取上传七牛的token
	
	public final static String PATIENT_MR_QN_TOKEN = "http://file.mingyizhudao.com/api/tokenpatientmr";//医生端上传患者预约病历    获取上传七牛的token

	public final static String PATIENT_SAVE_APP_FILE = "http://file.mingyizhudao.com/api/savepatientmr";//保存文件信息到自己服务器  类型mr(病历 默认)dc(出院小结 暂无)
	
	public final static String PATIENT_SAVE_APP_CER_FILE = "http://file.mingyizhudao.com/api/savedrcert";//保存文件信息到自己服务器  医生证明

	
	public final static String PATIENTAPP_FILE_CER_URL = "http://file.mingyizhudao.com/api/loaddrcert";//从自己服务器获取图片路径  医生证明
	
	public final static String PATIENTAPP_FILE_URL = "http://file.mingyizhudao.com/api/loadpatientmr";//从自己服务器获取图片路径  
	
	
	private StringBuffer sb = new StringBuffer();
	private String pid ="";
	String key = "";
	public UploadUtils(String id) {
		pid = id;
	}

	public void uploadFile( Activity activity, String url, final File file, final OnLoadListener listener) {
		//获取token
		HashMap<String, Object> map = new HashMap<String, Object>();
		QJNetUICallback2 callback = new QJNetUICallback2<QjResult<HashMap<String, String>>>(activity.getApplicationContext()) {
			@Override
			public void onSuccess(QjResult<HashMap<String, String>> result) {
				if (result != null) {
					//获取成功进行上传
					String token = result.getResults().get("uploadToken");
					String remoteDomain = result.getResults().get("remoteDomain");
					qiNiuLoad(remoteDomain ,file, token,-1, new OnQiNiuLoadListener() {
						@Override
						public void onLoadSuccess(String remoteDomain,String key ,String str, int index) {
							listener.onLoadSuccess(remoteDomain,key ,str);
						}

						@Override
						public void onLoadFail(int index) {
							listener.onLoadFail();
						}
					});
				}
			}

			public void onError(Exception e, QjResult<HashMap<String, String>> result) {
				super.onError(e, result);
				if (listener != null) {
					listener.onLoadFail();

				}
			}

		};
//		String path = new DataHandler().postDataObject(activity, url,null, new TypeToken<QjResult<HashMap<String, String>>>() {}, callback);

		NetOld.with(activity).fetch( url, null, new TypeToken<QjResult<HashMap<String, String>>>() {}, callback);
	}

	public void uploadFiles(final Activity activity, final String url, final List<File> files, final OnLoadListener listener) {
		//获取token
		//HashMap<String, Object> map = new HashMap<String, Object>();
		QJNetUICallback2 callback = new QJNetUICallback2<QjResult<HashMap<String, String>>>(activity.getApplicationContext()) {
			@Override
			public void onSuccess(QjResult<HashMap<String, String>> result) {
				if (result != null) {
					//获取成功进行上传
					String token = result.getResults().get("uploadToken");
					String remoteDomain = result.getResults().get("remoteDomain");
					qiNiuUploadFiles(remoteDomain ,files, token, 0, listener);
				}
			}

			public void onError(Exception e, QjResult<HashMap<String, String>> result) {
				super.onError(e, result);
				if (listener != null) {
					listener.onLoadFail();
				}
			}
			
			public void onCompleted(Exception e, QjResult<HashMap<String, String>> result) {
				super.onCompleted(e, result);
			}

		};
		
//		String path = new DataHandler().postDataObject(activity, SystemConfig.QN_TOKEN,null, new TypeToken<QjResult<HashMap<String, String>>>() {}, callback);
		NetOld.with(activity).fetch( url, null, new TypeToken<QjResult<HashMap<String, String>>>() {}, callback);

	}




	private void qiNiuLoad(final String remoteDomain ,final File file, String token, final int index, final OnQiNiuLoadListener listener) {

		UploadManager manager = new UploadManager();

		if (file.getName().endsWith(".png")) {
			key =pid+ "_"+ System.currentTimeMillis()+".png";
		}else {
			key =pid+ "_"+ System.currentTimeMillis()+".jpg";
		}
		manager.put(file, key, token, new UpCompletionHandler() {
			@Override
			public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
				if (responseInfo.isOK()) {
					//上传成功
					if (listener != null) {
						String result = null;

						try {
							Log.e("========",jsonObject.toString());
							result = String.valueOf(jsonObject.get("hash"));
							Log.e("uploadFiles", String.valueOf(jsonObject.get("hash")));

						} catch (JSONException e) {
							e.printStackTrace();
						}
						listener.onLoadSuccess(remoteDomain , key ,result.toString(), index);
					}
				} else {
					if (listener != null) {
						listener.onLoadFail(index);
					}   
				}
			}

		}, null);
	}

	private void qiNiuUploadFiles(String remoteDomain , final List<File> files, final String token, int index, final OnLoadListener listener) {
		qiNiuLoad( remoteDomain ,files.get(index), token, index, new OnQiNiuLoadListener() {
			@Override
			public void onLoadSuccess(String remoteDomain ,String key, String str, int successIndex) {
				listener.onLoadSuccess(remoteDomain ,key,str,successIndex);
				if (successIndex < files.size() - 1) {
					//递归
					sb.append(str + ",");
					qiNiuUploadFiles(remoteDomain,files, token, successIndex + 1, listener);

				} else {
					sb.append(str);
					//Log.e("uploadFilesss------------", sb.toString());
					listener.onLoadSuccess(remoteDomain,key,sb.toString());
				}
			}

			@Override
			public void onLoadFail(int failIndex) {
				listener.onLoadFail();
			}

		});
	}

	public interface OnLoadListener {
		void onLoadSuccess(String remoteDomain, String key, String str);
		void onLoadSuccess(String remoteDomain, String key, String str, int index);
		void onLoadFail();
	}
	public interface OnQiNiuLoadListener {
		void onLoadSuccess(String remoteDomain, String key, String str, int index);

		void onLoadFail(int index);
	}
}
