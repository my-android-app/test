package com.shoushuzhitongche.app.view.booking;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.QJNetUICallback;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.network.Net;
import com.dixiang.framework.utils.AlertDialogs;
import com.dixiang.framework.utils.ToastUtil;
import com.dixiang.framework.utils.Utils;
import com.dixiang.framework.utils.Utils.IOnTimeSelectListener;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.localdata.CitySelectView;
import com.shoushuzhitongche.app.view.localdata.bean.CommonEntity;
import com.shoushuzhitongche.app.view.localdata.dao.LocalDataDao;
import com.shoushuzhitongche.app.view.patient.bean.PatientEntity;

public class AddBaseInfoActivity extends BaseActivity{

	private String city_id;
	private String gender_id;
	private String province_id;
	private String yearStr;
	private String monthStr;
	private boolean isCompelet = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_baseinfo);
		createSexRB();
	}
	
	@Override
	public void initHeaderView() {
		setTopViewBg(R.color.actionbar_bg_color);
		setTitleViewValue(R.string.create_patient, 0, R.color.white);
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onDataCommit(View view){
		checkData();
	}
	
	public void onTimeClick(View view){
		Utils.showTimeDialog(activity,Calendar.getInstance(), 0, timeSelectListener);
	}
	
	public void onCityClick(View view){
//		IntentHelper.getInstance(activity).gotoActivity(CitySelectView.class);
		Intent intent = new Intent(this,CitySelectView.class);
		startActivityForResult(intent, Constants.INTENTCODE.CREATE_PATIENT_ACTIVITY);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			if(data != null ){
				String city_name = data.getStringExtra(Constants.PARMS.CITY_NAME);	
				((TextView)findViewById(R.id.et_city)).setText(city_name);
				city_id = data.getStringExtra(Constants.PARMS.CITY_ID);
				province_id = data.getStringExtra(Constants.PARMS.PROVINCE_ID);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private IOnTimeSelectListener timeSelectListener = new IOnTimeSelectListener() {

		@Override
		public void onTimeSelected(int viewid, String value) {
			((TextView)findViewById(R.id.et_birth)).setText(value);
			
		}
		
		public void onTimeSelected(int viewid, String year, String month, String day) {
			yearStr = year;
			monthStr = month;
		};
		
	};
	
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			int id = arg0.getCheckedRadioButtonId();
			gender_id = ((RadioButton)arg0.findViewById(id)).getTag().toString();
//			gender_id = ((RadioButton)arg0.getChildAt(arg1)).getTag().toString(); 
		}
	};
	
	
	private void checkData(){
		EditText et_patient_name = (EditText)findViewById(R.id.et_patient_name);
		EditText et_disease_name = (EditText)findViewById(R.id.et_disease_name);
		EditText et_patient_phone = (EditText)findViewById(R.id.et_patient_phone);
		EditText et_dsc = (EditText)findViewById(R.id.et_dsc);
		
		String username = et_patient_name.getText().toString();
		String birth = ((TextView)findViewById(R.id.et_birth)).getText().toString();
		String diseaseName = et_disease_name.getText().toString();
		String dsc = et_dsc.getText().toString();
		String phone = et_patient_phone.getText().toString();
		
		if(Utils.isStringEmpty(username)) {
			et_patient_name.setFocusable(true);
			ToastUtil.showToast(activity, getResources().getString(R.string.patient_name_error));
			return;
		}
		
		if(Utils.isStringEmpty(birth)){
			ToastUtil.showToast(activity, getResources().getString(R.string.birth_error));
			return;
		}
		
		if(Utils.isStringEmpty(city_id)){
			ToastUtil.showToast(activity, getResources().getString(R.string.city_error));
			return;
		}
		
		if(Utils.isStringEmpty(diseaseName)){
			et_disease_name.setFocusable(true);
			ToastUtil.showToast(activity, getResources().getString(R.string.disease_name_error));
			return;
		}
		
		if(Utils.isStringEmpty(gender_id)){
			ToastUtil.showToast(activity, getResources().getString(R.string.gender_error));
			return;
		}
	    
		if(Utils.isStringEmpty(phone)){
			et_patient_phone.setFocusable(true);
			ToastUtil.showToast(activity, getResources().getString(R.string.phone_error));
			return;
		}
		
		if (!Utils.checkMobile(phone)) {
			et_patient_phone.setFocusable(true);
			ToastUtil.showToast(activity, getResources().getString(R.string.phone_error));
			return;
		}
		
		if (Utils.isStringEmpty(dsc)) {
			et_dsc.setFocusable(true);
			ToastUtil.showToast(activity, getResources().getString(R.string.dcs_error));
			return;
		}
		
		doNet(username,birth,diseaseName,dsc,phone);
	}
	
	private void doNet(final String userName, String birth, String diseaseName, String dsc, String phone){
		LoadingDialog.getInstance(activity).show();
		if(!isCompelet){
			return;
		}
		isCompelet = false;
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("username", CommonUtils.getMobile(activity));
		params.put("disease_name", diseaseName);
		params.put("birth_year", yearStr);
		params.put("birth_month", monthStr);
		params.put("gender", gender_id);
		params.put("city_id", city_id);
		params.put("disease_detail", dsc);	
		params.put("name", userName);
		params.put("token", CommonUtils.getToken(activity));
		params.put("state_id",province_id);
		params.put("mobile",phone);
		params.put("key","patient");
		String url = CommonUtils.getSavepatient(activity);
		postHttpResult(url, params, Constants.HTTP_INDEX.First, new TypeToken<QjResult<HashMap<String, Object>>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		HashMap<String, Object> map = (HashMap<String, Object>) result.getResults();
		if(map != null && !map.isEmpty()){
			String id = map.get("id")+"";
			String actionUrl = map.get("actionUrl")+"";
			Bundle bundle = new Bundle();
			bundle.putString(Constants.PARMS.ID, id);
			bundle.putBoolean(Constants.PARMS.IS_NEED_RETURN, getIntent().getBooleanExtra(Constants.PARMS.IS_NEED_RETURN, false));
			IntentHelper.getInstance(activity).gotoActivity(UploadFileActivity.class, bundle);
			finish();
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		isCompelet = true;
		LoadingDialog.getInstance(activity).dismiss();
	}
	
	private void createSexRB(){
		float density = getResources().getDisplayMetrics().density;
		RadioGroup radioGroup = (RadioGroup)findViewById(R.id.rg_type);
		List<CommonEntity> list =  LocalDataDao.newInstance(activity).getGender();
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i ++){

				RadioButton radioButton = new RadioButton(activity);
				radioButton.setText(list.get(i).get_value());
				radioButton.setTag(list.get(i).getKey_id());
				radioButton.setId(Integer.parseInt(list.get(i).getKey_id()));
				RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				params_rb.setMargins(0, 0, (int)(20*density), 0);
//				if(i == 0){
//					gender_id = list.get(0).getKey_id();
//					radioButton.setChecked(true);
//				}
				radioGroup.addView(radioButton,i,params_rb);
			}
			radioGroup.invalidate();
			radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
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
