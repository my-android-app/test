package com.shoushuzhitongche.app.view;

import java.util.Timer;
import java.util.TimerTask;

import com.dixiang.framework.BaseActivity;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.R.layout;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_splash);
		initView();
	}

	private void initView() {
		Timer tExit = new Timer();
		tExit.schedule(new TimerTask() {
			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setClass(SplashActivity.this, MainActivity.class);
				//				intent.setClass(SplashActivity.this, CreateOrderActivity.class);
				startActivity(intent);
				finish();
			}
		}, 2000);

	}

	@Override
	public void initHeaderView() {

	}

}
