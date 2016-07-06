package com.dixiang.framework.utils;

import java.util.HashMap;

import com.dixiang.framework.common.QjResult;

public interface ILoadListener {
	public void onSuccess(Object object, String flg);
	public void onError(Exception ex,QjResult<HashMap<String, String>> result);
}
