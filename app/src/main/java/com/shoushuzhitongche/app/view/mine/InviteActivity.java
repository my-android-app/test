package com.shoushuzhitongche.app.view.mine;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.dixiang.framework.BaseActivity;
import com.shoushuzhitongche.app.R;

public class InviteActivity extends BaseActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite);
	}
	
	@Override
	public void initHeaderView() {
		setTopViewBg(R.color.actionbar_bg_color);
		setTitleViewValue(0, 0, R.color.white);
		setTitleViewValue("邀请专家");
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
