package com.shoushuzhitongche.app.view;

import android.content.Intent;
import com.dixiang.framework.BaseApplication;
import com.shoushuzhitongche.app.common.DBManager;

public class MYApplication extends BaseApplication{
	public static float density ,heightPixels , widthPixels;//依次屏幕密度， 屏幕高度，屏幕宽度
	public static int startheight; //状态栏高度
	private final static String ACTION = "com.loacaldata.service";
	@Override
	public void onCreate() {
		super.onCreate();
		new DBManager(this).removeDatabase();
		initLoaclData();
	}
	
	private void initLoaclData(){
		Intent intent = new Intent();
		intent.setAction(ACTION);
		intent.setPackage(getPackageName());
		startService(intent);
	}
	
}
