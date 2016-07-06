package com.shoushuzhitongche.app.view.discover;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.dixiang.framework.BaseFragment;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.WebViewActivity;
import com.shoushuzhitongche.app.view.login.LoginActivity;

public class DiscoverFragment extends BaseFragment{

    private WebView advertise_webview;
    private ProgressBar progressBar;
    private String title = "发现";
    private boolean isFirst = true;
    private String URL = "http://md.mingyizhudao.com/mobiledoctor/home/page/view/findView";
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.web_layout, container,
				false);
		advertise_webview = (WebView) mView.findViewById(R.id.advertise_webview);
	    progressBar = (ProgressBar) mView.findViewById(R.id.progress_bar);
	    
        advertise_webview.setHorizontalScrollBarEnabled(false);
        advertise_webview.setVerticalScrollBarEnabled(false);
        //填充webview
        advertise_webview.setNetworkAvailable(true);
        advertise_webview.getSettings().setJavaScriptEnabled(true);
        advertise_webview.getSettings().setDefaultTextEncodingName("utf-8");
        
        advertise_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	if(!CommonUtils.isLogin(activity)){
            		IntentHelper.getInstance(activity).gotoActivity(LoginActivity.class);
            		return false;
            	}else{
            		IntentHelper.getInstance(activity).gotoActivityWithURLAndTitle(WebViewActivity.class, title , url);	
            	}
            	
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
        
        advertise_webview.loadUrl(URL);
		return mView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void initHeaderView() {
		clearTitle();
		setTitleViewValue("发现");
		setTitleViewValue( 0, 0, R.color.white );
	}
	
}
