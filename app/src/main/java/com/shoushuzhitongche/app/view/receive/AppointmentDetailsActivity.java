package com.shoushuzhitongche.app.view.receive;

import java.util.HashMap;
import java.util.Map;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.bitmap.BimpTempHelper;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.utils.Utils;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.view.receive.bean.AppointDetailsEntity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;


public class AppointmentDetailsActivity extends BaseActivity{
	private String patientBookingId;
	private String accept;
	private String actionUrl;
	private String bkType;
	private String response;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_detail);
		
		String status = getIntent().getStringExtra(Constants.PARMS.STATE);
		if(Utils.isStringEmpty(status)){
			findViewById(R.id.view_option).setVisibility(View.VISIBLE);
		}else{
			findViewById(R.id.view_reply).setVisibility(View.VISIBLE);
		}
		initView();
		doNet();
	}
	
	
	@Override
	public void initHeaderView() {
		setTitleViewValue("手术预约详情");
		setTitleViewValue(0,0,R.color.white);
		setTopViewBg(R.color.actionbar_bg_color);
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}

	private void doNet(){
		LoadingDialog.getInstance(activity).show();
		String url = actionUrl+CommonUtils.getGetParm(activity,"&type="+bkType);
		getHttpResult(url, Constants.HTTP_INDEX.First, new TypeToken<QjResult<Map<String,AppointDetailsEntity>>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		if(flg.equals(Constants.HTTP_INDEX.First)){
			Map<String,AppointDetailsEntity> map = (Map<String, AppointDetailsEntity>) result.getResults();
			AppointDetailsEntity detailsEntity = map.get("booking");
			if (detailsEntity != null) {
				loadView(detailsEntity);
			}
		}else if(flg.equals(Constants.HTTP_INDEX.Second)){
			findViewById(R.id.view_option).setVisibility(View.GONE);
			findViewById(R.id.view_reply).setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.tv_reply)).setText(response);	
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}
	
	private void loadView(AppointDetailsEntity detailsEntity){
		((TextView)findViewById(R.id.intent)).setText(detailsEntity.getTravelType());
		((TextView)findViewById(R.id.tv_expertname)).setText(detailsEntity.getExpected_doctor());
		((TextView)findViewById(R.id.patient_name)).setText(detailsEntity.getPatientName());
		((TextView)findViewById(R.id.sex)).setText(detailsEntity.getGender());
		((TextView)findViewById(R.id.age)).setText(detailsEntity.getAge()+"岁");
		((TextView)findViewById(R.id.city)).setText(detailsEntity.getPlaceCity());
		((TextView)findViewById(R.id.disease_name)).setText(detailsEntity.getDiseaseName());
		((TextView)findViewById(R.id.opinion)).setText(detailsEntity.getDetail());
		((TextView)findViewById(R.id.disease_dsc)).setText(detailsEntity.getDiseaseDetail());
		
		String aa= detailsEntity.getDoctorOpinion();
		if ("null".equals(aa)||aa==null) {
			aa="";
		}
		((TextView)findViewById(R.id.tv_reply)).setText(getStringBuilder("您的答复："+aa, "您的答复："));
		
		patientBookingId = detailsEntity.getId();
	}
	
	public void onReply(View view){
		if(view.getId() == R.id.btn_agree){
			accept = "1";
		}else{
			accept = "0";
		}
		doReply();
	}
	
	private void doReply(){
		if(Utils.isStringEmpty(patientBookingId)) return;
		String url = CommonUtils.getDavedoctoropinion(activity);
		response = ((TextView)findViewById(R.id.tv_response)).getText().toString().trim();
		Map<String, Object> map = new HashMap<String ,Object>();
		map.put("id", patientBookingId);
		map.put("accept", accept);//0为拒绝，1位接受
		map.put("opinion", response);
		map.put("token", CommonUtils.getToken(activity));
		map.put("type", bkType);
		map.put("username", CommonUtils.getMobile(activity));
		map.put("key", "opinion");
		LoadingDialog.getInstance(activity).show();
		postHttpResult(url, map, Constants.HTTP_INDEX.Second, new TypeToken<QjResult<AppointDetailsEntity>>() {});
	}
	
	private void initView(){
		progressDialog = new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("上传中...");

		actionUrl = getIntent().getStringExtra(Constants.PARMS.ACTIONURL);
		
		bkType = getIntent().getStringExtra("type");
	}
	
	private static final int FAIL = 500;// 更新下载进度的标记
	private static final int SUCCESS = 200;
	private ProgressDialog progressDialog;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case SUCCESS:
				progressDialog.dismiss();
				Toast.makeText(activity, "上传成功！", 2000).show();
				finish();
				break;

			case FAIL:
				progressDialog.dismiss();
				Toast.makeText(activity, "上传失败！", 2000).show();
				break;

			default:
				break;
			}
		}
	};
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LoadingDialog.getInstance(activity).dismiss();
		BimpTempHelper.getInstance().clearData();
	}
	
    private SpannableStringBuilder getStringBuilder(String text,String flg) {
   	 if (text==null) {
   		 text="";
		}
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(activity.getResources().getColor(R.color.actionbar_bg_color));
       
        if (flg!=null&&flg.length()>0&&text.contains(flg)) {
       	 builder.setSpan(yellowSpan, text.indexOf(flg), text.indexOf(flg)+flg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
        
		return builder;
	}
}
