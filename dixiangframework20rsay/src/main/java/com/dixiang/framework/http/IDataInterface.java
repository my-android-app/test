package com.dixiang.framework.http;

import com.dixiang.framework.common.QJNetUICallback;
import com.google.gson.reflect.TypeToken;

public interface IDataInterface {
	public abstract void doHttpRequest( String url , Object params , TypeToken typeToken, QJNetUICallback callback ,String flg);
}
