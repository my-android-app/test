package com.dixiang.framework.common;

import android.content.Context;
import com.dixiang.framework.network.IResult;
import com.dixiang.framework.network2.NetUICallback;
import com.dixiang.framework.widget.LoadingDialog;

public abstract class QJNetUICallback2<T extends IResult> extends NetUICallback<T>{

	public QJNetUICallback2(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
    /**
     * 访问回调
     *
     * @param e
     * @param result
     */
    @Override
    public void onCompleted(Exception e, T result) {
        LoadingDialog.getInstance(mContext).dismiss();
        if (e == null && result != null) {
            if ("ok".equals(result.status())) {
                //亲近app如果code为0，代表无错误
                onSuccess(result);
                onSuccess(result, requestFlag);
            }else{
            	onError(e, result);
                onError(e,result,requestFlag);
            }
        } else {
            onError(e, result);
            onError(e,result,requestFlag);
        }
    }

}
