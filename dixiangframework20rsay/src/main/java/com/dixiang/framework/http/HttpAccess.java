package com.dixiang.framework.http;

import com.dixiang.framework.common.QJNetUICallback;
import com.dixiang.framework.network.Net;
import com.google.gson.reflect.TypeToken;

public class HttpAccess {

	public static void doHttpRequest(String url, Object params, TypeToken typeToken,
			QJNetUICallback callback ,String flg) {
		Net.fetch(url, params, typeToken, callback , flg);
	}
}
