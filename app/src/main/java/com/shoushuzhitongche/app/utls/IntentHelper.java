package com.shoushuzhitongche.app.utls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 此为跳转帮助类，在跳转过多的页面使用。
 */
public class IntentHelper {
    private Activity context;
    public static IntentHelper instance;

    private IntentHelper(Activity context) {
        this.context = context;
    }

    /*
    * 跳转，无数据传递的跳转
    * */
    public static IntentHelper getInstance(Context context1) {
        if (instance == null) {
            instance = new IntentHelper((Activity) context1);
            return instance;
        } else {
            return instance;
        }
    }

    public void gotoActivity(Class<?> atyClass) {
        gotoActivity(atyClass, null);
    }

    /*
    * 跳转，有数据传递的跳转
    * */
    public void gotoActivity(Class<?> atyClass, Bundle bundle, boolean isNewsTaks) {
        Intent i = new Intent(context, atyClass);
        if (bundle != null) {
            i.putExtras(bundle);
        }
        if (isNewsTaks) {
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(i);
    }


    public void gotoActivity(Class<?> atyClass, Bundle bundle) {
        Intent i = new Intent(context, atyClass);
        if (bundle != null) {
            i.putExtras(bundle);
        }
        context.startActivity(i);
    }
    
    public void gotoActivityWithURL(Class<?> atyClass, String actionUrl) {
        Intent i = new Intent(context, atyClass);
        i.putExtra(Constants.PARM_ACTION_URL, actionUrl);
        context.startActivity(i);
    }

    public void gotoActivityWithURLAndTitle(Class<?> atyClass,String title, String actionUrl) {
        Intent i = new Intent(context, atyClass);
        i.putExtra(Constants.PARM_PAGE_TITLE, title);
        i.putExtra(Constants.PARM_ACTION_URL, actionUrl);
        context.startActivity(i);
    }
    
    /*
      * 跳转，有数据有返回值传递的跳转
      * */
    public void gotoActivity(Class<?> atyClass, Bundle bundle, int requestCode) {
        Intent i = new Intent(context, atyClass);
        if (bundle != null) {
            i.putExtras(bundle);
        }
        context.startActivityForResult(i, requestCode);
    }

    /*
        * 跳转，有返回值传递的跳转
        * */
    public void gotoActivity(Class<?> atyClass, int requestCode) {
        gotoActivity(atyClass, null, requestCode);
    }
}
