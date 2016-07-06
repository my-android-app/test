package com.shoushuzhitongche.app.view.doctor;

import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.network.Net;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.booking.BookingDoctorActivity;
import com.shoushuzhitongche.app.view.doctor.bean.DoctorDetailsEntity;
import com.shoushuzhitongche.app.view.doctor.bean.DoctorsEntity;

public class DoctorDetailsActivity extends BaseActivity{

	private ImageView iv_doctor;
	private TextView tv_dept,tv_mtitle,tv_hospital,tv_good_at,tv_career_exp;
	private LinearLayout ll_reason,ll_good_at ,ll_honour,ll_career_exp;
	private RelativeLayout rl_reason;
	private Button btn_order ;
	private DoctorDetailsEntity detailsEntity;
	private DoctorsEntity doctorListEntity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_details);
		initView();
		getDoctorDetails();
	}

	@Override
	public void initHeaderView() {
		setTitleViewValue(0,0,R.color.white);
		setTitleViewValue("医生详情");
		setTopViewBg(R.drawable.doctor_details_top_bg);
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initView() {

		doctorListEntity = (DoctorsEntity) getIntent().getSerializableExtra("doctorListEntity");

		iv_doctor = (ImageView)findViewById(R.id.iv_doctor);
		tv_dept = (TextView)findViewById(R.id.tv_dept);
		tv_mtitle = (TextView)findViewById(R.id.tv_mtitle);
		tv_hospital = (TextView)findViewById(R.id.tv_hospital);
		tv_good_at = (TextView)findViewById(R.id.tv_good_at);
		tv_career_exp = (TextView)findViewById(R.id.tv_career_exp);

		ll_reason = (LinearLayout)findViewById(R.id.ll_reason);
		ll_good_at = (LinearLayout)findViewById(R.id.ll_good_at);
		ll_honour = (LinearLayout)findViewById(R.id.ll_honour);
		ll_career_exp = (LinearLayout)findViewById(R.id.ll_career_exp);
		rl_reason = (RelativeLayout)findViewById(R.id.rl_reason);

		rl_reason.setVisibility(View.GONE);
		ll_good_at.setVisibility(View.GONE);
		ll_honour.setVisibility(View.GONE);
		ll_career_exp.setVisibility(View.GONE);

		btn_order = (Button)findViewById(R.id.btn_order);

		iv_doctor.setImageResource(R.drawable.default_doctor);
		String tempUrl = doctorListEntity.getImageUrl();
		if (tempUrl != null && tempUrl.length() > 0) {
			Net.displayImage(tempUrl, iv_doctor,R.drawable.default_doctor);
		}

		String aTitle=  doctorListEntity.getaTitle();
		tv_dept.setText(doctorListEntity.getHpDeptName());

		String mTitle=  doctorListEntity.getmTitle();
		if (mTitle!=null&&(!"null".equals(mTitle))) {
			tv_mtitle.setText(mTitle);
		}

		tv_hospital.setText(doctorListEntity.getHpName());

	}

	private void getDoctorDetails() {
		String actionUrl = doctorListEntity.getActionUrl();//CommonUtils.getDoctorUrl(activity)+ doctorListEntity.getId();
		LoadingDialog.getInstance(activity).show();
		
		getHttpResult(actionUrl, Constants.HTTP_INDEX.First, new TypeToken<QjResult<Map<String,DoctorDetailsEntity>>>() {});
		
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		Map<String,DoctorDetailsEntity> map = (Map<String, DoctorDetailsEntity>) result.getResults();
		if(map != null && !map.isEmpty()){
			setDoctorDetails(map.get("doctor"));
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}
	
	

	private void setDoctorDetails(DoctorDetailsEntity doctorDetailsEntity) {
		
		if( "1".equals( doctorDetailsEntity.getIsContracted()) || "1.0".equals(doctorDetailsEntity.getIsContracted()))
		{
			final boolean is_from_booking = getIntent().getBooleanExtra(Constants.PARMS.IS_COME_FROM_BOOKING, false);
			if(is_from_booking) btn_order.setText("选择");
			findViewById(R.id.tv_tag_qianyue).setVisibility(View.VISIBLE); 
			btn_order.setVisibility( View.VISIBLE );
			btn_order.setOnClickListener( new OnClickListener()
			{
				@Override
				public void onClick( View v )
				{
					//CommonUtils.goToCreateOrder(activity, doctorListEntity.getId(), doctorListEntity.getName(), doctorListEntity.getHpDeptName(), doctorListEntity.getHpName(), doctorListEntity.getActionUrl(),false);
//					startActivity(new Intent(activity,BookOperationActivity.class));
					if(is_from_booking){
						Intent intent = new Intent();  //Itent就是我们要发送的内容
				        intent.putExtra(Constants.PARMS.BACK_DATA,detailsEntity);  
				        intent.setAction(Constants.PARMS.RECIVER_FLG);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
				        sendBroadcast(intent); 
				        closeActivity(DoctorListActivity.class.getSimpleName());
				        finish();
					}else{
						Bundle bundle = new Bundle();
						bundle.putSerializable("doctorDetails",detailsEntity);
						IntentHelper.getInstance(activity).gotoActivity(BookingDoctorActivity.class, bundle);
//						detailsEntity = null;
						bundle = null;
					}
				}
			} );

		}else {
			btn_order.setVisibility( View.GONE );
		}
		
		detailsEntity = doctorDetailsEntity;
		iv_doctor.setImageResource(R.drawable.default_doctor);
		String tempUrl = doctorDetailsEntity.getImageUrl();
		if (tempUrl != null && tempUrl.length() > 0) {
			Net.displayImage(tempUrl, iv_doctor,R.drawable.default_doctor);
		}

		String aTitle=  doctorDetailsEntity.getaTitle();
		tv_dept.setText(doctorDetailsEntity.getHpDeptName());

		String mTitle=  doctorDetailsEntity.getmTitle();
		if (mTitle!=null&&(!"null".equals(mTitle))) {
			tv_mtitle.setText(mTitle);
		}

		tv_hospital.setText(doctorDetailsEntity.getHospitalName());


		String description=  doctorDetailsEntity.getDescription();
		if (description!=null&&(!"null".equals(description))) {
			ll_good_at.setVisibility(View.VISIBLE);
			tv_good_at.setText(description);
		}else {
			ll_good_at.setVisibility(View.GONE);
		}

		String vareerExp=  doctorDetailsEntity.getCareerExp();
		if (vareerExp!=null&&(!"null".equals(vareerExp))) {
			ll_career_exp.setVisibility(View.VISIBLE);
			tv_career_exp.setText(vareerExp);
		}else {
			ll_career_exp.setVisibility(View.GONE);
		}

		String honour[] = doctorDetailsEntity.getHonour();
		if (honour != null && honour.length> 0) {
			ll_honour.setVisibility(View.VISIBLE);

			for (int i = 0; i < honour.length; i++) {

				View contentView = getLayoutInflater().inflate(R.layout.view_num, null);

				ImageView iv_num = (ImageView)contentView.findViewById(R.id.iv_num);
				TextView tv_num = (TextView)contentView.findViewById(R.id.tv_num);
				iv_num.setImageResource(R.drawable.doctor_details_honor_number);
				tv_num.setText(honour[i]);
				ll_honour.addView(contentView);
			}

		} else {
			ll_honour.setVisibility(View.GONE);
		} 

		String reasons[] = doctorDetailsEntity.getReasons();
		if (reasons != null && reasons.length> 0) {
			rl_reason.setVisibility(View.VISIBLE);

			for (int i = 0; i < reasons.length; i++) {

				View contentView = getLayoutInflater().inflate(R.layout.view_num, null);

				ImageView iv_num = (ImageView)contentView.findViewById(R.id.iv_num);
				//iv_num.setScaleType(ScaleType.CENTER);
				TextView tv_num = (TextView)contentView.findViewById(R.id.tv_num);
				iv_num.setImageResource(R.drawable.doctor_details_honor_number);
				tv_num.setText(reasons[i]);
				ll_reason.addView(contentView);
			}
		} else {
			rl_reason.setVisibility(View.GONE);
		} 
	}
	
}
