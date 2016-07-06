package com.dixiang.framework.common;

import com.dixiang.framework.common.QjResult;


public abstract class IDataLoadListenterByFlg {
	public void onSuccess(QjResult result, String flg){};
	public void onSuccess(QjResult result){};
	
	public void onError(Exception ex,Object result, String flg){};
	public void onError(Exception ex,Object result){};
	
	public void onFailure(QjResult result, String flg){};
	public void onFailure(QjResult result){};
    
	public void onCompleted(Exception e, String flg) {}
    public void onHttpError(int msg){};
    public void onHttpError(int msg,String flg){};
}
