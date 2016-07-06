package com.shoushuzhitongche.app.view.mine;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dixiang.framework.BaseActivity;
import com.shoushuzhitongche.app.R;

public class ResultActivity extends BaseActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_success);
	}
	
	@Override
	public void initHeaderView() {
		setTopViewBg(R.color.actionbar_bg_color);
		setTitleViewValue(R.string.commit_success, 0, R.color.white);
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
