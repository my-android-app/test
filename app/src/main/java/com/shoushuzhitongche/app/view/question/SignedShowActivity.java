package com.shoushuzhitongche.app.view.question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.QJNetUICallback;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.network.Net;
import com.dixiang.framework.utils.ToastUtil;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.question.bean.SignedEntity;

public class SignedShowActivity extends BaseActivity{
	 
	private View view_go_out;
	private View view_need_referral;
	private View view_refer,view_out;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_question_result);
		initView();
		LoadingDialog.getInstance(activity).show();
		doNet();
	}
	
	
	private void initView(){
		view_go_out = findViewById(R.id.view_go_out);
		view_need_referral = findViewById(R.id.view_need_referral);
		
		view_refer = findViewById(R.id.view_refer);
		view_out = findViewById(R.id.view_out);
		
		view_out.setOnClickListener(onClickListener);
		view_refer.setOnClickListener(onClickListener);
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
		setTitleViewValue("签约专家");
	}

	private void doNet(){
		String actionUrl = CommonUtils.getDoctordr(activity)+CommonUtils.getGetParm(activity, "");
		getHttpResult(actionUrl, Constants.HTTP_INDEX.First, new TypeToken<QjResult<SignedEntity>>() {});
	}
	
	public void onCannel(View view){
		LoadingDialog.getInstance(activity).show();
		String actionUrl = "";
		Map map = new HashMap<String,Object>();
		map.put("username", CommonUtils.getMobile(activity));
		map.put("token", CommonUtils.getToken(activity));
		if(view.getId() == R.id.btn_hz){
			map.put("key", "notjoinhz");
			actionUrl = CommonUtils.getNotjoinhz(activity);
		}else if(view.getId() == R.id.btn_zz){
			map.put("key", "notjoinzz");
			actionUrl = CommonUtils.getNotjoinzz(activity);
		}
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
			ToastUtil.showToast(activity, "修改成功！");
			doNet();
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.view_out:
				IntentHelper.getInstance(activity).gotoActivity(QuestionOut.class);
				break;
			case R.id.view_refer:
				IntentHelper.getInstance(activity).gotoActivity(QuestionReferral.class);
				break;
			default:
				break;
			}
		}
	};
	
	private void loadView(SignedEntity signedEntity){
		if(signedEntity != null && signedEntity.getUserDoctorHz() != null && !"0".equals(signedEntity.getUserDoctorHz().getIs_join())){
			TextView min_no_surgery = (TextView)findViewById(R.id.min_no_surgery);
			TextView travel_duration = (TextView)findViewById(R.id.travel_duration);
			TextView week_days = (TextView)findViewById(R.id.week_days);
			TextView patients_prefer = (TextView)findViewById(R.id.patients_prefer);
			TextView fee = (TextView)findViewById(R.id.fee);
			TextView freq_destination = (TextView)findViewById(R.id.freq_destination);
			TextView destination_req = (TextView)findViewById(R.id.destination_req);
			
			
			
			List travel = signedEntity.getUserDoctorHz().getTravel_duration();
			StringBuffer buffer = new StringBuffer("");
			String value = "";
			for (int i = 0; i < travel.size(); i++) {
				if("train3h".equals(travel.get(i))){
					value = "高铁3小时内";
				}else if("train5h".equals(travel.get(i))){
					value = "高铁5小时内";
				}else if("plane2h".equals(travel.get(i))){
					value = "飞机2小时内";
				}else if("plane3h".equals(travel.get(i))){
					value = "飞机3小时内";
				}else if("none".equals(travel.get(i))){
					value = "无";
				}
				
				buffer.append(value);
				if(i != (travel.size() - 1)){
					buffer.append("、");
				}
			}
			travel_duration.setText("时间成本要求："+buffer.toString());
			
			List weeks = signedEntity.getUserDoctorHz().getWeek_days();
			buffer = new StringBuffer("");
			for (int i = 0; i < weeks.size(); i++) {
				if("1".equals(weeks.get(i))){
					value = "周一";
				}else if("2".equals(weeks.get(i))){
					value = "周二";
				}else if("3".equals(weeks.get(i))){
					value = "周三";
				}else if("4".equals(weeks.get(i))){
					value = "周四";
				}else if("5".equals(weeks.get(i))){
					value = "周五";
				}else if("6".equals(weeks.get(i))){
					value = "周六";
				}else if("7".equals(weeks.get(i))){
					value = "周日";
				}
				
				buffer.append(value);
				if(i != (weeks.size() - 1)){
					buffer.append("、");
				}
			}
			min_no_surgery.setText("外出会诊台数："+signedEntity.getUserDoctorHz().getMin_no_surgery());
			
			week_days.setText("方便会诊时间："+buffer.toString());
			patients_prefer.setText("愿意会诊病例："+signedEntity.getUserDoctorHz().getPatients_prefer());
			fee.setText("每台会诊费区间："+signedEntity.getUserDoctorHz().getFee_min() +"  -  "+signedEntity.getUserDoctorHz().getFee_max());
			freq_destination.setText("常去地区或医院："+signedEntity.getUserDoctorHz().getFreq_destination());
			destination_req.setText("对手术地点/条件要求："+signedEntity.getUserDoctorHz().getDestination_req());
			view_go_out.setVisibility(View.VISIBLE);
		}else{
			view_go_out.setVisibility(View.GONE);
		}
		
		if(signedEntity != null && signedEntity.getUserDoctorZz() != null && !"0".equals(signedEntity.getUserDoctorZz().getIs_join())){
			((TextView)findViewById(R.id.referral_fees)).setText("转诊费："+signedEntity.getUserDoctorZz().getFee());
			((TextView)findViewById(R.id.lasttime)).setText("对转诊病例要求："+signedEntity.getUserDoctorZz().getPrep_days());
			((TextView)findViewById(R.id.referral_request)).setText("最快安排床位时间："+signedEntity.getUserDoctorZz().getPreferredPatient());
			view_need_referral.setVisibility(View.VISIBLE);
		}else{
			view_need_referral.setVisibility(View.GONE);
		}
	}
	

	@Override
	protected void onRestart() {
		super.onRestart();
		LoadingDialog.getInstance(activity).show();
		doNet();
	}
}
