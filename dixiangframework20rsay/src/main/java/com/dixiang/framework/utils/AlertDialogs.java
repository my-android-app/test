package com.dixiang.framework.utils;

import com.dixiang.framework.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class AlertDialogs extends AlertDialog {
     public static AlertDialogs dlg = null;
     private static String title = "温馨提示";
     private static String ok = "确定";
     private static String cancel = "取消";
     private static Activity contexts;

     public static TextView dialogTitle;
     public static TextView dialogContent,dialogContent2;
     public static Button confirm;
     public static Button cancels;
     private boolean isVisible = false;
     private String content2 = "";
     //恢复默认文字
     static void cleartext(){
          title = "温馨提示";
          ok = "确定";
          cancel = "取消";
     }

     protected AlertDialogs(Activity context) {
          super(context);
          initView();
     }
     public static AlertDialogs getAlertDialog(Activity context) {
          contexts = context;
          if (dlg == null) {
               dlg = new AlertDialogs(context);
          }
          return dlg;
     }

     public void setTitle(String title) {
          this.title = title;
     }
     
     public void setTitle(int titleString) {
         this.title = contexts.getResources().getString(titleString);;
    }

     public void setCancel(int cancelString) {
        this.cancel = contexts.getResources().getString(cancelString);
    }

     public void setOk(int okString) {
        this.ok = contexts.getResources().getString(okString);
    }


     public void setCancel(String cancel) {
          this.cancel = cancel;
     }

     public void setOk(String ok) {
          this.ok = ok;
     }
     
     public void setContent2(Boolean isVisible,String content2) {
    	   this.isVisible = isVisible;
    	  this.content2 = content2;
    }
     static  View view;
     void initView(){
          LayoutInflater l = LayoutInflater.from(contexts);
          view = l.inflate(R.layout.view_dialog,null);
          dialogTitle = (TextView) view.findViewById(R.id.title);
          dialogContent = (TextView) view.findViewById(R.id.content);
          dialogContent2 = (TextView) view.findViewById(R.id.content2);
          confirm = (Button) view.findViewById(R.id.ok);
          cancels = (Button) view.findViewById(R.id.res);
     }
     
     public void showAlertDialog(int contentId,final View.OnClickListener listener ){
    	 
    	 String content = contexts.getResources().getString(contentId);
    	 showAlertDialog(content, listener);
     }

     public void showAlertDialog(String content, final View.OnClickListener listener) {
          dlg = new AlertDialogs(contexts);
          initView();
          if (contexts.isFinishing()) return;
          dlg.show();
          Window window = dlg.getWindow();
          window.setContentView(view);
          dialogTitle.setText(title);
          dialogContent.setText(content);
            if( isVisible )
	  		{
	      		 dialogContent2.setVisibility( View.VISIBLE );
	      		 dialogContent2.setText(content2);
	  		}else {
	  			dialogContent2.setVisibility( View.GONE );
	  		}
         
          confirm.setText(ok);
          confirm.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    if (listener != null)
                         listener.onClick(view);
                    dlg.dismiss();
                    cleartext();
               }
          });
          cancels.setText(cancel);
          cancels.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    if (cli != null)
                         cli.onClick(view);
                    dlg.dismiss();
                    cleartext();
               }
          });

          dlg.setOnDismissListener(new OnDismissListener() {
               @Override
               public void onDismiss(DialogInterface dialogInterface) {
                    cleartext();
               }
          });
          dlg.setOnCancelListener(new OnCancelListener() {
               @Override
               public void onCancel(DialogInterface dialogInterface) {
                    cleartext();
               }
          });
     }
     public static View.OnClickListener cli;
     public void setListeners(View.OnClickListener onListener) {
          cli = onListener;
     }
}
