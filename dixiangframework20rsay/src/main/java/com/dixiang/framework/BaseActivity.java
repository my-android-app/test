package com.dixiang.framework;

import java.util.Map;
import com.baidu.mobstat.StatService;
import com.dixiang.framework.common.IDataLoadListenterByFlg;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.config.SystemConstants;
import com.dixiang.framework.http.DataUtils;
import com.dixiang.framework.utils.Utils;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {
	public Activity activity;
	private Map<String, BaseActivity> map = Utils.map;
	private BaseActivity myObject;
	public Handler handler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		myObject = this;
		addTask(this);
		Utils.initExcption(this);
		SystemConstants.putHandler(this.getClass().getSimpleName(), handler);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initHeaderView();
		StatService.onResume(activity);
		// umeng
		// MobclickAgent.onResume(activity);//统计

	}

	@Override
	protected void onPause() {
		super.onPause();
		// umeng
		// MobclickAgent.onPause(activity);//统计
		StatService.onPause(activity);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
		boolean isOpen=imm.isActive();
		if(isOpen){
			View view = getCurrentFocus();
			if(view != null){
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			}
		}
	}

	public abstract void initHeaderView();

	public void initBackView(int resourseId, int stringId,
			OnClickListener onListener) {
		View backView = findViewById(R.id.back_view);
		ImageView img_back = (ImageView) findViewById(R.id.img_back);
		TextView txt_back = (TextView) findViewById(R.id.lab_back);

		if (resourseId != 0) {
			img_back.setBackgroundResource(resourseId);
		}
		if (stringId != 0) {
			txt_back.setText(stringId);
		}
		backView.setOnClickListener(onListener);
	}

	public void initBackView(OnClickListener onListener) {
		initBackView(0, 0, onListener);
	}

	public void initBackView(boolean isShow) {
		View backView = findViewById(R.id.back_view);
		if (!isShow) {
			backView.setVisibility(View.GONE);
		} else {
			backView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		}
	}

	public void initOptionView(int resourseId, int stringId,
			OnClickListener onListener) {
		View optionView = findViewById(R.id.option_view);
		optionView.setVisibility(View.VISIBLE);
		ImageView img_option = (ImageView) findViewById(R.id.img_option);
		TextView txt_option = (TextView) findViewById(R.id.lab_option);

		if (resourseId != 0) {
			img_option.setBackgroundResource(resourseId);
		}
		if (stringId != 0) {
			txt_option.setText(stringId);
		}
		optionView.setOnClickListener(onListener);
	}

	public void setRightText(String rightText, int colorid) {
		TextView txt_option = (TextView) findViewById(R.id.lab_option);
		txt_option.setVisibility(View.VISIBLE);
		txt_option.setText(rightText);
		if (colorid != 0) {
			txt_option.setTextColor(getResources().getColor(colorid));
		}
	}

	public void initOptionView(OnClickListener onListener) {
		initOptionView(0, 0, onListener);
	}

	public void setTitleViewValue(int stringId) {
		setTitleViewValue(stringId, 0, 0);
	}

	public void setTitleViewValue(int stringId, int size, int colorid) {
		TextView title = (TextView) findViewById(R.id.lab_title);
		title.setVisibility(View.VISIBLE);
		if (size != 0) {
			title.setTextSize(size);
		}

		if (stringId != 0) {
			title.setText(stringId);
		}

		if (colorid != 0) {
			title.setTextColor(getResources().getColor(colorid));
		}
	}

	public void setTitleViewValue(String stringText) {
		TextView title = (TextView) findViewById(R.id.lab_title);
		title.setVisibility(View.VISIBLE);
		title.setText(stringText);
	}

	public void setTitleViewValue(String stringText, int colorid,
			int rightDrawable, OnClickListener onListener) {

		TextView title = (TextView) findViewById(R.id.lab_title);
		title.setVisibility(View.VISIBLE);
		title.setText(stringText);

		Drawable drawable = getResources().getDrawable(rightDrawable);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界

		title.setCompoundDrawables(null, null, drawable, null);// 画在右边
		title.setCompoundDrawablePadding(3);

		if (colorid != 0) {
			title.setTextColor(getResources().getColor(colorid));
		}
		if (onListener != null) {
			title.setOnClickListener(onListener);
		}

	}

	public void initCenterView(View view) {

		RelativeLayout sel_view = (RelativeLayout) findViewById(R.id.sel_view);
		sel_view.setVisibility(View.VISIBLE);
		sel_view.addView(view);

		TextView title = (TextView) findViewById(R.id.lab_title);
		title.setVisibility(View.GONE);
	}

	public void clearTitle() {
		View backView = activity.findViewById(R.id.back_view);
		backView.setVisibility(View.GONE);

		View sel = activity.findViewById(R.id.sel_view);
		sel.setVisibility(View.GONE);

		View optionView = activity.findViewById(R.id.option_view);
		optionView.setVisibility(View.GONE);
	}

	@SuppressLint("NewApi")
	public void setTopViewBg(int resourseId) {
		View topView = findViewById(R.id.actionbar);
		Drawable drawable = activity.getResources().getDrawable(resourseId);
		topView.setBackground(drawable);
	}
	
	public void setTitleOption(int stringId) {
		TextView txt_option = (TextView) findViewById(R.id.lab_option);
		if (stringId != 0) {
			txt_option.setText(stringId);
		}
	}
	
	private void addTask(BaseActivity activity) {
		map.put(activity.getClass().getSimpleName(), activity);
	}

	private void removeTask(BaseActivity activity) {
		map.remove(activity.getClass().getSimpleName());
	}

	public void closeActivity(String cls) {
		Activity activity = map.get(cls);
		map.remove(cls);
		if (activity != null)
			activity.finish();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		SystemConstants.refreshHandler(this.getClass().getSimpleName());
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		SystemConstants.refreshHandler(this.getClass().getSimpleName());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Activity activity = map.get(getClass().getSimpleName());
		removeTask(this);
		handler = null;
		SystemConstants.removeHandler(this.getClass().getSimpleName());
		if (activity != null){
			activity.finish();
		}
	}
	
	/**
	 * 
	 * @param url 请求地址
	 * @param httpRequest 网络请求对象 <T>
	 * @param flg 网络请求标记
	 */
	public void getHttpResult(String url, String flg ,TypeToken typeToken){
		DataUtils.getHttpGet(this, url, loadlistenter, flg ,typeToken);
	}
	
	/**
	 * 
	 * @param url 请求地址
	 * @param httpRequest 网络请求对象 <T>
	 * @param flg 网络请求标记
	 */
	public void postHttpResult(String url,  Map map ,String flg ,TypeToken typeToken){
		DataUtils.postHttp(this, url, map, loadlistenter, flg ,typeToken);
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
