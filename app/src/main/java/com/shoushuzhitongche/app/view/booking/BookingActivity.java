package com.shoushuzhitongche.app.view.booking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.utils.AlertDialogs;
import com.dixiang.framework.utils.ToastUtil;
import com.dixiang.framework.utils.Utils;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.doctor.DoctorListActivity;
import com.shoushuzhitongche.app.view.doctor.bean.DoctorDetailsEntity;
import com.shoushuzhitongche.app.view.localdata.bean.CommonEntity;
import com.shoushuzhitongche.app.view.localdata.dao.LocalDataDao;
import com.shoushuzhitongche.app.view.order.OrderDetailsActivity;
import com.shoushuzhitongche.app.view.patient.PatientDetailActivty;

public class BookingActivity extends BaseActivity implements OnClickListener{
	
	private View view_add;
	private RadioButton rb_come,rb_goto;
	private DoctorDetailsEntity doctorDetailsEntity;
	private SelectDoctorReceiver receiveBroad;
	private String travel_type;
	private List<CommonEntity> listCommon;
	private EditText edt_doctorName,et_doctodept,et_doctohp;
	
	private boolean isComplete = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent);
		initView();
		initData();
		initBroadcastReceiver();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public void initHeaderView() {
		setTitleViewValue("预约手术");
		setTitleViewValue(0,0,R.color.white);
		setTopViewBg(R.color.actionbar_bg_color);
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void initView() {
		view_add = findViewById(R.id.iv_add);
		view_add.setOnClickListener(this);
		rb_come = (RadioButton) findViewById(R.id.rb_come);
		rb_goto = (RadioButton) findViewById(R.id.rb_goto);
		
		edt_doctorName = (EditText)findViewById(R.id.et_doctorname);
		et_doctodept = (EditText)findViewById(R.id.et_doctodept);
		et_doctohp = (EditText)findViewById(R.id.et_doctohp);
		 
		edt_doctorName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if(doctorDetailsEntity != null){
					if(!(arg0.toString().trim()).equals(doctorDetailsEntity.getName())){
						et_doctodept.setText("");
						et_doctohp.setText("");
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});
		
		rb_come.setOnCheckedChangeListener(onCheckedChangeListener);
		rb_goto.setOnCheckedChangeListener(onCheckedChangeListener);
		
	}

	private void initData(){
		listCommon =  LocalDataDao.newInstance(activity).getBookingTrave();
		receiveBroad = new SelectDoctorReceiver();
		
		rb_come.setText(listCommon.get(0).get_value());
		rb_goto.setText(listCommon.get(1).get_value());
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add:
			Intent intent = new Intent(activity,DoctorListActivity.class);
			intent.putExtra(Constants.PARMS.IS_COME_FROM_BOOKING, true);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try{
			unregisterReceiver(receiveBroad);	
		}catch(Exception ex){
			
		}
	}
	
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if(arg1){
				if(R.id.rb_come == arg0.getId()){
					travel_type = listCommon.get(0).getKey_id();
					rb_come.setChecked(true);
					rb_goto.setChecked(false);
				}else{
					travel_type = listCommon.get(1).getKey_id();
					rb_come.setChecked(false);
					rb_goto.setChecked(true);
				}
			}
		}
	};
	
	public class SelectDoctorReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if(arg1 != null){
				doctorDetailsEntity = (DoctorDetailsEntity) arg1.getSerializableExtra(Constants.PARMS.BACK_DATA);
				if(doctorDetailsEntity != null){
					edt_doctorName.setText(doctorDetailsEntity.getName());	
					et_doctodept.setText(doctorDetailsEntity.getHpDeptName());
					et_doctohp.setText(doctorDetailsEntity.getHospitalName());
				}
			}
		}
	}
	
	private void initBroadcastReceiver(){
		// 注册广播接收
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.PARMS.RECIVER_FLG);    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(receiveBroad, filter);
	}
	
	public void onCommit(View view){
		doNet();
	}
	
	private void doNet(){
		if(Utils.isStringEmpty(travel_type)) {
			ToastUtil.showToast(getApplicationContext(), "请选择就诊意向！");
			return;
		}
		EditText et_dsc = ((EditText)findViewById(R.id.et_dsc));
		String detail = et_dsc.getText().toString();
		if (Utils.isStringEmpty(detail)){
			et_dsc.setFocusable(true);
			ToastUtil.showToast(getApplicationContext(), "请填写就诊意见！");
			return;
		}
		
		LoadingDialog.getInstance(activity).show();
		if(!isComplete){
			return;
		}
		isComplete = false;
		
		String doctor_name = edt_doctorName.getText().toString();
		
		boolean isNull = (doctorDetailsEntity == null);
		if(!isNull && !doctor_name.equals(doctorDetailsEntity.getName().toString().trim())){
			isNull = true;
		}
		String patient_id = getIntent().getStringExtra(Constants.PARMS.ID);
		String expected_dept = et_doctodept.getText().toString();
		String expected_hospital = et_doctohp.getText().toString();
		
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("patient_id", patient_id); //患者id
		map.put("username", CommonUtils.getMobile(activity)); //用户名
		map.put("token", CommonUtils.getToken(activity)); //tokn
		map.put("expected_doctor", doctor_name);
		map.put("expected_dept", expected_dept);
		map.put("expected_hospital", expected_hospital);
		map.put("travel_type", travel_type); //就诊意向
		map.put("detail", detail); //诊疗意见
		map.put("key", "patientbooking");
		String url = CommonUtils.getSavepatientbooking(activity);
		postHttpResult(url, map, Constants.HTTP_INDEX.First, new TypeToken<QjResult<Map<String, Object>>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		Map<String, Object> map = (Map<String, Object>) result.getResults();
		if(map != null && !map.isEmpty()){
			String actionUrl = map.get("actionUrl")+""; 
			IntentHelper.getInstance(activity).gotoActivityWithURL(OrderDetailsActivity.class, actionUrl);
			closeActivity(PatientDetailActivty.class.getSimpleName());
			finish();
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}
	
	@Override
	public void onBackPressed() {
		AlertDialogs.getAlertDialog(this).showAlertDialog(
				R.string.isCancell, new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						unregisterReceiver(receiveBroad);
						finish();
					}
				});
		return;
	}
}
