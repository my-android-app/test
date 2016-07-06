package com.dixiang.framework.config;

import android.os.Environment;

public class SystemConfig {
	
	public final static boolean IS_MD5 = true;
	
	public final static String API = "3";
	
	public final static String API_ID = "&app_id=c99ae561f36db004";
	
	public final static String API_KEY = "dbb66bc015db74b4652d51b270a720ca";
	
	public final static String SERVER_KEY = "server";
	
	public static String baseURL = "http://192.168.31.119/new.md/apimd/";
//	public static String baseURL = "http://mdapi.mingyizhudao.com/apimd/";
	
	
//	public static String baseURL = "http://mdapi.dev.mingyizd.com/apimd/";
//	public static String baseURL = "http://mdapi.mingyizhudao.com/apimd/";
	
	
	//七牛服务器空间地址 
	public final static String BUCKET = "7xndhx.com1.z0.glb.clouddn.com";
	
	//public final static String BASEPOINT = "http://mdapi.mingyizhudao.com";
	
	public static String CACHE_FILE = Environment.getExternalStorageDirectory() + "/minyizhudao/temp";//缓存图片文件地址
	
	public static String CACHE_FILE_OBJECT = Environment.getExternalStorageDirectory() + "/minyizhudao/data";//缓存图片文件地址
	
	public final  static String PARAM_TIME="&timestamp=";
	
	public final  static String PARAM_SIGN="&sign=";
	
	public final static String basePARAM  = "?os=android&appv=";
	
	public final static String basePARAM_AND  = "&os=android&appv=";
	
	public final static String DATA_VERSION = baseURL + "dataversion" +basePARAM;
	
	public final static String DATA_LOCALDATA = baseURL + "localdata";
	
	public final static String DATA_BOOKING_LIST = baseURL + "userbooking";
	
//	public final static String QN_TOKEN = "http://file.mingyizhudao.com/api/tokenbookingmr";//获取上传七牛的token
//
//	public final static String SAVE_APP_FILE = "http://file.mingyizhudao.com/api/savebookingmr";//保存文件信息到自己服务器
//
//	public final static String APP_FILE_URL = "http://file.mingyizhudao.com/api/loadbookingmr";//从自己服务器获取图片路径
	
}
