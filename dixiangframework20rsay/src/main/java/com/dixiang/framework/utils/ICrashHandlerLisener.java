package com.dixiang.framework.utils;

import android.content.Context;

public interface ICrashHandlerLisener {
	/**
	 * app闪退回调处理方法
	 * @param context
	 */
	public void crashHanderCallBack(Context context);
}
