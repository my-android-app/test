package com.shoushuzhitongche.app.view.booking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.QJNetUICallback;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.network.Net;
import com.dixiang.framework.utils.AlertDialogs;
import com.dixiang.framework.utils.ToastUtil;
import com.dixiang.framework.utils.Utils;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.doctor.bean.DoctorDetailsEntity;
import com.shoushuzhitongche.app.view.localdata.bean.CommonEntity;
import com.shoushuzhitongche.app.view.localdata.dao.LocalDataDao;
import com.shoushuzhitongche.app.view.order.OrderDetailsActivity;
import com.shoushuzhitongche.app.view.patient.PatientDetailActivty;
import com.shoushuzhitongche.app.view.patient.PatientSelectActivity;
import com.shoushuzhitongche.app.view.patient.bean.PatientEntity;

public class BookingDoctorActivity extends BaseActivity implements OnClickListener{
	private View view_add;
	private RadioButton rb_come,rb_goto;
	private DoctorDetailsEntity doctorDetailsEntity;
	private PatientEntity patientEntity;
	private String travel_type;
	private List<CommonEntity> listCommon;
	private boolean isComplete = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_doctor);
		initView();
		initData();
	}
	
	private void initData(){
		listCommon =  LocalDataDao.newInstance(activity).getBookingTrave();
		rb_come.setText(listCommon.get(0).get_value());
		rb_goto.setText(listCommon.get(1).get_value());
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
		view_add = findViewById(R.id.view_add);
		view_add.setOnClickListener(this);
		rb_come = (RadioButton) findViewById(R.id.rb_come);
		rb_goto = (RadioButton) findViewById(R.id.rb_goto);
		
		rb_come.setOnCheckedChangeListener(onCheckedChangeListener);
		rb_goto.setOnCheckedChangeListener(onCheckedChangeListener);
		doctorDetailsEntity =  (DoctorDetailsEntity) getIntent().getSerializableExtra("doctorDetails");
		
		((TextView)findViewById(R.id.tv_response)).setText("您已选择："+doctorDetailsEntity.getName() +"   "+doctorDetailsEntity.getmTitle() + "  " + doctorDetailsEntity.getaTitle() +"\n"+doctorDetailsEntity.getHospitalName()+"   "+doctorDetailsEntity.getHpDeptName());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_add:
			Intent intent = new Intent(BookingDoctorActivity.this,PatientSelectActivity.class);
			startActivityForResult(intent , 100);
			break;
		default:
			break;
		}
	}
	
	private void doNet(){
		
		if(Utils.isStringEmpty(travel_type)) {
			ToastUtil.showToast(getApplicationContext(), "请选择就诊意向！");
			return;
		}
		
		if(patientEntity == null){
			ToastUtil.showToast(getApplicationContext(), "您还没有选择患者！");
			return;
		}
		
		EditText et_dsc = ((EditText)findViewById(R.id.et_dsc));
		String detail = et_dsc.getText().toString();
		if(Utils.isStringEmpty(detail)){
			et_dsc.setFocusable(true);
			ToastUtil.showToast(getApplicationContext(), "请填写就诊意见！");
			return;
		}
		
		LoadingDialog.getInstance(activity).show();
		if(!isComplete){
			return;
		}
		isComplete = false;
		
		String doctor_name = patientEntity.getName();
		String patient_id = patientEntity.getId();
		String doctor_id = doctorDetailsEntity.getId();
		
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("patient_id", patient_id); //患者id
		map.put("username", CommonUtils.getMobile(activity)); //用户名
		map.put("token", CommonUtils.getToken(activity)); //tokn
		map.put("expected_doctor", doctor_name); // 医生姓名
		map.put("travel_type", travel_type); //就诊意向
		map.put("detail", detail); //诊疗意见
		map.put("key", "patientbooking"); //诊疗意见
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
	
	public void onCommit(View view){
		doNet();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && data != null){
			patientEntity = (PatientEntity) data.getSerializableExtra(Constants.PARMS.BACK_DATA);
			if(patientEntity != null){
				((TextView)findViewById(R.id.et_patientname)).setText(patientEntity.getName());	
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		AlertDialogs.getAlertDialog(this).showAlertDialog(
				R.string.isCancell, new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
		return;
	}
}
