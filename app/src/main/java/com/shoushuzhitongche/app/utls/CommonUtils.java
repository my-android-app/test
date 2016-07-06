package com.shoushuzhitongche.app.utls;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.dixiang.framework.utils.SharedPreferenceUtils;
import com.dixiang.framework.utils.Utils;
import com.shoushuzhitongche.app.view.WebViewActivity;
import com.shoushuzhitongche.app.view.booking.BookingActivity;
import com.shoushuzhitongche.app.view.localdata.dao.LocalDataDao;
import com.shoushuzhitongche.app.view.login.LoginActivity;

public class CommonUtils {
	/**
	 * 获取包名
	 * 
	 * @author kong
	 * @createDate 2014-4-21
	 * @param
	 * @return
	 */
	public static String getPackageName(Context c) {
		String str = "";
		PackageManager manager = c.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
			str = info.packageName; // 包名
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 获得APP的版本名称
	 * 
	 * @param c
	 *            上下文
	 * @return 返回版本名称(例如: 3.2.1)
	 */
	public static String getAppVersionName(Context c) {
		try {
			PackageManager manager = c.getPackageManager();
			PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得APP的版本编号
	 * 
	 * @param c
	 *            上下文
	 * @return 返回版本名称(例如: 3.2.1)
	 */
	public static int getAppVersionCode(Context c) {
		try {
			PackageManager manager = c.getPackageManager();
			PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}



	@SuppressLint("SimpleDateFormat")
	public static String transformDte(String type, String dateString,
			String toType) {
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		SimpleDateFormat sdf2 = new SimpleDateFormat(toType);
		String toDateString = null;
		try {
			if (dateString != null) {
				Date date = sdf.parse(dateString);
				toDateString = sdf2.format(date);
			} else {
				return null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return toDateString;
	}

	// 获取状态栏高度
	public static int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	public static void goToWebView(Context mContext, String title,
			String actionUrl) {
		Bundle bundle = new Bundle();
		bundle.putString(Constants.PARM_PAGE_TITLE, title);
		bundle.putString(Constants.PARM_ACTION_URL, actionUrl);
		IntentHelper.getInstance(mContext).gotoActivity(WebViewActivity.class,
				bundle);
	}

	public static String getMobile(Context context) {
		return (String) SharedPreferenceUtils.getInstance(context,
				Constants.APPINFO).get(Constants.MOBILE);
//		return "18818081163";
	}

	public static String getToken(Context context) {
		return (String) SharedPreferenceUtils.getInstance(context,
				Constants.APPINFO).get(Constants.USERTOKEN);
//		return "E8D6E4F8AAE274FA0B1FB160A085A202";
	}
	
//	public static String getMobile(Context context) {
////		return (String) SharedPreferenceUtils.getInstance(context,
////				Constants.APPINFO).get(Constants.MOBILE);
//		//return "18818081163";
//		//return "13816439927";
//		return "13127640379";
//		//return "15221216919";
//	}
//
//	public static String getToken(Context context) {
////		return (String) SharedPreferenceUtils.getInstance(context,
////				Constants.APPINFO).get(Constants.USERTOKEN);
//		//return "250757E18CB75D6A2D87DF23F49D4995";
//		//return "8B03B640F780AE5A01ABF0C6988A6247";
//		return "A4F383FFAE75D28D5355106AE755E0E5";
//		
//		//return "5373224F9B27C55543A51F45E2F95838";
//	}
	

	public static void setMobile(Context context, String mobile) {
		SharedPreferenceUtils.getInstance(context, Constants.APPINFO).save(
				Constants.MOBILE, mobile);
	}
	
	public static void setUserName(Context context, String userName) {
		SharedPreferenceUtils.getInstance(context, Constants.APPINFO).save(
				getMobile(context), userName);
	}
	
	public static String getUserName(Context context) {
		return (String) SharedPreferenceUtils.getInstance(context,
				Constants.APPINFO).get(getMobile(context));
		
	}
	

	public static void setToken(Context context, String token) {
		SharedPreferenceUtils.getInstance(context, Constants.APPINFO).save(
				Constants.USERTOKEN, token);
	}
	
	public static String getDataVersion(Context context) {
//		String dataVersion = (String) SharedPreferenceUtils.getInstance(context,Constants.APPINFO).get("dataVersion");
//		if( !( dataVersion!=null&&dataVersion.length()>0) ){
//			dataVersion = "0";
//		}
		String dataVersion = Constants.baseURL+"dataversion";
		return dataVersion;
	}
	
	public static void setDataVersion(Context context, String dataVersion) {
		SharedPreferenceUtils.getInstance(context, Constants.APPINFO).save("dataVersion", dataVersion);
	}
	
	//isProfile  是否填写了个人信息 1已填写 0未填写
	public static String getProfile(Context context) {
		String isProfile = (String) SharedPreferenceUtils.getInstance(context,Constants.APPINFO).get("isProfile");
		if( !( isProfile!=null&&isProfile.length()>0) ){
			isProfile = "0";
		}
		return isProfile;
	}
	
	//isProfile  是否填写了个人信息 1已填写 0未填写
	public static void setProfile(Context context, String isProfile) {
		SharedPreferenceUtils.getInstance(context, Constants.APPINFO).save("isProfile", isProfile);
	}
	
	public static String getTokenParam(Activity activity){
		return "?username="+CommonUtils.getMobile( activity )+"&token="+CommonUtils.getToken( activity );
	}

	public static String getPayUrl(Context activity,String refno){
		String url = LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.PAYMENTURL);
		if(Utils.isStringEmpty(url)){
			return "";
		}
		url = url.replaceFirst("#", refno);
		url = url.replaceFirst("#", "android");
		return url;
	}
	
	public static void saveLocalData(Context context, Boolean isTrue){
		SharedPreferenceUtils.getInstance(context, Constants.APPINFO).save(Constants.LOACLE_DATA, isTrue);
	}
	
	public static void goToCreateOrder(Context context, String id,
			String doctorName, String hpDept, String hpName, String actionUrl,
			boolean isDept) {
		String token = getToken(context);
		if (token == null || token.length() == 0 || token.equals("null")) {
			IntentHelper.getInstance(context).gotoActivity(LoginActivity.class);
			return;
		}
		Bundle bundle = new Bundle();
		bundle.putString(Constants.ORDER.PARM_ORDER_DOCTOR_ID, id);
		bundle.putString(Constants.ORDER.PARM_ORDER_DOCTOR_NAME, doctorName);
		bundle.putString(Constants.ORDER.PARM_ORDER_DOCTOR_DEPT, hpDept);
		bundle.putString(Constants.ORDER.PARM_ORDER_DOCTOR_HP, hpName);
		bundle.putString(Constants.PARM_ACTION_URL, actionUrl);
		bundle.putBoolean(Constants.PARM_ORDER_TYPE, isDept);
		IntentHelper.getInstance(context).gotoActivity(
				BookingActivity.class, bundle);
	}
	
	/**获取发送验证码的接口地址*/
	public static String getSmsverifycode(Context activity){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.smsverifycode);
	}
	
	/**获取登录接口地址*/
	public static String getUserlogin(Context activity){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.userlogin);
	}
	
	/**获取创建患者接口地址*/
	public static String getSavepatient(Context activity){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.savepatient);
	}
	
	/**获取我的换着 列表接口地址*/
	public static String getPatientlist(Context activity){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.patientlist);
	}
	
	/**获取发出的预约接口地址*/
	public static String getBookinglist(Context activity,String param){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.bookinglist);
	}
	
