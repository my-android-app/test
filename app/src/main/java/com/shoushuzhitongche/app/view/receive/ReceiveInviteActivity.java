package com.shoushuzhitongche.app.view.receive;

import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.receive.adapter.ReceiveAppointAdapter;
import com.shoushuzhitongche.app.view.receive.bean.AppointEntity;
import com.shoushuzhitongche.app.view.receive.bean.AppointTemEntity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class ReceiveInviteActivity extends BaseActivity implements
		OnClickListener {
	private RelativeLayout back_view;
	private RadioButton wait_handle, finished;
	private ListView listView;
	private ReceiveAppointAdapter adapter;
	private int state = 1;
	private AppointTemEntity appointTemEntity;
	private LinearLayout ll_data_null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_invite);
		initView();
	}

	@Override
	public void initHeaderView() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_view:
			finish();
			break;
		case R.id.wait_handle:
			state = 1;
			loadView(false);
			break;
		case R.id.finished:
			state = 8;
			loadView(true);
			break;
		default:
			break;
		}
		
	}

	private void initView() {
		back_view = (RelativeLayout) findViewById(R.id.back_view);
		back_view.setOnClickListener(this);
		wait_handle = (RadioButton) findViewById(R.id.wait_handle);
		finished = (RadioButton) findViewById(R.id.finished);
		wait_handle.setOnClickListener(this);
		finished.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.lv_receive_appoint);
		ll_data_null = (LinearLayout) findViewById(R.id.ll_data_null);
		
		adapter = new ReceiveAppointAdapter(activity);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(onItemClickListener);

		doNet();
	}

	private void doNet() {
		LoadingDialog.getInstance(activity).show();
		String param = CommonUtils.getGetParm(activity , "");
		String url = CommonUtils.getDoctorbookinglist(activity,param);
		getHttpResult(url, Constants.HTTP_INDEX.First, new TypeToken<QjResult<AppointTemEntity>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		appointTemEntity = (AppointTemEntity) result.getResults();
		loadView(true);
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}

	
	private void loadView(Boolean tag){
		if(appointTemEntity == null) {
			ll_data_null.setVisibility(View.VISIBLE);
			ll_data_null.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					doNet();
				}
			});
			
			return;
		}
		
		if (!(appointTemEntity.getProcessingList()!=null&&appointTemEntity.getProcessingList().size()>0) 
				&&!(appointTemEntity.getDoneList()!=null&&appointTemEntity.getDoneList().size()>0)) {
			ll_data_null.setVisibility(View.VISIBLE);
			ll_data_null.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					doNet();
				}
			});
		}
		
		if (tag) {
			if (!(appointTemEntity.getProcessingList()!=null&&appointTemEntity.getProcessingList().size()>0) 
					&&appointTemEntity.getDoneList()!=null&&appointTemEntity.getDoneList().size()>0) {
				finished.setChecked(true);
				state = 8;
			}
		}
		
		
		adapter.clear();
		if(state == 1){
			adapter.addAll(appointTemEntity.getProcessingList());	
		}else{
			adapter.addAll(appointTemEntity.getDoneList());
		}
		adapter.notifyDataSetChanged();
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			AppointEntity entity = adapter.getItem(arg2);
			Bundle bundle = new Bundle();
		    bundle.putString(Constants.PARMS.ACTIONURL, entity.getActionUrl());
		    bundle.putString("type", entity.getBkType());
		    bundle.putString(Constants.PARMS.STATE, entity.getDoctorAccept() == null ? "" : entity.getDoctorAccept());
			IntentHelper.getInstance(activity).gotoActivity(
					AppointmentDetailsActivity.class,bundle);
		}
	};
}
