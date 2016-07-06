package com.dixiang.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.config.SystemConfig;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.DatePicker;
import android.widget.EditText;

public class Utils {
	public static Map<String,BaseActivity> map= new HashMap<>();
    /***
     * dx
     * 捕捉全局闪退异常 UncaughtException
     * @param context
     */
    public static void initExcption(Context context){
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.setCallBackLisener(callBackLisener);
        crashHandler.init(context);
    }
    
    /**
     * md5 加密
     * @param plainText
     * @return
     */
    public static String Md5String(String plainText) {
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;


            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            //            System.out.println("result: " + buf.toString().substring(8, 24));//16位的加密

        } catch (NoSuchAlgorithmException e) {
        	// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return buf.toString();
    }
    
    static ICrashHandlerLisener callBackLisener = new ICrashHandlerLisener() {
		
		@Override
		public void crashHanderCallBack(Context context) {

        Intent intent = new Intent();
//      intent.setClass(context, StartActivity.class);
//      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
			
		}
	};
	
	/**
	 * 数据保存到缓存文件
	 * @param dirPath
	 * @param fileName
	 * @param object
	 * @throws Exception
	 */
	public static void saveDataToCache(String dirPath, String fileName, Object object) throws Exception{
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		File file = FileUtils.dataCache(dirPath, fileName);
		fos = new FileOutputStream(file);  
        oos = new ObjectOutputStream(fos);  
        oos.writeObject(object);    //括号内参数为要保存java对象  
	}
	
	/**
	 * 数据保存到缓存文件
	 * @param fileName
	 * @param object
	 * @throws Exception
	 */
	public static void saveDataToCache(String fileName, Object object)throws Exception{
		saveDataToCache(SystemConfig.CACHE_FILE_OBJECT, fileName, object);
	}
	
	/**
	 * 读取文件缓存
	 * @param dirPath
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static Object getDataFromCache(String dirPath, String fileName) throws Exception{
		FileInputStream fis = null;  
        ObjectInputStream ois = null; 
        File file = null;
		try {
	        file = FileUtils.dataCache(dirPath, fileName);
	        fis = new FileInputStream(file);  
	        ois = new ObjectInputStream(fis);
		} catch (Exception e) {
			e.printStackTrace();
			try{
				file.delete();
				return null;
			}catch(Exception ex){
				
			}
		}
		    
        return ois.readObject();//强制类型转换  
	}
	
	public static Object getDataFromCache(String fileName) throws Exception{
		return getDataFromCache(SystemConfig.CACHE_FILE_OBJECT, fileName);
	}
	
	public static boolean checkMobile(EditText edit){
		String mobile = edit.getText().toString().trim();
		if (!isStringEmpty(mobile) && mobile.length() == 11 && checkMobile(mobile)){
			return true;
		}
		edit.requestFocus();
		return false;
	}
	
	/**
	 * 检查是否是合法手机号.
	 * 
	 * @param str
	 *            手机号
	 * @return 合法返回true, 否则返回false
	 */
	public static boolean checkMobile(String phone) {
		Pattern p = Pattern.compile("1[34578][0-9]{9}");
		Matcher m = p.matcher(phone);
		return m.matches();
	}
	
	public static boolean isStringEmpty(Object object) {
		if (object != null && object.toString().length() > 0 && !"null".equals(object)) {
			return false;
		}
		return true;

	}
	
	/** 

	 * 分享功能 
	 *  
	 * @param context 
	 *            上下文 
	 * @param activityTitle 
	 *            Activity的名字 
	 * @param msgTitle 
	 *            消息标题 
	 * @param msgText 
	 *            消息内容 
	 * @param imgPath 
	 *            图片路径，不分享图片则传null 
	 */  
	public static void shareMsg(Activity activity, String activityTitle, String msgTitle, String msgText,  
					String imgPath) {  
		Intent intent = new Intent(Intent.ACTION_SEND);  
		if (imgPath == null || imgPath.equals("")) {  
			intent.setType("text/plain"); // 纯文本  
		} else {  
			File f = new File(imgPath);  
			if (f != null && f.exists() && f.isFile()) {  
				intent.setType("image/jpg");  
				Uri u = Uri.fromFile(f);  
				intent.putExtra(Intent.EXTRA_STREAM, u);  
			}  
		}  
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);  
		intent.putExtra(Intent.EXTRA_TEXT, msgText);  
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		activity.startActivity(Intent.createChooser(intent, activityTitle));  
	}  

	public static abstract class IOnTimeSelectListener{
		public void onTimeSelected(int viewid,String value){}
		public void onTimeSelected(int viewid,String year, String month , String day){}
	}
	
	public static void showTimeDialog(Context context,Calendar calendar,final int viewId,final IOnTimeSelectListener listener){
		if( calendar==null )
		{
			calendar = Calendar.getInstance();
		}
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		final int day = calendar.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog datePicker = new DatePickerDialog(context,
				AlertDialog.THEME_HOLO_LIGHT, new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						String vlaue = year +"-"+(monthOfYear+1)+"-"+dayOfMonth;
						listener.onTimeSelected(viewId, vlaue);
						listener.onTimeSelected(viewId, year+"",""+ (monthOfYear+1),""+ dayOfMonth);
					}
				}, year, month, day);
		datePicker.show();
	}
}
