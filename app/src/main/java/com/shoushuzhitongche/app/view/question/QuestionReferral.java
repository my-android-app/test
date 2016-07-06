package com.shoushuzhitongche.app.view.question;

import java.util.HashMap;
import java.util.Map;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import com.shoushuzhitongche.app.view.question.bean.SignedEntity;

public class QuestionReferral extends BaseActivity{
	private CheckBox rb11,rb12,rb13,rb14;
	private CheckBox check31,check32,check33,check34;
	private CheckBox [] radioButtons;
	private EditText edt_number,preferred_patient;
	private CheckBox [] radioButtons3;
	private String fee;
	private String prep_days;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_referral);
		initView();
		doNet();
	}
	
	@Override
	public void initHeaderView() {
		setTopViewBg(R.color.actionbar_bg_color);
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setTitleViewValue(0, 0, R.color.white);
		setTitleViewValue("接受病人转诊");
	}
	
	private void initView(){
		rb11 = (CheckBox) findViewById(R.id.rb11);
		rb12 = (CheckBox) findViewById(R.id.rb12);
		rb13 = (CheckBox) findViewById(R.id.rb13);
		rb14 = (CheckBox) findViewById(R.id.rb14);
		
		radioButtons = new CheckBox []{rb11,rb12,rb13,rb14};
		
		check31 = (CheckBox) findViewById(R.id.check31);
		check32 = (CheckBox) findViewById(R.id.check32);
		check33 = (CheckBox) findViewById(R.id.check33);
		check34 = (CheckBox) findViewById(R.id.check34);
		
		radioButtons3 = new CheckBox []{check31,check32,check33,check34};
		
		rb11.setOnCheckedChangeListener(onCheckedChangeListener);
		rb12.setOnCheckedChangeListener(onCheckedChangeListener);
		rb13.setOnCheckedChangeListener(onCheckedChangeListener);
		rb14.setOnCheckedChangeListener(onCheckedChangeListener);
		
		check31.setOnCheckedChangeListener(onCheckedChangeListener);
		check32.setOnCheckedChangeListener(onCheckedChangeListener);
		check33.setOnCheckedChangeListener(onCheckedChangeListener);
		check34.setOnCheckedChangeListener(onCheckedChangeListener);
		
		edt_number = (EditText) findViewById(R.id.edt_number);
		preferred_patient = (EditText) findViewById(R.id.preferred_patient);

		edt_number.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				rb11.setChecked(false);
				rb12.setChecked(false);
				rb13.setChecked(false);
				rb14.setChecked(true);
			}
		});
	}
	
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			String value = arg0.getTag()+"";
			switch (arg0.getId()) {
				case R.id.rb11:
				case R.id.rb12:
				case R.id.rb13:
				case R.id.rb14:
					if(arg1){
						for (int i = 0; i < radioButtons.length; i++) {
							int index = Integer.parseInt(value) - 1;
							if(index != i){
								radioButtons[i].setChecked(false);
							}
						}
						if("1".equals(value)){
							fee = "0";
						}else if("2".equals(value)){
							fee = "500";
						}else if("3".equals(value)){
							fee = "1000";
						}else if("4".equals(value)){
							fee = "";
						}
					}
					break;
				case R.id.check31:
				case R.id.check32:
				case R.id.check33:
				case R.id.check34:
					if(arg1){
						for (int i = 0; i < radioButtons3.length; i++) {
							int index = Integer.parseInt(value) - 1;
							if(index != i){
								radioButtons3[i].setChecked(false);
							}
						}
						if("1".equals(value)){
							prep_days = "3d";
						}else if("2".equals(value)){
							prep_days = "1w";
						}else if("3".equals(value)){
							prep_days = "2w";
						}else if("4".equals(value)){
							prep_days = "3w";
						}
					}
					break;
			default:
				break;
			}
		}
	};
	
	private void doNet(){
		LoadingDialog.getInstance(activity).show();
		String actionUrl = CommonUtils.getDoctorzzview(activity)+CommonUtils.getGetParm(activity, "");
		getHttpResult(actionUrl, Constants.HTTP_INDEX.First, new TypeToken<QjResult<SignedEntity>>() {});
	}
	
	public void onCommit(View view){
		String preferred = preferred_patient.getText().toString();
		
		if(Utils.isStringEmpty(preferred)){
			ToastUtil.showToast(activity, "请填写对转诊病例的要求!");
			return;
		}
		
		if(Utils.isStringEmpty(fee)){
			fee = edt_number.getText().toString();
			if(Utils.isStringEmpty(fee)){
				ToastUtil.showToast(activity, "请选择或填写正确的转诊费!");
				return;
			}
		}
		
		if(Utils.isStringEmpty(prep_days)){
			ToastUtil.showToast(activity, "请选择最快安排床位时间!");
		}
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("username", CommonUtils.getMobile(activity));
		map.put("token", CommonUtils.getToken(activity));
		map.put("preferred_patient", preferred);
		map.put("fee", fee);
		map.put("prep_days", prep_days);
		map.put("is_join", "1");
		map.put("key", "doctorzz");
		LoadingDialog.getInstance(activity).show();
		String actionUrl = CommonUtils.getSavedoctorzz(activity);
		postHttpResult(actionUrl, map, Constants.HTTP_INDEX.Second, new TypeToken<QjResult<Object>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		if(flg.equals(Constants.HTTP_INDEX.First)){
			SignedEntity signedEntity = (SignedEntity) result.getResults();
			if(signedEntity != null){
				loadView(signedEntity);
			}
		}else if(flg.equals(Constants.HTTP_INDEX.Second)){
			ToastUtil.showToast(activity, "提交成功！");
			finish();
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}
	
	private void loadView(SignedEntity signedEntity){
		
		if(signedEntity != null && signedEntity.getUserDoctorZz()!= null){
			fee = signedEntity.getUserDoctorZz().getFee();
			if( "0".equals(fee) ){
				rb11.setChecked(true);
			}else if( "500".equals(fee) ){
				rb12.setChecked(true);
			}else if( "1000".equals(fee) ){
				rb13.setChecked(true);
			}else{
				rb14.setChecked(true);
				edt_number.setText(fee);
			}
		}
		
		prep_days = signedEntity.getUserDoctorZz().getPrep_days();
		if("三天内".equals(prep_days)){
			check31.setChecked(true);
		}else if("一周内".equals(prep_days)){
			check32.setChecked(true);
		}else if("两周内".equals(prep_days)){
			check33.setChecked(true);
		}else if("三周内".equals(prep_days)){
			check34.setChecked(true);
		}
		
		preferred_patient.setText(signedEntity.getUserDoctorZz().getPreferredPatient());
	}
	

}
