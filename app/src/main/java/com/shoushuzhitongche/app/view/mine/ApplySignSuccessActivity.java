package com.shoushuzhitongche.app.view.mine;

import com.dixiang.framework.BaseActivity;
import com.shoushuzhitongche.app.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ApplySignSuccessActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applysign_success);
		initView();
	}

	@Override
	public void initHeaderView() {
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setTitleViewValue(R.string.apply_success, 0, R.color.white);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//			case R.id.btn_commit:
//				break;

			default:
				break;
		}		
	}

	private void initView() {
		
	}

}
