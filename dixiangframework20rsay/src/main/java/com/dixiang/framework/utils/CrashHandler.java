package com.dixiang.framework.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    
    public static ICrashHandlerLisener callBackLisener;

    // CrashHandler 瀹炰緥
    private static CrashHandler INSTANCE = new CrashHandler();

    // 绋嬪簭鐨� Context 瀵硅薄
    private Context mContext;

    // 绯荤粺榛樿鐨� UncaughtException 澶勭悊绫�
    private UncaughtExceptionHandler mDefaultHandler;

    // 鐢ㄦ潵瀛樺偍璁惧淇℃伅鍜屽紓甯镐俊鎭�
    private Map<String, String> infos = new HashMap<>();

    // 鏃堕棿鏍煎紡
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private CrashHandler() {
    }

    /** 鑾峰彇CrashHandler 瀹炰緥 */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 鍒濆鍖�
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;

        // 鑾峰彇绯荤粺榛樿鐨� UncaughtException 澶勭悊鍣�
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 璁剧疆CrashHandler 浣嶇▼搴忕殑榛樿澶勭悊鍣�
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    
    /**
     * 褰揢ncaughtException 鍙戠敓鏃朵細浼犲叆璇ュ嚱鏁版潵澶勭悊
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 濡傛灉鐢ㄦ埛娌℃湁澶勭悊锛屽垯璁╃郴缁熼粯璁ょ殑寮傚父澶勭悊鍣ㄦ潵澶勭悊
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }

            callBackLisener.crashHanderCallBack(mContext);

        }  
    }  
  
    /** 
     * 鑷畾涔夐敊璇鐞嗭紝鏀堕泦閿欒淇℃伅锛屽彂閫侀敊璇姤鍛婄瓑鎿嶄綔
     *  
     * @param ex 
     * @return true 濡傛灉澶勭悊浜嗚寮傚父淇℃伅锛涘惁鍒欒繑鍥瀎alse
     */  
    private boolean handleException(Throwable ex) {  
        if (ex == null) {  
            return false;  
        }  
  
        // 浣跨敤Toast 鏉ユ樉绀哄紓甯镐俊鎭�
        new Thread() {  
            @Override  
            public void run() {  
                Looper.prepare();  
                Toast.makeText(mContext, "寰堟姳姝夛紝绋嬪簭鍑虹幇寮傚父锛屽嵆灏嗛��鍑�", Toast.LENGTH_LONG).show();
                Looper.loop();  
            }  
        }.start();  
  
        // 鏀堕泦璁惧鍙傛暟淇℃伅
        collectDeviceInfo(mContext);  
        // 淇濆瓨鏃ュ織鏂囦欢
        saveCrashInfo2File(ex);  
        return true;  
    }  
  
    /** 
     * 鏀堕泦璁惧鍙傛暟淇℃伅
     * @param ctx 
     */  
    public void collectDeviceInfo(Context ctx) {  
        try {  
            PackageManager pm = ctx.getPackageManager();  
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);  
  
            if (pi != null) {  
                String versionName = pi.versionName == null ? "null" : pi.versionName;  
                String versionCode = pi.versionCode + "";  
                infos.put("versionName", versionName);  
                infos.put("versionCode", versionCode);  
            }  
        } catch (NameNotFoundException e) {  
            Log.e(TAG, "an error occured when collect package info", e);  
        }  
  
        Field[] fields = Build.class.getDeclaredFields();  
        for (Field field : fields) {  
            try {  
                field.setAccessible(true);  
                infos.put(field.getName(), field.get(null).toString());  
                Log.d(TAG, field.getName() + " : " + field.get(null));  
            } catch (Exception e) {  
                Log.e(TAG, "an error occured when collect crash info", e);  
            }  
        }  
    }  
  
    /** 
     * 淇濆瓨閿欒淇℃伅鍒版枃浠朵腑
    * 
     * @param ex 
     * @return  杩斿洖鏂囦欢鍚嶇О渚夸簬灏嗘枃浠跺彂閫佸埌鏈嶅姟鍣�
     */  
    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();  
        for (Map.Entry<String, String> entry : infos.entrySet()) {  
            String key = entry.getKey();  
            String value = entry.getValue();  
            sb.append(key + "=" + value + "\n");  
        }  
  
        Writer writer = new StringWriter();  
        PrintWriter printWriter = new PrintWriter(writer);  
        ex.printStackTrace(printWriter);  
        Throwable cause = ex.getCause();  
        while (cause != null) {  
            cause.printStackTrace(printWriter);  
            cause = cause.getCause();  
        }  
        printWriter.close();  
  
        String result = writer.toString();  
        Log.e("error info  ",result);
        sb.append(result);  
        try {  
            long timestamp = System.currentTimeMillis();  
            String time = formatter.format(new Date());  
            String fileName = "crash-" + time + "-" + timestamp + ".log";  
              
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
                String path = Environment.getExternalStorageDirectory().getPath()+ "crash/";
                File dir = new File(path);
                if (!dir.exists()) {  
                    dir.mkdirs();  
                }  
                FileOutputStream fos = new FileOutputStream(path + fileName);  
                fos.write(sb.toString().getBytes());  
                fos.close();  
            }  
  
            return fileName;  
        } catch (Exception e) {  
            Log.e(TAG, "an error occured while writing file...", e);  
        }  
  
        return null;  
    }  
    
    public static void setCallBackLisener(ICrashHandlerLisener callBackLisener) {
		CrashHandler.callBackLisener = callBackLisener;
	}
}  