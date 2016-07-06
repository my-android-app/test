package com.dixiang.framework.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class FragmentUtils {
	private static Map<String,SoftReference<Fragment>> map = new HashMap<>();
	private static Fragment previousFragment;
//	public static void goToFragment(Activity activity ,Class<? extends Fragment> fragmentClass,int container){
//		FragmentManager fragmentManager = activity.getFragmentManager();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		String fragmentName = fragmentClass.getName();
//		Fragment fragment = fragmentManager.findFragmentByTag(fragmentName);
//	    boolean isAddToStack = false;
//		
//		try{
//			if (fragment == null){
//				fragment = fragmentClass.newInstance();
//				isAddToStack = true;
//			}
//			fragmentTransaction.replace(container, fragment,fragmentName);
//			
//			if(isAddToStack){
//				fragmentTransaction.addToBackStack(null);	
//			}
//			
//			fragmentTransaction.commit();
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//		
//	}
	
//	public static void putDefaultFragment(Activity activity ,int container ,Fragment fragment){
//		FragmentManager fragmentManager = activity.getFragmentManager();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//		try{
//			fragmentTransaction.replace(container, fragment,fragment.getClass().getName());
//			fragmentTransaction.addToBackStack(null);
//			fragmentTransaction.commit();
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//	}
	
	public static void goToFragment(Activity activity ,Class<? extends Fragment> fragmentClass,int container){
		FragmentManager fragmentManager = activity.getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		String fragmentName = fragmentClass.getSimpleName();
		SoftReference<Fragment> soft = map.get(fragmentName);
		Fragment fragment=null;
		try{
		    fragmentTransaction.hide(previousFragment);
			if (soft == null){
				fragment = fragmentClass.newInstance();
				fragmentTransaction.add(container, fragment);
				map.put(fragmentName, new SoftReference<>(fragment));
			}else{
				fragment = soft.get();
				fragmentTransaction.show(fragment);
			}
			previousFragment = fragment;
			fragmentTransaction.commit();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	public static void putDefaultFragment(Activity activity ,int container ,Fragment fragment){
		FragmentManager fragmentManager = activity.getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		try{
			fragmentTransaction.add(container, fragment);
			fragmentTransaction.commit();
			previousFragment = fragment;
			map.put(fragment.getClass().getSimpleName(), new SoftReference<>(fragment));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
