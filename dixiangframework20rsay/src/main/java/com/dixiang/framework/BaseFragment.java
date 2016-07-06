package com.dixiang.framework;

import com.dixiang.framework.common.IDataLoadListenterByFlg;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.http.DataUtils;
import com.dixiang.framework.utils.Utils;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment{
	public Activity activity;
	private BaseFragment myObject;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		myObject = this;
		Utils.initExcption(activity);
		initHeaderView();
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden){
			initHeaderView();
		}
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		Log.e("======","onResume()");
//		StatService.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
//		StatService.onPause(this);
	}
	
	public abstract void initHeaderView();
	
	public void initBackView(int resourseId,int stringId,OnClickListener onListener){
		View backView = activity.findViewById(R.id.back_view);
		ImageView img_back = (ImageView)activity.findViewById(R.id.img_back);
		TextView txt_back = (TextView)activity.findViewById(R.id.lab_back);
		
		if(resourseId != 0){
			img_back.setBackgroundResource(resourseId);
		}
		if(stringId != 0){
			txt_back.setText(stringId);
		}
		backView.setOnClickListener(onListener);
	}
	
	public void initBackView(OnClickListener onListener){
		initBackView(0,0,onListener);
	}
	
	@SuppressLint("NewApi")
	public void setTopViewBg(int resourseId){
		View topView = activity.findViewById(R.id.actionbar);
//		topView.setBackground(n);
		//topView.setBackgroundColor(resourseId);
		Drawable drawable = getResources().getDrawable(resourseId);
		topView.setBackground(drawable);
	}
	
	
	public void initOptionView(int resourseId,int stringId,OnClickListener onListener){
		View optionView = activity.findViewById(R.id.option_view);
		optionView.setVisibility(View.VISIBLE);
		ImageView img_option = (ImageView)activity.findViewById(R.id.img_option);
		TextView txt_option = (TextView)activity.findViewById(R.id.lab_option);
		
		if(resourseId != 0){
			img_option.setBackgroundResource(resourseId);
		}
		if(stringId != 0){
			txt_option.setText(stringId);
		}
		optionView.setOnClickListener(onListener);
	}
	
	public void initOptionView(OnClickListener onListener){
		initOptionView(0,0,onListener);
	}
	public void setTitleViewValue(int stringId ,int size,int colorid){
		TextView title = (TextView)activity.findViewById(R.id.lab_title);
		title.setVisibility(View.VISIBLE);
		if(size != 0){
			title.setTextSize(size);
		}

		if(stringId != 0){
			title.setText(stringId);	
		}
		
		if(colorid != 0){
			title.setTextColor(activity.getResources().getColor(colorid));
		}
	}
	
	public void setTitleViewValue(int stringId){
		TextView title = (TextView)activity.findViewById(R.id.lab_title);
		title.setVisibility(View.VISIBLE);
		title.setText(stringId);
	}
	public void setTitleViewValue(String stringText){
		TextView title = (TextView)activity.findViewById(R.id.lab_title);
		title.setText(stringText);
		title.setVisibility(View.VISIBLE);
	}
	
	public void initCenterView(View view){
		
		RelativeLayout sel_view = (RelativeLayout) activity.findViewById(R.id.sel_view);
		sel_view.removeAllViews();
		sel_view.setVisibility(View.VISIBLE);
		sel_view.addView(view);
		
		TextView title = (TextView) activity.findViewById(R.id.lab_title);
		title.setVisibility(View.GONE);
	}
	
	public void clearTitle(){
		View backView = activity.findViewById(R.id.back_view);
		backView.setVisibility(View.GONE);
		
		View sel = activity.findViewById(R.id.sel_view);
		sel.setVisibility(View.GONE);
		
		View optionView = activity.findViewById(R.id.option_view);
		optionView.setVisibility(View.GONE);
		
	}

	/**
	 * 
	 * @param url 请求地址
	 * @param httpRequest 网络请求对象 <T>
	 * @param flg 网络请求标记
	 */
	public void getHttpResult(String url, String flg ,TypeToken typeToken){
		DataUtils.getHttpGet((BaseActivity)getActivity(), url, loadlistenter, flg ,typeToken);
	}
	
	public void onSuccess(QjResult result, String flg){}
	public void onSuccess(QjResult result){}
	
	public void onError(Exception ex, Object result, String flg){}
	public void onError(Exception ex, Object result){}
	
	public void onFailure(QjResult result, String flg){}
	public void onFailure(QjResult result){}
	
	public void onCompleted(Exception e,String flg){}
	
	public void onHttpError(int msg){}
	public void onHttpError(int msg, String flg){}
	
	
	private IDataLoadListenterByFlg loadlistenter = new IDataLoadListenterByFlg() {


		@Override
		public void onSuccess(QjResult result, String flg) {
			myObject.onSuccess(result, flg);
		}

		@Override
		public void onSuccess(QjResult result) {
			myObject.onSuccess(result);
			
		}

		@Override
		public void onError(Exception ex, Object result, String flg) {
			myObject.onError(ex, result, flg);
		}

		@Override
		public void onError(Exception ex, Object result) {
			myObject.onError(ex, result);
		}

		@Override
		public void onFailure(QjResult result, String flg) {
			myObject.onFailure(result, flg);
		}


		@Override
		public void onFailure(QjResult result) {
			myObject.onFailure(result);
			
		}

		@Override
		public void onCompleted(Exception e, String flg) {
			myObject.onCompleted(e, flg);
		}

		@Override
		public void onHttpError(int msg) {
			onHttpError(msg);
		}

		@Override
		public void onHttpError(int msg, String flg) {
			onHttpError(msg, flg);
		}
		
	};
}
