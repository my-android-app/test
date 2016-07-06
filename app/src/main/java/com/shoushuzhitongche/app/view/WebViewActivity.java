package com.shoushuzhitongche.app.view;

import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.utils.Utils;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.doctor.DoctorDetailsActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewActivity extends BaseActivity{
	
    private WebView advertise_webview;
    private ProgressBar progressBar;
    private String title;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
	    setContentView(R.layout.activity_web_layout);
	    advertise_webview = (WebView) findViewById(R.id.advertise_webview);
	    progressBar = (ProgressBar) findViewById(R.id.progress_bar);
	    
	    String actionUrl = getIntent().getStringExtra(Constants.PARM_ACTION_URL);
	    title = getIntent().getStringExtra(Constants.PARM_PAGE_TITLE);
	    
        advertise_webview.setHorizontalScrollBarEnabled(false);
        advertise_webview.setVerticalScrollBarEnabled(false);
        //填充webview
        advertise_webview.setNetworkAvailable(true);
        advertise_webview.getSettings().setJavaScriptEnabled(true);
        advertise_webview.getSettings().setDefaultTextEncodingName("utf-8");
        
        advertise_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	if(!Utils.isStringEmpty(url)){
//            		http://m.mingyizhudao.com/mobile/doctor/view/id/2926/is_commonweal/1
            		String key = "/id/";
            		if(url.contains("/doctor/view/")){
            			int index = url.indexOf(key);
            			url = url.substring(index+key.length());
            			int end = url.indexOf("/");
            			String id = "";
            			if(end == -1){
            				id = url.substring(0);
            			}else{
            				id = url.substring(0,end);
            			}
            			Bundle bundle = new Bundle();
            			bundle.putString("doctorId", id);
            			IntentHelper.getInstance(activity).gotoActivity(DoctorDetailsActivity.class,bundle);
            			return true;
            		}
            	}
                view.loadUrl(url);
                return true;
            }
            
        });
        
        advertise_webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress >= 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        
        advertise_webview.loadUrl(actionUrl +"/username/"+CommonUtils.getMobile(activity)+"/token/"+CommonUtils.getToken(activity));
    }
    
	
	@Override
	public void initHeaderView() {
		setTopViewBg(R.color.actionbar_bg_color);
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setTitleViewValue(0, 0, R.color.white);
		setTitleViewValue(title);
	}

}