	/**获取收到的预约接口地址*/
	public static String getDoctorbookinglist(Context activity,String param){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.doctorbookinglist)+param;
	}
	
	/**查看个人信息*/
	public static String getDoctorinfo(Context activity,String param){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.doctorinfo);
	}
	
	/**获取查看个人信息（基本信息）接口地址*/
	public static String getSaveprofile(Context activity){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.saveprofile);
	}
	
	/**获取查看个人信息（实名认证）接口地址*/
	public static String getMain(Context activity,String param){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.main);
	}
	
	/**获取查看个人信息（实名认证）接口地址*/
	public static String getSavepatientbooking(Context activity){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.savepatientbooking);
	}
	
	/**获取患者基本信息接口地址*/
	public static String getPatientinfo(Context activity ,String param){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.patientinfo);
	}
	
	/**签约专家*/
	public static String getContractdoctorlist(Context activity){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.contractdoctorlist);
	}
	
	
	public static String getOrderlist(Context activity){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.orderlist);
	}
	
	
	
	/**收到的预约详情*/
	public static String getDoctorpatientinfo(Context activity,String id ,String param){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.doctorpatientinfo);
	}
	
	/**用户注册*/
	public static String getRegisterUrl(Context activity){
		return LocalDataDao.newInstance(activity).getActionUrl(Constants.DATAYPTE.userregister);
	}
	
	/**
	 * 用户登录地址
	 * @param context
	 * @return
	 */
	public static String getUserpawlogin(Context context) {
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.userpawlogin);
	}
	
	/**忘记密码*/
	public static String getForgetpassword(Context context) {
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.forgetpassword);
	}
	
	/**发现*/
	public static String getFindView(Context context) {
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.findView);
	}
	
	/**获取城市列表*/
	public static String getCitylist(Context context) {
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.citylist);
	}
	
	/**首页接口*/
	public static String getIndexannouncement(Context context){
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.indexannouncement);
	}
	
	/**个人中心接口*/
	public static String getMain(Context context){
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.main);
	}
	
	public static String getDoctorpatientinfo(Context context){
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.doctorpatientinfo);
	}
	
	
	public static String getDavedoctoropinion(Context context){
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.savedoctoropinion);
	}
	
	/**科室列表*/
	public static String getDeptlist(Context context){
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.deptlist);
	}
	
	/**转诊会诊信息*/
	public static String getDoctordr(Context context){
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.doctordr);
	}
	
	/**会诊信息*/
	public static String getDoctorhzview(Context context){
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.doctorhzview);
	}
	
	/**转诊信息*/
	public static String getDoctorzzview(Context context){
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.doctorzzview);
	}
	
	/**创建或修改会诊信息*/
	public static String getSavedoctorhz(Context context){
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.savedoctorhz);
	}
	
	/**创建或修改转诊信息*/
	public static String getSavedoctorzz(Context context){
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.savedoctorzz);
	}
	
	/**不参与转诊*/
	public static String getNotjoinzz(Context context){
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.notjoinzz);
	}
	
	/**不参与会诊*/
	public static String getNotjoinhz(Context context){
		return LocalDataDao.newInstance(context).getActionUrl(Constants.DATAYPTE.notjoinhz);
	}
	
	
	private static String getResult(InputStream inputStream){
		try{
			byte[] buffer = new byte[2048];
			int readBytes = 0;
			StringBuilder stringBuilder = new StringBuilder();
			while((readBytes = inputStream.read(buffer)) > 0){
				stringBuilder.append(new String(buffer, 0, readBytes));
			}
			return stringBuilder.toString();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public static void gotoLogin(Activity context){
		IntentHelper.getInstance(context).gotoActivity(LoginActivity.class);
	}
	
	public static boolean isLogin(Context context){
//		setMobile(context, "13816439927");
//		setToken(context, "8B03B640F780AE5A01ABF0C6988A6247");
		if(Utils.isStringEmpty(getToken(context))) return false;
		return true;
	}
	
	public static String getGetParm(Activity activity ,String param){
		
		if(Utils.isStringEmpty(param))
			param = "";
		else
			param = "&"+param;
		return "?username="+CommonUtils.getMobile(activity) +"&token="+CommonUtils.getToken(activity) +param;
	}
	
	public static void showErrorAtBottom(final Context context,Handler handler,final Exception ex){
		handler.post(new Runnable() {
			@Override
			public void run() {
					Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();	
			}
		});
	}
	
	public static void showErrorAtBottom(final Context context,Handler handler,final String msg){
		handler.post(new Runnable() {
			@Override
			public void run() {
					Toast.makeText(context, msg, Toast.LENGTH_LONG).show();	
			}
		});
	}
	
	public static void showHttpError(final Context context,Handler handler, final int msg){
		handler.post(new Runnable() {
			@Override
			public void run() {
					Toast.makeText(context, "网络访问错误："+msg, Toast.LENGTH_LONG).show();	
			}
		});
	}
	
//	public static String getGetParm(Activity activity ,String param){
//		return "?username="+"15670794261" +"&token="+ "9FBE057BF4257FE71A52BBF7CB9D992F" + "&"+param;
//	}
}
