package com.shoushuzhitongche.app.view.mine;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.QJNetUICallback;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.network.Net;
import com.dixiang.framework.utils.AlertDialogs;
import com.dixiang.framework.utils.ToastUtil;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.view.mine.bean.UserInfoEntity;

public class PersonalTempActivity extends BaseActivity {

	private String verified;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_temp);
		findViewById(R.id.rl_1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!CommonUtils.isLogin(activity)) {
					CommonUtils.gotoLogin(activity);
					return;
				}

				if ("1".equals(CommonUtils.getProfile(activity))
						|| "1.0".equals(CommonUtils.getProfile(activity))) {

					Intent intent = new Intent(activity,
							PersonalInfoActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(activity,
							AddUserInfoActivity.class);
					startActivity(intent);
				}

			}
		});

		findViewById(R.id.rl_2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!CommonUtils.isLogin(activity)) {
					CommonUtils.gotoLogin(activity);
					return;
				}
				if ("1".equals(CommonUtils.getProfile(activity))
						|| "1.0".equals(CommonUtils.getProfile(activity))) {

					Intent intent = null;

					if (verified != null && verified.length() > 0) {

						if ("未认证".equals(verified)) {
							intent = new Intent(activity,
									PersonalInfoAuthActivity.class);
							intent.putExtra("verified", verified);

							activity.startActivity(intent);
						} else if ("认证中".equals(verified)) {
							intent = new Intent(activity,
									PersonalInfoAuthActivity.class);
							intent.putExtra("verified", verified);

							activity.startActivity(intent);
						} else {// 已认证
							intent = new Intent(activity,
									PersonalInfoAuthIngActivity.class);
							activity.startActivity(intent);
						}

					} else {
						Toast.makeText(activity, "数据加载未完成！", Toast.LENGTH_SHORT)
								.show();
					}

				} else {
					Toast.makeText(activity, "请先完善个人信息！", Toast.LENGTH_SHORT)
							.show();
					Intent intent = new Intent(activity,
							AddUserInfoActivity.class);
					startActivity(intent);
				}
			}
		});

		findViewById(R.id.login_out).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialogs.getAlertDialog(activity).showAlertDialog(
						"您确定要退出登录?", new OnClickListener() {
							@Override
							public void onClick(View arg0) {

								CommonUtils.setToken(activity, null);
								CommonUtils.gotoLogin(activity);
								finish();
							}
						});

			}
		});
		getShiMingState();
	}

	@Override
	public void initHeaderView() {
		setTopViewBg(R.color.actionbar_bg_color);
		setTitleViewValue(0, 0, R.color.white);
		setTitleViewValue("个人信息");
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void getShiMingState() {

		String url = CommonUtils.getMain(activity)
				+ CommonUtils.getGetParm(activity, "");
		getHttpResult(url, Constants.HTTP_INDEX.First,
				new TypeToken<QjResult<HashMap<String, UserInfoEntity>>>() {
				});

	}

	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		HashMap<String, UserInfoEntity> map = (HashMap<String, UserInfoEntity>) result
				.getResults();
		if (map != null && !map.isEmpty()) {
			UserInfoEntity userInfoEntity = map.get("userInfo");
			if (userInfoEntity.getDoctorCerts()) {
				if (userInfoEntity.getVerified()) {
					verified = "已认证";
				} else {
					verified = "认证中";
				}

			} else {
				verified = "未认证";
			}
		} else {
			ToastUtil.showToast(activity, "获取用户信息失败！", Toast.LENGTH_LONG);
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}
}
