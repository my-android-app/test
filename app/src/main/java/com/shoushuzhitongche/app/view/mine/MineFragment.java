package com.shoushuzhitongche.app.view.mine;

import java.util.HashMap;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dixiang.framework.BaseFragment;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.utils.ToastUtil;
import com.dixiang.framework.utils.Utils;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.WebViewActivity;
import com.shoushuzhitongche.app.view.WebViewActivity2;
import com.shoushuzhitongche.app.view.mine.bean.UserInfoEntity;
import com.shoushuzhitongche.app.view.patient.PatientListActivity;
import com.shoushuzhitongche.app.view.receive.ReceiveInviteActivity;

public class MineFragment extends BaseFragment implements OnClickListener{
	private RelativeLayout rv_person;
	private TextView tv_user ,tv_shiming;
	private String verified;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine, container, false); 
		view.findViewById(R.id.view_mypatient).setOnClickListener(this);
		view.findViewById(R.id.view_receive).setOnClickListener(this);
		view.findViewById(R.id.view_contact).setOnClickListener(this);
		view.findViewById(R.id.view_invite).setOnClickListener(this);
		view.findViewById(R.id.view_sign).setOnClickListener(this);
		view.findViewById(R.id.view_agreement).setOnClickListener(this);

		tv_shiming = (TextView) view.findViewById(R.id.tv_shiming);
		tv_shiming.setVisibility(View.INVISIBLE);

		tv_shiming.setOnClickListener(this);
		tv_user = (TextView) view.findViewById(R.id.tv_user);
		rv_person = (RelativeLayout) view.findViewById(R.id.rv_person);
		rv_person.setOnClickListener(this);

		getShiMingState();
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(CommonUtils.isLogin(activity)){
			if(!Utils.isStringEmpty(CommonUtils.getUserName(activity))){
				tv_user.setText("您好！"+CommonUtils.getUserName(activity));
			}else if (CommonUtils.getMobile(activity)!=null&&CommonUtils.getMobile(activity).length()>0) {
				tv_user.setText("您好！"+CommonUtils.getMobile(activity));
			}
			getShiMingState();
		}else {
			tv_user.setText("您好！请登录");
			tv_shiming.setVisibility(View.INVISIBLE);
		}
		
	}
	@Override
	public void initHeaderView() {
		clearTitle();
		setTitleViewValue("个人中心");
		setTitleViewValue( 0, 0, R.color.white );
	}
	@Override
	public void onClick(View view) {
		Bundle bundle ;
		Intent intent ;
		switch (view.getId()) {
		case R.id.view_mypatient:
			if(!CommonUtils.isLogin(activity)){
				CommonUtils.gotoLogin(activity);
				return;
			}
			intent = new Intent(activity,PatientListActivity.class);
			bundle = new Bundle();
			intent.putExtras(bundle);
			activity.startActivity(intent);
			break;

		case R.id.view_receive:
			if(!CommonUtils.isLogin(activity)){
				CommonUtils.gotoLogin(activity);
				return;
			}
			bundle = new Bundle();
			IntentHelper.getInstance(activity).gotoActivity(ReceiveInviteActivity.class, bundle);

			//			activity.startActivity(new Intent(activity,AppointmentDetailsActivity.class));
			break;

		case R.id.rv_person:
			if(!CommonUtils.isLogin(activity)){
				CommonUtils.gotoLogin(activity);
				return;
			}
			activity.startActivity(new Intent(activity,PersonalTempActivity.class));

			break;

		case R.id.view_contact:
			getPicAlert();

			break;

		case R.id.view_invite:
			activity.startActivity(new Intent(activity,InviteActivity.class));
			break;
		case R.id.tv_shiming:
			if(!CommonUtils.isLogin(activity)){
				CommonUtils.gotoLogin(activity);
				return;
			}
			if ("1".equals(CommonUtils.getProfile(activity)) || "1.0".equals(CommonUtils.getProfile(activity))) {


				if (verified!=null&&verified.length()>0) {
					if ("未认证".equals(verified)) {
						intent = new Intent(activity,PersonalInfoAuthActivity.class);
						intent.putExtra("verified", verified);
						activity.startActivity(intent);
					}else if ("认证中".equals(verified)) {
						intent = new Intent(activity,PersonalInfoAuthActivity.class);
						intent.putExtra("verified", verified);
						activity.startActivity(intent);
					}else if ("已认证".equals(verified)){//已认证
						intent = new Intent(activity,PersonalInfoAuthIngActivity.class);
						activity.startActivity(intent);
					}else {
						Toast.makeText(activity, "未知状态！", Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(activity, "数据加载未完成！", Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(activity, "请先完善个人信息！", Toast.LENGTH_SHORT).show();
				intent = new Intent(activity,AddUserInfoActivity.class);
				activity.startActivity(intent);
			}

			break;
		case R.id.view_sign:
			if(!CommonUtils.isLogin(activity)){
				CommonUtils.gotoLogin(activity);
				return;
			}
			if ("1".equals(CommonUtils.getProfile(activity)) || "1.0".equals(CommonUtils.getProfile(activity))) {
				IntentHelper.getInstance(activity).gotoActivityWithURLAndTitle(WebViewActivity2.class, "专家签约协议", "file:///android_asset/agreement.html");
			}else {
				Toast.makeText(activity, "请先完善个人信息！", Toast.LENGTH_SHORT).show();
				intent = new Intent(activity,AddUserInfoActivity.class);
				activity.startActivity(intent);
			}


			break;

		case R.id.view_agreement:
			if(!CommonUtils.isLogin(activity)){
				CommonUtils.gotoLogin(activity);
				return;
			}
			if ("1".equals(CommonUtils.getProfile(activity)) || "1.0".equals(CommonUtils.getProfile(activity))) {
				if (verified!=null&&verified.length()>0) {
					if ("未认证".equals(verified)) {

						Toast toast = Toast.makeText(activity, "请您先完成实名认证！", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}else if ("认证中".equals(verified)) {

						Toast toast	= Toast.makeText(activity, "请等待实名认证通过！", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();

					}else if ("已认证".equals(verified)){//已认证
						String url = "http://md.mingyizhudao.com/mobiledoctor/home/page/view/consultingAgreement";
						IntentHelper.getInstance(activity).gotoActivityWithURLAndTitle(WebViewActivity.class, "医生顾问协议", url);
					}else {
						Toast.makeText(activity, "未知状态！", Toast.LENGTH_SHORT).show();
					}

				}else {
					Toast.makeText(activity, "数据加载未完成！", Toast.LENGTH_SHORT).show();
				}

			}else {
				Toast.makeText(activity, "请先完善个人信息！", Toast.LENGTH_SHORT).show();
				intent = new Intent(activity,AddUserInfoActivity.class);
				activity.startActivity(intent);
			}

			break;
		default:
			break;
		}
	}


	private void getPicAlert() {

		final AlertDialog dlg = new AlertDialog.Builder(activity).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.dialog_get_pic);
		dlg.setCanceledOnTouchOutside(true);
		TextView checkphoto = (TextView) window.findViewById(R.id.checkphoto);
		checkphoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"400-6277-120"));    
				startActivity(intent); 

			}
		});

		// 关闭alert对话框架
		TextView cancel = (TextView) window.findViewById(R.id.dialog_btn_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
			}
		});
	}


	private void getShiMingState() {

		String url = CommonUtils.getMain(activity)+ CommonUtils.getGetParm(activity, "");
		
		getHttpResult(url, Constants.HTTP_INDEX.First, new TypeToken<QjResult<HashMap<String,UserInfoEntity>>>() {});

	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
			HashMap<String,UserInfoEntity> map = (HashMap<String, UserInfoEntity>) result.getResults();
			if(map != null && !map.isEmpty()){
				UserInfoEntity  userInfoEntity = map.get("userInfo");
				tv_shiming.setVisibility(View.VISIBLE);
				if (userInfoEntity.getDoctorCerts()) {
					if (userInfoEntity.getVerified()) {
						verified="已认证";
					}else {
						verified="认证中";
					}
					CommonUtils.setUserName(activity, userInfoEntity.getName());
					if(!Utils.isStringEmpty(userInfoEntity.getName())){
						tv_user.setText("您好！"+userInfoEntity.getName());
					}
				}else {
					verified="未认证";
				}
				tv_shiming.setText(verified);
			}else {

				ToastUtil.showToast(activity, "获取用户信息失败！",Toast.LENGTH_LONG);

			}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}


}
