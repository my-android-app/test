package com.dixiang.framework.network2;

import org.apache.http.NameValuePair;

import com.dixiang.framework.network.IResult;

public abstract class BaseMetadata<T extends IResult> implements IMetadata<T> {

    @Override
    public String getMethod() {
        return POST;
    }

    @Override
    public NameValuePair[] getHeader() {
        return new NameValuePair[0];
    }

    @Override
    public String getUserAgent() {
        return null;
    }

}
