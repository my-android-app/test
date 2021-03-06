package com.shoushuzhitongche.app.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.Constants;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class DBManager {
	private final int BUFFER_SIZE = 400000;
    public static String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() +"/";  //在手机里存放数据库的位置
    private Context context;
 
    public DBManager(Context context) {
        this.context = context;
        DB_PATH = DB_PATH + context.getPackageName();
    }
 
    public void removeDatabase() {
    	File dbDir = new File(DB_PATH, "databases");
    	if(dbDir != null && !dbDir.exists()){
    		dbDir.mkdir();
    	}
    	removeDatabase(dbDir.getAbsolutePath() + "/" + Constants.DB_NAME);
    }
 
    private void removeDatabase(String dbfile) {
        try {
            if (!(new File(dbfile).exists())) { //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = this.context.getResources().openRawResource(
                        R.raw.mingyizhudao); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
    }
}