package com.shoushuzhitongche.app.view.login;

import java.util.HashMap;
import java.util.Map;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

public class RegisterActivity extends BaseActivity{
	
	private EditText phone,phone_code,pwd;
	private TimeCount timeCount;
	private TextView btn_getsmscode;
	private CheckBox see_password_cb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
	}
	
	private void initView(){
		phone = (EditText) findViewById(R.id.et_phone);
		phone_code = (EditText) findViewById(R.id.et_phone_code);
		pwd = (EditText) findViewById(R.id.et_pwd);
		btn_getsmscode = (TextView) findViewById(R.id.btn_getsmscode);
		see_password_cb = (CheckBox)findViewById(R.id.see_password_cb);
		see_password_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	            @Override
	            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
	                if (b){
	                	pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
	                	charEnd();
	                }else {
	                	pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	                	charEnd();
	                }
	            }
	        });
		timeCount = new TimeCount(60000, 1000);// 构造CountDownTimer对象
	}
	
	public void onSmsCode(View view) {
		String phoneStr = phone.getText().toString().trim();
		if(Utils.checkMobile(phoneStr)){
			timeCount.start();// 开始计时
			Map params = new HashMap<String , Object>();
			params.put("mobile", phoneStr);
			params.put("action_type", 100);
			params.put("key", "smsVerifyCode");
			LoadingDialog.getInstance(activity).show();
			
			String url = CommonUtils.getSmsverifycode(activity);
			postHttpResult(url, params, Constants.HTTP_INDEX.First,  new TypeToken<QjResult<Map<String, Object>>>() {});
		}else{
			ToastUtil.showToast(activity, "请核对你的手机号");
		}
	}
	
	public void doRegister(View view){
		String phoneStr = phone.getText().toString().trim();
		String codeStr = phone_code.getText().toString().trim();
		String pwdStr = pwd.getText().toString().trim();
		
		if(!Utils.checkMobile(phoneStr)){
			ToastUtil.showToast(activity, "请核对你的手机号");
			return;
		}
		if(Utils.isStringEmpty(codeStr)){
			ToastUtil.showToast(activity, "请输入手机验证码");
			return;
		}
		if(Utils.isStringEmpty(pwdStr)){
			ToastUtil.showToast(activity, "请输入密码");
			return;
		}
		
		if(pwdStr.length()<4){
			ToastUtil.showToast(activity, "密码长度至少4位！");
			return;
		}
		
		Map map = new HashMap<String,Object>();
		map.put("username", phoneStr);
		map.put("password", pwdStr);
		map.put("verify_code", codeStr);
		map.put("autoLogin", "1");
		map.put("key", "userregister");
		LoadingDialog.getInstance(activity).show();
		
		String url = CommonUtils.getRegisterUrl(activity);
		postHttpResult(url, map, Constants.HTTP_INDEX.Second, new TypeToken<QjResult<Map<String, Object>>>() {});
	}
	
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		if(flg.equals(Constants.HTTP_INDEX.First)){
			Toast.makeText(activity, "验证码已发送，注意查收！", 0)
			.show();
		}else if(flg.equals(Constants.HTTP_INDEX.Second)){
			Map<String, Object> map = (Map<String, Object>) result.getResults();
			if(map != null && !map.isEmpty()){
				String token = map.get("token")+"";
						StatService.onEvent(activity, "register", "eventLabel", 1);
				CommonUtils.setToken(activity, token);
			}
			finish();
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
		setTitleViewValue("注册");
		
		setRightText( "登录" ,R.color.white);
		initOptionView(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//IntentHelper.getInstance(activity).gotoActivity(LoginActivity2.class);
				finish();
			}
		});
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
	
	//切换后将EditText光标置于末尾
    public void charEnd() {
        CharSequence charSequence = pwd.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

}
