package com.dixiang.framework.utils;

import java.util.Map;
import java.util.Set;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceUtils {
    private SharedPreferences preferences = null;
    private Context context;
    private static SharedPreferenceUtils instance;

    private SharedPreferenceUtils(Context context, String key) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public static SharedPreferenceUtils getInstance(Context context, String key) {
        if (instance == null) {
            return new SharedPreferenceUtils(context, key);
        } else {
            return instance;
        }
    }

    public void save(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void save(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void save(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void save(String key, float value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public Object get(String key) {
        Map<String, Object> map = (Map<String, Object>) preferences.getAll();
        if (!map.containsKey(key)) {
            return "";
        }
        return map.get(key) == null ? "" : map.get(key);
    }


    public void clear() {
        //全部清空
        Map<String, Object> map = (Map<String, Object>) preferences.getAll();
        Set<String> keys = map.keySet();
        for (String key : keys) {
//            preferences.get
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
        }
    }
}
