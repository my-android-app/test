package com.shoushuzhitongche.app.view.question;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
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

public class QuestionOut extends BaseActivity{

	private CheckBox rb11,rb12,rb13,rb14;
	private CheckBox check31,check32,check33,check34,check35;
	private CheckBox chek41,chek42,chek43,chek44,chek45,chek46,chek47;
	private EditText edt_number,fee_min,fee_max;
	private EditText patients_prefer,freq_destination,destination_req;
	private String min_no_surgery;
	private Set<String> week_days = new HashSet<String>();
	private Set<String> travel_duration = new HashSet<String>();
	private CheckBox [] radioButtons;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_out);
		initView();
		doNet();
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
		check35 = (CheckBox) findViewById(R.id.check35);
		
		chek41 = (CheckBox) findViewById(R.id.chek41);
		chek42 = (CheckBox) findViewById(R.id.chek42);
		chek43 = (CheckBox) findViewById(R.id.chek43);
		chek44 = (CheckBox) findViewById(R.id.chek44);
		chek45 = (CheckBox) findViewById(R.id.chek45);
		chek46 = (CheckBox) findViewById(R.id.chek46);
		chek47 = (CheckBox) findViewById(R.id.chek47);
		
		edt_number = (EditText) findViewById(R.id.edt_number);
		fee_min = (EditText) findViewById(R.id.fee_min);
		fee_max = (EditText) findViewById(R.id.fee_max);
		
		patients_prefer = (EditText) findViewById(R.id.patients_prefer);
		freq_destination = (EditText) findViewById(R.id.freq_destination);
		destination_req = (EditText) findViewById(R.id.destination_req);
		
		rb11.setOnCheckedChangeListener(onCheckedChangeListener);
		rb12.setOnCheckedChangeListener(onCheckedChangeListener);
		rb13.setOnCheckedChangeListener(onCheckedChangeListener);
		rb14.setOnCheckedChangeListener(onCheckedChangeListener);
		
		check31.setOnCheckedChangeListener(onCheckedChangeListener);
		check32.setOnCheckedChangeListener(onCheckedChangeListener);
		check33.setOnCheckedChangeListener(onCheckedChangeListener);
		check34.setOnCheckedChangeListener(onCheckedChangeListener);
		check35.setOnCheckedChangeListener(onCheckedChangeListener);
		
		chek41.setOnCheckedChangeListener(onCheckedChangeListener);
		chek42.setOnCheckedChangeListener(onCheckedChangeListener);
		chek43.setOnCheckedChangeListener(onCheckedChangeListener);
		chek44.setOnCheckedChangeListener(onCheckedChangeListener);
		chek45.setOnCheckedChangeListener(onCheckedChangeListener);
		chek46.setOnCheckedChangeListener(onCheckedChangeListener);
		chek47.setOnCheckedChangeListener(onCheckedChangeListener);
		
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
		setTitleViewValue("去外地会诊");
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
						if(arg0.getId() != R.id.rb14){
							min_no_surgery =  value;
						}else{
							min_no_surgery = "";
						}
					}
					break;
				case R.id.check31:
				case R.id.check32:
				case R.id.check33:
				case R.id.check34:
				case R.id.check35:
					if(arg1){
						if(arg0.getId() == R.id.check31){
							check32.setChecked(false);
							check35.setChecked(false);
						}else if(arg0.getId() == R.id.check32){
							check31.setChecked(false);
							check35.setChecked(false);
						}else if(arg0.getId() == R.id.check33){
							check34.setChecked(false);
							check35.setChecked(false);
						}else if(arg0.getId() == R.id.check34){
							check33.setChecked(false);
							check35.setChecked(false);
						}else if(arg0.getId() == R.id.check35){
							check31.setChecked(false);
							check32.setChecked(false);
							check33.setChecked(false);
							check34.setChecked(false);
						}
						
						travel_duration.add(value);
					}else{
						travel_duration.remove(value);
					}
					break;
				case R.id.chek41:
				case R.id.chek42:
				case R.id.chek43:
				case R.id.chek44:
				case R.id.chek45:
				case R.id.chek46:
				case R.id.chek47:
					if(arg1){
						week_days.add(value);
					}else{
						week_days.remove(value);
					}
					break;

			default:
				break;
			}
		}
	};
	
	public void onCommit(View view){
		String fee_min, fee_max ,patients ,freq , destination;
		fee_min = ((TextView)findViewById(R.id.fee_min)).getText().toString();	
		fee_max = ((TextView)findViewById(R.id.fee_max)).getText().toString();	
		
		if(Utils.isStringEmpty(fee_min)){
			ToastUtil.showToast(activity, "请填写最低额!");
			return;
		}
		
		if(Utils.isStringEmpty(fee_max)){
			ToastUtil.showToast(activity, "请填写最高额!");
			return;
		}
		
		if(Integer.parseInt(fee_max) <= Integer.parseInt(fee_min)){
			ToastUtil.showToast(activity, "最高额必须大于最低额!");
			return;
		}
		
		if(travel_duration.isEmpty()){
			ToastUtil.showToast(activity, "请选择时间成本要求!");
			return;
		}
		
		if(week_days.isEmpty()){
			ToastUtil.showToast(activity, "请选择方便会诊的时间!");
			return;
		}
		
		patients = patients_prefer.getText().toString();
		freq = freq_destination.getText().toString();
		destination = destination_req.getText().toString();
		
		if(Utils.isStringEmpty(patients_prefer)){
			ToastUtil.showToast(activity, "请填写希望会诊什么样的病人!");
			return;
		}
		
		if(Utils.isStringEmpty(freq_destination)){
			ToastUtil.showToast(activity, "请填写常去会诊的地方或医院!");
			return;
		}
		
		if(Utils.isStringEmpty(destination_req)){
			ToastUtil.showToast(activity, "请填写对手术地点条件的要求!");
			return;
		}
		
		if(Utils.isStringEmpty(min_no_surgery)){
			min_no_surgery = edt_number.getText().toString();
			if(Utils.isStringEmpty(min_no_surgery)){
				ToastUtil.showToast(activity, "请选择或填写正确的台数!");
				return;
			}
		}
		
		String week = "";
		Iterator<String> iter = week_days.iterator();
		while(iter.hasNext()){
			String value = iter.next();
			week += value+",";
		}
		week = week.substring(0, week.length()-1);
		
		String duration = "";
		iter = travel_duration.iterator();
		while(iter.hasNext()){
			String value = iter.next();
			duration += value+",";
		}
		duration = duration.substring(0, duration.length()-1);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("username", CommonUtils.getMobile(activity));
		map.put("token", CommonUtils.getToken(activity));
		map.put("min_no_surgery", min_no_surgery);
		map.put("travel_duration", duration);
		map.put("fee_min", fee_min);
		map.put("fee_max", fee_max);
		map.put("week_days", week);
		map.put("patients_prefer", patients);
		map.put("freq_destination", freq);
		map.put("destination_req", destination);
		map.put("is_join", 1);
		map.put("key", "doctorhz");
		String actionUrl = CommonUtils.getSavedoctorhz(activity);
		LoadingDialog.getInstance(activity).show();
		postHttpResult(actionUrl, map, Constants.HTTP_INDEX.First, new TypeToken<QjResult<Object>>() {});
	}
	
	
	private void doNet(){
		LoadingDialog.getInstance(activity).show();
		String actionUrl = CommonUtils.getDoctorhzview(activity)+CommonUtils.getGetParm(activity, "");
		getHttpResult(actionUrl, Constants.HTTP_INDEX.Second, new TypeToken<QjResult<SignedEntity>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		if(flg.equals(Constants.HTTP_INDEX.First)){
			ToastUtil.showToast(activity, "提交成功！");
			finish();
		}else if(flg.equals(Constants.HTTP_INDEX.Second)){
			SignedEntity signedEntity = (SignedEntity) result.getResults();
			if(signedEntity != null){
				loadView(signedEntity);	
			}
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}

	
	private void loadView(SignedEntity signedEntity){
		if(signedEntity != null && signedEntity.getUserDoctorHz() != null){
			min_no_surgery = signedEntity.getUserDoctorHz().getMin_no_surgery();
			if("1".equals(min_no_surgery)){
				rb11.setChecked(true);
			}else if("2".equals(min_no_surgery)){
				rb12.setChecked(true);
			}else if("3".equals(min_no_surgery)){
				rb13.setChecked(true);
			}else {
				rb14.setChecked(true);
				edt_number.setText(min_no_surgery);
			}
			
			fee_min.setText(signedEntity.getUserDoctorHz().getFee_min());
			fee_max.setText(signedEntity.getUserDoctorHz().getFee_max());
			
			List<String> travel = signedEntity.getUserDoctorHz().getTravel_duration();
			for (String object : travel) {
				if("train3h".equals(object)){
					check31.setChecked(true);
				}else if("train5h".equals(object)){
					check32.setChecked(true);
				}else if("plane2h".equals(object)){
					check33.setChecked(true);
				}else if("plane3h".equals(object)){
					check34.setChecked(true);
				}else if("none".equals(object)){
					check35.setChecked(true);
				}
			}
			
			List<String> weeks = signedEntity.getUserDoctorHz().getWeek_days();
			for (String object : weeks) {
				if("1".equals(object)){
					chek41.setChecked(true);
				}else if("2".equals(object)){
					chek42.setChecked(true);
				}else if("3".equals(object)){
					chek43.setChecked(true);
				}else if("4".equals(object)){
					chek44.setChecked(true);
				}else if("5".equals(object)){
					chek45.setChecked(true);
				}else if("6".equals(object)){
					chek46.setChecked(true);
				}else if("7".equals(object)){
					chek47.setChecked(true);
				}
			}
			
			patients_prefer.setText(signedEntity.getUserDoctorHz().getPatients_prefer());
			freq_destination.setText(signedEntity.getUserDoctorHz().getFreq_destination());
			destination_req.setText(signedEntity.getUserDoctorHz().getDestination_req());
		}
		
	}
}
