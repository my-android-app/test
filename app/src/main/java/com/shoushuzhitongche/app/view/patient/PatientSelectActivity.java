package com.shoushuzhitongche.app.view.patient;

import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.utils.ToastUtil;
import com.dixiang.framework.utils.Utils;
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
import com.shoushuzhitongche.app.view.patient.adapter.PatientAdapter;
import com.shoushuzhitongche.app.view.patient.bean.PatientEntity;
import com.shoushuzhitongche.app.view.patient.bean.PatientTemEntity;
public class PatientSelectActivity extends BaseActivity{
	private PatientAdapter adapter;
	private final static String WATTING_TYPE = "1";
	private PullToRefreshView lv_refresh;
	private SelectDoctorReceiver receiveBroad;
	private int page = 1;
	private String patientId;
	private PatientEntity selectPatient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_list);
		initView();
		doNet();
		receiveBroad = new SelectDoctorReceiver();
		initBroadcastReceiver();
	}
	
	private void initView(){
		lv_refresh = (PullToRefreshView) findViewById(R.id.lv_refresh);
		
		adapter = new PatientAdapter(activity ,true);
		
		ListView listView = ((ListView)findViewById(R.id.listview));
				
		listView.setAdapter(adapter);
		
		lv_refresh.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

			@Override
			public void onHeaderRefresh(PullToRefreshView refreshView) {
				// 刷新不加载更多
				page = 1;
				doNet();
			}
		});

		lv_refresh.setOnFooterRefreshListener(new OnFooterRefreshListener() {

			@Override
			public void onFooterRefresh(PullToRefreshView refreshView) {
				// 加载更多
				page++;
				doNet();
			}
		});
		
		findViewById(R.id.et_search).setVisibility(View.GONE);
	}
	
	private void initBroadcastReceiver(){
		// 注册广播接收
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.PARMS.RECIVER_FLG);    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(receiveBroad, filter);
	}

	@Override
	public void initHeaderView() {
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		setTitleViewValue(0,0,R.color.white);
		setTitleViewValue("选择患者");
		initOptionView(0, R.string.dlg_ok, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				PatientEntity entity = adapter.getPatientEntity();
				if(selectPatient == null ){
					selectPatient = entity;
				}
				if(selectPatient != null){
					Intent intent = new Intent();  //Itent就是我们要发送的内容
					intent.putExtra(Constants.PARMS.BACK_DATA, selectPatient);
			        setResult(RESULT_OK, intent);
			        finish();	
				}else{
					ToastUtil.showToast(getApplicationContext(), "您还没有选择患者");	
				}
			}
		});
	}

	private void doNet(){
		doNet(true);
	}
	
	private void doNet(final boolean isRefresh){
		LoadingDialog.getInstance(activity).show();
		String param = CommonUtils.getGetParm(activity,"hasBooking="+WATTING_TYPE+"&api=3&page="+page);
		String url = CommonUtils.getPatientlist(activity)+param;
		getHttpResult(url, Constants.HTTP_INDEX.First, new TypeToken<QjResult<PatientTemEntity>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		PatientTemEntity temEntity = (PatientTemEntity) result.getResults();
		if(temEntity != null){
			List<PatientEntity> list = temEntity.getNoBookingList();
			if(list != null && list.size() > 0){
				if(page == 1){
					adapter.clear();
				}
				for (PatientEntity entity : list) {
					if(entity.getId().equals(patientId)){
						entity.setCheck(true);
						selectPatient = entity;
					}
					adapter.add(entity);	
				}
				
				adapter.notifyDataSetChanged();
			}
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
	
	
	public class SelectDoctorReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if(arg1 != null){
				patientId = arg1.getStringExtra(Constants.PARMS.BACK_DATA);
				if(!Utils.isStringEmpty(patientId)){
					if(adapter.getPatientEntity() != null){
						selectPatient = adapter.getPatientEntity();
						patientId = adapter.getPatientEntity().getId();
					}
					doNet();
				}
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		try{
			unregisterReceiver(receiveBroad);	
		}catch(Exception ex){
			
		}
		finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try{
			unregisterReceiver(receiveBroad);	
		}catch(Exception ex){
			
		}
	}
	
	public void toCreatePatient(View view){
		Bundle bundle = new Bundle();
		bundle.putBoolean(Constants.PARMS.IS_NEED_RETURN, true);
		IntentHelper.getInstance(activity).gotoActivity(AddBaseInfoActivity.class, bundle);
	}
}
