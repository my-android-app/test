package com.shoushuzhitongche.app.view;

import com.dixiang.framework.BaseActivity;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.question.SignedShowActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

@SuppressLint("JavascriptInterface")
public class WebViewActivity2 extends BaseActivity {

	private WebView advertise_webview;
	private ProgressBar progressBar;
	private String title;
	private String targetPatient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advertisement_layout2);

		targetPatient = getIntent().getStringExtra("value");
		advertise_webview = (WebView) findViewById(R.id.advertise_webview);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);

		String actionUrl = getIntent()
				.getStringExtra(Constants.PARMS.ACTIONURL);
		// getIntent().getStringExtra(Constants.PARMS.ACTIONURL);
		title = getIntent().getStringExtra(Constants.PARMS.PAGE_TITLE);

		advertise_webview.setHorizontalScrollBarEnabled(false);
		advertise_webview.setVerticalScrollBarEnabled(false);
		// 填充webview
		advertise_webview.setNetworkAvailable(true);
		advertise_webview.getSettings().setJavaScriptEnabled(true);
		advertise_webview.getSettings().setDefaultTextEncodingName("utf-8");
		advertise_webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
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
		advertise_webview.loadUrl(actionUrl);
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
		setTitleViewValue("专家签约协议");
	}

	public void onBtnOption(View view) {
		switch (view.getId()) {
		case R.id.btn_agree:
			IntentHelper.getInstance(activity).gotoActivity(SignedShowActivity.class);
			
			break;
		case R.id.btn_cancle:
			break;
		default:
			break;
		}
		finish();
	}

}
