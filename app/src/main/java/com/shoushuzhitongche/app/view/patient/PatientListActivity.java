package com.shoushuzhitongche.app.view.patient;

import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.QJNetUICallback;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.network.Net;
import com.dixiang.framework.widget.LoadingDialog;
import com.dixiang.framework.widget.PullToRefreshView;
import com.dixiang.framework.widget.PullToRefreshView.OnFooterRefreshListener;
import com.dixiang.framework.widget.PullToRefreshView.OnHeaderRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.booking.AddBaseInfoActivity;
import com.shoushuzhitongche.app.view.order.OrderDetailsActivity;
import com.shoushuzhitongche.app.view.order.OrderDetailsMoreActivity;
import com.shoushuzhitongche.app.view.patient.adapter.PatientAdapter;
import com.shoushuzhitongche.app.view.patient.bean.PatientTemEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;


public class PatientListActivity extends BaseActivity implements OnClickListener{

	private TextView tv_add_patient ;
	private PatientAdapter adapter;
	private final static int WATTING_TYPE = 1;
	private final static int COMPLETE_TYPE = 0;
	private PullToRefreshView lv_refresh;
	private int type = WATTING_TYPE;
	private boolean isLoading = true;
	private PatientTemEntity patientTemEntity;
	
	private RadioButton rb1 ,rb2;
	private LinearLayout ll_data_null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_list);
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		doNet(isLoading);
	}
	
	@Override
	public void initHeaderView() {
		if(type == COMPLETE_TYPE){
			return;
		}
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		View view = getLayoutInflater().inflate(R.layout.activity_patient_top, null , false);
		rb1 =((RadioButton)view.findViewById(R.id.rb1));
		rb1.setOnCheckedChangeListener(onCheckedChangeListener);
		rb2 =((RadioButton)view.findViewById(R.id.rb2));
		rb2.setOnCheckedChangeListener(onCheckedChangeListener);
		
		initCenterView(view);
		
	}
	
	private void initView() {
		tv_add_patient = (TextView) findViewById(R.id.tv_add_patient);
		tv_add_patient.setOnClickListener(this);
		
		lv_refresh = (PullToRefreshView) findViewById(R.id.lv_refresh);
		ll_data_null = (LinearLayout) findViewById(R.id.ll_data_null);
		
		adapter = new PatientAdapter(activity);
		
		ListView listView = ((ListView)findViewById(R.id.listview));
				
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(onItemClickListener);
		
		lv_refresh.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

			@Override
			public void onHeaderRefresh(PullToRefreshView refreshView) {
				// 刷新不加载更多
				doNet(false);
			}
		});

		lv_refresh.setOnFooterRefreshListener(new OnFooterRefreshListener() {

			@Override
			public void onFooterRefresh(PullToRefreshView refreshView) {
				// 加载更多
				doNet(false);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_add_patient:
			startActivity(new Intent(activity,AddBaseInfoActivity.class));
			break;
		default:
			break;
		}
	}
	
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if(arg1){
				if(arg0.getId() == R.id.rb1){
					type = WATTING_TYPE;
				}else{
					type = COMPLETE_TYPE;
				}
				loadView();
			}
			isLoading = true;
		}
	};
	
	private void doNet(boolean showLoading){
		if(showLoading)
			LoadingDialog.getInstance(activity).show();
		String param = CommonUtils.getGetParm(activity,"");
		String url = CommonUtils.getPatientlist(activity)+param;
		
		getHttpResult(url, Constants.HTTP_INDEX.First, new TypeToken<QjResult<PatientTemEntity>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		patientTemEntity = (PatientTemEntity) result.getResults();
		if(patientTemEntity != null){
			loadView();
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		handler.post(new Runnable() {
			@Override
			public void run() {
				lv_refresh.onHeaderRefreshComplete();
				lv_refresh.onFooterRefreshComplete();
			}
		});
		LoadingDialog.getInstance(activity).dismiss();
	}
	
	private void loadView(){
		
		if(patientTemEntity == null) {
			ll_data_null.setVisibility(View.VISIBLE);
			ll_data_null.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					doNet(true);
				}
			});
			
			return;	
		}
		
		if (!(patientTemEntity.getNoBookingList()!=null&&patientTemEntity.getNoBookingList().size()>0) 
				&&!(patientTemEntity.getHasBookingList()!=null&&patientTemEntity.getHasBookingList().size()>0)) {
			ll_data_null.setVisibility(View.VISIBLE);
			ll_data_null.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					doNet(true);
				}
			});
			
		}
		
		
		if (!(patientTemEntity.getNoBookingList()!=null&&patientTemEntity.getNoBookingList().size()>0) 
				&&patientTemEntity.getHasBookingList()!=null&&patientTemEntity.getHasBookingList().size()>0) {
			rb2.setChecked(true);
		}
		
		adapter.clear();
		if(type == WATTING_TYPE){
			if (patientTemEntity.getNoBookingList()==null) {
				adapter.clear();
			}else {
				ll_data_null.setVisibility(View.GONE);
				adapter.addAll(patientTemEntity.getNoBookingList());
			}
		}else{
			if (patientTemEntity.getHasBookingList()==null) {
				adapter.clear();
			}else {
				ll_data_null.setVisibility(View.GONE);
				adapter.addAll(patientTemEntity.getHasBookingList());	
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if(type == WATTING_TYPE){
				Bundle bundle = new Bundle();
				bundle.putString(Constants.PARMS.ID,  adapter.getItem(arg2).getId());
				bundle.putString(Constants.PARMS.ACTIONURL,  adapter.getItem(arg2).getActionUrl());
				IntentHelper.getInstance(activity).gotoActivity(PatientDetailActivty.class,bundle);	
			}else{
				IntentHelper.getInstance(activity).gotoActivityWithURL(OrderDetailsMoreActivity.class, adapter.getItem(arg2).getActionUrl());
			}
			
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		isLoading = false;
	}
	
	
}
