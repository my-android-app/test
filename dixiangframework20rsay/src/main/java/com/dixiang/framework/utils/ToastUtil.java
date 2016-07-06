package com.dixiang.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class ToastUtil {
    private static Toast mToast;

    public static void showToast(Context context, String msg, int duration) {
        if (mToast == null && context != null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
    
    public static void showToast(Activity activity, String msg, int duration) {
    	
    	if(activity == null ){
    		
    	}
        if (mToast == null && activity != null) {
            mToast = Toast.makeText(activity, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void showToast(Context context, String msg) {
        try {
            if (context != null) {
                showToast(context, msg, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static Toast currentToast;
    private static Context mlastContext;
    private static View toastView;

    /**
     * 使用同1个toast,避免多toast重复问题
     *
     * @param context
     * @param text
     * @param duration
     * @return
     */
    public static Toast makeText(Context context, CharSequence text, int duration) {
        if (currentToast == null && context != null) {
            currentToast = Toast.makeText(context.getApplicationContext(), text, duration);
            toastView = currentToast.getView();
        } else {
            // try {
            // currentToast.cancel();
            // } catch (Exception e) {
            // }
            // if (mlastContext != context.getApplicationContext()) {
            // currentToast = Toast.makeText(context, text, duration);
            // }
        }
        if (toastView != null) {
            currentToast.setView(toastView);
            currentToast.setText(text);
            currentToast.setDuration(duration);
        }
        return currentToast;
    }
}
