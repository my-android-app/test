package com.dixiang.framework.network2;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import com.dixiang.framework.network.IResult;
import com.dixiang.framework.widget.LoadingDialog;

/**
 * 网络回调抽象
 *
 * @param <T> 实现IResult接口
 * @author solo
 */
public abstract class NetUICallback<T extends IResult> implements INetCallback<T> {

    private static final String TAG = NetUICallback.class.getSimpleName();
    protected Context mContext;
    protected String requestFlag;

    public NetUICallback(Context context) {
        mContext = context;
        this.requestFlag = getRequestFlag();
    }

    protected String getRequestFlag(){return "";}

    /**
     * 访问回调
     *
     * @param e
     * @param result
     */
    @Override
    public void onCompleted(Exception e, T result) {
    	LoadingDialog.getInstance(mContext).dismiss();
//        if (e == null && result != null && result.getState()) {
        if (e == null && result != null && result.status().equals("ok")) {
            //亲近app如果code为0，代表无错误
            onSuccess(result);
            onSuccess(result, requestFlag);
        } else {
//            e.printStackTrace();
            onError(e, result);
            onError(e, result,requestFlag);
        }
    }

    /**
     * 成功回调，简化onCompleted
     *
     * @param result 结果
     */
    public void onSuccess(T result){}

    public void onSuccess(T result, String url){}

    /**
     * 异常错误回调，简化onCompleted
     *
     * @param e      异常
     * @param result 结果
     */
    public void onError(Exception e, T result) {
        toastError(e, result);
    }

    public void onError(Exception e, T result ,String requestFlag) {
        toastError(e, result);
    }

    // 错误处理
    public void toastError(Exception e, T result) {
        Toast.makeText(mContext, handleError(e, result), Toast.LENGTH_SHORT).show();
    }

    /**
     * 提取错误信息
     *
     * @param e      异常
     * @param result 结果
     * @param <T>    实现IResult接口
     * @return 错误信息
     */
    public static <T extends IResult> String handleError(Exception e, T result) {
        String error = null;
        if (e != null) {
//            e.printStackTrace();
            if (e instanceof TimeoutException) {
                error = "网络不给力，连接超时。";
            } else if (e instanceof UnknownHostException) {
                error = "网络连接失败，请检查您的网络设置。";
            }else{
            	 error = "获取影像资料失败!";
            }
//        } else if (!result.getState()) {
        } else if (result != null && result.errorCode().equals(0)) {
            error = result.errorMsg();
            Log.e(TAG, error);
        } else if (result != null && result.status().equals("no")){
        	error = result.errorMsg();
        }
        return error;
    }

    /**
     * 打印结果，需要T实现toString方法
     *
     * @param result 结果
     */
    public void print(T result) {
        Log.d(TAG, result.toString());
    }

}
