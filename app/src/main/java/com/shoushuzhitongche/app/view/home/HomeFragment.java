package com.shoushuzhitongche.app.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import com.dixiang.framework.BaseFragment;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.utils.Utils;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.WebViewActivity;
import com.shoushuzhitongche.app.view.doctor.DoctorListActivity;
import com.shoushuzhitongche.app.view.home.adpter.AdViewPagerAdapter;
import com.shoushuzhitongche.app.view.home.bean.HomeEntity;
import com.shoushuzhitongche.app.widget.AdView;
import com.shoushuzhitongche.app.widget.ScrollTextView;

public class HomeFragment extends BaseFragment implements OnClickListener{
	private HomeEntity homeEntity;
	private String showTexts [] ;
	ScrollTextView switcher1;
	private AdView advice_viewpager;
	private AdViewPagerAdapter mCounselViewPagerAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
					Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false); 
		initView(view);
		initLocalData();
		doNet();
		return view;
	}
	@Override
	public void initHeaderView() {
		clearTitle();
		setTitleViewValue("名医主刀");
		setTitleViewValue( 0, 0, R.color.white );
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden){
			startMove();
		}else{
			stopMove();
		}
	}
	
	private void initView(View view) {
		switcher1 = (ScrollTextView) view.findViewById(R.id.switcher1);
		view.findViewById(R.id.ll_expert).setOnClickListener(this);
		view.findViewById(R.id.gongyi).setOnClickListener(this);
		view.findViewById(R.id.laojie).setOnClickListener(this);
		
		advice_viewpager = (AdView) view.findViewById(R.id.advice_viewpager);
		advice_viewpager.setTitleViewVisibility(View.GONE);
		advice_viewpager.setmCanScroll(false);
		advice_viewpager.setIndicatorGravity(Gravity.CENTER_HORIZONTAL);
		advice_viewpager.setDuration(3000);

		advice_viewpager.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return false;
			}
		});
	}
	
	private void initLocalData(){
		try{
			homeEntity =  (HomeEntity) Utils
				.getDataFromCache(HomeFragment.class
						.getSimpleName());
			if(homeEntity != null){
				loadView();	
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View view) {
		String url = "";
		switch (view.getId()) {
		case R.id.ll_expert:
			if(!CommonUtils.isLogin(activity)){
				CommonUtils.gotoLogin(activity);
				return;
			}
			Intent intent = new Intent(activity,DoctorListActivity.class);
			Bundle bundle = new Bundle();
			intent.putExtras(bundle);
			activity.startActivity(intent);
			break;
		case R.id.gongyi:
			url = homeEntity.getJoinUrl();
			IntentHelper.getInstance(activity).gotoActivityWithURLAndTitle(WebViewActivity.class, "名医公益", url);
			break;
		case R.id.laojie:
			url = homeEntity.getPublicWelfareUrl();
			IntentHelper.getInstance(activity).gotoActivityWithURLAndTitle(WebViewActivity.class, "名医主刀", url);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		stopMove();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(!isHidden()){
			startMove();
		}
	}
	
	private void doNet(){
		LoadingDialog.getInstance(activity).show();
		String url = CommonUtils.getIndexannouncement(activity);
		getHttpResult(url, Constants.HTTP_INDEX.First, new TypeToken<QjResult<HomeEntity>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		try{
			homeEntity = (HomeEntity) result.getResults();
			loadView();
			Utils.saveDataToCache(
					HomeFragment.class.getSimpleName(),
					homeEntity);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}
	
	private void loadView(){
		
		String [] doctors = homeEntity.getDoctors();
		if(doctors != null && doctors.length > 0){
			showTexts = doctors;
			if(homeEntity.getBanners() != null &&  !homeEntity.getBanners().isEmpty()){
				advice_viewpager.stopAutoCycle();
				mCounselViewPagerAdapter = null;
				mCounselViewPagerAdapter = new AdViewPagerAdapter(activity,
						homeEntity.getBanners());
				advice_viewpager.setAdapter(mCounselViewPagerAdapter);
				advice_viewpager.startAutoCycle();
			}
			startMove();
		}
	}
	
	private void startMove(){
		
		if (advice_viewpager != null)
			advice_viewpager.startAutoCycle();
		if(showTexts != null && showTexts.length > 0){
			switcher1.setText(showTexts);
			switcher1.beginScroll();
		}
	}
	
	private void stopMove(){
		if (advice_viewpager != null)
			advice_viewpager.stopAutoCycle();
		switcher1.stopScroll();
	}
}
