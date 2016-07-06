package com.dixiang.framework.config;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import android.os.Handler;

public class SystemConstants {
	
	public static PublicKey publicKey;
	public static PrivateKey privateKey;
private final static String serverkey = "server";
	
	public static Map<String, Boolean> map = new HashMap<>();
	
	public static Stack<Handler> statck = new Stack<>();
	
	/**将handler压栈，将handler的句柄放入缓存中，当前UI在前台，handler为活动状态*/
	public static void putHandler(String key ,Handler handler){
		if(!map.containsKey(key)){
			map.put(key, true);
			statck.push(handler);
		}
	}
	
	public static boolean isServerHandler(String key){
		if(serverkey.equals(key)){
			return true;
		}
		return false;
	}
	
	/**判断当前UI是否在前台*/
	public static boolean isHandlerExist(String key){
		return  map.get(key) == null ? false : map.get(key);
		
	}
	
	/**刷新handler的活动状态 ，在onReStart()，和onStop() 方法中调用   ;  onStop()时，handler置于等待状态， onReStart()时，handler处于活动状态*/
	public static void refreshHandler(String key){
		if(map.containsKey(key)){
			boolean isTrue = map.get(key);
			isTrue = !isTrue;
			map.put(key, isTrue);
		}
	}
	
	/**获取当前handler对象，只是获取handler引用，不能直接弹出handler*/
	public static Handler getHandler(){
		if(statck != null && !statck.isEmpty())
		return statck.peek();
		else return null;
	}
	
	/**移除handler对象，并置空，已经移除缓存中的句柄*/
	public static void removeHandler(String key){
		if(map.containsKey(key)){
			map.remove(key);
			Handler handler = statck.pop();
			handler = null;
		}
	}
	
}
