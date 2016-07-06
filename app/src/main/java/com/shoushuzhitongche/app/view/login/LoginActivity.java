package com.shoushuzhitongche.app.view.login;

import java.util.HashMap;
import java.util.Map;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mobstat.StatService;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.utils.ToastUtil;
import com.dixiang.framework.utils.Utils;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;

public class LoginActivity extends BaseActivity {

	private View by_pwd,by_phone,login_by_pwd,login_by_phone;
	private TimeCount timeCount;
	private TextView btn_getsmscode;
	private EditText et_phone_phone,et_pwd_phone,et_pwd,et_phonecode;
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
		setTitleViewValue("登录");
		
		setRightText( "注册" ,R.color.white);
		initOptionView(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				IntentHelper.getInstance(activity).gotoActivity(RegisterActivity.class);
				//finish();
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login1);
		initView();
	}
	
	private void initView(){
		by_pwd = findViewById(R.id.by_pwd);
		by_phone = findViewById(R.id.by_phone);
		login_by_pwd = findViewById(R.id.login_by_pwd);
		login_by_phone = findViewById(R.id.login_by_phone);
		
		et_phone_phone = (EditText) findViewById(R.id.et_phone_phone);
		et_pwd_phone = (EditText) findViewById(R.id.et_pwd_phone);
		
		if (CommonUtils.getMobile(activity)!=null&&CommonUtils.getMobile(activity).length()>0) {
			et_pwd_phone.setText(CommonUtils.getMobile(activity));
		}
		
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_phonecode = (EditText) findViewById(R.id.et_phonecode);
		
		btn_getsmscode = (TextView) findViewById(R.id.btn_getsmscode);
		
		timeCount = new TimeCount(60000, 1000);// 构造CountDownTimer对象
		
		findViewById(R.id.tv_changepassword).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentHelper.getInstance(activity).gotoActivity(ChangePassWordActivity.class);
			}
		});
	}
	
	public void onSmsCode(View view) {
		String phoneStr = et_phone_phone.getText().toString().trim();
		if(Utils.checkMobile(phoneStr)){
			timeCount.start();// 开始计时
			Map params = new HashMap<String , Object>();
			params.put("mobile", phoneStr);
			params.put("action_type", 102);
			params.put("key", "smsVerifyCode");
			LoadingDialog.getInstance(activity).show();
			String url = CommonUtils.getSmsverifycode(activity);
			postHttpResult(url, params, Constants.HTTP_INDEX.First, new TypeToken<QjResult<Map<String, Object>>>() {});
		}else{
			ToastUtil.showToast(activity, "请核对你的手机号");
		}
	}
	
	public void doLoginByPhone(View view){
		String phoneStr = et_phone_phone.getText().toString().trim();
		String codeStr = et_phonecode.getText().toString().trim();
		if(!Utils.checkMobile(phoneStr)){
			ToastUtil.showToast(activity, "请核对你的手机号");
			return;
		}
		if(Utils.isStringEmpty(codeStr)){
			ToastUtil.showToast(activity, "请输入手机验证码");
			return;
		}
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("username", phoneStr);
		params.put("verify_code", codeStr);// 验证码
		params.put("key", "userlogin");
		LoadingDialog.getInstance(activity).show();
		
		String url = CommonUtils.getUserlogin(activity);
		postHttpResult(url, params, Constants.HTTP_INDEX.Second, new TypeToken<QjResult<Map<String, Object>>>() {});
		CommonUtils.setMobile(activity, phoneStr);
	}
	
	public void doLoginByPwd(View view){
		String phoneStr = et_pwd_phone.getText().toString().trim();
		String pwdStr = et_pwd.getText().toString().trim();
		if(!Utils.checkMobile(phoneStr)){
			ToastUtil.showToast(activity, "请核对你的手机号");
			return;
		}
		if(pwdStr.length()<4){
			ToastUtil.showToast(activity, "密码长度至少4位！");
			return;
		}
		LoadingDialog.getInstance(activity).show();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("username", phoneStr);
		params.put("password", pwdStr);// 验证码
		params.put("key", "userpawlogin");
		
		String url = CommonUtils.getUserpawlogin(activity);
		postHttpResult(url, params, Constants.HTTP_INDEX.Third, new TypeToken<QjResult<Map<String, Object>>>() {});
		CommonUtils.setMobile(activity, phoneStr);
		
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		if(flg.equals(Constants.HTTP_INDEX.First)){
			Toast.makeText(activity, "验证码已发送，注意查收！", 0)
			.show();
		}else{
			Map<String, Object> map = (Map<String, Object>) result.getResults();
			if(map != null && !map.isEmpty()){
				String token = map.get("token")+"";
				Object object = map.get("is_new_user");
				if(object != null){
				    double is_new_user = Double.parseDouble(object+""); 
					if(is_new_user == 2){
						StatService.onEvent(activity, "register", "eventLabel", 1);
					}
				}
				CommonUtils.setToken(activity, token);
				String isProfile = map.get("isProfile")+"";
				CommonUtils.setProfile(activity, isProfile);
				finish();
			}
		}
	}
	
	@Override
	public void onFailure(QjResult result, String flg) {
		super.onFailure(result, flg);
		if(flg.equals(Constants.HTTP_INDEX.First)){
			Toast.makeText(activity, "验证码发送失败！", 0).show();
			timeCount.cancel();
			btn_getsmscode.setText("获取验证码");
			btn_getsmscode.setClickable(true);
			btn_getsmscode.setBackgroundColor(getResources().getColor(R.color.white));
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}
	
	public void onMenuChange(View view){
		if(view.getId() == R.id.rb1){
			by_pwd.setVisibility(View.VISIBLE);
			by_phone.setVisibility(View.INVISIBLE);
			login_by_pwd.setVisibility(View.VISIBLE);
			login_by_phone.setVisibility(View.GONE);
		}
		
		if(view.getId() == R.id.rb2){
			by_phone.setVisibility(View.VISIBLE);
			by_pwd.setVisibility(View.INVISIBLE);
			login_by_phone.setVisibility(View.VISIBLE);
			login_by_pwd.setVisibility(View.GONE);
		}
		
	}
	


	
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btn_getsmscode.setText("获取验证码");
			btn_getsmscode.setClickable(true);
			btn_getsmscode.setBackgroundColor(getResources().getColor(R.color.white));
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btn_getsmscode.setClickable(false);
			btn_getsmscode.setText(millisUntilFinished / 1000 + "秒后重发");
			btn_getsmscode.setBackgroundColor(getResources().getColor(R.color.actionbar_bg_color));
		}
	}
}
