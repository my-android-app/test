package com.dixiang.framework.widget;

import com.dixiang.framework.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoadingDialog {
    private static LoadingDialog instance;
    private Dialog dialog;
    private Context context;
    String content;

    private LoadingDialog() {

    }

    public static LoadingDialog getInstance(Context context) {
        if (instance == null) {
            instance = new LoadingDialog();
        }
        instance.context = context;
        return instance;
    }

    private void initDialog(Context context, String content) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog_layout, null);// 得到加载view
        v.setOnClickListener(null);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        TextView tv = (TextView) v.findViewById(R.id.tipTextView);
        if (!TextUtils.isEmpty(content)) {
            tv.setText(content);
        }
        dialog = new Dialog(context, R.style.LoadingDialogStyle);// 创建自定义样式dialog
        dialog.setCancelable(true);// 不可以用“返回键”取消
        dialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
    }


    public void show() {
        show("");
    }

    public void show(String content) {
        instance.initDialog(context, content);
        if (context instanceof Activity) {
            if (dialog != null && !((Activity) context).isFinishing()) {
            	 try {
            	dialog.show();
            	 } catch (IllegalArgumentException e) {
                     e.printStackTrace();
                 }
            }
        }
    }

    public void dismiss() {

        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
                dialog = null;
            } catch (IllegalArgumentException e) {

            }
        }
    }

}
