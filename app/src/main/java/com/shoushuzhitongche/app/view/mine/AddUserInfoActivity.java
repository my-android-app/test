package com.shoushuzhitongche.app.view.mine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.QJNetUICallback;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.network.Net;
import com.dixiang.framework.utils.ToastUtil;
import com.dixiang.framework.utils.Utils;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.view.localdata.CitySelectView;
import com.shoushuzhitongche.app.view.localdata.bean.CommonEntity;
import com.shoushuzhitongche.app.view.localdata.dao.LocalDataDao;
import com.shoushuzhitongche.app.view.localdata.widget.SelectDialog;
import com.shoushuzhitongche.app.view.localdata.widget.SelectDialog.IOnSelectedListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class AddUserInfoActivity extends BaseActivity implements
		OnClickListener {
	private Drawable drawable;
	private EditText et_name, et_hospital_name, et_dept;
	private TextView tv_city, medical_title, academic_title ,tv_btn;
	private boolean isEdit = true;
	private String  name, state_id, city_id, hospital_name,
			hp_dept_name, clinical_title_id, academic_title_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_userinfo);
		initView();
	}

	@Override
	public void initHeaderView() {
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setTitleViewValue(0, 0, R.color.white);
		setTitleViewValue("完善个人信息");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {

		List<CommonEntity> list = null;
		switch (v.getId()) {
		case R.id.tv_city:
			Intent intent = new Intent(this, CitySelectView.class);
			startActivityForResult(intent,
					Constants.INTENTCODE.CREATE_PATIENT_ACTIVITY);
			break;
		case R.id.medical_title:
			list = LocalDataDao.newInstance(activity).getClinical();
			SelectDialog.showAlertDialog(activity, list, onSelectedListener);
			break;
		case R.id.academic_title:
			list = LocalDataDao.newInstance(activity).getAcademic();
			SelectDialog.showAlertDialog(activity, list, onSelectedListener2);
			break;
			
		case R.id.tv_btn:
			if (checkContent()) {
				addUserInfo();
			}
			break;	
			
		default:
			break;
		}
	}


	private void addUserInfo() {
		LoadingDialog.getInstance(activity).show();
		Map<String ,Object> map = new HashMap<String, Object>();
		map.put("username", CommonUtils.getMobile(activity));
		map.put("token", CommonUtils.getToken(activity));
		map.put("name", name);
		map.put("state_id", state_id);
		map.put("city_id", city_id);
		map.put("hospital_name", hospital_name);
		map.put("hp_dept_name", hp_dept_name);
		map.put("clinical_title", clinical_title_id);
		map.put("academic_title", academic_title_id);
		map.put("key", "profile");
		String url = CommonUtils.getSaveprofile(activity);
		postHttpResult(url, map, Constants.HTTP_INDEX.First, new TypeToken<QjResult<HashMap<String, Object>>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		if(flg.equals(Constants.HTTP_INDEX.First)){
			CommonUtils.setProfile(activity, "1");
			ToastUtil.showToast(activity, "信息保存成功！");
			finish();
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data != null) {
				String city_name = data
						.getStringExtra(Constants.PARMS.CITY_NAME);
				tv_city.setText(city_name);
				city_id = data.getStringExtra(Constants.PARMS.CITY_ID);
				state_id = data.getStringExtra(Constants.PARMS.PROVINCE_ID);
			}
		}
	}

	private IOnSelectedListener onSelectedListener = new IOnSelectedListener() {

		@Override
		public void onSelectedListener(CommonEntity entity) {
			medical_title.setText(entity.get_value());
			clinical_title_id = entity.getKey_id();
		}
	};

	private IOnSelectedListener onSelectedListener2 = new IOnSelectedListener() {

		@Override
		public void onSelectedListener(CommonEntity entity) {

			academic_title.setText(entity.get_value());
			academic_title_id = entity.getKey_id();
		}
	};

	
	
	private void initView() {
		drawable = getResources().getDrawable(R.drawable.select_ico);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight()); // 设置边界

		et_name = (EditText) findViewById(R.id.et_name);
		et_hospital_name = (EditText) findViewById(R.id.et_hospital_name);
		et_dept = (EditText) findViewById(R.id.et_dept);

		tv_city = (TextView) findViewById(R.id.tv_city);
		medical_title = (TextView) findViewById(R.id.medical_title);
		academic_title = (TextView) findViewById(R.id.academic_title);
		tv_city.setOnClickListener(this);
		medical_title.setOnClickListener(this);
		academic_title.setOnClickListener(this);
		
		tv_btn = (TextView) findViewById(R.id.tv_btn);
		tv_btn.setOnClickListener(this);

		setInput();// 编辑状态
	}

	private void setInput() {
		if (isEdit) {
			et_name.setEnabled(true);
			et_hospital_name.setEnabled(true);
			et_dept.setEnabled(true);
			tv_city.setClickable(true);
			medical_title.setClickable(true);
			academic_title.setClickable(true);
			tv_city.setCompoundDrawables(null, null, drawable, null);// 画在右边
			medical_title.setCompoundDrawables(null, null, drawable, null);// 画在右边
			academic_title.setCompoundDrawables(null, null, drawable, null);// 画在右边
		} else {
			et_name.setEnabled(false);
			et_hospital_name.setEnabled(false);
			et_dept.setEnabled(false);
			tv_city.setClickable(false);
			medical_title.setClickable(false);
			academic_title.setClickable(false);
			tv_city.setCompoundDrawables(null, null, null, null);// 画在右边
			medical_title.setCompoundDrawables(null, null, null, null);// 画在右边
			academic_title.setCompoundDrawables(null, null, null, null);// 画在右边
		}
	}

	private boolean checkContent() {
		name = et_name.getText().toString();
		hospital_name = et_hospital_name.getText().toString();
		hp_dept_name = et_dept.getText().toString();

		if(Utils.isStringEmpty(name)){
			ToastUtil.showToast(activity, "请填写姓名！");
			et_name.requestFocus();
			return false;
		}
		
		if(Utils.isStringEmpty(city_id)){
			ToastUtil.showToast(activity, getResources().getString(R.string.city_error));
			return false;
		}
		
		if(Utils.isStringEmpty(hospital_name)){
			ToastUtil.showToast(activity, "请填写医院名称！");
			et_hospital_name.requestFocus();
			return false;
		}
		
		if(Utils.isStringEmpty(hp_dept_name)){
			ToastUtil.showToast(activity, "请填写医院科室！");
			et_dept.requestFocus();
			return false;
		}
		
		if(Utils.isStringEmpty(clinical_title_id)){
			ToastUtil.showToast(activity, "请选择医学职称！");
			return false;
		}
		
		if(Utils.isStringEmpty(academic_title_id)){
			ToastUtil.showToast(activity, "请选择学术职称！");
			return false;
		}
		return true;
	}

}
