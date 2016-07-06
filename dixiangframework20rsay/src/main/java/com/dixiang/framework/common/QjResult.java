package com.dixiang.framework.common;

import com.dixiang.framework.network.IResult;
import com.google.gson.Gson;

public class QjResult<T> implements IResult<T> {


	private String status; 
	 
	private String errorCode;
	
	private String errorMsg;
	
	private T results;

    public String getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


	@Override
    public String errorCode() {
        return getErrorCode();
    }


    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String status() {
		return status;
	}

	@Override
	public String errorMsg() {
		// TODO Auto-generated method stub
		return errorMsg;
	}
    
}
