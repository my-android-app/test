package com.shoushuzhitongche.app.view;

import java.util.ArrayList;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.utils.AlertDialogs;
import com.dixiang.framework.utils.FragmentUtils;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.booking.AddBaseInfoActivity;
import com.shoushuzhitongche.app.view.discover.DiscoverFragment;
import com.shoushuzhitongche.app.view.home.HomeFragment;
import com.shoushuzhitongche.app.view.mine.MineFragment;
import com.shoushuzhitongche.app.view.order.OrderListFragment;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseActivity {

	private RadioButton rb1,rb2,rb3,rb4;
	private ArrayList<RadioButton> list = new ArrayList<RadioButton>();
	private int index = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		getDensity();
		rb1 = (RadioButton) findViewById(R.id.rb1);
		rb2 = (RadioButton) findViewById(R.id.rb2);
		rb3 = (RadioButton) findViewById(R.id.rb3);
		rb4 = (RadioButton) findViewById(R.id.rb4);
		
		if(list == null || list.size() == 0){
			list.add(rb1);
			list.add(rb2);
			list.add(rb3);
			list.add(rb4);
		}
		
		findViewById(R.id.rb6).setOnClickListener(onClickListener);
		FragmentUtils.putDefaultFragment(this, R.id.container,
				new HomeFragment());
	}

	@Override
	protected void onResume() {
		super.onResume();
		for(int i = 0; i < 4 ; i ++){
			if( i == index){
				list.get(i).setChecked(true);
			}else{
				list.get(i).setChecked(false);
			}
		}
	}
	
	public void onMenuChange(View view) {
		if(!CommonUtils.isLogin(MainActivity.this)){
			CommonUtils.gotoLogin(MainActivity.this);
			return;
		}
		switch (view.getId()) {
			case R.id.rb1:
				FragmentUtils.goToFragment(this, HomeFragment.class, R.id.container);
				index = 0;
				break;
			case R.id.rb2:
				index = 1;
				FragmentUtils.goToFragment(this, DiscoverFragment.class, R.id.container);
				break;
			case R.id.rb3:
				index = 2;
				FragmentUtils.goToFragment(this, OrderListFragment.class, R.id.container);
				break;
			case R.id.rb4:
				index = 3;
				FragmentUtils.goToFragment(this, MineFragment.class, R.id.container);
				break;
			default:
				break;
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if(!CommonUtils.isLogin(MainActivity.this)){
				CommonUtils.gotoLogin(MainActivity.this);
				return;
			}
			IntentHelper.getInstance(MainActivity.this).gotoActivity(
					AddBaseInfoActivity.class);
		}
	};
	

	// 依次屏幕密度， 屏幕高度，屏幕宽度,状态栏高度
	private void getDensity() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MYApplication.density = dm.density;
		MYApplication.heightPixels = dm.heightPixels;
		MYApplication.widthPixels = dm.widthPixels;
		MYApplication.startheight = CommonUtils.getStatusHeight(this);
	}
	
	@Override
	public void onBackPressed() {
		AlertDialogs.getAlertDialog(MainActivity.this).showAlertDialog(
				R.string.back_app, new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						System.exit(0);
					}
				});
		return;
	}

	@Override
	public void initHeaderView() {
		// TODO Auto-generated method stub
		
	}
	
}
