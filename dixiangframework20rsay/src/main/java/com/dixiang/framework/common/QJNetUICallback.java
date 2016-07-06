package com.dixiang.framework.common;

import android.content.Context;
import com.dixiang.framework.network.NetUICallback;

public abstract class QJNetUICallback  extends NetUICallback {

	public QJNetUICallback(Context context) {
		super(context);
	}
	
	public QJNetUICallback(Context context , String key) {
		super(context,key);
	}
	
	
	public void onCompleted(Exception e,
			Object result){
	}
}
