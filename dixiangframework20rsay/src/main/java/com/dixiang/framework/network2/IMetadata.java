package com.dixiang.framework.network2;

import com.dixiang.framework.network.IResult;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;

import java.util.Map;

public interface IMetadata<T extends IResult> {

    String POST = "POST";
    String GET = "GET";

    String getUrl();

    Map<String, Object> getParameters();

    Map<String, Object> getMultiParameters();

    String getMethod();

    NameValuePair[] getHeader();

    String getUserAgent();

    TypeToken<T> getType();

}
